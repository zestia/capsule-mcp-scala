package com.zestia.capsulemcp.model

import zio.json.{JsonDecoder, JsonEncoder}

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/nested_activity_type"</a>
 */
final case class ActivityType(
    id: Long,
    name: String
) derives JsonDecoder,
      JsonEncoder

final case class Activity(
    id: Long,
    activityType: ActivityType,
    createdAt: Option[String],
    createdAtLocalised: Option[String],
    user: User,
    entry: Option[Entry],
    task: Option[Task],
    kase: Option[Project],
    opportunity: Option[Opportunity],
    parties: Option[List[Party]],
    deleted: Option[Boolean],
    isRestricted: Option[Boolean]
) derives JsonDecoder,
      JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/entry"</a>
 */
final case class Entry(
    id: Long,
    `type`: String,
    createdAt: Option[String],
    updatedAt: Option[String],
    entryAt: Option[String],
    subject: Option[String],
    content: Option[String],
    creator: Option[User],
    activityType: Option[ActivityType],
    kase: Option[Project],
    opportunity: Option[Opportunity],
    party: Option[Party],
    parties: Option[List[Party]],
    participants: Option[List[Participant]],
    attachments: Option[List[Attachment]]
) derives JsonDecoder,
      JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/participant"</a>
 */
final case class Attachment(
    id: Long,
    token: Option[String],
    filename: Option[String],
    contentType: Option[String],
    size: Option[Int]
) derives JsonDecoder,
      JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/participant"</a>
 */
final case class Participant(
    id: Long,
    address: Option[String],
    name: Option[String],
    role: Option[String]
) derives JsonDecoder,
      JsonEncoder
