---
command:
  added: Pre-0.2.7
  aliases:
  - away
  configuration:
  - afk_format
  - return_format
  description: Marks the player as away from keyboard.
  permissions:
  - rcmds.afk
  - rcmds.exempt.afkkick
  - rcmds.exempt.autoafk
  supports:
    name-completion: false
    time-format: false
  usage: /afk
layout: command
title: /afk
---

```/afk``` marks the player using the command as away from the keyboard (AFK) and will send out an alert stating such.
If the player chats or moves afterward, another alert will be sent out, notifying of the player's return.

