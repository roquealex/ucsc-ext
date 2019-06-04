#!/usr/bin/python
# Roque Arcudia

from kafka import KafkaProducer
import sys

#ISO 3166-1 country code

# Stablish a connection:
#producer = KafkaProducer(bootstrap_servers=['localhost:9092'], api_version=(0, 10))
#producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

#broker, topic = sys.argv[1:]

broker = 'localhost:9092'
topic = 'iotmsgs'

brokerList = [broker]

try:
    producer = KafkaProducer(bootstrap_servers=brokerList)
except Exception as ex:
    print 'Exiting due to connection issue:', str(ex)
    sys.exit(1)

msgKey = "json"

#3rd Ave Channel
#Station ID: KCAFOSTE7
#Lat: 37.57Lon: -122.28

#US-CA-ALAMEDA-062
#US-CA-FOSTERCITY-007

msgs = [
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:25:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindSpeedMPH" : 20.0,
            "WindDirectionDegrees" : 45,
            "WindSpeedGustMPH": 21.0
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-FOSTERCITY-007",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:25:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindDirectionDegrees" : 90,
            "WindSpeedMPH" : 16.0,
            "WindSpeedGustMPH": 18.0
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:27:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindSpeedMPH" : 20.0,
            "WindDirectionDegrees" : 50,
            "WindSpeedGustMPH": 23.0
        }
    }
}
"""
,
"""
{
    "guid" : "USER0001",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:28:10.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.771,
            "lon" : -122.291
        }
    }
}
"""
,
"""
{
    "guid" : "USER0001",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:28:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.772,
            "lon" : -122.292
        }
    }
}
"""
,
"""
{
    "guid" : "USER0002",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:28:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.775,
            "lon" : -122.285
        }
    }
}
"""
,
"""
{
    "guid" : "USER0004",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:28:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.78,
            "lon" : -122.285
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:29:00.000000Z",
    "payload" : {
         "format": "urn:windchaser:pws:reading", 
         "data": {
            "WindSpeedMPH" : 25.0,
            "WindDirectionDegrees" : 55,
            "WindSpeedGustMPH": 27.0
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:30:00.000000Z",
    "payload" : {
         "format": "urn:windchaser:pws:reading", 
         "data": {
            "WindSpeedMPH" : 21.0,
            "WindDirectionDegrees" : 45,
            "WindSpeedGustMPH": 27.0
         }
    }
}
"""
,
"""
{
    "guid" : "US-CA-FOSTERCITY-007",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:30:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindDirectionDegrees" : 90,
            "WindSpeedMPH" : 18.0,
            "WindSpeedGustMPH": 20.0
        }
    }
}
"""
,
"""
{
    "guid" : "USER0004",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:30:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.78,
            "lon" : -122.285
        }
    }
}
"""
,
"""
{
    "guid" : "USER0005",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:30:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.571,
            "lon" : -122.289
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:32:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindSpeedMPH" : 20.0,
            "WindDirectionDegrees" : 50,
            "WindSpeedGustMPH": 23.0
        }
    }
}
"""
,
"""
{
    "guid" : "USER0005",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:32:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.572,
            "lon" : -122.288
        }
    }
}
"""
,
"""
{
    "guid" : "USER0006",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:32:20.000000Z",
    "payload" : {
        "format": "urn:windchaser:device:reading", 
        "data": {
            "lat" : 37.571,
            "lon" : -122.279
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:35:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindSpeedMPH" : 16.0,
            "WindDirectionDegrees" : 3,
            "WindSpeedGustMPH": 20.1
        }
    }
}
"""
,
"""
{
    "guid" : "US-CA-ALAMEDA-062",
    "destination" : "A012",
    "eventTime" : "2019-06-02T14:37:00.000000Z",
    "payload" : {
        "format": "urn:windchaser:pws:reading", 
        "data": {
            "WindSpeedMPH" : 18.0,
            "WindDirectionDegrees" : 355,
            "WindSpeedGustMPH": 19.5
        }
    }
}
"""
,

]


#msgKeyBytes = bytes(msgKey, encoding='utf-8')
#valBytes = bytes(msgVal, encoding='utf-8')


try:
    msgKeyBytes = msgKey.encode(encoding='UTF-8',errors='strict')
    for msgVal in msgs :
        msgValBytes = msgVal.encode(encoding='UTF-8',errors='strict')
        producer.send(topic, key=msgKeyBytes, value=msgValBytes)
        producer.flush()
    print('Message published successfully.')
except Exception as ex:
    print 'Ex when posting:', str(ex)




