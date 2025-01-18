package com.pergamon.Pergamon.v1.service;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ValidationError {
    List<String> failureMessages;
}
