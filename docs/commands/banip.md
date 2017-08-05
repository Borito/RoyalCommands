---
command:
  added: Pre-0.2.7
  aliases: []
  configuration: []
  description: Bans an IP address or a player's IP.
  permissions:
  - rcmds.banip
  supports:
    name-completion: true
    time-format: false
  usage: /banip (player/IP)
layout: command
title: /banip
---

```/banip``` allows for IP addresses to be blocked from connecting to the server. The ```(player/IP)``` argument will
accept a player name, offline or online, or an IP address. If given a player name, it will find the player's last IP
address and ban it. If given an IP, it will ban the IP.

### Examples 

```/banip 1.2.3.4``` - Bans the IP ```1.2.3.4```.  
```/banip Notch``` - Bans ```Notch```'s last IP.

