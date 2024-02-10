INSERT INTO content (id, name, storage_name, type, created_at) VALUES
(200, 'test.txt', '8d4073ce-17d5-43e1-90a0-62e94fba1402', 'plain/text', NOW()),
(400, 'test.txt', '405c6f9c-cd36-473d-88ea-5d1fbedde283', 'plain/text', NOW()),
(401, 'test.txt', 'f503a9d1-9e01-432d-9728-7eebd01ed739', 'plain/text', NOW()),
(402, 'test.txt', '4beb8c23-7f67-4423-a947-c83c32faee20', 'plain/text', '2020-01-01'),
(403, 'test.txt', 'f503a9d1-9e01-432d-9728-7eebd01ed739', 'plain/text', '2020-01-01');

INSERT INTO resource (id, content_id, url, status, created_at, attempt_number) VALUES
(100, null, 'https://loremipsum.net/1',                       'NEW',   NOW(),        0),
(200, 200,  'https://example.com',                            'NEW',   NOW(),        0),
(300, null, 'https://resourcecommandrepositorysave.com',      'RETRY', NOW(),        0),
(400, 400,  'https://listResourceRootToRetry.com',            'ERROR', '2020-01-01', 0),
(401, 401,  'https://listResourceRootToRetry2.com',           'RETRY', NOW(),        0),
(402, 402,  'https://listResourceRootToRetry3.com',           'NEW',   '2020-01-01', 0),
(403, 403,  'https://listResourceRootToRetry4.com',           'RETRY', '2020-01-01', 0);