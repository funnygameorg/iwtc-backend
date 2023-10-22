-- H2 DB

-- 월드컵 게임 1
INSERT INTO world_cup_game (created_at, description, member_id, round, soft_delete, title, updated_at, views, visible_type)
VALUES (DATEADD('HOUR', -12, NOW()), null, 1, 'ROUND_8', false, '역대 K-POP 월드컵', null, 15, 'PUBLIC');

INSERT INTO media_file (absolute_name, created_at, extension, file_path, original_name, updated_at)
VALUES ('2023-warklmf389-gzzz-adfA', DATEADD('HOUR', -12, NOW()), 'JPG', 'https://picsum.photos/seed/awf/400/500', 'null', null),
       ('2023-warklmf389-s-aAs', DATEADD('HOUR', -12, NOW()), 'PNG', 'https://picsum.photos/seed/azz/400/500', 'null', null),
       ('2023-awfe234-23215-aA3', DATEADD('HOUR', -12, NOW()), 'JPEG', 'https://picsum.photos/seed/efg/400/500', '컵', null),
       ('2023-vcxve-23215-5aA', DATEADD('HOUR', -12, NOW()), 'PNG', 'https://picsum.photos/seed/gerh/400/500', '쥬스', null),
       ('2023-dfew-32sd-a4A', DATEADD('HOUR', -12, NOW()), 'GIF', 'https://picsum.photos/seed/fdf/400/500', 'Roller', null),
       ('2023-sdg43g-zcxv-aafA', DATEADD('HOUR', -12, NOW()), 'GIF', 'https://picsum.photos/seed/love/400/500', 'null', null);

INSERT INTO world_cup_game_contents (created_at, media_file_id, name, updated_at, world_cup_game_id)
VALUES (DATEADD('HOUR', -12, NOW()), 1, '시계와 시계', null, 1),
       (DATEADD('HOUR', -12, NOW()), 2, '산의 정기', null, 1),
       (DATEADD('HOUR', -12, NOW()), 3, '시계와 시계', null, 1),
       (DATEADD('HOUR', -12, NOW()), 4, '닌텐도 스위치', null, 1),
       (DATEADD('HOUR', -12, NOW()), 5, '두비두답', null, 1),
       (DATEADD('HOUR', -12, NOW()), 6, '유튜브 스프링 강의', null, 1);



-- 월드컵 게임 2
INSERT INTO world_cup_game (created_at, description, member_id, round, soft_delete, title, updated_at, views, visible_type)
VALUES (DATEADD('HOUR', -8, NOW()), null, 1, 'ROUND_8', false, '힘든 군부대 월드컵', null, 0, 'PUBLIC');

INSERT INTO media_file (absolute_name, created_at, extension, file_path, original_name, updated_at)
VALUES ('2023-warklmf389-gzzz-adfA', DATEADD('HOUR', -8, NOW()), 'JPG', 'https://picsum.photos/seed/zv/400/500', '이기자 부대', null),
       ('2023-warklmf389-s-aAs', DATEADD('HOUR', -8, NOW()), 'PNG', 'https://picsum.photos/seed/rr/300/400', '정곡골 부대', null),
       ('2023-awfe234-23215-aA3', DATEADD('HOUR', -8, NOW()), 'JPEG', 'https://picsum.photos/seed/az/300/400', '청산리 부대', null),
       ('2023-vcxve-23215-5aA', DATEADD('HOUR', -8, NOW()), 'PNG', 'https://picsum.photos/seed/fwe/400/500', '이빨 부대', null),
       ('2023-dfew-32sd-a4A', DATEADD('HOUR', -8, NOW()), 'GIF', 'https://picsum.photos/seed/gf/600/800', '붉은 피 부대', null),
       ('2023-sdg43g-zcxv-aafA', DATEADD('HOUR', -8, NOW()), 'GIF', 'https://picsum.photos/seed/12ffa/100/200', '해운대 부대', null);

INSERT INTO world_cup_game_contents (created_at, media_file_id, name, updated_at, world_cup_game_id)
VALUES (DATEADD('HOUR', -8, NOW()), 1, '이기자 부대', null, 2),
       (DATEADD('HOUR', -8, NOW()), 2, '정곡골 부대', null, 2),
       (DATEADD('HOUR', -8, NOW()), 3, '청산리 부대', null, 2),
       (DATEADD('HOUR', -8, NOW()), 4, '이빨 부대', null, 2),
       (DATEADD('HOUR', -8, NOW()), 5, '붉은 피 부대', '2023-08-01 23:39:56', 2),
       (DATEADD('HOUR', -8, NOW()), 6, '해운대 부대', null, 2);



-- 월드컵 게임 3
INSERT INTO world_cup_game (created_at, description, member_id, round, soft_delete, title, updated_at, views, visible_type)
VALUES (DATEADD('HOUR', -3, NOW()), null, 1, 'ROUND_8', false, '연애상대로 좋은 MBTI 월드컵', null, 15, 'PUBLIC');

INSERT INTO media_file (absolute_name, created_at, extension, file_path, original_name, updated_at)
VALUES ('2023-warklmf389-gzzz-adfA', DATEADD('HOUR', -2, NOW()), 'JPG', 'https://picsum.photos/seed/wef/400/500', 'RQWtqwf', null),
       ('2023-warklmf389-s-aAs', DATEADD('HOUR', -2, NOW()), 'PNG', 'https://picsum.photos/seed/qa/300/400', 'FEWGewgw', null),
       ('2023-awfe234-23215-aA3', DATEADD('HOUR', -2, NOW()), 'JPEG', 'https://picsum.photos/seed/az/300/400', 'undifined', null),
       ('2023-vcxve-23215-5aA', DATEADD('HOUR', -2, NOW()), 'PNG', 'https://picsum.photos/seed/c/400/500', 'f323g@', null),
       ('2023-dfew-32sd-a4A', DATEADD('HOUR', -2, NOW()), 'GIF', 'https://picsum.photos/seed/az/600/800', 'undefined', null),
       ('2023-sdg43g-zcxv-aafA', DATEADD('HOUR', -2, NOW()), 'GIF', 'https://picsum.photos/seed/b/100/200', 'undefined', null);

INSERT INTO world_cup_game_contents (created_at, media_file_id, name, updated_at, world_cup_game_id)
VALUES (DATEADD('HOUR', -2, NOW()), 1, 'INFP', null, 3),
       (DATEADD('HOUR', -2, NOW()), 2, 'ESTP', null, 3),
       (DATEADD('HOUR', -2, NOW()), 3, 'ISFP', null, 3),
       (DATEADD('HOUR', -2, NOW()), 4, 'ENFP', null, 3),
       (DATEADD('HOUR', -2, NOW()), 5, 'ISTP', null, 3),
       (DATEADD('HOUR', -2, NOW()), 6, 'ESTJ', '2023-08-01 23:38:56', 3);
