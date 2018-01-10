#Import the necessary methods from tweepy library
from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream
from kafka import SimpleProducer, KafkaClient


#Variables that contains the user credentials to access Twitter API
access_token = "934190655777181697-YGevqZsS2K68ycmzB0ov3wc19PO2V1H"
access_token_secret = "nDc1Ygwe6SRxJl26wDXB1lAc2yOnT0xAtJ3kht20qSHt3"
consumer_key = "RnG2doPnRyVKCETyv8mK3iVGU"
consumer_secret = "D4oN8jw7Ml9lFpIzftkLsJpuJDNAel1YOPElp1qrl3OJMo5Aj0"


#This is a basic listener that just prints received tweets to stdout.
class StdOutListener(StreamListener):

    def on_data(self, data):
        producer.send_messages("twitterstream", data.encode('utf-8'))   # 'twitterstream' is the kafka topic name
        print data
        return True

    def on_error(self, status):
        print status


#if __name__ == '__main__': Was just like main() but doesn't matter here

#EXECUTION STARTS FROM HERE
#Sending console tweets to kafka's SimpleProducer
kafka = KafkaClient("localhost:9092")
producer = SimpleProducer(kafka)
#This handles Twitter authetification and the connection to Twitter Streaming API
l = StdOutListener()
auth = OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
stream = Stream(auth, l)

#This line filter Twitter Streams to capture data by the keywords: 'python', 'javascript', 'ruby'
#Also it shows only the tweets from USA
stream.filter(track=['python', 'javascript', 'ruby'],locations=[-125,25,-65,48])






