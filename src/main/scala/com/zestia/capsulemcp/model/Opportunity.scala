package com.zestia.capsulemcp.model

import zio.json.*

// --- Models specific to Opportunity ------------------------------------

final case class LostReason(
    id: Long,
    name: String
) derives JsonDecoder,
      JsonEncoder

final case class OpportunityValue(
    amount: Option[Double],
    currency: Option[String]
) derives JsonDecoder,
      JsonEncoder

final case class Pipeline(
    id: Long,
    name: String
) derives JsonDecoder,
      JsonEncoder

final case class Milestone(
    id: Long,
    name: String,
    description: Option[String],
    complete: Option[Boolean],
    probability: Option[Long],
    daysUntilStale: Option[Int],
    pipeline: Option[Pipeline]
) derives JsonDecoder,
      JsonEncoder

enum DurationBasis derives JsonDecoder, JsonEncoder:
  case FIXED, HOUR, DAY, WEEK, MONTH, QUARTER, YEAR

// --- Opportunity -------------------------------------------------------

final case class Opportunity(
    id: Long,
    createdAt: Option[String],
    updatedAt: Option[String],
    party: Option[Party],
    name: String,
    milestone: Option[Milestone],
    lostReason: Option[LostReason],
    description: Option[String],
    owner: Option[User],
    team: Option[Team],
    value: Option[OpportunityValue],
    expectedCloseOn: Option[String],
    probability: Option[Long],
    durationBasis: Option[DurationBasis],
    duration: Option[Int],
    closedOn: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    lastContactedAt: Option[String],
    lastStageChangedAt: Option[String],
    lastOpenMilestone: Option[Milestone],
    missingImportantFields: Option[Boolean]
) derives JsonDecoder,
      JsonEncoder
