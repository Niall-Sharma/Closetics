CREATE TABLE user (
    user_id binary(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    user_created_date DATE NOT NULL DEFAULT (CURRENT_DATE())
);

CREATE TABLE clothing_item (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id binary(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    brand VARCHAR(255),
    color VARCHAR(50),
    date_bought DATE,
    price DECIMAL(10,2),
    item_name VARCHAR(255),
    clothing_category VARCHAR(100) NOT NULL,
    clothing_type VARCHAR(100) NOT NULL,
    is_favorite BOOLEAN DEFAULT FALSE,
    image_path1 VARCHAR(1024) NOT NULL,
    image_path2 VARCHAR(1024),
    image_path3 VARCHAR(1024)
);

CREATE TABLE outfit (
    outfit_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id binary(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    outfit_name VARCHAR(255),
    creation_date DATE NOT NULL DEFAULT (CURRENT_DATE()),
    is_favorite BOOLEAN DEFAULT FALSE
);

CREATE TABLE outfit_items (
    outfit_item_id INT AUTO_INCREMENT PRIMARY KEY,
    outfit_id INT NOT NULL,
    item_id INT NOT NULL,
    FOREIGN KEY (outfit_id) REFERENCES outfit(outfit_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES clothing_item(item_id) ON DELETE CASCADE
);

CREATE TABLE clothing_item_stats (
    item_tracked_stats_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES clothing_item(item_id) ON DELETE CASCADE,
    outfit_id INT,
    FOREIGN KEY (outfit_id) REFERENCES outfit(outfit_id) ON DELETE SET NULL,
    date_worn DATE NOT NULL DEFAULT (CURRENT_DATE()),
    high_temp DECIMAL(10,2),
    low_temp DECIMAL(10,2)
);

CREATE TABLE outfit_stats (
    outfit_tracked_stats_id INT AUTO_INCREMENT PRIMARY KEY,
    outfit_id INT NOT NULL,
    FOREIGN KEY (outfit_id) REFERENCES outfit(outfit_id) ON DELETE CASCADE,  -- Fix FK reference
    date_worn DATE NOT NULL DEFAULT (CURRENT_DATE()),
    high_temp DECIMAL(10,2),
    low_temp DECIMAL(10,2)
);