INSERT INTO godutchtrip.ROLE(id, name, description) VALUES(1,'admin','Has unlimited privileges to everything.');
INSERT INTO godutchtrip.ROLE(id, name, description) VALUES(2,'guest','Has very limited access. Just can visit general pages and do limited actions.');

INSERT INTO godutchtrip.user(id, USER_NAME, PASSWORD, EMAIL) VALUES (1,'admin', 'e519d416c8b2623331c17695e7c6ab641e96cd0ce3f636b097b1be68fd793e16', 'admin@goDutchTrip.com');
INSERT INTO godutchtrip.traveller(id, FIRST_NAME, LAST_NAME, USER_ID) VALUES (1, 'Admin', 'Admin', 1);

INSERT INTO godutchtrip.USER_ROLE(user_id, role_id) VALUES (1, 1);
