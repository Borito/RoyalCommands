#!/usr/bin/python3
from requests import post
from json import dumps
from sys import argv

if len(argv) < 4:
    quit()

## EDIT BELOW HERE ##

URL = argv[1]
SECRET = argv[2]
EVENTS = [
    "success",
    "fail"
]
RESULTS_URL = argv[3]

# ${bamboo.buildKey} ${bamboo.buildResultsUrl} ${bamboo.buildNumber} ${bamboo.buildPlanName}
## DO NOT EDIT BELOW HERE ##

print(argv)
