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

import com.zestia.capsulemcp.server.schemas.SchemaTypes.{ValueType, *}

/**
 * JSON Schemas for Project-related Tools
 */
object ProjectSchemas extends HasFilterSchema with HasCustomFieldFilterFields:

  override protected val filterFields: List[FilterField] = List(
    ExactMatchFilterField("id", "Project ID", ValueType.Number),
    StringFilterField("name", "Project name"),
    ExactMatchFilterField("status", "Project status (open, closed)", ValueType.String),
    ExactMatchFilterField("primaryContact", "Primary contact ID", ValueType.Number),
    StringFilterField("primaryContactName", "Primary contact name"),
    ExactMatchFilterField("board", "Board name", ValueType.String),
    ExactMatchFilterField("board", "Board ID", ValueType.Number),
    ExactMatchFilterField("stage", "Stage name", ValueType.String),
    ExactMatchFilterField("stage", "Stage ID", ValueType.Number),
    ExactMatchFilterField("tag", "Tag ID", ValueType.Number),
    ExactMatchFilterField("tag", "Tag name", ValueType.String),
    ExactMatchFilterField("owner", "User owner name", ValueType.String),
    ExactMatchFilterField("owner", "User owner ID", ValueType.Number),
    ExactMatchFilterField("team", "Team owner name", ValueType.String),
    ExactMatchFilterField("team", "Team owner ID", ValueType.Number),
    BooleanFilterField("isOpen", "Is open"),
    BooleanFilterField("hasTags", "Has tags"),
    BooleanFilterField("hasOpenTasks", "Has open tasks"),
    DateFilterField("addedOn", "Date added"),
    DateFilterField("updatedOn", "Date updated"),
    DateFilterField("closedOn", "Date closed on"),
    DateFilterField("expectedCloseOn", "Expected date close on"),
    DateFilterField("startOn", "Start on date")
  ) ++ customFieldFilterFields
