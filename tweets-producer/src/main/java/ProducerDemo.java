import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {

    private KafkaProducer<String,String> producer;

    public ProducerDemo() {
        // Criar as propriedades do produtor
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Criar o produtor
        this.producer = new KafkaProducer<String, String>(properties);
    }
    public void send(String tweetText){

        // Enviar mensagens
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("tweets_topic", tweetText);
        this.producer.send(record); // Envio assíncrono

    }
    public void close(){
        this.producer.close();

    }
}
