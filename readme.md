# Tuleap-Mattermost task notification bot
## How to make it work:
- compile `jar` from this sourcefile and launch it at the **host**.
- create an _incoming webhook_ (integrations menu) in `Mattermost` and set it to a channel
- create a _webhook_ in `Tuleap` `Tasks` section (Administration - Workflow - Webhooks) webhook must sent ot's POST requests to `http://host:12345/cmec` to work properly
- no further adjustment is allowed yet
## in 0.0.1a
Hardcoded behavior, port and endpoint
#### Feel free to contribute to this repository
Contact me: http://t.me/WayneShephard