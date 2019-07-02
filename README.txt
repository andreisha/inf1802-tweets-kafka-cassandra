//atençao a ter as credentials do twitter no intelliJ
Run > Edit Configurations > Environment Variables
TWITTER_CONSUMER_KEY
TWITTER_CONSUMER_SECRET
TWITTER_ACCESS_TOKEN
TWITTER_ACCESS_TOKEN_SECRET

//usar kafka
cd cp-docker-images/examples/kafka-single-node
docker-compose up -d
docker-compose ps
docker-compose exec kafka bash



//criacao dos topicos
kafka-topics --zookeeper $KAFKA_ZOOKEEPER_CONNECT --create --topic tweets_input --partitions 3 --replication-factor 1

//asocia grupo ao consumidor
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_input --group tweets_application



//envia mensagens para topico
kafka-console-producer --broker-list localhost:9092 --topic tweets_input

//recebe mensagens topico
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_output

//começa produtor e consumidor
curl http://localhost:8080/tweets/collector
curl http://localhost:9080/tweets/consumer

//acaba produtor e consumidor
curl --request DELETE http://localhost:8080/tweets/collector
curl --request DELETE http://localhost:9080/tweets/consumer

////TWEETS

docker run -d --name cassandra-db20190702 -m 1024M --net=host cassandra:3
docker ps 
