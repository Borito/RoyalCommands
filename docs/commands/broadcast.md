---
command:
  added: Pre-0.2.7
  aliases:
  - bc
  - broadc
  - bcast
  configuration:
  - bcast_format
  description: Broadcasts a message to the server.
  permissions:
  - rcmds.broadcast
  supports:
    name-completion: false
    time-format: false
  usage: /broadcast [message]
layout: command
title: /broadcast
---

```/broadcast``` will send a message formatted with configuration ```bcast_format``` to the server.

### Examples 

```/broadcast Hello!``` - Sends ```Hello!``` to the server with ```bcast_format``` applied to it.

