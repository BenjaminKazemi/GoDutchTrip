INSERT INTO godutchtrip.ROLE(id,name,description) VALUES(1,'admin','Has unlimited privileges to everything.');
INSERT INTO godutchtrip.ROLE(id,name,description) VALUES(2,'guest','Has very limited access. Just can visit general pages and do limited actions.');

INSERT INTO godutchtrip.person(id,FIRST_NAME,LAST_NAME) VALUES (1, 'Admin', 'Admin');
INSERT INTO godutchtrip.user(id,PASSWORD,USER_NAME) VALUES (1,'admin', 'admin');

INSERT INTO godutchtrip.person(id,FIRST_NAME,LAST_NAME) VALUES (2, 'Guest', 'Guest');
INSERT INTO godutchtrip.user(id,PASSWORD,USER_NAME) VALUES (2,'guest', 'guest');

INSERT INTO godutchtrip.USER_ROLE(user_id,role_id) VALUES (1,1);
INSERT INTO godutchtrip.USER_ROLE(user_id,role_id) VALUES (2,2);