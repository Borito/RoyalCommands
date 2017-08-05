---
layout: titled
title: Time Format
---

If a component of RoyalCommands states that it supports *time format*, then it will allow for a string to represent a
time, as seen below.

Whenever ```(time)``` or ```[time]``` is an argument to a command, a time format string can be used.

### Examples

The following modifiers are allowed in a time format string:

```y```, ```d```, ```h```, ```m```, and ```s```

These stand for ```years```, ```days```, ```hours```, ```minutes```, and ```seconds```.

An example of a time string may be ```3h5m20s```, which is equivalent to three hours, five minutes, and twenty seconds,
or 11,120 seconds.

If no modifiers are specified, the number is assumed to be in seconds.
