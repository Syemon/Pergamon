package com.pergamon.Pergamon.v1.service;

import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
public class Event<T> extends ApplicationEvent {

    private final T payload;

    public Event(Object source, T payload) {
        super(source);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

}
