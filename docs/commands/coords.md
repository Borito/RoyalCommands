---
command:
  added: Pre-0.2.7
  aliases:
  - pos
  - position
  - loc
  - location
  - coordinates
  configuration: []
  description: Displays coordinates for yourself or a player.
  permissions:
  - rcmds.coords
  - rcmds.others.coords
  supports:
    name-completion: true
    time-format: false
  usage: /coords (player)
layout: command
title: /coords
---

```/coords``` displays the coordinates (x, y, z, pitch, yaw, and world) of the sender or ```(player)``` if specified
and the sender has ```rcmds.other.coords```.

### Examples 

```/coords``` - Shows the coordinates of the sender to himself.  
```/coords Notch``` - Shows the coordinates of ```Notch``` to the sender.

