package com.pergamon.Pergamon.v1.service;

public class StoreResourceEvent extends Event<StoreResourcePayload> {
    public StoreResourceEvent(Object source, StoreResourcePayload payload) {
        super(source, payload);
    }
}
