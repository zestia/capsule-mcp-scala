/*
 * Copyright 2025 Zestia Ltd
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

import zio.json.*

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/address"</a>
 */
final case class Address(
    id: Long,
    `type`: Option[String],
    street: Option[String],
    city: Option[String],
    state: Option[String],
    country: Option[String],
    zip: Option[String]
) derives JsonDecoder,
      JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/email_address"</a>
 */
final case class EmailAddress(id: Long, `type`: Option[String], address: String) derives JsonDecoder, JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/phone_number"</a>
 */
final case class PhoneNumber(id: Long, `type`: Option[String], number: String) derives JsonDecoder, JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/website"</a>
 */
final case class Website(id: Long, service: String, address: String, `type`: Option[String], url: String)
    derives JsonDecoder,
      JsonEncoder

enum PartyType:
  case person, organisation

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/party"</a>
 */
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
