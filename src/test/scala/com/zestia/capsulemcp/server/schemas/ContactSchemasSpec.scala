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

package com.zestia.capsulemcp.server.schemas

import com.networknt.schema.{Error => ValidationError, InputFormat, Schema, SchemaRegistry, SpecificationVersion}
import com.zestia.capsulemcp.server.schemas.ContactSchemas
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters.*

class ContactSchemasSpec extends AnyFlatSpec with Matchers:

  "ContactSchemas.contactFilterSchema" should "produce valid JSON schema" in new ContactSchemasFixture {
    val exampleBody: String =
      """
        |{}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "produce schema that accepts a valid contact filter body" in new ContactSchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "pagination": { "page": 1, "perPage": 100 },
        |  "filter": {
        |    "conditions": [
        |      { "field": "name", "operator": "contains", "value": "John" },
        |      { "field": "hasEmailAddress", "operator": "is", "value": true }
        |    ]
        |  }
        |}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "produce schema that rejects an invalid contact filter body" in new ContactSchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "filter": {
        |    "conditions": [
        |      { "field": "hasEmailAddress", "operator": "is", "value": "true" }
        |    ]
        |  }
        |}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors.head.getEvaluationPath.toString should include("field")
  }

  it should "accept custom field patterns" in new ContactSchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "filter": {
        |    "conditions": [
        |      { "field": "custom:123", "operator": "is", "value": "test" },
        |      { "field": "org.custom:456", "operator": "is", "value": 42 }
        |    ]
        |  }
        |}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

private trait ContactSchemasFixture:
  private val schemaRegistry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7)
  protected val filterSchema: Schema = schemaRegistry.getSchema(ContactSchemas.contactFilterSchema)
