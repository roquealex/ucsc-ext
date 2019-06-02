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
msgVal = """
pepe pecas
pica papas
con un pico
"""

#msgKeyBytes = bytes(msgKey, encoding='utf-8')
#valBytes = bytes(msgVal, encoding='utf-8')


try:
    msgKeyBytes = msgKey.encode(encoding='UTF-8',errors='strict')
    msgValBytes = msgVal.encode(encoding='UTF-8',errors='strict')
    producer.send(topic, key=msgKeyBytes, value=msgValBytes)
    producer.flush()
    print('Message published successfully.')
except Exception as ex:
    print 'Ex when posting:', str(ex)




