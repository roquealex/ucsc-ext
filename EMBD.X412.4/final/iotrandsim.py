#!/usr/bin/python
# Roque Arcudia

import sys
import os
import time
import datetime
import random
import string
# Make sure this packages are installed:
import numpy as np
import pandas as pd
from kafka import KafkaProducer

###########################
# Functions
###########################

def randDevice() :
    baseGuid = "0-ZZZ123"
    #randInt = random.randrange(0, 9)
    guid = baseGuid
    for i in range(3) :
        guid += str(random.randrange(0, 9))
    return guid

###########################
# PWS sim data class
###########################

# Stores info relevant to the simulation
class PwsSimData :
    def __init__(self, iname, ilat, ilon):
        self.name = iname
        # From DB:
        self.lat = ilat
        self.lon = ilon
        self.lon = ilon
        # State vars:
        self.lastWindWindSpeedMPH = 14.9
        self.maxSpeed = 15
        self.devList = []
        # Basic JSON
        self.iotmsg_header = """\
        {
          "guid": "%s",
          "destination": "%s", """

        self.iotmsg_eventTime = """\
          "eventTime": "%sZ", """

        self.iotmsg_payload ="""\
          "payload": {
             "format": "%s", """


    def randomReading(self) :
        msg = ""
        destinationStr = "0-AAA12345678"

        iotmsg_data ="""\
             "data": {
               "WindDirectionDegrees": %.0f,
               "WindSpeedMPH": %.2f,
               "WindSpeedGustMPH": %.2f
             }
           }
        }"""

        msg += self.iotmsg_header % (self.name, destinationStr)
        msg += os.linesep

        #today = datetime.datetime.today()
        today = datetime.datetime.utcnow()
        # Spark structured streaming doesn't like the microseconds at all
        today = today.replace(microsecond = 0)
        datestr = today.isoformat()
        msg += self.iotmsg_eventTime % (datestr)
        msg += os.linesep

        msg += self.iotmsg_payload % ("urn:windchaser:pws:reading")
        msg += os.linesep

        # Generate a random floating point number
        randTemperatureF = random.uniform(0.0, 40.0) + 60.0
        randPreasureIn = np.random.normal(29.92,1.0)
        randWindDirectionDegrees = np.random.uniform(0,359)
        randWindWindSpeedMPH = np.random.normal(self.lastWindWindSpeedMPH,0.25)
        randWindWindSpeedMPH = randWindWindSpeedMPH if randWindWindSpeedMPH > 0 else 0
        self.lastWindWindSpeedMPH = randWindWindSpeedMPH
        randWindSpeedGustMPH = randWindWindSpeedMPH + abs(np.random.normal(0.0,randWindWindSpeedMPH/10))
        msg += iotmsg_data % \
            (randWindDirectionDegrees, \
            randWindWindSpeedMPH, \
            randWindSpeedGustMPH) 

        msg += os.linesep

        return msg

    # Simulates kiters showing up close to the sensor due to increase in wind reading
    def evalDevices(self) :
        if (self.lastWindWindSpeedMPH > self.maxSpeed) :
            if (
                (self.maxSpeed <= 15 and self.lastWindWindSpeedMPH > 15) or
                (self.maxSpeed < 20 and self.lastWindWindSpeedMPH > 20) or
                (self.maxSpeed < 25 and self.lastWindWindSpeedMPH > 25)
            ) :
                # 5 is good enough to observe
                addDevices = random.randint(0, 5)
                print "crossed Boundary adding ",addDevices
                for i in range(addDevices) :
                    self.devList.append(randDevice())

            self.maxSpeed = self.lastWindWindSpeedMPH

        if (self.lastWindWindSpeedMPH < 14 and self.maxSpeed > 15) :
            print "reset 15"
            self.maxSpeed = 15
            del self.devList[:]

    def randomDevices(self) :
        destinationStr = "0-AAA12345678"

        iotmsg_data ="""\
             "data": {
               "lat": %.2f,
               "lon": %.2f
             }
           }
        }"""

        msgList = []
        for device in self.devList :

            msg = ""
            msg += self.iotmsg_header % (device, destinationStr)
            msg += os.linesep

            today = datetime.datetime.utcnow()
            # Spark structured streaming doesn't like the microseconds at all
            today = today.replace(microsecond = 0)
            datestr = today.isoformat()
            msg += self.iotmsg_eventTime % (datestr)
            msg += os.linesep

            msg += self.iotmsg_payload % ("urn:windchaser:device:reading")
            msg += os.linesep

            randLat = np.random.normal(self.lat,0.004)
            randLon = np.random.normal(self.lon,0.004)

            msg += iotmsg_data % \
                (randLat, \
                randLon) 

            msg += os.linesep
            msgList.append(msg)

        return msgList


    # To debug print all the contents
    def toString(self):
        msg = ""
        msg += "PWS Sim:" + os.linesep
        #s/name/YYY/g
        msg += "name:" + str(self.name) + os.linesep
        msg += "lat:" + str(self.lat) + os.linesep
        msg += "lon:" + str(self.lon) + os.linesep
        msg += "lastWindWindSpeedMPH:" + str(self.lastWindWindSpeedMPH) + os.linesep
        msg += "maxSpeed:" + str(self.maxSpeed) + os.linesep
        msg += "devList:" + str(self.devList) + os.linesep
        return msg

    def __str__(self):
        return self.toString()


