package com.zestia.capsulemcp.model

import zio.json.*

// --- Models specific to Project ----------------------------------------

final case class Stage(
    id: Long,
    name: String
) derives JsonDecoder,
      JsonEncoder

// --- Project -----------------------------------------------------------

final case class Project(
    id: Long,
    party: Party,
    name: String,
    description: Option[String],
    owner: Option[User],
    team: Option[Team],
    status: Option[String],
    opportunity: Option[Opportunity],
    stage: Option[Stage],
    createdAt: Option[String],
    updatedAt: Option[String],
    expectedCloseOn: Option[String],
    closedOn: Option[String],
    lastContactedAt: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    missingImportantFields: Option[Boolean]
) derives JsonDecoder,
      JsonEncoder
