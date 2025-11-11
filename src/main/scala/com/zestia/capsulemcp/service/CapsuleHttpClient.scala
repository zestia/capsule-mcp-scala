/*
 * Copyright 2025 Zestia Ltd
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

import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.model.filter.{Filter, FilterRequestWrapper}
import sttp.client3.*
import sttp.client3.ziojson.*
import sttp.model.{HeaderNames, Method}
import zio.json.*

import scala.reflect.ClassTag

trait BaseCapsuleHttpClient extends HttpClient:

  private val DEFAULT_EMBEDS = List("meta", "fields", "tags", "missingImportantFields")
  
  private[service] def requestWithBearer: RequestT[Empty, Either[String, String], Any] =
    baseRequest.header(HeaderNames.Authorization, s"Bearer $apiToken")

  private[service] def buildEmbedMap(customEmbeds: List[String]): Map[String, String] =
    val allEmbeds = DEFAULT_EMBEDS ++ customEmbeds
    Map("embed" -> allEmbeds.mkString(","))

  protected def apiToken: String
  
  /**
   * Execute an HTTP request without a body (e.g., GET, DELETE)
   */
  private def executeRequest[T: {JsonDecoder, ClassTag}](
      method: Method,
      path: String,
      pagination: Pagination,
      queryParams: Map[String, Any],
      embed: List[String]
  ): T =
    val embedMap = buildEmbedMap(embed)
    val uri = constructUri(baseUrl, path, pagination, queryParams ++ embedMap)

    logger.info(s"${method.method} ${uri.toString}")

    val response = requestWithBearer
      .method(method, uri)
      .response(asStringAlways)
      .send(backend)

    handleResponseAsJson[T](response)

  /**
   * Execute an HTTP request with a body (e.g., POST, PUT)
   */
  private def executeRequestWithBody[T: {JsonDecoder, ClassTag}, B: JsonEncoder](
      method: Method,
      path: String,
      pagination: Pagination,
      queryParams: Map[String, Any],
      embed: List[String],
      body: B
  ): T =
    val embedMap = buildEmbedMap(embed)
    val uri = constructUri(baseUrl, path, pagination, queryParams ++ embedMap)

    logger.info(s"${method.method} ${uri.toString}\nBody: ${body.toJson}")

    val response = requestWithBearer
      .method(method, uri)
      .body(body)
      .response(asStringAlways)
      .send(backend)

    handleResponseAsJson[T](response)

  def getRequest[T: {JsonDecoder, ClassTag}](
      path: String,
      pagination: Pagination = Pagination(),
      queryParams: Map[String, Any] = Map.empty,
      embed: List[String] = List.empty
  ): T =
    executeRequest[T](Method.GET, path, pagination, queryParams, embed)

  def filterRequest[T: {JsonDecoder, ClassTag}](
      path: String,
      filter: Filter,
      pagination: Pagination = Pagination(),
      queryParams: Map[String, Any] = Map.empty,
      embed: List[String] = List.empty
  ): T =
    executeRequestWithBody[T, FilterRequestWrapper](
      Method.POST,
      path,
      pagination,
      queryParams,
      embed,
      FilterRequestWrapper(filter)
    )

object CapsuleHttpClient extends BaseCapsuleHttpClient:

  private val DEFAULT_CAPSULE_HOST = "https://api.capsulecrm.com"

  override protected val apiToken: String = sys.env.getOrElse(
    "CAPSULE_API_TOKEN",
    throw new RuntimeException("CAPSULE_API_TOKEN environment variable is required")
  )

  override protected val baseUrl: String =
    s"${sys.env.getOrElse("CAPSULE_BASE_URL", DEFAULT_CAPSULE_HOST)}/api/v2"
    
  override protected val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()  
