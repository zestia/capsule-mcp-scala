# Troubleshooting
If your AI assistant is having trouble connecting to the MCP server, ensure the following:

* Docker is already running **before** starting your AI assistant
* In a terminal, run `docker ps` and ensure you see `ghcr.io/zestia/capsule-mcp-scala` running 
* Your API token is valid and correctly set in your config file

If you're still having issues, please let us know with the following information:

* Your Operating System & what AI assistant you are using
* Screenshots / the exact error message you are seeing
* If some questions about your Capsule account succeed and others do not - i.e. your AI assistant is connecting to the MCP server, but you're having an issue with a specific question/prompt
* Locate the logs for the MCP server:
    * **Claude Desktop** - in `Settings → Developer → Local MCP Servers`, you should see `capsule-mcp`. Select `View Logs` to view the file
    * **Cursor** - in `Settings → Cursor Settings → Tools & MCP`, under `Installed MCP Servers` find `capsule-mcp`, and select `Show output`
* Anything else you think might be helpful