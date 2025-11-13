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
