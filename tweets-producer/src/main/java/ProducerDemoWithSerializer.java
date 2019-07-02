import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class ProducerDemoWithSerializer {

    private static Logger logger = LoggerFactory.getLogger(ProducerDemoWithSerializer.class.getName());
    private KafkaProducer<String, Tweet> producer;

    public ProducerDemoWithSerializer() {

        // Criar as propriedades do produtor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TweetSerializer.class.getName());

        // Criar o produtor
        this.producer = new KafkaProducer<>(properties);
    }

    public void send(Tweet tweet) {
        // Enviar as mensagens
        ProducerRecord<String, Tweet> record = new ProducerRecord<>("tweets_input", tweet);
        producer.send(record);
        logger.info(tweet.toString());
    }

    public void close(){
        this.producer.close();

    }
}
