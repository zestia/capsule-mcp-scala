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

import com.zestia.capsulemcp.model.*
import zio.json.{JsonDecoder, JsonEncoder}

final case class Track(
    id: Long,
    tasks: Option[List[Task]],
    description: Option[String],
    trackDateOn: Option[String],
    direction: Option[String],
    definition: Option[TrackDefinition],
    kase: Option[Project],
    opportunity: Option[Opportunity]
) derives JsonDecoder,
      JsonEncoder

final case class TrackDefinition(
    id: Long,
    description: Option[String],
    tag: Option[String],
    captureRule: Option[String],
    direction: Option[String],
    createdAt: Option[String],
    updatedAt: Option[String],
    taskDefinitions: Option[List[TrackTaskDefinition]]
) derives JsonDecoder,
      JsonEncoder

final case class TrackTaskDefinition(
    id: Long,
    description: Option[String],
    daysAfterRule: Option[String],
    daysAfter: Option[Long],
    displayOrder: Option[Long],
    dayDelayRule: Option[String],
    category: Option[TaskCategory],
    trackTaskAssignee: Option[TrackTaskAssignee],
    createdAt: Option[String],
    updatedAt: Option[String]
) derives JsonDecoder,
      JsonEncoder

final case class TrackTaskAssignee(
    assigneeType: String,
    owner: Option[User]
) derives JsonDecoder,
      JsonEncoder
