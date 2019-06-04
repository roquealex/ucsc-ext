## Final Project

### Introduction

This project is the simplified infrastructure of WindChaser a system that
connects Personal Weather Stations with real time GPS recording from wind
sports (Kiteboarding or wind surfing).

PWS are similar to this product:

https://weatherflow.com/smart-home-weather-stations/

And trackers similar to this:

https://woosports.com/

The project will correlate realtime the weather data with the number of
people whowing to practice the sport close to the sensor, this will help other
people to find out if the conditions are good. Data will be stored for the
development of better weather models.

Project currently does not include any notifications but could be imporved to
do so. For instance notify suscribers when the wind in some area passes some treshold or when the ammount of people on the water is more than a treshold

### Dependencies

The project requires the following libraries:

~~~
sudo pip install numpy
sudo pip install kafka-python
sudo pip install pandas
~~~

And was tested only with python 2.7 and spark 3.3

### Messaging

JSON is used to communicate information from PWS and devices. There is a
generic JSON that will contain the timestamp of the message and the GUID of
the issuer.

The payload will be different based on the format. Currently only 2 formats are
implemented:

 * urn:windchaser:pws:reading : This is a PWS giving a full report of the
   weather. The amount of info in this demo is restricted to the wind
   information only. Updated every minute for this demo.

 * urn:windchaser:device:reading : This is a device giving its GPS lattitude
   and longitude readings. Updated N times per minute for this demo.

## Streaming Processing : iotconsumer.py

This project relies only on Spark Structured Streaming, it is connected to
kafka using the topic iotmsgs for this demo. All the messages go in the same
topic and will be difernetiated by the format.

Two sinks are currently implemented. Both of them use a window size of 5 min
but it is configurable. Both sink to console for this demo but they could be
modified to use a distrubuted DB.

One of the streams will get different aggregations (average) in the range of
the 5 min.

The other stream will try to find from the devices updates how many people is
close to a sensor so it will aggregate this informetion per window and per
sensor. The sensor coordinates are stored in a CSV now but they could be stored
in a fancier DB. The registry should be done by another JSON message indicating
the coordinates of the sensor.

# Simulators : iotsimulator.py and iotrandsim.py

There are 2 simulators, the first one listed uses a prerecorded sequence of
messages that show the capabilities of the processing engine.


The second one is a more timing accurate random message generator. The PWS used
have to come from the database so the GUID for PWS are not randomly generated.
Once a sensor detects some increase in the wind is possible for people to show
up to the spot close to the PWS. This is modeled in the random simulator but
it may randomly happen. Once devices start to show close to the sensor they
will produce updates until the wind drops to some treshold. The GUID of the 
devices are randomly generated but once a GUID is assigned it will keep
producing data close to the sensor.

The random simulator ir real time so user needs to wait one minute to see
another update from the list of PWS and a fraction of it for the devices.

Both simulators are stand alone and don't have to be piped. They rely on the
library kafka-python. The consumer will filter out anything that doesn't have
the key 'json' so using raw text will have no impact.





