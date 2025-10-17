package com.zestia.capsulemcp.model

import com.zestia.capsulemcp.model.*
import zio.json.{JsonDecoder, JsonEncoder}

final case class TaskCategory(
    id: Long,
    name: String
) derives JsonDecoder,
      JsonEncoder

final case class TaskRepeat(
    frequency: String,
    interval: Int,
    repeatOn: String
) derives JsonDecoder,
      JsonEncoder

final case class Task(
    id: Long,
    category: Option[TaskCategory],
    description: Option[String],
    detail: Option[String],
    dueDate: Option[String],
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
