package com.example.standings;

import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Beka Tsotsoria
 */
public class AbstractStandingsProvider implements StandingsProvider {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ConcurrentHashMap<String, Participant> participants = new ConcurrentHashMap<>();

    @Override
    public final Standings getStandings() {
        return new Standings(new ArrayList<>(participants.values()));
    }

    protected void onOperation(OperationStarted started) {
        boolean newParticipant = participants.putIfAbsent(started.getUser(),
            new Participant(started.getUser(), started.getOperationId(), LocalDateTime.now(), 0)) == null;
        if (newParticipant) {
            log.info("New participant \"{}\"", started.getUser());
        }
    }

    protected void onOperation(OperationCompleted completed) {
        Participant participant = participants.get(completed.getUser());
        if (participant != null) {
            if (!participant.getOperationId().equals(completed.getOperationId())) {
                log.info("Participant \"{}\" is now busy with operation \"{}\"", participant.getOperationId());
            } else {
                participant.calculateScore();
            }
        } else {
            log.info("Participant with \"{}\" does not exist", completed.getUser());
        }
    }
}
