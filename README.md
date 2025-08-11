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

| **Data Type** | **What You Can Do** |
|---------------|-------------------|
| **👥 Contacts** | List, Search by field |
| **💼 Opportunities** | List, Search by field |
| **📋 Projects** | List, Search by field |
| **Custom Fields** | List |
| **Users** | List |
| **Teams** | List |
| **Tasks** | Coming soon |
| **Activity** | Coming soon |

## Development

### Running
There are a few options for running / testing the server locally during development.

#### AI Assistant / MCP Client
Refer to [Quick Start](#quick-start).

#### MCP Inspector (recommended)
The [MCP Inspector](https://modelcontextprotocol.io/legacy/tools/inspector) is an interactive developer tool for testing and debugging MCP servers.

See [developer documentation](https://github.com/modelcontextprotocol/inspector?tab=readme-ov-file#running-the-inspector) for installation.

```bash
npx @modelcontextprotocol/inspector scala-cli . -e CAPSULE_API_TOKEN=yourToken -e CAPSULE_BASE_URL=https://api.capsule.run
```

#### Scala CLI
Running your MCP server via your AI Assistant or the MCP Inspector starts up the server via `scala-cli` in the background.
To run this manually instead, use:
```bash
scala-cli run . -e CAPSULE_API_TOKEN=yourToken
```

You can optionally pass `-e CAPSULE_BASE_URL=baseUrl` to test against a specific instance of Capsule.
The default is `https://api.capsulecrm.com`.

Note that if you make changes to files, you will need to restart the server to pick these up.

#### SBT
TBC instructions on running server via sbt - need to figure out sending SBT logs to a file so it doesn't break the stdio transport used by MCP.

### Debugging
Server logs are written to `/capsule-mcp-scala/capsule-mcp.log`.