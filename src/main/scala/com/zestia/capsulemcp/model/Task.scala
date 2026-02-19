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

final case class TaskCategory(id: Long, name: String) derives JsonDecoder, JsonEncoder

final case class TaskRepeat(frequency: String, interval: Int, repeatOn: String) derives JsonDecoder, JsonEncoder

final case class Task(
    id: Long,
    category: Option[TaskCategory],
    description: Option[String],
    detail: Option[String],
    dueOn: Option[String],
    dueTime: Option[String],
    status: Option[String],
    completedBy: Option[String],
    completedAt: Option[String],
    party: Option[Party],
    kase: Option[Project],
    opportunity: Option[Opportunity],
    owner: Option[User],
    daysAfter: Option[Int],
    taskDelayRule: Option[String],
    taskDayDelayRule: Option[String],
    nextTask: Option[Task],
    active: Option[Boolean],
    repeatDetails: Option[TaskRepeat],
    hasTrack: Option[Boolean]
) derives JsonDecoder,
      JsonEncoder
