package com.example.model;

import com.example.model.command.CompleteOperation;
import com.example.model.command.StartOperation;
import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;

/**
 * @author Beka Tsotsoria
 */
public class OperationProcessor {

    private EventBus eventBus;

    public OperationProcessor(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void process(StartOperation startOperation) {
        eventBus.publish(new OperationStarted(startOperation.getUser(), startOperation.getOperationId()));
    }

    public void process(CompleteOperation completeOperation) {
        eventBus.publish(new OperationCompleted(completeOperation.getUser(), completeOperation.getOperationId()));
    }
}
