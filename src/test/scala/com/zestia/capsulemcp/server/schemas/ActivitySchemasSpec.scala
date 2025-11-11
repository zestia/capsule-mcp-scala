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

import com.networknt.schema.{InputFormat, Schema, SchemaRegistry, SpecificationVersion, Error => ValidationError}
import com.zestia.capsulemcp.server.schemas.ActivitySchemas
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters.*

class ActivitySchemasSpec extends AnyFlatSpec with Matchers:

  "ActivitySchemas.activityFilterSchema" should "produce valid JSON schema" in new ActivitySchemasFixture {
    val exampleBody: String =
      """
        |{}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "produce schema that accepts a valid activity filter body" in new ActivitySchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "pagination": { "page": 1, "perPage": 100 },
        |  "filter": {
        |    "conditions": [
        |      { "field": "taskCategory", "operator": "is", "value": 1 },
        |      { "field": "addedOn", "operator": "is after", "value": "2020-01-01" }
        |    ]
        |  }
        |}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "produce schema that rejects an invalid activity filter body" in new ActivitySchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "filter": {
        |    "conditions": [
        |      { "field": "taskCategory", "operator": "is", "value": "one" }
        |    ]
        |  }
        |}
        |""".stripMargin

    val errors: List[ValidationError] = filterSchema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors should not be empty
    errors.head.getEvaluationPath.toString should include("field")
  }

private trait ActivitySchemasFixture:
  private val schemaRegistry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7)
  protected val filterSchema: Schema = schemaRegistry.getSchema(ActivitySchemas.activityFilterSchema)
