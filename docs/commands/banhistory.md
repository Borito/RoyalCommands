---
command:
  added: 0.2.7
  aliases:
  - bh
  configuration: []
  description: Views the ban history of the player.
  permissions:
  - rcmds.banhistory
  supports:
    name-completion: true
    time-format: false
  usage: /banhistory [name] [ban#]
layout: command
title: /banhistory
---

```/banhistory``` looks up and displays previous bans performed on a player along with information about the bans. If
```(ban#)``` is not specified, a reading of how many bans the player has will be shown, along with a prompt to enter a
number. Executing ```/banhistory playerName number``` will display information about the specific ban.

### Examples 

```/banhistory Notch``` - Displays the amount of bans ```Notch``` has.  
```/banhistory Notch 1``` - Displays information about ban ```1``` on ```Notch```'s account.

