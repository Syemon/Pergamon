INSERT INTO content (id, name, storage_name, type, created_at) VALUES
(200, 'test.txt', '8d4073ce-17d5-43e1-90a0-62e94fba1402', 'plain/text', NOW());

INSERT INTO resource (id, content_id, url, status, created_at) VALUES
(100, null, 'https://loremipsum.net/1',                       'NEW',   NOW()),
(200, 200,  'https://example.com',                            'NEW',   NOW()),
(300, null, 'https://resourcecommandrepositorysave.com',      'RETRY', NOW());
