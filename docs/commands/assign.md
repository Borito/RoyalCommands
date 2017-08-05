---
command:
  added: Pre-0.2.7
  aliases:
  - as
  - pt
  - powertool
  configuration:
  - assign.lore_and_display_names
  - assign.durability
  description: Assigns a command to a certain block.
  permissions:
  - rcmds.assign
  supports:
    name-completion: false
    time-format: false
  usage: /assign [command|-#|~]
layout: command
title: /assign
---

```/assign``` allows for commands to be "assigned" to items. If the command is run while holding an item, clicking with
that item will execute the command. The ```[command]``` argument should not contain the first ```/``` of a command.

```/assign``` also allows for chat messages to be "assigned" to items, as well. A chat message should be prefixed with
```c:``` when being used for the ```[command]``` argument.

```assign.lore_and_display_names``` and ```assign.durability``` can be set to determine whether the command will
differentiate between items with different display names (and lore) and different durabilities.

### Examples

```/assign jump``` - This will assign ```/jump``` to the item in the player's hand, allowing him to click and teleport
to the block he is looking at.  
```/assign c:Hello!``` - This will assign the chat message ```Hello!``` to the item in the player's hand, allowing him
to click and say ```Hello!```.

