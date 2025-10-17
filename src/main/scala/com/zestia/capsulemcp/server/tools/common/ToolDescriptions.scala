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

package com.zestia.capsulemcp.server.tools.common

object ToolDescriptions:
  def searchToolDescription(
      entityType: String,
      entityFieldReference: String
  ): String = s"""
      |The `search_$entityType` tool allows you to filter $entityType using a list of structured conditions. You can include as
      |many conditions as needed.
      |
      |Each `condition` must specify:
      |  - `field`: the contact field to filter on (e.g. `name`, `id`, `addedOn`)
      |  - `operator`: the type of comparison (e.g. `is`, `is not`, `contains`, `is after`)
      |  - `value`: the value to match against
      |  - `type`: the type of the field value
      |
      |You can also customise pagination to fetch more pages of results:
      |  - `page`: the page number to return (default: 1)
      |  - `perPage`: the number of results per page (default: 100)
      |
      |Example:
      |{
      |  "conditions": [
      |    {
      |      "fieldName": "name",
      |      "operator": "contains",
      |      "fieldValue": "John"
      |    }
      |  ],
      |  "pagination": {
      |    "page": 1,
      |    "perPage": 100
      |  }
      |}
      |
      |Operator Reference:
      |Operator,Supported Field Types,Type of Value
      |is,"Boolean, date, number, string",As field type
      |is not,"Boolean, date, number, string",As field type
      |starts with,String,String
      |ends with,String,String
      |contains,String,String
      |is greater than,Number,Number
      |is less than,Number,Number
      |is after,Date,Date
      |is before,Date,Date
      |is older than,Date,"Number (of days)"
      |is within last,Date,"Number (of days)"
      |is within next,Date,"Number (of days)"
      |
      |$entityFieldReference
      |""".stripMargin

  val contactFieldReference: String =
    """
      |Contact Search Field Reference:
      |Field,Type
      |id,Number
      |name,String
      |jobTitle,String
      |email,String
      |phone,String
      |city,String
      |zip,String
      |state,String
      |country,String
      |type,String
      |tag,"Number, string"
      |owner,Number
      |team,Number
      |hasEmailAddress,Boolean
      |hasPhoneNumber,Boolean
      |hasAddress,Boolean
      |hasPeople,Boolean
      |hasTags,Boolean
      |addedOn,Date
      |updatedOn,Date
      |lastContactedOn,Date
      |custom:{customFieldId},"Boolean, date, number, string"
      |org.custom:{customFieldId},"Boolean, date, number, string"
      |org.name,String
      |org.tag,"Number, string"
      |""".stripMargin

  val opportunityFieldReference: String =
    """
      |Opportunity Search Field Reference:
      |Field,Type
      |id,Number
      |name,String
      |status,String
      |pipeline,"Number, string"
      |milestone,String
      |probability,Number
      |lostReason,String
      |currency,String
      |expectedValue,String
      |tag,"Number, string"
      |owner,Number
      |team,Number
      |isOpen,Boolean
      |isStale,Boolean
      |isOwnedByMe,Boolean
      |hasTags,Boolean
      |addedOn,Date
      |updatedOn,Date
      |closedOn,Date
      |expectedCloseOn,Date
      |custom:{customFieldId},"Boolean, date, number, string"
      |""".stripMargin

  val projectFieldReference: String =
    """
      |Project Search Field Reference:
      |Field,Type
      |id,Number
      |name,String
      |status,String
      |board,"Number, string"
      |stage,"Number, string"
      |tag,"Number, string"
      |owner,Number
      |team,Number
      |isOpen,Boolean
      |isOwnedByMe,Boolean
      |hasTags,Boolean
      |addedOn,Date
      |updatedOn,Date
      |closedOn,Date
      |expectedCloseOn,Date
      |custom:{customFieldId},"Boolean, date, number, string"
      |""".stripMargin

  val activityFieldReference: String =
    """
      |Activity Search Field Reference:
      |Field,Type
      |user,Number
      |taskCategory,Number
      |activityType,Number
      |addedOn,Date
      |""".stripMargin

  val calculateValueOfOpportunitiesToolDescription: String =
    s"""
      |Returns the calculated Total Value (based off 100% success) and Projected Value (based off Opportunity probability) of Opportunities, converted to the current user's currency.
      |The `filter` parameter allows you to filter the Opportunities to calculate the values for, e.g. by Sales Pipeline, assignee or Milestone.
      |You can include as many conditions as needed. The values returned are converted into the current user's currency.
      |
      |Each `condition` must specify:
      |  - `field`: the contact field to filter on (e.g. `name`, `id`, `addedOn`)
      |  - `operator`: the type of comparison (e.g. `is`, `is not`, `contains`, `is after`)
      |  - `value`: the value to match against
      |  - `type`: the type of the field value
      |
      |Example:
      |{
      |  "conditions": [
      |    {
      |      "fieldName": "name",
      |      "operator": "contains",
      |      "fieldValue": "John"
      |    }
      |  ]
      |}
      |
      |Operator Reference:
      |Operator,Supported Field Types,Type of Value
      |is,"Boolean, date, number, string",As field type
      |is not,"Boolean, date, number, string",As field type
      |starts with,String,String
      |ends with,String,String
      |contains,String,String
      |is greater than,Number,Number
      |is less than,Number,Number
      |is after,Date,Date
      |is before,Date,Date
      |is older than,Date,"Number (of days)"
      |is within last,Date,"Number (of days)"
      |is within next,Date,"Number (of days)"
      |
      |$opportunityFieldReference
      |""".stripMargin
