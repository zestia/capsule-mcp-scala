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

case class ContactListWrapper(parties: List[Party], meta: Meta) derives JsonDecoder, JsonEncoder

case class OpportunityListWrapper(opportunities: List[Opportunity], meta: Meta) derives JsonDecoder, JsonEncoder

case class OpportunityValueMeta(currency: String) derives JsonDecoder, JsonEncoder

case class OpportunityValueWrapper(totalValue: Double, projectedValue: Double, meta: OpportunityValueMeta)
    derives JsonDecoder,
      JsonEncoder

case class ProjectListWrapper(kases: List[Project], meta: Meta) derives JsonDecoder, JsonEncoder

case class FieldDefinitionListWrapper(definitions: List[FieldDefinition], meta: Meta) derives JsonDecoder, JsonEncoder

case class TagDefinitionListWrapper(tags: List[Tag], meta: Meta) derives JsonDecoder, JsonEncoder

case class PipelineListWrapper(pipelines: List[Pipeline], meta: Meta) derives JsonDecoder, JsonEncoder

case class MilestoneListWrapper(milestones: List[Milestone], meta: Meta) derives JsonDecoder, JsonEncoder

case class LostReasonListWrapper(lostReasons: List[Milestone], meta: Meta) derives JsonDecoder, JsonEncoder

case class BoardListWrapper(boards: List[Board], meta: Meta) derives JsonDecoder, JsonEncoder

case class StageListWrapper(stages: List[Stage], meta: Meta) derives JsonDecoder, JsonEncoder

case class UserListWrapper(users: List[User]) derives JsonDecoder, JsonEncoder

case class TeamListWrapper(teams: List[Team]) derives JsonDecoder, JsonEncoder

case class ActivityListWrapper(activities: List[Activity], meta: Meta) derives JsonDecoder, JsonEncoder

case class TaskListWrapper(tasks: List[Task], meta: Meta) derives JsonDecoder, JsonEncoder

case class TrackListWrapper(tracks: List[Track]) derives JsonDecoder, JsonEncoder

case class TrackWrapper(track: Track) derives JsonDecoder, JsonEncoder
