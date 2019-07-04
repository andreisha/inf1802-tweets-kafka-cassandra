import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TweetConsumerLifecycleManager implements LifecycleManager, Serializable {

    private static final String KAFKA_CLUSTER = System.getenv().getOrDefault("KAFKA_CLUSTER", "localhost:9092");
    private static final String CONSUMER_GROUP = "tweets_application";
    private static final String TOPIC_NAME = "tweets_input";
    private static final Logger logger = LoggerFactory.getLogger(TweetConsumerLifecycleManager.class.getName());
    private final AtomicBoolean running = new AtomicBoolean(false);
    private KafkaConsumer<String, Tweet> consumer;
    private Future<?> future;
    Cluster cluster = null;
    TweetRepository br;
    KeyspaceRepository sr;


    public TweetConsumerLifecycleManager() {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CLUSTER);
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TweetDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(kafkaProps);
    }

    public void start() {
        logger.info("Start");

        // do the set up of Cassandra the first time you run
        setUpCassandra();

        // do this set up if Cassandra is already running
        //setUpCassandraAlreadyRunning();

        if (running.compareAndSet(false, true)) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            future = executor.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("Start");
                    try {
                        consumer.subscribe(Arrays.asList(TOPIC_NAME));
                        logger.info("Consumidor subscrito no tópico: ", TOPIC_NAME);


                        while (true) {
                            ConsumerRecords<String, Tweet> records = consumer.poll(Duration.ofMillis(1000));
                            for (ConsumerRecord<String, Tweet> record : records) {
                                Tweet tweet = record.value();
                                logger.info("Consumindo do Kafka o Tweet: " + tweet);
                                sendToCassandra(tweet);
                                logger.info("Sent to Cassandra");
                            }
                        }
                    }
                    catch (Exception e) {
                        //printAll();  // mesmo com erro, vemos o estado das tabelas
                        //e.printStackTrace();
                        logger.error ("Erro no consumo dos tweets do Kafka", e);

                    } finally {

                    }
                }
            });
            logger.info("Serviço iniciado");
        } else {
            logger.warn("O serviço já está executando.");
        }
    }

    public void stop()  {
        if (running.compareAndSet(true, false)) {
            if (future.cancel(true)) {
                consumer.wakeup();
            }

            printAll();
            // stop Cassandra when you finish
            // stopCassandra();
            consumer.close();
            logger.info("Serviço finalizado");

        } else {
            logger.warn("O serviço não está executando. Não pode ser parado.");
        }
    }

    public void setUpCassandra(){
        cluster = Cluster.builder()
                .addContactPoint("localhost")
                .build();

        Session session = cluster.connect();

        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();
        System.out.println(((Row) row).getString("release_version"));

        this.sr = new KeyspaceRepository(session);
        sr.createKeyspace("library", "SimpleStrategy", 1);
        System.out.println("Create repository");
        sr.useKeyspace("library");
        System.out.println("Using repository library");
        this.br = new TweetRepository(session);

        br.createTable();
        br.createTableTweetsByLanguage();
    }

    public void setUpCassandraAlreadyRunning(){

        cluster = Cluster.builder()
                .addContactPoint("localhost")
                .build();

        Session session = cluster.connect();

        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();
        System.out.println(((Row) row).getString("release_version"));

        this.sr = new KeyspaceRepository(session);
        sr.useKeyspace("library");
        System.out.println("Using repository library");
        this.br = new TweetRepository(session);
    }

    public void stopCassandra(){
        br.deleteTable("tweets");
        System.out.println("Delete table tweets done ");
        br.deleteTable("tweetsByLanguage");
        System.out.println("Delete table tweetsByLanguage done ");

        sr.deleteKeyspace("library");
        System.out.println("Delete Keyspace library done");

        if (cluster != null) cluster.close();

    }

    public void sendToCassandra(Tweet tweet){
        br.inserttweet(tweet);
        br.inserttweetByLanguage(tweet);
    }

    public void printAll(){
        br.selectAll();
        br.selectAllByLanguage();
    }

}
