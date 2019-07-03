A ideia deste projeto é criar uma ferramenta para coletar e processar dados de streaming obtidos em tempo real da rede social Twitter.
Para isso, o projeto tem varios modulos:
* O primeiro, tweets-producer, onde sao coletados os dados do Twitter através da biblioteca Twitter4J e injetados em um topico do Kafka.
* O segundo, tweets-consumer, que consome esses dados do Kafka e os salva no Cassandra (tabela tweets). Além disso, esse consumidor também salva os dados separados por lingua (tabela tweetsByLanguage)
* O terceiro, tweetsByLanguage, que permite processar em tempo reals os tweets do Kafka e bota-los numa KTable que conta quantos tweets têm em cada lingua.

Para usar a ferramenta, primeiro baixe os três modulos e:

* Active o Kafka e o Cassandra no docker:
// usando kafka
cd cp-docker-images/examples/kafka-single-node
docker-compose up -d
docker-compose ps
docker-compose exec kafka bash

// usando cassandra
docker run -d --name cassandra-dbDATADODIA -m 1024M --net=host cassandra:3
docker ps

PARA OS DOIS PRIMEIROS MUDOLOS:
* Crie os topicos necessarios:
// criacao do topico para cliente/consumidor
kafka-topics --zookeeper $KAFKA_ZOOKEEPER_CONNECT --create --topic tweets_input --partitions 3 --replication-factor 1
// asocia grupo ao consumidor
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_input --group tweets_application

// atençao a ter as credentials do twitter no intelliJ
Run > Edit Configurations > Environment Variables
TWITTER_CONSUMER_KEY
TWITTER_CONSUMER_SECRET
TWITTER_ACCESS_TOKEN
TWITTER_ACCESS_TOKEN_SECRET

* Faça RUN nas aplicaçoes dos modulos tweets-producer e tweets-consumer e use pelo terminal:
// começa produtor
curl http://localhost:8080/tweets/collector
// começa consumidor
curl http://localhost:9080/tweets/consumer

* Quando quiser terminar de coletar dados, use pelo terminal:
// acaba produtor
curl --request DELETE http://localhost:8080/tweets/collector
// acaba consumidor
curl --request DELETE http://localhost:9080/tweets/consumer

PARA O TERCEIRO MODULO:

* Crie os dois topicos necessarios (tweets_by_language para a resposta e um outro topico intermediario):
// criacao dos topicos para o terceiro modulo
kafka-topics --zookeeper $KAFKA_ZOOKEEPER_CONNECT --create --topic tweets_by_language --partitions 3 --replication-factor 1
kafka-topics --zookeeper $KAFKA_ZOOKEEPER_CONNECT --create --topic topico_intermediario_language --partitions 3 --replication-factor 1

* Mostre os resultados no terminal:
// recebe mensagens topico
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_by_language


* Outras informaçoes para testes:
//envia mensagens para topico
kafka-console-producer --broker-list localhost:9092 --topic tweets_input

//recebe mensagens topico
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_output
