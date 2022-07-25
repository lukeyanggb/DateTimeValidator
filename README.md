##  Date Time Validator


### Overview
The Date Time Validator program is a simple Java program that can:
  1) Read a large list of date-time values from a `txt` file
  1) Validate the date-time value, format it into the standard ISO 8601 format: YYYY-MM-DDThh:mm:ssTZD. For example, `2022-07-20T18:01:02Z`, `2022-07-20T15:01:02-03:00` (These two are at the same moment). Details see [ISO 8601 wikipedia page](https://en.wikipedia.org/wiki/ISO_8601) and [offical ISO page](https://www.iso.org/iso-8601-date-and-time-format.html). Duplicate date-time values are dropped. 
  1) Write the formatted unique date-time values into a txt file.

## Requirements

* Java 14

## Assumptions

* Year: The randomly generated test data are between Year 1900 to Year 2021. But the program should work for all valid date-time values.
* Days in a month: For simplicity, months can have up to 31 days (i.e., February 31st is considered valid though there is no 31th day in February).
* Time zone: According to the [Coordinated Univerisal Time (UTC)](https://en.m.wikipedia.org/wiki/Time_zone), some countries and regions use UTC-12:00, UTC+12:45, UTC+13:00, UTC+14:00. For the purpose of this program, these time zones are considered invalid. Only UTC-11:00 to UTC+12:00 are considered valid.

<img src='img/World_Time_Zones_Map.png' height=300>

Time zones of the world


## Test Data:

* At least 10 % duplicated date-time values are present in the test data.
* Test data size: 100,000


## Steps:

1) Step
1) Step
1) Step
1) Step
1) Step