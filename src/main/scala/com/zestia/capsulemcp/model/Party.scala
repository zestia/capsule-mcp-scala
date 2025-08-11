package com.zestia.capsulemcp.model

import zio.json.*

// --- Models specific to Party ------------------------------------------

final case class Address(
    id: Long,
    `type`: String,
    street: Option[String],
    city: Option[String],
    state: Option[String],
    country: Option[String],
    zip: Option[String]
) derives JsonDecoder,
      JsonEncoder

final case class EmailAddress(
    id: Long,
    `type`: Option[String],
    address: String
) derives JsonDecoder,
      JsonEncoder

final case class PhoneNumber(
    id: Long,
    `type`: Option[String],
    number: String
) derives JsonDecoder,
      JsonEncoder

final case class Website(
    id: Long,
    service: String,
    address: String,
    `type`: Option[String],
    url: String
) derives JsonDecoder,
      JsonEncoder

enum PartyType:
  case person, organisation

// --- Party -------------------------------------------------------------

@jsonDiscriminator("type")
sealed trait Party derives JsonDecoder, JsonEncoder:
  val id: Long
  val `type`: String
  val about: Option[String]
  val createdAt: Option[String]
  val updatedAt: Option[String]
  val lastContactedAt: Option[String]
  val addresses: Option[List[Address]]
  val phoneNumbers: Option[List[PhoneNumber]]
  val websites: Option[List[Website]]
  val emailAddresses: Option[List[EmailAddress]]
  val pictureURL: Option[String]
  val tags: Option[List[Tag]]
  val fields: Option[List[FieldValue]]
  val owner: Option[User]
  val team: Option[Team]
  val missingImportantFields: Option[Boolean]

@jsonHint(PartyType.person.toString)
final case class Person(
    id: Long,
    `type`: String = PartyType.person.toString,
    firstName: Option[String],
    lastName: Option[String],
    title: Option[String],
    jobTitle: Option[String],
    organisation: Option[Organisation],
    about: Option[String],
    createdAt: Option[String],
    updatedAt: Option[String],
    lastContactedAt: Option[String],
    addresses: Option[List[Address]],
    phoneNumbers: Option[List[PhoneNumber]],
    websites: Option[List[Website]],
    emailAddresses: Option[List[EmailAddress]],
    pictureURL: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    owner: Option[User],
    team: Option[Team],
    missingImportantFields: Option[Boolean]
) extends Party derives JsonDecoder, JsonEncoder

@jsonHint(PartyType.organisation.toString)
final case class Organisation(
    id: Long,
    `type`: String = PartyType.organisation.toString,
    name: Option[String],
    about: Option[String],
    createdAt: Option[String],
    updatedAt: Option[String],
    lastContactedAt: Option[String],
    addresses: Option[List[Address]],
    phoneNumbers: Option[List[PhoneNumber]],
    websites: Option[List[Website]],
    emailAddresses: Option[List[EmailAddress]],
    pictureURL: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    owner: Option[User],
    team: Option[Team],
    missingImportantFields: Option[Boolean]
) extends Party derives JsonDecoder, JsonEncoder
