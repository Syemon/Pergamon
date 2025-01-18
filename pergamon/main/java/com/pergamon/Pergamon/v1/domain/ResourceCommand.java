package com.pergamon.Pergamon.v1.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.net.URL;

@Data
public class ResourceCommand {
    @NotNull
    URL url;
}
