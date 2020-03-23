INSERT INTO `facturapp`.`users` (username, password, name, lastname, email, created_at, active) VALUES ('admin','$2a$04$tJi21NZfSLBkJ6vfLHJB6eq8Et9zud.6KhbhclVLnR5qX0DhA3xBK','Administrador','SysAdmin','admin@facturapp.com',NOW(),1);
INSERT INTO `facturapp`.`users` (username, password, name, lastname, email, created_at, active) VALUES ('rafa','$2a$04$tJi21NZfSLBkJ6vfLHJB6eq8Et9zud.6KhbhclVLnR5qX0DhA3xBK','Rafael','Serrano Luna','rafa.serrano1996@gmail.com',NOW(),1);

INSERT INTO `facturapp`.`authorities` (description, name) VALUES ('ROLE_ADMIN', 'ADMIN');
INSERT INTO `facturapp`.`authorities` (description, name) VALUES ('ROLE_USER' ,'USER');

INSERT INTO `facturapp`.`users_authorities` (user_id, role_id) VALUES (1, 1);
INSERT INTO `facturapp`.`users_authorities` (user_id, role_id) VALUES (1, 2);
INSERT INTO `facturapp`.`users_authorities` (user_id, role_id) VALUES (2, 2);