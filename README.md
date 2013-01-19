StopFinder
===============

An utility for scraping info from [http://info.jyvaskylanliikenne.fi](http://info.jyvaskylanliikenne.fi). Can be used for searching bus stops and displaying virtualmonitor information (the departure timetable of a certain bus stop).

- Android friendly

Usage
-----

- Search for stops

```java
List<Stop> stops = StopFinder.searchStops("kirjasto"); // parameter is the searchterm used for searching bus stops
```

- Generate virtualmonitor information for a given stop

```java
VirtualMonitor vm = StopFinder.getVirtualMonitorInfo(stops.get(0));
```

TODO
----
- Better exception handling? Now the util just swallows all exceptions.

Notes
-----
You might notice there's a test class for scraping stop and virtualmonitor information of Tampereen liikenne, as well. However, I haven't actualy implemented the functionality to the util, as I'm under the impression that there's ongoing development to make their API public and more developer friendly.


License
-------
 Copyright 2013 Jouni Latvatalo

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 [http://www.apache.org/licenses/LICENSE-2.0](http://)

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

