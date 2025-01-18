package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentCommand;
import com.pergamon.Pergamon.v1.domain.ContentCommandRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContentCommandRepositoryImpl implements ContentCommandRepository {

    private final PostgresFileRepository fileRepository;
    private final ContentMapper contentMapper;

    @Override
    public Content createContent(ContentCommand contentCommand) {
        ContentEntity contentEntity = fileRepository.create(contentCommand);
        return contentMapper.mapContentEntityToContent(contentEntity);
    }
}
