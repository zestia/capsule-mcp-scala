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

package com.zestia.capsulemcp.model

import zio.json.*

case class ResponseWrapper[T](response: T, paginationHasMore: Option[Boolean]) derives JsonDecoder, JsonEncoder

case class ContactListWrapper(parties: List[Party], meta: Meta) derives JsonDecoder, JsonEncoder
case class ContactWrapper(party: Party) derives JsonDecoder, JsonEncoder

case class OpportunityListWrapper(opportunities: List[Opportunity], meta: Meta) derives JsonDecoder, JsonEncoder
case class OpportunityWrapper(opportunity: Opportunity) derives JsonDecoder, JsonEncoder

case class OpportunityValueMeta(currency: String) derives JsonDecoder, JsonEncoder

case class OpportunityValueWrapper(totalValue: Double, projectedValue: Double, meta: OpportunityValueMeta)
    derives JsonDecoder,
      JsonEncoder

case class ProjectListWrapper(kases: List[Project], meta: Meta) derives JsonDecoder, JsonEncoder
case class ProjectWrapper(kase: Project) derives JsonDecoder, JsonEncoder

case class FieldDefinitionListWrapper(definitions: List[FieldDefinition], meta: Meta) derives JsonDecoder, JsonEncoder
case class DataTagFieldDefinitionListWrapper(definitions: List[FieldDefinition]) derives JsonDecoder, JsonEncoder
case class FieldDefinitionWrapper(definition: FieldDefinition) derives JsonDecoder, JsonEncoder

case class TagDefinitionListWrapper(tags: List[Tag], meta: Meta) derives JsonDecoder, JsonEncoder
case class TagDefinitionWrapper(tag: Tag) derives JsonDecoder, JsonEncoder

case class PipelineListWrapper(pipelines: List[Pipeline], meta: Meta) derives JsonDecoder, JsonEncoder
case class PipelineWrapper(pipeline: Pipeline) derives JsonDecoder, JsonEncoder

case class MilestoneListWrapper(milestones: List[Milestone], meta: Meta) derives JsonDecoder, JsonEncoder
case class MilestoneWrapper(milestone: Milestone) derives JsonDecoder, JsonEncoder

case class LostReasonListWrapper(lostReasons: List[LostReason], meta: Meta) derives JsonDecoder, JsonEncoder
case class LostReasonWrapper(lostReason: LostReason) derives JsonDecoder, JsonEncoder

case class BoardListWrapper(boards: List[Board], meta: Meta) derives JsonDecoder, JsonEncoder
case class BoardWrapper(board: Board) derives JsonDecoder, JsonEncoder

case class StageListWrapper(stages: List[Stage], meta: Meta) derives JsonDecoder, JsonEncoder
case class StageWrapper(stage: Stage) derives JsonDecoder, JsonEncoder

case class UserListWrapper(users: List[User]) derives JsonDecoder, JsonEncoder
case class UserWrapper(user: User) derives JsonDecoder, JsonEncoder

case class TeamListWrapper(teams: List[Team]) derives JsonDecoder, JsonEncoder
case class TeamWrapper(team: Team) derives JsonDecoder, JsonEncoder

case class ActivityListWrapper(activities: List[Activity], meta: Meta) derives JsonDecoder, JsonEncoder

case class EntryListWrapper(entries: List[Entry]) derives JsonDecoder, JsonEncoder
case class EntryWrapper(entry: Entry) derives JsonDecoder, JsonEncoder

case class TaskListWrapper(tasks: List[Task], meta: Meta) derives JsonDecoder, JsonEncoder
case class TaskWrapper(task: Task) derives JsonDecoder, JsonEncoder

case class TrackListWrapper(tracks: List[Track]) derives JsonDecoder, JsonEncoder
case class TrackWrapper(track: Track) derives JsonDecoder, JsonEncoder