###########################
# Arguments
###########################
if len(sys.argv) != 3:
    print "Usage: iotrandsim.py <broker_list> <msg_topic>"
    exit(-1)

broker, topic = sys.argv[1:]

# Hard coded:
#broker = 'localhost:9092'
#topic = 'iotmsgs'

###########################
# Parameters
###########################

# Number of PWS to simulate (max 9)
pwsNumber = 3
msgKey = "json"
brokerList = [broker]
# The PWS update will be every minute fixed, devices is configurable (min 1)
deviceUpdatePerMin = 2

pwsInfo = pd.read_csv("pwsInfo.csv")

pwsInfoSelect = pwsInfo.head(pwsNumber)

print "Final list of sensors"
print pwsInfoSelect

#p = PwsSimData("hi",34,56)
#print p
#print p.randomReading()
#print p.lastWindWindSpeedMPH

pwsList = []
for idx, row in pwsInfoSelect.iterrows():
    pwsList.append(PwsSimData(row["guid"],row["lat"],row["lon"]))


"""
demo = pwsList[0]
print demo.randomReading()

#demo.devList.append("USER0")
demo.evalDevices()
print demo
#for d in demo.randomDevices() :
#    print d

"""
# Open kafka communication
try:
    producer = KafkaProducer(bootstrap_servers=brokerList)
except Exception as ex:
    print 'Exiting due to connection issue:', str(ex)
    sys.exit(1)

## Infinite loop requires ctrl c to stop
while True :
    try:
        for pws in pwsList :
            msgVal = pws.randomReading()
            # See if the change in wind attracts more devices
            pws.evalDevices()
            print msgVal;
            msgKeyBytes = msgKey.encode(encoding='UTF-8',errors='strict')
            msgValBytes = msgVal.encode(encoding='UTF-8',errors='strict')
            producer.send(topic, key=msgKeyBytes, value=msgValBytes)
        producer.flush()
        print('Messages published successfully.')
    except Exception as ex:
        print 'Ex when posting:', str(ex)

    for i in range(deviceUpdatePerMin) :
        try:
            for pws in pwsList :
                #msgVal = pws.randomReading()
                msgList = pws.randomDevices()
                for msgVal in pws.randomDevices() :
                    print msgVal;
                    msgKeyBytes = msgKey.encode(encoding='UTF-8',errors='strict')
                    msgValBytes = msgVal.encode(encoding='UTF-8',errors='strict')
                    producer.send(topic, key=msgKeyBytes, value=msgValBytes)
            producer.flush()
            print('Device messages published successfully.')
        except Exception as ex:
            print 'Ex when posting:', str(ex)


        time.sleep(60/deviceUpdatePerMin)


