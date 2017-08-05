---
command:
  added: Pre-0.2.7
  aliases:
  - pack
  - bp
  configuration:
  - reset_backpack_death
  description: Opens a personal backpack.
  permissions:
  - rcmds.backpack
  - rcmds.exempt.backpack
  - rcmds.others.backpack
  supports:
    name-completion: true
    time-format: false
  usage: /backpack (player)
layout: command
title: /backpack
---

```/backpack``` opens a secure container for items that persists through server restarts and deaths (if configured).

The backpack for other players can be viewed using ```/backpack playerName``` if the player does not have the
```rcmds.exempt.backpack``` permission and the person using the command has the ```rcmds.others.backpack``` permission.

### Examples 

```/backpack``` - Opens a personal storage container for the player.  
```/backpack joe``` - Opens the personal storage container for ```joe```.

