package com.zestia.capsulemcp.model

import zio.json.*

case class Team(
    id: Long,
    name: Option[String],
    memberships: Option[List[TeamMembership]]
) derives JsonDecoder,
      JsonEncoder

case class TeamMembership(
    user: User
) derives JsonDecoder,
      JsonEncoder
