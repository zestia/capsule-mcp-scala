package com.zestia.capsulemcp.model

import zio.json.*

case class ContactsResponse(parties: List[Party], meta: Meta)
    derives JsonDecoder,
      JsonEncoder

case class OpportunitiesResponse(opportunities: List[Opportunity], meta: Meta)
    derives JsonDecoder,
      JsonEncoder

case class ProjectsResponse(kases: List[Project], meta: Meta)
    derives JsonDecoder,
      JsonEncoder

case class FieldDefinitionsResponse(definitions: List[FieldDefinition])
    derives JsonDecoder,
      JsonEncoder

case class UsersResponse(users: List[User]) derives JsonDecoder, JsonEncoder

case class TeamsResponse(teams: List[Team]) derives JsonDecoder, JsonEncoder

case class ResponseWrapper(result: String, meta: Meta) derives JsonEncoder
