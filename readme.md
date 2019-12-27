# Tuleap-Mattermost task notification bot
## How to make it work:
- compile `jar` from this sourcefile, put `properties.json` to a folder, containing compiled `jar` file, and launch it at the **host**.
- create an _incoming webhook_ (integrations menu) in `Mattermost` and set it to a channel
- create a _webhook_ in `Tuleap` `Tasks` section (Administration - Workflow - Webhooks) webhook must sent ot's POST requests to `http://host:port/endpoint` to work properly
- host is your current hos or ip address, port and endpoint could be customized by editing `properties.json` file.
- `properties.json` contains properties for bot name and `Mattermost` _incoming webhook_ address 
## current version is 0.0.1a
- `properties.json` added
- adds user, that changed the artifact to a message
- checks is it's an update or create action
- creates a md link to an artifact
- appends details of an artifact if they changed
- appends comment if it was added
- shows assignee if changed
#### Feel free to contribute to this repository
Contact me: http://t.me/WayneShephard