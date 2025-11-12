# Capsule CRM MCP Server
[![Model Context Protocol](https://img.shields.io/badge/MCP-Compatible-blue)](https://modelcontextprotocol.io) [![Scala 3.6+](https://img.shields.io/badge/scala3--compiler-3.6.4-green.svg?)](https://python.org) [![Capsule CRM API v2](https://img.shields.io/badge/Capsule%20CRM-API%20v2-orange)](https://developer.capsulecrm.com)

Scala3-based MCP server implementation PoC that connects to your Capsule CRM data.

## Quick Start
You can get started with the server and use it with your favourite AI assistant.

### 1. Get Your Capsule API Token
1. Log into your Capsule CRM account
2. Go to **My Preferences → API Authentication**
3. Create a new API token and copy it

### 2. Install & Configure

#### macOS Setup

```bash
# Clone this repo:
git clone git@github.com:zestia/capsule-mcp-scala.git
cd capsule-mcp-scala

# Install jEnv (skip if already installed):
brew install jenv

# Add jenv to your zsh (skip if already installed):
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(jenv init -)"' >> ~/.zshrc

# reload your terminal session:
exec $SHELL -l

# Install Java 17 and add to jenv (skip if already installed):
brew install openjdk@17
jenv add $(/usr/libexec/java_home -v 17)

# Install scala-cli (skip if already installed):
brew install Virtuslab/scala-cli/scala-cli

# reload your terminal session:
exec $SHELL -l
```

Confirm [jEnv](https://github.com/jenv/jenv?tab=readme-ov-file#1-getting-started) and [scala-cli](https://scala-cli.virtuslab.org/install/) installation with:
```bash
jenv --version
scala-cli --version
```

#### Linux/Windows Setup
Coming soon

### 3. Connect to Your AI Assistant

#### Claude Desktop

Add the following to your Claude Desktop config file and replace:
1. `your-api-token` with your Capsule API token.
2. `/path/to/your/` with the path to your `capsule-mcp-scala` directory that you cloned via git.

**Config Location:**
- **macOS:** `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows:** `%APPDATA%/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "capsule-mcp": {
      "command": "/path/to/your/capsule-mcp-scala/runServer.sh",
      "args": [],
      "transport": "stdio",
      "env": {
        "CAPSULE_API_TOKEN": "your-api-token",
        "WORKSPACE": "/path/to/your/capsule-mcp-scala"
      }
    }
  }
}
```


You can also optionally override the `CAPSULE_BASE_URL` environment variable to point to a different Capsule CRM instance.

##### Example
```json
{
  "mcpServers": {
    "capsule-mcp": {
      "command": "/Users/mirandahawkes/projects/capsule-mcp-scala/runServer.sh",
      "args": [],
      "transport": "stdio",
      "env": {
        "CAPSULE_API_TOKEN": "abc123",
        "CAPSULE_BASE_URL": "https://api.capsule.run",
        "WORKSPACE": "/Users/mirandahawkes/projects/capsule-mcp-scala"
      }
    }
  }
}
```

#### Cursor
TODO

#### Other MCP Clients
This server is compatible with any MCP client. Refer to your client's documentation for MCP server configuration.

## Start Using
1. Restart your AI assistant
2. Start asking questions

## What You Can Access

This MCP server provides **complete read-only access** to your Capsule CRM:

| **Data Type**       | **Tool Name**                      | **Description**                                                                           | **Paginated Response?** | 
|---------------------|------------------------------------|-------------------------------------------------------------------------------------------|-------------------------|
| **Contacts**        | `list_contacts`                    | Retrieve Contacts with comprehensive filtering ability                                    | ✅                       |
|                     | `get_contact`                      | Get Contact by ID                                                                         | ❌                       |
| **Opportunities**   | `list_opportunities`               | Retrieve Opportunities with comprehensive filtering ability                               | ✅                       |
|                     | `get_opportunity`                  | Get Opportunity by ID                                                                     | ❌                       |
|                     | `calculate_value_of_opportunities` | Retrieves Total & Projected Values for Opportunities with comprehensive filtering ability | ❌                       |
| **Projects**        | `list_projects`                    | Retrieve Projects with comprehensive filtering ability                                    | ✅                       |
| **Custom Fields**   | `list_contact_custom_fields`       | Retrieves Custom Fields defined for Contacts                                              | ✅                       |
|                     | `list_opportunity_custom_fields`   | Retrieves Custom Fields defined for Opportunities                                         | ✅                       |
|                     | `list_project_custom_fields`       | Retrieves Custom Fields defined for Projects                                              | ✅                       |
|                     | `get_contact_custom_field`         | Get Contact Custom Field Definition by ID                                                 | ❌                       |
|                     | `get_opportunity_custom_field`     | Get Opportunity Custom Field Definition by ID                                             | ❌                       |
|                     | `get_project_custom_field`         | Get Project Custom Field Definition by ID                                                 | ❌                       |
| **Sales Pipelines** | `list_pipelines`                   | Retrieves Sales Pipelines defined for Opportunities with optional searching by name       | ✅                       |
|                     | `get_pipeline`                     | Get Pipeline by ID                                                                        | ❌                       |
| **Milestones**      | `list_milestones`                  | Retrieves Milestones defined across all Sales Pipelines                                   | ✅                       |
|                     | `list_milestones_by_pipeline`      | Retrieves Milestones for a given Sales Pipeline                                           | ✅                       |
|                     | `get_milestone`                    | Get Milestone by ID                                                                       | ❌                       |
| **Lost Reasons**    | `list_lost_reasons`                | Retrieves Lost Reasons with optional searching by name                                    | ✅                       |
|                     | `get_lost_reason`                  | Get Lost Reason by ID                                                                     | ❌                       |
| **Project Boards**  | `list_boards`                      | Retrieves Project Boards with optional searching by name                                  | ✅                       |
|                     | `get_board`                        | Get Project Board by ID                                                                   | ❌                       |
| **Stages**          | `list_stages`                      | Retrieves Stages defined across all Project Boards                                        | ✅                       |
|                     | `get_stage`                        | Get Project Stage                                                                         | ❌                       |
|                     | `list_stages_by_board`             | Retrieves Stages for a given Project Board                                                | ✅                       |
| **Tags**            | `list_contact_tags`                | Retrieves Tags defined for Contacts                                                       | ✅                       |
|                     | `list_opportunity_tags`            | Retrieves Tags defined for Opportunities                                                  | ✅                       |
|                     | `list_project_tags`                | Retrieves Tags defined for Projects                                                       | ✅                       |
|                     | `get_contact_tag`                  | Get Contact Tag Definition by ID                                                          | ❌                       |
|                     | `get_opportunity_tag`              | Get Opportunity Tag Definition by ID                                                      | ❌                       |
|                     | `get_project_tag`                  | Get Project Tag Definition by ID                                                          | ❌                       |
| **Users**           | `list_users`                       | Retrieves all Users                                                                       | ❌                       |
|                     | `get_user`                         | Get User by ID                                                                            | ❌                       |
|                     | `get_current_user`                 | Get current User                                                                          | ❌                       |
| **Teams**           | `list_teams`                       | Retrieves all Teams and Team members                                                      | ❌                       |
|                     | `get_team`                         | Get Team by ID                                                                            | ❌                       |
| **Tasks**           | `list_tasks`                       | Retrieve Tasks with basic filtering ability                                               | ✅                       |
|                     | `get_task`                         | Get Task by ID                                                                            | ❌                       |
| **Tracks**          | `get_track`                        | Get Track by ID                                                                           | ❌                       |
|                     | `list_tracks_for_contact`          | List Tracks for specified Contact                                                         | ✅                       |
|                     | `list_tracks_for_opportunity`      | List Tracks for specified Opportunity                                                     | ✅                       |
|                     | `list_tracks_for_project`          | List Tracks for specified Project                                                         | ✅                       |
| **Activity**        | `list_activity`                    | Retrieve Activity with basic filtering ability                                            | ✅                       |
|                     | `list_entries_for_contact`         | Retrieve Entries for specified Contact                                                    | ✅                       |
|                     | `list_entries_for_project`         | Retrieve Entries for specified Project                                                    | ✅                       |
|                     | `list_entries_for_opportunity`     | Retrieve Entries for specified Opportunity                                                | ✅                       |
|                     | `get_entry`                        | Get Entry by ID                                                                           | ❌                       |

## Development

### Running
There are a few options for running / testing the server locally during development.

#### AI Assistant / MCP Client
Refer to [Quick Start](#quick-start).

#### MCP Inspector (recommended)
The [MCP Inspector](https://modelcontextprotocol.io/legacy/tools/inspector) is an interactive developer tool for testing and debugging MCP servers.

See [developer documentation](https://github.com/modelcontextprotocol/inspector?tab=readme-ov-file#running-the-inspector) for installation.

```bash
scala-cli clean . && npx @modelcontextprotocol/inspector scala-cli . -e CAPSULE_API_TOKEN=yourToken -e CAPSULE_BASE_URL=https://api.capsule.run
```

#### Scala CLI
Running your MCP server via your AI Assistant or the MCP Inspector starts up the server via `scala-cli` in the background.
To run this manually instead, use:
```bash
export CAPSULE_API_TOKEN=yourToken
export CAPSULE_BASE_URL=baseUrl
scala-cli clean . && scala-cli run .
```

You can optionally override `CAPSULE_BASE_URL=baseUrl` to test against a specific instance of Capsule.
The default is `https://api.capsulecrm.com`.

Note that if you make changes to files, you will need to restart the server to pick these up.

#### SBT
TBC instructions on running server via sbt - need to figure out sending SBT logs to a file so it doesn't break the stdio transport used by MCP.

### Debugging
Server logs are written to `/capsule-mcp-scala/capsule-mcp.log`.