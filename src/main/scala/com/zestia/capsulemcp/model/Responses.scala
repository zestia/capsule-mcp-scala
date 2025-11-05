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

package com.zestia.capsulemcp.model

import zio.json.*

case class ContactsResponse(parties: List[Party], meta: Meta) derives JsonDecoder, JsonEncoder

case class OpportunitiesResponse(opportunities: List[Opportunity], meta: Meta) derives JsonDecoder, JsonEncoder

case class OpportunityValueMeta(currency: String) derives JsonDecoder, JsonEncoder

case class OpportunityValueResponse(totalValue: Double, projectedValue: Double, meta: OpportunityValueMeta)
    derives JsonDecoder,
      JsonEncoder

case class ProjectsResponse(kases: List[Project], meta: Meta) derives JsonDecoder, JsonEncoder

case class FieldDefinitionsResponse(definitions: List[FieldDefinition], meta: Meta) derives JsonDecoder, JsonEncoder

case class TagDefinitionsResponse(tags: List[Tag], meta: Meta) derives JsonDecoder, JsonEncoder

case class PipelinesResponse(pipelines: List[Pipeline], meta: Meta) derives JsonDecoder, JsonEncoder

case class MilestonesResponse(milestones: List[Milestone], meta: Meta) derives JsonDecoder, JsonEncoder

case class LostReasonsResponse(lostReasons: List[Milestone], meta: Meta) derives JsonDecoder, JsonEncoder

case class BoardsResponse(boards: List[Board], meta: Meta) derives JsonDecoder, JsonEncoder

case class StagesResponse(stages: List[Stage], meta: Meta) derives JsonDecoder, JsonEncoder

case class UsersResponse(users: List[User]) derives JsonDecoder, JsonEncoder

case class TeamsResponse(teams: List[Team]) derives JsonDecoder, JsonEncoder

case class ActivitiesResponse(activities: List[Activity], meta: Meta) derives JsonDecoder, JsonEncoder

case class TasksResponse(tasks: List[Task], meta: Meta) derives JsonDecoder, JsonEncoder
