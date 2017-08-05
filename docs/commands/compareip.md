---
command:
  added: Pre-0.2.7
  aliases:
  - compip
  - cip
  configuration:
  - disable_getip
  description: Compares two users' IP addresses.
  permissions:
  - rcmds.compareip
  supports:
    name-completion: false
    time-format: false
  usage: /compareip [player1] [player2]
layout: command
title: /compareip
---

```/compareip``` retrieves and displays two IP addresses, one from ```[player1]``` and one from ```[player2]```.

This may be a useful instrument when determining if a player is using an alternate account or if a player lives with
another player.

### Examples 

```/compareip Notch jeb_``` - Shows the IP of ```Notch``` and the IP of ```jeb_```.

