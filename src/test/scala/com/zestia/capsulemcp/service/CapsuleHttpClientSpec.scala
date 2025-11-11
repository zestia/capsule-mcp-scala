package com.zestia.capsulemcp.service

import com.zestia.capsulemcp.model.{ContactListWrapper, Pagination}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{Empty, HttpClientSyncBackend, Identity, RequestT, StringBody, SttpBackend}
import sttp.model.{Header, Uri}
import com.zestia.capsulemcp.model.filter.{Filter, NumberCondition}
import com.zestia.capsulemcp.model.ActivityListWrapper

class CapsuleHttpClientSpec extends AnyFlatSpec with Matchers:

  "requestWithBearer" should "add Authorization header with Bearer token" in
    new CapsuleHttpClientFixture {
      // given
      val request: RequestT[Empty, Either[String, String], Any] = client.requestWithBearer

      // then
      val authHeader: Option[Header] = request.headers.find(_.name == "Authorization")
      authHeader shouldBe defined
      authHeader.get.value shouldBe "Bearer api-token"
    }

  it should "include base headers" in
    new CapsuleHttpClientFixture {
      // given
      val request: RequestT[Empty, Either[String, String], Any] = client.requestWithBearer

      // then
      val userAgentHeader: Option[Header] = request.headers.find(_.name == "User-Agent")
      userAgentHeader shouldBe defined
      userAgentHeader.get.value shouldBe "capsule-mcp-v1"

      val contentTypeHeader: Option[Header] = request.headers.find(_.name == "Content-Type")
      contentTypeHeader shouldBe defined
      contentTypeHeader.get.value shouldBe "application/json"
    }

  "buildEmbedMap" should "merge default embeds with custom embeds" in
    new CapsuleHttpClientFixture {
      // given
      val customEmbeds = List("tasks", "notes")

      // when
      val embedMap: Map[String, String] = client.buildEmbedMap(customEmbeds)

      // then
      embedMap should have size 1
      embedMap("embed") should include("meta")
      embedMap("embed") should include("fields")
      embedMap("embed") should include("tags")
      embedMap("embed") should include("missingImportantFields")
      embedMap("embed") should include("tasks")
      embedMap("embed") should include("notes")
    }

  it should "include only default embeds when no custom embeds provided" in
    new CapsuleHttpClientFixture {
      // given
      val customEmbeds = List.empty[String]

      // when
      val embedMap: Map[String, String] = client.buildEmbedMap(customEmbeds)

      // then
      embedMap("embed") shouldBe "meta,fields,tags,missingImportantFields"
    }

  "constructUri" should "correctly build URIs with pagination and query params" in
    new CapsuleHttpClientFixture {
      // given
      val uri: Uri = client.constructUri(
        "https://api.example.com/api/v2",
        "parties",
        Pagination(page = 2, perPage = 50),
        Map("embed" -> "meta,fields")
      )

      // then
      uri.toString shouldBe "https://api.example.com/api/v2/parties?embed=meta,fields&page=2&perPage=50"
    }

  "getRequest" should "successfully deserialize response from GET request" in {
    // given
    val mockResponse =
      """
        {
          "parties": [
            { "id": 123, "name": "Test Party", "type": "person" }
          ],
          "meta": {
            "totalCount": 12
          }
        }
      """

    val backend = SttpBackendStub.synchronous
      .whenRequestMatches { request =>
        request.uri.toString.contains("https://api.example.com/api/v2/parties") &&
        request.uri.toString.contains("&page=1&perPage=10") &&
        request.method.method == "GET" &&
        request.headers.find(_.name == "Authorization").get.value == "Bearer api-token"
      }
      .thenRespond(mockResponse)

    val client = new TestCapsuleHttpClient(backend)

    // when
    val result: ContactListWrapper = client.getRequest[ContactListWrapper](
      "parties",
      Pagination(page = 1, perPage = 10)
    )

    // then
    result.parties should have size 1
    result.parties.head.id shouldBe 123
  }

  "filterRequest" should "send POST request with filter body" in {
    // given
    val mockResponse =
      """
        {
          "activities": [
            {
              "id": 456,
              "activityType": { "id": 1, "name": "Note" },
              "user": { "id": 1, "username": "test@example.com", "name": "Test User" }
            }
          ],
          "meta": {
            "totalCount": 1
          }
        }
      """

    val expectedBody =
      """{"filter":{"conditions":[{"field":"user","operator":"is","value":123}]}}"""

    val backend = SttpBackendStub.synchronous
      .whenRequestMatches { request =>
        request.uri.toString.contains("https://api.example.com/api/v2/activities/filters/results") &&
        request.uri.toString.contains("&page=1&perPage=10") &&
        request.method.method == "POST" &&
        request.body.asInstanceOf[StringBody].s == expectedBody &&
        request.headers.find(_.name == "Authorization").get.value == "Bearer api-token"
      }
      .thenRespond(mockResponse)

    val client = new TestCapsuleHttpClient(backend)

    // when
    val result = client.filterRequest[ActivityListWrapper](
      "activities/filters/results",
      Filter(
        conditions = List(
          NumberCondition(field = "user", operator = "is", value = 123)
        )
      ),
      Pagination(page = 1, perPage = 10)
    )

    // then
    result.activities should have size 1
  }

  private class TestCapsuleHttpClient(protected val backend: SttpBackend[Identity, Any]) extends BaseCapsuleHttpClient:
    override protected val apiToken = "api-token"
    override protected val baseUrl = "https://api.example.com/api/v2"

  private trait CapsuleHttpClientFixture:
    private val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()
    val client: TestCapsuleHttpClient = TestCapsuleHttpClient(backend)
