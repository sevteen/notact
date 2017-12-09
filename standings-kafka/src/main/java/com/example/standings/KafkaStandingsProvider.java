package com.example.standings;

import com.ecample.model.serde.ModelSerdes;
import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;

/**
 * @author Beka Tsotsoria
 */
@SuppressWarnings("deprecated")
public class KafkaStandingsProvider extends AbstractStandingsProvider {

    private KafkaStreams startedStreams;
    private KafkaStreams completedStreams;

    private StreamBuilder<OperationStarted> startedStreamBuilder;
    private StreamBuilder<OperationCompleted> completedStreamBuilder;

    public KafkaStandingsProvider(String kafkaAddress, String appId, String startedTopic, String completedTopic) {
        startedStreamBuilder = new StreamBuilder<>(kafkaAddress, "started-" + appId, startedTopic, ModelSerdes.OPERATION_STARTED);
        completedStreamBuilder = new StreamBuilder<>(kafkaAddress, "completed-" + appId, completedTopic, ModelSerdes.OPERATION_COMPLETED);

        startedStreamBuilder.build().foreach((key, operationStarted) -> onOperation(operationStarted));
        completedStreamBuilder.build().foreach((key, operationCompleted) -> onOperation(operationCompleted));
    }

    public void start() {
        startedStreams = new KafkaStreams(startedStreamBuilder.b, startedStreamBuilder.props);
        startedStreams.start();

        completedStreams = new KafkaStreams(completedStreamBuilder.b, completedStreamBuilder.props);
        completedStreams.start();
    }

    public void stop() {
        startedStreams.close();
        completedStreams.close();
    }

    static class StreamBuilder<T> {

        private String topic;
        private Properties props = new Properties();
        private KStreamBuilder b = new KStreamBuilder();

        public StreamBuilder(String kafkaAddress, String appId, String topic, Serde<T> serde) {
            this.topic = topic;
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, serde.getClass().getName());
        }

        public KStream<String, T> build() {
            return b.stream(topic);
        }
    }
}
