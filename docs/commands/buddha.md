---
command:
  added: Pre-0.2.7
  aliases:
  - tbm
  - buddhamode
  configuration: []
  description: Toggles buddha mode.
  permissions:
  - rcmds.buddha
  - rcmds.others.buddha
  supports:
    name-completion: true
    time-format: false
  usage: /buddha (player)
layout: command
title: /buddha
---

```/buddha``` toggles "buddha mode," a mode in which a player can take damage up until one half-heart remains. The
player will not fall below one half-heart (die), but will still be able to take damage.

If this command is executed without the ```(player)``` argument, it will toggle buddha mode on the sender. If it is
executed with the ```(player)``` argument and the sender has the ```rcmds.others.buddha``` permission, this will
toggle buddha mode on the given player.

### Examples 

```/buddha``` - Toggles the buddha mode status on the command sender.  
```/buddha Notch``` - Toggles ```Notch```'s buddha mode status.

