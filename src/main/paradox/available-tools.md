# Available Tools
This MCP server provides **complete read-only access** to your Capsule CRM:

| **Data Type**       | **Tool Name**                                 | **Description**                                                                           | **Paginated Response?** | 
|---------------------|-----------------------------------------------|-------------------------------------------------------------------------------------------|-------------------------|
| **Contacts**        | `list_contacts`                               | Retrieve Contacts with comprehensive filtering ability                                    | ✅                       |
|                     | `get_contact`                                 | Get Contact by ID                                                                         | ❌                       |
| **Opportunities**   | `list_opportunities`                          | Retrieve Opportunities with comprehensive filtering ability                               | ✅                       |
|                     | `get_opportunity`                             | Get Opportunity by ID                                                                     | ❌                       |
|                     | `calculate_value_of_opportunities`            | Retrieves Total & Projected Values for Opportunities with comprehensive filtering ability | ❌                       |
| **Projects**        | `list_projects`                               | Retrieve Projects with comprehensive filtering ability                                    | ✅                       |
| **Custom Fields**   | `list_contact_custom_fields`                  | Retrieves Custom Fields defined for Contacts                                              | ✅                       |
|                     | `list_opportunity_custom_fields`              | Retrieves Custom Fields defined for Opportunities                                         | ✅                       |
|                     | `list_project_custom_fields`                  | Retrieves Custom Fields defined for Projects                                              | ✅                       |
|                     | `get_contact_custom_field`                    | Get Contact Custom Field Definition by ID                                                 | ❌                       |
|                     | `get_opportunity_custom_field`                | Get Opportunity Custom Field Definition by ID                                             | ❌                       |
|                     | `get_project_custom_field`                    | Get Project Custom Field Definition by ID                                                 | ❌                       |
|                     | `list_custom_fields_for_contact_data_tag`     | List Custom Fields defined for a Contact DataTag                                          | ✅                       |
|                     | `list_custom_fields_for_opportunity_data_tag` | List Custom Fields defined for an Opportunity DataTag                                     | ✅                       |
|                     | `list_custom_fields_for_project_data_tag`     | List Custom Fields defined for a Project DataTag                                          | ✅                       |
| **Sales Pipelines** | `list_pipelines`                              | Retrieves Sales Pipelines defined for Opportunities with optional searching by name       | ✅                       |
|                     | `get_pipeline`                                | Get Pipeline by ID                                                                        | ❌                       |
| **Milestones**      | `list_milestones`                             | Retrieves Milestones defined across all Sales Pipelines                                   | ✅                       |
|                     | `list_milestones_by_pipeline`                 | Retrieves Milestones for a given Sales Pipeline                                           | ✅                       |
|                     | `get_milestone`                               | Get Milestone by ID                                                                       | ❌                       |
| **Lost Reasons**    | `list_lost_reasons`                           | Retrieves Lost Reasons with optional searching by name                                    | ✅                       |
|                     | `get_lost_reason`                             | Get Lost Reason by ID                                                                     | ❌                       |
| **Project Boards**  | `list_boards`                                 | Retrieves Project Boards with optional searching by name                                  | ✅                       |
|                     | `get_board`                                   | Get Project Board by ID                                                                   | ❌                       |
| **Stages**          | `list_stages`                                 | Retrieves Stages defined across all Project Boards                                        | ✅                       |
|                     | `get_stage`                                   | Get Project Stage                                                                         | ❌                       |
|                     | `list_stages_by_board`                        | Retrieves Stages for a given Project Board                                                | ✅                       |
| **Tags**            | `list_contact_tags`                           | Retrieves Tags defined for Contacts                                                       | ✅                       |
|                     | `list_opportunity_tags`                       | Retrieves Tags defined for Opportunities                                                  | ✅                       |
|                     | `list_project_tags`                           | Retrieves Tags defined for Projects                                                       | ✅                       |
|                     | `get_contact_tag`                             | Get Contact Tag Definition by ID                                                          | ❌                       |
|                     | `get_opportunity_tag`                         | Get Opportunity Tag Definition by ID                                                      | ❌                       |
|                     | `get_project_tag`                             | Get Project Tag Definition by ID                                                          | ❌                       |
| **Users**           | `list_users`                                  | Retrieves all Users                                                                       | ❌                       |
|                     | `get_user`                                    | Get User by ID                                                                            | ❌                       |
|                     | `get_current_user`                            | Get current User                                                                          | ❌                       |
| **Teams**           | `list_teams`                                  | Retrieves all Teams and Team members                                                      | ❌                       |
|                     | `get_team`                                    | Get Team by ID                                                                            | ❌                       |
| **Tasks**           | `list_tasks`                                  | Retrieve Tasks with basic filtering ability                                               | ✅                       |
|                     | `get_task`                                    | Get Task by ID                                                                            | ❌                       |
| **Tracks**          | `get_track`                                   | Get Track by ID                                                                           | ❌                       |
|                     | `list_tracks_for_contact`                     | List Tracks for specified Contact                                                         | ✅                       |
|                     | `list_tracks_for_opportunity`                 | List Tracks for specified Opportunity                                                     | ✅                       |
|                     | `list_tracks_for_project`                     | List Tracks for specified Project                                                         | ✅                       |
| **Activity**        | `list_activity`                               | Retrieve Activity with basic filtering ability                                            | ✅                       |
|                     | `list_entries_for_contact`                    | Retrieve Entries for specified Contact                                                    | ✅                       |
|                     | `list_entries_for_project`                    | Retrieve Entries for specified Project                                                    | ✅                       |
|                     | `list_entries_for_opportunity`                | Retrieve Entries for specified Opportunity                                                | ✅                       |
|                     | `get_entry`                                   | Get Entry by ID                                                                           | ❌                       |
