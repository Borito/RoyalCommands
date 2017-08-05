---
command:
  added: Pre-0.2.7
  aliases:
  - ignite
  - enflame
  configuration: []
  description: Sets a player on fire for a set amount of time.
  permissions:
  - rcmds.burn
  - rcmds.exempt.burn
  supports:
    name-completion: true
    time-format: true
  usage: /burn [player] (time)
layout: command
title: /burn
---

```/burn``` sets ```[player]``` on fire for ```(time)```. If ```(time)``` is not specified, five seconds will be
assumed.

### Examples 

```/burn Notch``` - Sets ```Notch``` on fire for five seconds.  
```/burn Notch 20``` - Sets ```Notch``` on fire for ```20``` seconds.

