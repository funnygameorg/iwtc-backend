INSERT INTO `ERROR_CODE_ENTITY` (`name`, `code`, `message`, `http_status`)
VALUES
    ('DUPLICATED_MEMBER_SERVICE_ID', 4001, '이미 존재하는 아이디입니다.', 409),
    ('DUPLICATED_MEMBER_NICKNAME', 4002, '이미 존재하는 닉네임입니다.', 409),
    ('DUPLICATED_WORLD_CUP_GAME_TITLE', 4003, '이미 존재하는 타이틀입니다.', 409),

    ('INVALID_CLIENT_REQUEST_BODY', 3001, '올바르지 않은 요청입니다.', 400),
    ('INVALID_TOKEN_EXCEPTION', 3002, '올바르지 않은 토큰입니다.', 401),
    ('NOT_NULL_ARGUMENT', 3003, '필수 데이터입니다.', 400),

    ('NOT_FOUND_DATA_IN_REQUEST', 5001, '사용자 요청에 데이터가 없습니다.', 404),
    ('NOT_FOUND_WORLD_CUP_GAME', 5002, '존재하지 않는 월드컵 게임입니다.', 404),
    ('NOT_FOUND_MEMBER', 5003, '존재하지 않는 사용자입니다.', 404),
    ('NOT_FOUND_WORLD_CUP_GAME_CONTENTS', 5004, '존재하지 않는 월드컵 게임 컨텐츠입니다.', 404),
    ('NOT_FOUND_MEDIA_FILE', 5005, '존재하지 않는 미디어 파일입니다.', 404),
    ('NOT_FOUND_COMMENT', 5006, '존재하지 않는 댓글입니다.', 404),

    ('NO_ROUNDS_AVAILABLE_TO_PLAY', 10001, '컨텐츠 부족으로 플레이할 수 없습니다.', 400),
    ('ILLEGAL_WORLD_CUP_GAME_CONTENTS', 10002, '올바르지 않은 월드컵 게임 컨텐츠입니다.', 400),
    ('EXPIRED_REMEMBER_ME', 10003, '만료된 사용자 인증입니다.', 401),
    ('REQUEST_WITH_BLACK_LIST_TOKEN', 10004, '사용할 수 없는 토큰입니다.', 401),
    ('NOT_SUPPORTED_GAME_ROUND', 10005, '진행할 수 없는 게임 라운드입니다.', 400),
    ('NOT_OWNER_GAME', 10006, '월드컵 게임 작성자가 아닙니다.', 400),
    ('NOT_EXISTS_S3_MEDIA_FILE', 10007, '미디어 파일 저장소에 문제가 생겼습니다.', 400),
    ('NOT_OWNER_COMMENT', 10006, '댓글 작성자가 아닙니다.', 400),


    ('SERVER_INTERNAL_ERROR', 444, '서버에 문제가 생겼습니다.', 500);







INSERT INTO `WORLD_CUP_GAME` (`created_at`, `description`, `member_id`, `soft_delete`, `title`, `updated_at`, `views`, `visible_type`)
VALUES
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '역대 K-POP 월드컵', null, 15, 'PUBLIC'),
    (DATEADD('DAY', -8, NOW()), null, 1, false, '힘든 군부대 월드컵', null, 0, 'PUBLIC'),
    (DATEADD('HOUR', -3, NOW()), null, 1, false, '연애상대로 좋은 MBTI 월드컵', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 1', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 2', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 3', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 4', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 5', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 6', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 7', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 8', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 9', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 10', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 11', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 12', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 13', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 14', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 15', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 16', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 17', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 18', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 19', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 20', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 21', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 22', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 23', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 24', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 25', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 26', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 27', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 28', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 29', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 30', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 31', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 32', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 33', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 34', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 35', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 36', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 37', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 38', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '게임 39', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), '2023 기준 20대가 선호하는 월드컵', 1, false, '재미있는 게임 월드컵', null, 15, 'PUBLIC'),
    (DATEADD('MONTH', -2, NOW()), null, 1, false, '맛있는 음식 월드컵', null, 15, 'PUBLIC');

INSERT INTO `MEDIA_FILE`(`object_key`, `file_type`, `d_type`, `created_at`, `updated_at`)
VALUES
    ('a', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('b', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('c', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('d', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('e', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('f', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('g', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('h', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('i', 'STATIC_MEDIA_FILE', 'StaticMediaFile', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('j', 'INTERNET_VIDEO_URL', 'InternetVideoUrl', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('k', 'INTERNET_VIDEO_URL', 'InternetVideoUrl', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('l', 'INTERNET_VIDEO_URL', 'InternetVideoUrl', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('m', 'INTERNET_VIDEO_URL', 'InternetVideoUrl', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW())),
    ('n', 'INTERNET_VIDEO_URL', 'InternetVideoUrl', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()));

INSERT INTO `STATIC_MEDIA_FILE` (`extension`, `original_name`, `id`)
VALUES
    ('PNG', 'originalName1', 1),
    ('PNG', 'originalName2', 2),
    ('JPEG', 'originalName3', 3),
    ('GIF', 'originalName4', 4),
    ('GIF', 'originalName5', 5),
    ('JPEG', 'originalName6', 6),
    ('JPEG', 'originalName7', 7),
    ('JPEG', 'originalName8', 8),
    ('JPEG', 'originalName9', 9);

INSERT INTO `INTERNET_VIDEO_URL`(`id`, `is_playable_video`, `video_start_time`, `video_play_duration`, `video_detail_type`)
VALUES
    (10, true, '00001', 5, 'YOU_TUBE_URL'),
    (11, true, '00010', 4, 'YOU_TUBE_URL'),
    (12, true, '00101', 3, 'YOU_TUBE_URL'),
    (13, true, '01001', 5, 'YOU_TUBE_URL'),
    (14, true, '00030', 2, 'YOU_TUBE_URL');

INSERT INTO `WORLD_CUP_GAME_CONTENTS` (`media_file_id`, `name`, `world_cup_game_id`, `game_score`, `game_rank`, `visible_type`, `created_at`, `updated_at`, `soft_delete`)
VALUES
    (1, '익명의 컨텐츠명1', 1, 10, 6, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (2, '익명의 컨텐츠명2', 1, 300, 4, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (3, '익명의 컨텐츠명3', 1, 1000, 1, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (4, '익명의 컨텐츠명4', 1, 500, 3, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (5, '익명의 컨텐츠명5', 1, 600, 2, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (6, '익명의 컨텐츠명6', 1, 30, 5, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (7, '익명의 컨텐츠명7', 1, 0, 7, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (8, '익명의 컨텐츠명8', 1, 0, 8, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (9, '익명의 컨텐츠명9', 2, 30, 1, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (10, '익명의 컨텐츠명10', 2, 7, 5, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (11, '익명의 컨텐츠명11', 2, 10, 4, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (12, '익명의 컨텐츠명12', 2, 15, 2, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false),
    (13, '익명의 컨텐츠명13', 2, 12, 3, 'PUBLIC', DATEADD('MONTH', -2, NOW()), DATEADD('MONTH', -2, NOW()), false);