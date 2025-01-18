package com.pergamon.Pergamon.v1.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContentCommand {
    String name;
    String storageName;
    String type;
}
