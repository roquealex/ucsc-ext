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
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:25:00",
    "payload" : {
        "WindSpeed" : 20.0,
        "WindDirection" : 45
    }
}
"""
,
"""
{
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:27:00",
    "payload" : {
        "WindSpeed" : 20.0,
        "WindDirection" : 50
    }
}
"""
,
"""
{
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:29:00",
    "payload" : {
        "WindSpeed" : 25.0,
        "WindDirection" : 55
    }
}
"""
,
"""
{
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:30:00",
    "payload" : {
        "WindSpeed" : 21.0,
        "WindDirection" : 45
    }
}
"""
,
"""
{
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:32:00",
    "payload" : {
        "WindSpeed" : 20.0,
        "WindDirection" : 50
    }
}
"""
,
"""
{
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:35:00",
    "payload" : {
        "WindSpeed" : 16.0,
        "WindDirection" : 45
    }
}
"""
,
"""
{
    "guid" : "ALAMEDA",
    "type" : "pws",
    "eventTime" : "2019-06-02 14:37:00",
    "payload" : {
        "WindSpeed" : 18.0,
        "WindDirection" : 40
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




