USE shop;
CREATE TABLE shop.comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    member_id INT NOT NULL,
    review_text TEXT NOT NULL,
    star_rating INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO shop.comments (item_id, member_id, review_text, star_rating)
VALUES
    (1, 21, 'ㅋㅋㅋㅋㅋㅋㅋ', 5);