---
command:
  added: Pre-0.2.7
  aliases:
  - bi
  - whobanned
  - whoban
  - wbanned
  - banwho
  - bannedwho
  - wban
  - banreason
  - br
  - banr
  - breason
  - banned
  configuration: []
  description: Get information about a ban.
  permissions:
  - rcmds.baninfo
  supports:
    name-completion: false
    time-format: false
  usage: /baninfo [player]
layout: command
title: /baninfo
---

```/baninfo``` displays all recorded information about a player that is banned. If you are looking for information on
past bans, try [/banhistory](/commands/banhistory) instead.

### Examples

```/baninfo jeb_``` - Gets the information available about ```jeb_```'s current ban.

