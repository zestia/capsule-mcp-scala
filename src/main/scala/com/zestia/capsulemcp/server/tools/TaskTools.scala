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

package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.macros.MapToFunctionMacro
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.server.schemas.TaskSchemas
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.*
import zio.json.*

object TaskTools extends HasManualTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Task#listTasks"</a>
   */
  private def listTasks(
      pagination: Option[Pagination],
      since: Option[String],
      status: Option[List[String]],
      owner: Option[Int],
      category: Option[Int],
      dueFrom: Option[String],
      dueTo: Option[String],
      repeating: Option[Boolean],
      relatedTo: Option[List[String]]
  ): String = {
    val queryParams = List(
      since.map("since" -> _),
      status.map(list => "status" -> list.mkString(",")),
      owner.map("owner" -> _),
      category.map("category" -> _),
      dueFrom.map("dueFrom" -> _),
      dueTo.map("dueTo" -> _),
      repeating.map("repeating" -> _),
      relatedTo.map(list => "relatedTo" -> list.mkString(","))
    ).flatten.toMap

    getRequest[TaskListWrapper]("tasks", pagination.getOrElse(Pagination()), queryParams).toJson
  }

  /**
   * Register manual tools that need custom schemas (workaround for annotation bugs)
   */
  override def registerManualTools(server: FastMcpServer): ZIO[Any, Throwable, Unit] =
    for
    // Register list_tasks
    _ <- {
      server.tool(
        name = "list_tasks",
        description = Some(
          "List Tasks with optional filtering. By default only Tasks with a status of 'OPEN' are returned."
        ),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listTasks)(args)),
        inputSchema = Right(TaskSchemas.listTasksSchema)
      )
    }
    // Add more manual tool registrations here
    // _ <- server.tool(...)
    yield ()
