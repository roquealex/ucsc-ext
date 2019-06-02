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
pepe pecas
pica papas
con un pico
"""
,
"""
She sells sea-shells on the sea-shore.
The shells she sells are sea-shells, I'm sure.
For if she sells sea-shells on the sea-shore
Then I'm sure she sells sea-shore shells.
"""
,
"""
Betty Botter bought a bit of butter.
The butter Betty Botter bought was a bit bitter
And made her batter bitter.
But a bit of better butter makes better batter.
So Betty Botter bought a bit of better butter
Making Betty Botter's bitter batter better
The following twister won the "grand prize" in a contest in Games Magazine in 1979:[5]
"""
,
"""
Shep Schwab shopped at Scott's Schnapps shop;
One shot of Scott's Schnapps stopped Schwab's watch.
"""
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




