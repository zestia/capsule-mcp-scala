# Capsule MCP Server

MCP server that connects to your Capsule CRM data.

## Setup
You can get started with the server and use it with your favourite AI assistant.

### Install Docker
The Capsule MCP Server runs inside [Docker](https://www.docker.com/). Follow the instructions below for your operating system.

@@@ note
Already have Docker installed? Skip ahead to [Configuration](#configuration).
@@@

**Option 1: Docker Desktop (recommended for most users)**

1. Go to [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop/)
2. Click **Download Docker Desktop**, selecting your OS
3. Run the installer and follow the prompts
4. Restart your computer when prompted
5. Launch Docker Desktop

**Option 2: CLI only (for developers)**

Follow the [official installation guide](https://docs.docker.com/engine/install/) for your specific distribution.

### Verify your installation

Once Docker is installed (via any method above) **and running**, confirm it is working:

```
docker --version
```

The above should print a version number.

### Configuration
Configure your favourite AI assistant to use the Capsule MCP Server.

#### Generate an API key
Generate an API key in your Capsule CRM account.

In your Capsule account, navigate to: `My Preferences > API Authentication Tokens > Generate New API Token`

   - **Description:** Capsule MCP Server
   - **Scope of this token:** Select `Read information from your Capsule account` only

Copy the generated token and temporarily save it somewhere safe.

#### Connect to your AI Assistant

##### Claude Desktop
[Download Claude Desktop](https://claude.com/download)

Locate your Claude Desktop config file:

- **macOS:** `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows:** `%APPDATA%/Claude/claude_desktop_config.json`

Add the following to your Claude Desktop config file and replace `YOUR-API-TOKEN` with your Capsule API token.

```json
{
  "mcpServers": {
    "capsule-mcp": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "--name",
        "capsule-mcp",
        "-e",
        "CAPSULE_API_TOKEN=YOUR-API-TOKEN",
        "ghcr.io/zestia/capsule-mcp-scala:latest"
      ]
    }
  }
}
```

@@@ index
* [Troubleshooting](troubleshooting.md)
@@@
