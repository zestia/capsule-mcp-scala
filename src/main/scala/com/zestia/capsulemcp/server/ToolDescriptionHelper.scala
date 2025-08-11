package com.zestia.capsulemcp.server

object ToolDescriptionHelper {
  def searchToolDescription(
      entityType: String,
      entityFieldReference: String
  ): String = s"""
      |The `search_$entityType` tool allows you to filter contacts using a list of structured conditions. You can include as
      |many conditions as needed.
      |
      |Each `condition` must specify:
      |  - `field`: the contact field to filter on (e.g. `name`, `id`, `addedOn`)
      |  - `operator`: the type of comparison (e.g. `is`, `is not`, `contains`, `is after`)
      |  - `value`: the value to match against
      |  - `type`: the type of the field value
      |
      |You can also optionally apply pagination:
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
}
