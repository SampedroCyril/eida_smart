DROP DATABASE IF EXISTS smart;

CREATE DATABASE smart;

CREATE USER IF NOT EXISTS 'smart'@'localhost' IDENTIFIED BY 'aaa';

GRANT ALL ON `smart`.* TO 'smart'@'localhost';
