---
command:
  added: Pre-0.2.7
  aliases:
  - rban
  - banish
  configuration:
  - on_ban
  - default_ban_message
  - ingame_ban_format
  - ban_format
  description: Bans a user.
  permissions:
  - rcmds.ban
  - rcmds.exempt.ban
  - rcmds.see.ban
  supports:
    name-completion: true
    time-format: false
  usage: /ban [player] (message)
layout: command
title: /ban
---

```/ban``` is a server management tool that, if used, will prevent a player from connecting to the server under the
same name. If the ```(message)``` argument is not used, the message from configuration ```default_ban_message``` will
be used instead. The ```(message)``` argument supports [color codes](/color-codes).

```ingame_ban_format``` is the message shown to players in-game about bans if they have the ```rcmds.see.ban```
permission node. The banned player will see ```ban_format``` when he tries to connect and when he is originally kicked.

### Examples

```/ban Notch``` - Bans Notch from connecting the server and will kick him off of the server if he is online.
```/ban Notch Leave now and never come back.``` - Will ban Notch with the reason ```Leave now and never come back.```

