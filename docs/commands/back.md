---
command:
  added: Pre-0.2.7
  aliases:
  - backs
  configuration:
  - sts_back
  - max_back_stack
  description: Takes you to your previous location.
  permissions:
  - rcmds.back
  supports:
    name-completion: false
    time-format: false
  usage: /back (#)
layout: command
title: /back
---

```/back``` will take the player using it back to his previous location before a RoyalCommands teleport. Any teleport
from RoyalCommands will be registered in /back for convenience.

If enabled, a stack of previous locations is visible from ```/backs```, and the number displayed next to each location
can be used as ```/back #``` to go to that particular previous location.

### Examples 

```/back``` - Takes the player to his most recent previous location.  
```/backs``` - Displays the player's previous locations.  
```/back 3``` - Take the player to his third most recent previous location (as can be viewed from ```/backs```).

