# The Internet of Things: Big Data Processing and Analytics

## Final Project: WindChaser App

### Introduction

This project is the simplified infrastructure of WindChaser a system that
connects Personal Weather Stations with real time GPS recording for wind
sports enthusiasts (Kiteboarding or wind surfing).

PWS are similar to this product:

https://weatherflow.com/smart-home-weather-stations/

And trackers similar to this:

https://woosports.com/

The project will correlate in realtime the weather data with the number of
people showing to practice the sport close to the sensor, this will help other
people to find out if the conditions are good. Data will be stored for the
development of better weather models.

Project currently does not include any notifications but could be improved to
do so. For instance notify subscribers when the wind in some area passes some
threshold or when the amount of people on the water is more than a threshold

### Dependencies

The project requires the following libraries:

~~~
sudo pip install numpy
sudo pip install kafka-python
sudo pip install pandas
~~~

And was tested only with python 2.7 and spark 2.3.3

Instructions to run are listed at the end of this document.

### Messaging

JSON is used to communicate information from PWS and devices. There is a
generic JSON that will contain the timestamp of the message and the GUID of
the issuer.

The payload will be different based on the format. Currently only 2 formats are
implemented:

 * urn:windchaser:pws:reading : This is a PWS giving a full report of the
   weather. The amount of info in this demo is restricted to the wind
   information only. Updated every minute for this demo.

 * urn:windchaser:device:reading : This is a device giving its GPS latitude
   and longitude readings. Updated N times per minute for this demo.

## Streaming Processing : iotconsumer.py

This project relies only on Spark Structured Streaming, it is connected to
kafka using the topic iotmsgs for this demo. All the messages go in the same
topic and will be differentiated by the format.

Two sinks are currently implemented. Both of them use a window size of 5 min
but it is configurable. Both sink to console for this demo but they could be
modified to use a distributed DB.

One of the streams will get different aggregations (average) in the range of
the 5 min.

The other stream will try to find from the devices updates how many people is
close to a sensor so it will aggregate this information per window and per
sensor. The sensor coordinates are stored in a CSV now but they could be stored
in a fancier DB. The registry should be done by another JSON message indicating
the coordinates of the sensor.

### Simulators : iotsimulator.py and iotrandsim.py

There are 2 simulators, the first one listed uses a prerecorded sequence of
messages that show the capabilities of the processing engine.

The second one is a more timing accurate random message generator. The PWS used
have to come from the database so the GUID for PWS are not randomly generated.
Once a sensor detects some increase in the wind is possible for people to show
up to the spot close to the PWS. This is modeled in the random simulator but
it may randomly happen. Once devices start to show close to the sensor they
will produce updates until the wind drops to some threshold. The GUID of the 
devices are randomly generated but once a GUID is assigned it will keep
producing data close to the sensor.

The random simulator in real time so user needs to wait one minute to see
another update from the list of PWS and a fraction of it for the devices.

Both simulators are stand alone and don't have to be piped. They rely on the
library kafka-python. The consumer will filter out anything that doesn't have
the key 'json' so using raw text will have no impact.

### Instructions To Run

It is recommended to run the project in this order and in this number of
terminals:

#### Terminal 1:

Run Kafka:

~~~
#stop:
bin/kafka-server-stop.sh
bin/zookeeper-server-stop.sh
pkill -9 java
pkill -9 python

#launch:
KAFKA_HEAP_OPTS="-Xmx32M" \
./bin/zookeeper-server-start.sh config/zookeeper.properties > \
 /tmp/zookeeper.log & 

KAFKA_HEAP_OPTS="-Xmx200M" \
./bin/kafka-server-start.sh config/server.properties > \
 /tmp/kafka.log 2>&1 &
~~~

#### Terminal 2:

Start apache-streaming processor. Keep in mind all the analytics is done in
windows of 5 minutes.

~~~
spark-submit \
    --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.3.3 \
    iotconsumer.py \
    localhost:9092 iotmsgs
~~~

It is recomended to wait for 'Batch: 0' outputs before running the simulator.

#### Terminal 3:

Run the random simulator, it is using real-time delays so it will take several
minutes to get good data for the streaming processing. There is no need to use
pipes since it relies on a Kafka library.

~~~
./iotrandsim.py localhost:9092 iotmsgs
~~~

The PWSs will start close to the wind speed that will trigger devices to show
up but it is possible that the wind randomly goes down and this will not happen
soon. To increase the likelihood of devices showing up increase the number of
PWS but this decreases readability of the analytics (tables grow). Or simply
rerun.

If this text is seen on the first update: Device messages published
successfully. Preceded by a urn:windchaser:device:reading json it means random
devices are updating.


OR

Run the focused scenario, similarly it doesn?t require pipes but broker and
topic are hard coded:

~~~
./iotsimulator.py
~~~

If everything is fine this is the result:

~~~
+------------------------------------------+--------------------+------------------+-----------------+-------------------+---+
|window                                    |guid                |avgWindSpeedMPH   |avgWindDirDegrees|maxWindSpeedGustMPH|cnt|
+------------------------------------------+--------------------+------------------+-----------------+-------------------+---+
|[2019-06-02 14:30:00, 2019-06-02 14:35:00]|US-CA-FOSTERCITY-007|18.0              |90.0             |20.0               |1  |
|[2019-06-02 14:25:00, 2019-06-02 14:30:00]|US-CA-ALAMEDA-062   |21.666666666666668|50.0             |27.0               |3  |
|[2019-06-02 14:25:00, 2019-06-02 14:30:00]|US-CA-FOSTERCITY-007|16.0              |90.0             |18.0               |1  |
|[2019-06-02 14:30:00, 2019-06-02 14:35:00]|US-CA-ALAMEDA-062   |20.5              |47.5             |27.0               |2  |
|[2019-06-02 14:35:00, 2019-06-02 14:40:00]|US-CA-ALAMEDA-062   |17.0              |359.0            |20.1               |2  |
+------------------------------------------+--------------------+------------------+-----------------+-------------------+---+

-------------------------------------------
Batch: 2
-------------------------------------------
+------------------------------------------+--------------------+-----------+
|window                                    |guid                |deviceCount|
+------------------------------------------+--------------------+-----------+
|[2019-06-02 14:30:00, 2019-06-02 14:35:00]|US-CA-FOSTERCITY-007|2          |
|[2019-06-02 14:25:00, 2019-06-02 14:30:00]|US-CA-ALAMEDA-062   |3          |
|[2019-06-02 14:30:00, 2019-06-02 14:35:00]|US-CA-ALAMEDA-062   |1          |
+------------------------------------------+--------------------+-----------+
~~~


