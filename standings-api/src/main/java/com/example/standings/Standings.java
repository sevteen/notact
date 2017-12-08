package com.example.standings;

import java.util.List;

/**
 * @author Beka Tsotsoria
 */
public class Standings {

    private List<Participant> participants;

    public Standings(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Participant> getParticipants() {
        return participants;
    }
}
