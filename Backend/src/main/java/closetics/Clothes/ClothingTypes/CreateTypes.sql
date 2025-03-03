-- Create the Types Table
CREATE TABLE Types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL
);

-- Insert Types
INSERT INTO Types (type_name) VALUES
    ('ACCESSORIES'),
    ('ACTIVEWEAR'),
    ('BOTTOMS'),
    ('DRESSES'),
    ('FOOTWEAR'),
    ('FORMALWEAR'),
    ('OUTERWEAR'),
    ('SEASONAL'),
    ('SLEEPWEAR'),
    ('TOPS'),
    ('UNDERGARMENTS'),
    ('WORKWEAR');
