package com.pergamon.Pergamon.v1.domain;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ResourceQueryRepository {
    Optional<Resource> findByUrl(URL url);
    
    List<Resource> list();
    List<Resource> list(String urlFragment);

}
