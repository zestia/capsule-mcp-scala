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
import com.zestia.capsulemcp.server.schemas.TaskSchemas
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters.*

class TaskSchemasSpec extends AnyFlatSpec with Matchers:

  "TaskSchemas.listTasksSchema" should "produce valid JSON schema" in new TaskSchemasFixture {
    val exampleBody: String =
      """
        |{}
        |""".stripMargin

    val errors: List[ValidationError] = schema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "produce schema that accepts a valid Task filter body" in new TaskSchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "pagination": { "page": 1, "perPage": 100 },
        |  "since": "2020-01-01T00:00:00.000Z",
        |  "status": [ "open", "completed" ],
        |  "owner": 1,
        |  "category": 2,
        |  "dueFrom": "2020-01-02T00:00:00.000Z",
        |  "dueTo": "2020-01-03T00:00:00.000Z",
        |  "repeating": true,
        |  "relatedTo": [ "party", "opportunity", "kase" ]
        |}
        |""".stripMargin

    val errors: List[ValidationError] = schema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors shouldBe Nil
  }

  it should "produce schema that rejects an invalid Task filter body" in new TaskSchemasFixture {
    val exampleBody: String =
      """
        |{
        |  "repeating": "true"
        |}
        |""".stripMargin

    val errors: List[ValidationError] = schema.validate(exampleBody, InputFormat.JSON).asScala.toList

    errors should not be empty
    errors.head.getEvaluationPath.toString should include("repeating")
  }

  private trait TaskSchemasFixture:
    private val schemaRegistry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7)
    protected val schema: Schema = schemaRegistry.getSchema(TaskSchemas.listTasksSchema)
