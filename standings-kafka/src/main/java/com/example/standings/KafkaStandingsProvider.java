package com.example.standings;

import com.ecample.model.serde.ModelSerdes;
import com.example.model.User;
import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Beka Tsotsoria
 */
@SuppressWarnings("deprecated")
public class KafkaStandingsProvider implements StandingsProvider {

	private final Logger log = LoggerFactory.getLogger(KafkaStandingsProvider.class);

	private ConcurrentHashMap<String, Participant> participants = new ConcurrentHashMap<>();

	private KafkaStreams startedStreams;
	private KafkaStreams completedStreams;

	private StreamBuilder<OperationStarted> startedStreamBuilder;
	private StreamBuilder<OperationCompleted> completedStreamBuilder;

	public KafkaStandingsProvider(String kafkaAddress, String startedTopic, String completedTopic) {
		startedStreamBuilder = new StreamBuilder<>(kafkaAddress, "started-app-id", startedTopic, ModelSerdes.OPERATION_STARTED);
		completedStreamBuilder = new StreamBuilder<>(kafkaAddress, "completed-app-id", completedTopic, ModelSerdes.OPERATION_COMPLETED);

		KStream<String, OperationStarted> startedStream = startedStreamBuilder.build();
		startedStream.foreach((key, operationStarted) -> {
			User user = operationStarted.getUser();
			boolean newParticipant = participants.putIfAbsent(user.getName(),
					new Participant(user.getName(), operationStarted.getOperationId(), LocalDateTime.now(), 0)) == null;
			if (newParticipant) {
				log.info("New participant \"{}\"", user.getName());
			}
		});

		completedStreamBuilder.build().foreach((key, operationCompleted) -> {
			User user = operationCompleted.getUser();
			Participant participant = participants.get(user.getName());
			if (participant != null) {
				if (!participant.getOperationId().equals(operationCompleted.getOperationId())) {
					log.info("Participant \"{}\" is now busy with operation \"{}\"", participant.getOperationId());
				} else {
					participant.calculateScore();
				}
			} else {
				log.info("Participant with \"{}\" does not exist", user.getName());
			}
		});
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

	@Override
	public Standings getStandings() {
		return new Standings(new ArrayList<>(participants.values()));
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
