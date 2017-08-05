---
command:
  added: Pre-0.2.7
  aliases: []
  configuration: []
  description: Changes the biome of the chunk you're in.
  permissions:
  - rcmds.biome
  supports:
    name-completion: false
    time-format: false
  usage: /biome [biome] (radius)
layout: command
title: /biome
---

```/biome``` sets the biome of the chunk the player is standing in to ```[biome]```. If ```(radius)``` is specified,
it will set the biome of all chunks in that radius from the chunk the player is standing in to ```[biome]```.

Doing the command without arguments will display a list of valid biomes to use for ```[biome]```.

If the changes are not immediately apparent, [/fixchunk](/commands/fixchunk) can be used to resend the chunk.

### Examples

```/biome plains``` - Changes the biome the player is standing in to ```plains```.
```/biome plains 3``` - Changes the biome the player is standing in and all chunks within a ```3``` chunk radius to
```plains```.
