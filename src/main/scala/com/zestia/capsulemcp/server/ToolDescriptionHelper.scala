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
      | Operator Reference:
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
      | $entityFieldReference
      |""".stripMargin

  val contactFieldReference: String =
    """
       | Contact Field Reference:
       | Name,Type,Description
       |id,Long (Read only),"The unique id of this party."
       |type,String (Required),"Represents if this party is a person or an organisation. Accepted values: person, organisation."
       |firstName,String,"The first name of the person. Present only when type is person."
       |lastName,String,"The last name of the person. Present only when type is person."
       |title,String,"The title of the person. Present only when type is person. Accepted values: existing custom titles."
       |jobTitle,String,"The job title of the person. Present only when type is person."
       |organisation,Nested Party,"The organisation this party is associated with. Present only when type is person."
       |name,String,"The name of the organisation. Present only when type is organisation."
       |about,String,"A short description of the party."
       |createdAt,Date (Read only),"The ISO date/time when this party was created."
       |updatedAt,Date (Read only),"The ISO date/time when this party was last updated."
       |lastContactedAt,Date (Read only),"The ISO date/time when this party was last contacted."
       |addresses,Array of Address,"An array of all the addresses associated with this party."
       |phoneNumbers,Array of Phone Number,"An array of all the phone numbers associated with this party."
       |websites,Array of Website,"An array of the websites and social network accounts associated with this party."
       |emailAddresses,Array of Email Address,"An array of all the email addresses associated with this party."
       |pictureURL,String (Read only),"A URL representing the location of the profile picture for this party. Automatically derived by Capsule."
       |tags,Array of Nested Tag,"An array of tags added to this party"
       |fields,Array of Field Value,"An array of custom fields defined for this party"
       |owner,Nested User,"The user this party is assigned to."
       |team,Nested Team,"The team this party is assigned to."
       |missingImportantFields,Boolean,"Indicates if this party has any Important custom fields missing a value"
       |""".stripMargin

  val opportunityFieldReference: String =
    """
      |Opportunity Field Reference:
      |Name,Type,Description
      |id,Long (Read only),"The unique id of this opportunity."
      |createdAt,Date (Read only),"The ISO date/time this opportunity was created."
      |updatedAt,Date (Read only),"The ISO date/time when this opportunity was last updated."
      |party,Nested Party (Required),"The main contact for this opportunity."
      |lostReason,Nested Lost Reason,"Optional lost reason that can be applied to opportunities that have been lost. These are used as a reporting category."
      |name,String (Required),"The name of this opportunity."
      |description,String,"The description of this opportunity."
      |owner,Nested User,"The user this opportunity is assigned to. This and/or team is required."
      |team,Nested Team,"The team this opportunity is assigned to. This and/or owner is required."
      |milestone,Nested Milestone (Required),"The milestone this opportunity belongs to."
      |value,Opportunity Value,"The value of this opportunity."
      |expectedCloseOn,Date,"The expected close date of this opportunity."
      |probability,Long,"The probability of winning this opportunity."
      |durationBasis,String,"The time unit used by the duration field. Accepted values: FIXED, HOUR, DAY, WEEK, MONTH, QUARTER, YEAR."
      |duration,Integer,"The duration of this opportunity. Must be null if durationBasis is set to FIXED."
      |closedOn,Date,"The date this opportunity was closed."
      |tags,Array of Nested Tag,"An array of tags that are applied to this opportunity"
      |fields,Array of Field Value,"An array of custom fields that are defined for this opportunity"
      |lastContactedAt,Date (Read only),"The ISO date/time when this opportunity was last contacted."
      |lastStageChangedAt,Date (Read only),"The ISO date/time when this opportunity last had its milestone changed."
      |lastOpenMilestone,Nested Milestone (Read only),"The last milestone selected on the opportunity while the opportunity was open."
      |missingImportantFields,Boolean,"Indicates if this opportunity has any Important custom fields that are missing a value"
      |""".stripMargin

  val projectFieldReference: String =
    """
      |Project Field Reference:
      |Name,Type,Description
      |id,Long (Read only),"The unique id of this project."
      |party,Nested Party (Required),"The main contact for this project."
      |name,String (Required),"The name of this project."
      |description,String,"The description of this project."
      |owner,Nested User,"The user this project is assigned to. This and/or team is required."
      |team,Nested Team,"The team this project is assigned to. This and/or owner is required."
      |status,String,"The status of the project. Accepted values: OPEN, CLOSED."
      |opportunity,Nested Opportunity,"An optional link to the opportunity that this project was created to support."
      |stage,Nested Stage,"The stage that this project is on."
      |createdAt,Date (Read only),"The ISO date/time when this project was created."
      |updatedAt,Date (Read only),"The ISO date/time when this project was last updated."
      |expectedCloseOn,Date,"The expected close date of this project."
      |closedOn,Date (Read only),"The ISO date when this project was closed."
      |lastContactedAt,Date (Read only),"The ISO date/time when this project was last contacted. Automatically set by Capsule based on attached history entries."
      |tags,Array of Nested Tag,"An array of tags added to this project"
      |fields,Array of Field Value,"An array of custom fields defined for this project"
      |missingImportantFields,Boolean,"Indicates if this project has any Important custom fields missing a value"
      |""".stripMargin
}
