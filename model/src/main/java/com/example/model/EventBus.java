package com.example.model;

import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;

/**
 * @author Beka Tsotsoria
 */
public interface EventBus {

    void publish(OperationStarted operationStarted);

    void publish(OperationCompleted operationCompleted);
}
