USE shop;
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) UNIQUE not null,
    password VARCHAR(100) not null
);
SELECT * FROM shop.members;
INSERT INTO members (email, password) VALUES ('test@example.com', 'abc1234');