USE shop;
CREATE TABLE `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_id` int NOT NULL,
  `nickname` varchar(10) NOT NULL,
  `text` varchar(500) NOT NULL,
  `rating` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;