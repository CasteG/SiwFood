insert into chef (id, first_name, last_name, date_of_birth) values(nextval('chef_seq'), 'Bruno', 'Barbieri', '1962-01-12');
insert into chef (id, first_name, last_name, date_of_birth) values(nextval('chef_seq'), 'Carlo', 'Cracco', '1965-10-08');
insert into chef (id, first_name, last_name, date_of_birth) values(nextval('chef_seq'), 'Antonio', 'Cannavacciuolo', '1975-04-16');
insert into chef (id, first_name, last_name, date_of_birth) values(nextval('chef_seq'), 'Massimo', 'Bottura', '1962-09-30');
insert into recipe (id, name, description, chef_id) values(nextval('recipe_seq'), 'Lasagna', 'Piatto tradizionale', 51);
insert into recipe (id, name, description, chef_id) values(nextval('recipe_seq'), 'Pasta alla carbonara', 'Piatto tradizionale della cucina romana', 1);
insert into recipe (id, name, description, chef_id) values(nextval('recipe_seq'), 'Tiramisu', 'Il dolce che non puo mancare in un menù italiano', 51);
insert into ingredient (id, name) values(nextval('ingredient_seq'), 'Salsa di pomodoro');
insert into ingredient (id, name) values(nextval('ingredient_seq'), 'Macinato di carne');
insert into ingredient (id, name) values(nextval('ingredient_seq'), 'Besciamella');
insert into ingredient (id, name) values(nextval('ingredient_seq'), 'Uova');
insert into ingredient (id, name) values(nextval('ingredient_seq'), 'Guanciale');
insert into recipe_ingredient(id, recipe_id, ingredient_id, quantity) values(nextval('recipe_ingredient_seq'),1 , 1, 2);
insert into recipe_ingredient(id, recipe_id, ingredient_id, quantity) values(nextval('recipe_ingredient_seq'), 51, 51, 1);
insert into recipe_ingredient(id, recipe_id, ingredient_id, quantity) values(nextval('recipe_ingredient_seq'), 101, 101, 3);




