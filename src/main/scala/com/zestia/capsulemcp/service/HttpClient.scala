/*
 * Copyright 2026 Zestia Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zestia.capsulemcp.service

import sttp.client3.*
import sttp.model.{HeaderNames, Uri}
import zio.json.*
import com.zestia.capsulemcp.util.{FileLogging, Version}
import com.zestia.capsulemcp.model.Pagination
import com.zestia.capsulemcp.model.ResponseWrapper
import scala.reflect.ClassTag

abstract class HttpClient extends FileLogging:
  protected val backend: SttpBackend[Identity, Any]
  protected val baseUrl: String

  protected def baseRequest: RequestT[Empty, Either[String, String], Any] =
    basicRequest
      .header(HeaderNames.UserAgent, s"capsule-mcp-${Version.current}")
      .header(HeaderNames.ContentType, "application/json")

  protected[service] def constructUri(
      baseUrl: String,
      path: String,
      pagination: Option[Pagination],
      queryParams: Map[String, Any]
  ): Uri =
    val params = queryParams ++ pagination.getOrElse(Pagination()).toMap
    val url = s"$baseUrl/$path"
    uri"$url?$params"

  protected def handleResponseAsJson[T: {JsonDecoder, ClassTag}](
      response: Identity[Response[String]]
  ): ResponseWrapper[T] =
    if (response.code.isSuccess) {
      response.body.fromJson[T] match {
        case Right(result) =>
          val hasMore = response.header("X-Pagination-Has-More").map(_.equalsIgnoreCase("true"))
          logger.info(s"Response: ${response.code}")
          ResponseWrapper(result, hasMore)

        case Left(error) =>
          logger.error(s"Could not deserialize response to JSON: $error\nBody: ${response.body}")
          throw new RuntimeException(s"Error reading response: $error")
      }
    } else {
      response.code.code match {
        case 404 =>
          logger.warn("Not found")
          throw new RuntimeException("API error: 404 Not Found")
        case _ =>
          logger.error(s"API request failed: ${response.code}\nBody: ${response.body}")
          throw new RuntimeException(s"API error: ${response.code}\nBody: ${response.body}")
      }
    }
