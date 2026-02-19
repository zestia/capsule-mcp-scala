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

package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.*

/**
 * JSON Schemas for Opportunity-related Tools
 */
object OpportunitySchemas extends HasFilterSchema with HasCustomFieldFilterFields:

  override protected val filterFields: List[FilterField] = List(
    ExactMatchFilterField("id", "Opportunity ID", ValueType.Number),
    StringFilterField("name", "Opportunity name"),
    ExactMatchFilterField("status", "Opportunity status (open, won, lost)", ValueType.String),
    ExactMatchFilterField("party", "Associated contact ID", ValueType.Number),
    ExactMatchFilterField("primaryContact", "Primary contact ID", ValueType.Number),
    StringFilterField("primaryContactName", "Primary contact name"),
    ExactMatchFilterField("pipeline", "Pipeline name", ValueType.String),
    ExactMatchFilterField("pipeline", "Pipeline ID", ValueType.Number),
    ExactMatchFilterField("milestone", "Milestone name", ValueType.String),
    ExactMatchFilterField("milestone", "Milestone ID", ValueType.Number),
    NumberFilterField("probability", "Probability (0-100)"),
    ExactMatchFilterField("lostReason", "Lost reason name", ValueType.String),
    ExactMatchFilterField("lostReason", "Lost reason ID", ValueType.Number),
    ExactMatchFilterField("currency", "Currency", ValueType.String),
    MoneyFilterField("expectedValue", "Expected value"),
    ExactMatchFilterField("tag", "Tag ID", ValueType.Number),
    ExactMatchFilterField("tag", "Tag name", ValueType.String),
    ExactMatchFilterField("owner", "User owner name", ValueType.String),
    ExactMatchFilterField("owner", "User owner ID", ValueType.Number),
    ExactMatchFilterField("team", "Team owner name", ValueType.String),
    ExactMatchFilterField("team", "Team owner ID", ValueType.Number),
    BooleanFilterField("isOpen", "Is open"),
    BooleanFilterField("isStale", "Is stale"),
    BooleanFilterField("isOwnedByMe", "Is owned by me"),
    BooleanFilterField("hasTags", "Has tags"),
    BooleanFilterField("includedForConversion", "Is included in conversion rate"),
    BooleanFilterField("hasOpenTasks", "Has open tasks"),
    DateFilterField("addedOn", "Date added"),
    DateFilterField("updatedOn", "Date updated"),
    DateFilterField("closedOn", "Date closed on"),
    DateFilterField("expectedCloseOn", "Expected date close on"),
    DateFilterField("lastContactedOn", "Last contacted date")
  ) ++ customFieldFilterFields
