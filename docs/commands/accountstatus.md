---
command:
  added: Pre-0.2.7
  aliases:
  - accs
  - ispremium
  - iscracked
  configuration: []
  description: Checks to see if a name is premium.
  permissions:
  - rcmds.accountstatus
  supports:
    name-completion: true
    time-format: false
  usage: /accountstatus [name]
layout: command
title: /accountstatus
---

```/accountstatus``` checks Mojang's servers to see if the name given for the ```[name]``` parameter has purchased Minecraft.
This allows for checking to see if a player is "premium" or "cracked" on offline-mode servers. It doesn't serve much
use on online-mode servers.

### Examples

```/accountstatus Notch``` - Checks to see if ```Notch``` has bought Minecraft.

