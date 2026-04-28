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

  "ContactSchemas.filterSchema" should "produce valid JSON schema" in new ContactSchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "filter": {
        |    "conditions": []
        |  }
        |}
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

  "ContactSchemas.updateContactSchema" should "produce valid JSON schema" in new ContactSchemasFixture {
    val errors: List[ValidationError] =
      updateContactSchema.validate("""{"id": 14, "fields": []}""", InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "accept a field with a string value" in new ContactSchemasFixture {
    val body = """{"id": 14, "fields": [{"definition": 135, "value": "some text"}]}"""

    updateContactSchema.validate(body, InputFormat.JSON).asScala.toList shouldBe Nil
  }

  it should "accept a field with a boolean value" in new ContactSchemasFixture {
    val body = """{"id": 14, "fields": [{"definition": 135, "value": true}]}"""

    updateContactSchema.validate(body, InputFormat.JSON).asScala.toList shouldBe Nil
  }

  it should "accept a field with a number value" in new ContactSchemasFixture {
    val body = """{"id": 14, "fields": [{"definition": 135, "value": 42}]}"""

    updateContactSchema.validate(body, InputFormat.JSON).asScala.toList shouldBe Nil
  }

  it should "reject a body missing id" in new ContactSchemasFixture {
    val body = """{"fields": [{"definition": 135, "value": "test"}]}"""

    val errors: List[ValidationError] = updateContactSchema.validate(body, InputFormat.JSON).asScala.toList

    errors should not be Nil
  }

  it should "reject a body missing fields" in new ContactSchemasFixture {
    val body = """{"id": 14}"""

    val errors: List[ValidationError] = updateContactSchema.validate(body, InputFormat.JSON).asScala.toList

    errors should not be Nil
  }

  it should "reject a field item missing definition" in new ContactSchemasFixture {
    val body = """{"id": 14, "fields": [{"value": "test"}]}"""

    val errors: List[ValidationError] = updateContactSchema.validate(body, InputFormat.JSON).asScala.toList

    errors should not be Nil
  }

  it should "reject a field item missing value" in new ContactSchemasFixture {
    val body = """{"id": 14, "fields": [{"definition": 135}]}"""

    val errors: List[ValidationError] = updateContactSchema.validate(body, InputFormat.JSON).asScala.toList

    errors should not be Nil
  }

private trait ContactSchemasFixture:
  private val schemaRegistry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7)
  protected val filterSchema: Schema = schemaRegistry.getSchema(ContactSchemas.filterSchema)
  protected val updateContactSchema: Schema = schemaRegistry.getSchema(ContactSchemas.updateContactSchema)
