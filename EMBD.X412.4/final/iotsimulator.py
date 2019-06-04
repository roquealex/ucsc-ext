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


msgs = [
"""
{
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:25:00",
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
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:27:00",
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
    "eventTime" : "2019-06-02 14:28:10",
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
    "eventTime" : "2019-06-02 14:28:20",
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
    "eventTime" : "2019-06-02 14:28:20",
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
    "eventTime" : "2019-06-02 14:28:20",
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
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:29:00",
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
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:30:00",
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
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:32:00",
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
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:35:00",
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
    "guid" : "USA-CA-ALAMEDA-0062",
    "destination" : "A012",
    "eventTime" : "2019-06-02 14:37:00",
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




