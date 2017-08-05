---
command:
  added: Pre-0.2.7
  aliases:
  - ci
  - clearinv
  configuration: []
  description: Clears your inventory.
  permissions:
  - rcmds.clearinventory
  - rcmds.others.clearinventory
  supports:
    name-completion: true
    time-format: false
  usage: /clearinventory (user)
layout: command
title: /clearinventory
---

```/clearinventory``` removes all items from the sender's inventory (or ```(user)```'s, if the sender has
```rcmds.others.clearinventory```) except for armor being worn.

### Examples 

```/clearinventory``` - Removes all items from the sender's inventory.  
```/clearinventory Notch``` - Removes all items from ```Notch```'s inventory.

