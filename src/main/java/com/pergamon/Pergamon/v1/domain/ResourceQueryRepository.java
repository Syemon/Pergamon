package com.pergamon.Pergamon.v1.domain;

import java.net.URL;
import java.util.Optional;

public interface ResourceQueryRepository {
    Optional<Resource> findByUrl(URL url);
}
