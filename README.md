# Capsule CRM MCP Server
[![Model Context Protocol](https://img.shields.io/badge/MCP-Compatible-blue)](https://modelcontextprotocol.io) [![Scala 3.6+](https://img.shields.io/badge/scala3--compiler-3.6.4-green.svg?)](https://python.org) [![Capsule CRM API v2](https://img.shields.io/badge/Capsule%20CRM-API%20v2-orange)](https://developer.capsulecrm.com)

Scala3-based MCP server implementation PoC that connects to your Capsule CRM data.

## Quickstart
For non-developers, refer to the quickstart setup guide on the GitHub pages site: https://zestia.github.io/capsule-mcp-scala

## Setup - Developers
The quickstart instructions use the published Docker image of the MCP server and require minimal technical setup.
If contributing, you will need to follow the steps below to setup your development environment and all dependencies
needed to run `scala-cli` natively.

### Prerequisites

You will need an AI assistant that supports **local** MCP servers. Some popular options:

- **[Claude Desktop](https://claude.com/download)** - Anthropic's desktop app
- **[Cursor](https://www.cursor.com/)** - AI code editor

#### 1. Generate an API key
Generate an API key in your Capsule CRM account.

In your Capsule account, navigate to: `My Preferences → API Authentication Tokens → Generate New API Token`

- **Description:** Capsule MCP Server
- **Scope of this token:** Select `Read information from your Capsule account` only

Copy the generated token and temporarily save it somewhere safe.

#### 2. Install Dependencies (macOS)

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

#### 3. Locate your AI assistant config file
Locate the config file for your chosen AI assistant:

- **Claude Desktop**
    - MacOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
    - Windows: `%APPDATA%/Claude/claude_desktop_config.json`
- **Cursor** - [configuration locations](https://cursor.com/docs/context/mcp#configuration-locations)

Add the following to the config file, replacing `YOUR-API-TOKEN` with your Capsule API token and save.
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

### Start Using
1. Restart your AI assistant
2. Start asking questions

## Available Tools
See the [full list of available tools](https://zestia.github.io/capsule-mcp-scala/available-tools.html) in the documentation.

## Development

### Running
There are a few options for running / testing the server locally during development.

#### AI Assistant / MCP Client
Refer to [Setup - Developers](#setup---developers).

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

### Publishing
To publish a new version of the server Docker container:
1. Bump the version in `project/Version.scala` and `.github/workflows/publish.yml`
2. Run the GitHub action `Publish`

To publish doc changes to GitHub Pages site:
```bash
# sbt>

# preview site
makeSite

# publish site
ghpagesPushSite 
```