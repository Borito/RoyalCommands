---
command:
  added: 3.2.0
  aliases:
  - dbh
  - rbh
  - removebanhistory
  configuration: []
  description: Deletes ban history records.
  permissions:
  - rcmds.deletebanhistory
  supports:
    name-completion: true
    time-format: false
  usage: /deletebanhistory [player] [ban#]
layout: command
title: /deletebanhistory
---

```/deletebanhistory``` deletes ban history record ```[ban#]``` (seen in [[banhistory|/banhistory]]) off of
```[player]```. A record that is deleted will no longer appear on ```[player]```'s ban history.

### Examples

```/deletebanhistory Notch 1``` - Deletes ban ```1``` from ```Notch```'s ban history.

