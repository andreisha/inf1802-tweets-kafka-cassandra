# Big Data & Streaming

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

* Se NAO for a primeira vez que voce roda o Consumidor, que o Cassandra esta iniciado, e o keyspace e as tabelas criadas, vai na classe TweetConsumerLifecycleManager do modulo tweets-consumer e :
- comente a linha 48
- descomente a linha 51

* Se quiser apagar as tabelas e o keyspace ao acabar de consumir dados do Kafka, descomente a linha 98 da mesma classe

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

* Crie o topico necessario (tweets_by_language):
kafka-topics --zookeeper $KAFKA_ZOOKEEPER_CONNECT --create --topic tweets_by_language --partitions 3 --replication-factor 1

* Mostre os resultados no terminal:
// recebe mensagens topico
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_by_language -formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true --property  key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property --value.deserializer=org.apache.kafka.common.serialization.LongDeserializer --from-beginning


MELHORAS POSSIVEIS
No Consumidor:
- Por enquanto, a criaçao da tabela tweetsByLanguage usa como primary key (language, userName). Isso faz que somente seja guardado um tweet por pessoa e lingua, o que nao é otimo em termos de coleta de dados. Para melhorar isso, poderia-se botar a coluna dateCreated na clustering key, assim : ( (language, (userName, dateCreated) WITH CLUSTERING ORDER BY (userName ASC, dateCreated ASC) ), mas nesse caso deveria-se tambem inserir a hora no dateCreated ( modificar metodo getDateCreatedForCassandra() na classe Tweets desse mesmo modulo).
- No final, poderia-se imprimir o total de elementos de cada tabela do Cassandra ao invés de todas as instáncias das tabelas.


* Outras informaçoes para testes:
//envia mensagens para topico
kafka-console-producer --broker-list localhost:9092 --topic tweets_input

//recebe mensagens topico
kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets_output
