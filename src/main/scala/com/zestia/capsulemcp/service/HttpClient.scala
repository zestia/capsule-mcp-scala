package com.zestia.capsulemcp.service

import sttp.client3.*
import sttp.model.{HeaderNames, Uri}
import zio.json.*
import com.zestia.capsulemcp.util.FileLogging
import com.zestia.capsulemcp.model.Pagination
import scala.reflect.ClassTag

abstract class HttpClient extends FileLogging:
  protected val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()
  protected val baseUrl: String

  protected def baseRequest: RequestT[Empty, Either[String, String], Any] =
    basicRequest
      .header(HeaderNames.UserAgent, "capsule-mcp")
      .header(HeaderNames.ContentType, "application/json")

  protected def constructUri(
      baseUrl: String,
      path: String,
      pagination: Pagination,
      queryParams: Map[String, Any]
  ): Uri =
    val params = queryParams ++ pagination.toMap
    val url = s"$baseUrl/$path"
    uri"$url?$params"

  protected def handleResponseAsJson[T: {JsonDecoder, ClassTag}](
      response: Identity[Response[String]]
  ): T =
    if (response.code.isSuccess) {
      response.body.fromJson[T] match {
        case Right(result) =>
          logger.info(s"Response: ${response.code}")
          result
        case Left(error) =>
          logger.error(
            s"Could not deserialize response to JSON: $error\nBody: ${response.body}"
          )
          throw new RuntimeException(s"Error reading response: $error")
      }
    } else {
      logger.error(
        s"API request failed: ${response.code}\nBody: ${response.body}"
      )
      throw new RuntimeException(
        s"API error: ${response.code}\nBody: ${response.body}"
      )
    }
