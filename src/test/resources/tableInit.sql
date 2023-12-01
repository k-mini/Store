--  h2 데이터베이스
SET REFERENTIAL_INTEGRITY FALSE;
truncate table users RESTART IDENTITY;
truncate table board RESTART IDENTITY;
truncate table item_board RESTART IDENTITY;
truncate table community_board RESTART IDENTITY;
-- truncate table category RESTART IDENTITY;
-- truncate table board_category RESTART IDENTITY;
truncate table comment RESTART IDENTITY;
truncate table trade RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;