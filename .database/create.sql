create database mydatabase;

create user 'myuser'@'%' identified by 'mypassword';
grant all on mydatabase.* to 'myuser'@'%';