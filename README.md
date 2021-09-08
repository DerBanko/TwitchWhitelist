# TwitchWhitelist

This plugin connects your **Minecraft Server** with your **Twitch Chat**.<br>
You can **whitelist** players by using following functions:<br>
* **Channel Points Redemptions** → Enter the username in a specific redemption
* **Using a Command** → Enter `!<command> <username>` in the twitch chat

Minecraft Server Version Support: **1.8 or above**

# Setup

1) Download the plugin via [the releases](https://github.com/DerBanko/TwitchWhitelist/releases).
2) Setup your minecraft server *(many tutorials on the internet)* and place the jar file in the `/plugins/` directory.
3) Start the server and connect on it.
4) Check for the permissions the player should have to use the commands.

# Commands

* /whitelist (alias: `/wl`, required permission: `twl.whitelist`)<br> 
  * `/whitelist (help)` - shows the help of the command
  * `/whitelist list` - shows the whitelisted players
  * `/whitelist add <Player>` - add the player to the whitelist
  * `/whitelist remove <Player>` - remove the player from the whitelist
  * `/whitelist user <Player>` - shows the information of the command

* /settings (required permission: `twl.settings`)<br>
  * `/settings (help)` - shows the help of the command
  * `/settings token <Access Token>` - set the access token of the twitch account ([get it here](https://twitchtokengenerator.com/))
  * `/settings cpname <Channel Points Reward Name>` - set the name of the channel points reward
  * `/settings command <Command>` - set the command which will be listened in twitch chat
  * `/settings users <Users>` - set the users in which the bot will connect
  * `/settings togglecp <true/false>` - set if the players will be able to whitelist with channel points
  * `/settings togglecommand <true/false>` - set if the players will be able to whitelist with the command
  * `/settings reconnect` - reconnect the twitch bot
  
# Credits

The plugin uses the [twitch4j](https://github.com/twitch4j/twitch4j) API.
