# Capsule CRM MCP Server
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
# Install jenv & java 17
TODO

# Install scala-cli
TODO
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
| **📋 Custom Fields** | List |
| **✅ Tasks** | Coming soon |
| **📝 Activity** | Coming soon |

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