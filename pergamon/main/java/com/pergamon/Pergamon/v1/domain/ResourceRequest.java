package com.pergamon.Pergamon.v1.domain;

import jakarta.validation.constraints.NotNull;

import java.net.URL;

public record ResourceRequest(@NotNull URL url) {
}
