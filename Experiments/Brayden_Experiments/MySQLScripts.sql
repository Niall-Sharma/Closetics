CREATE TABLE public.user (
	user_id UUID CONSTRAINT user_pk PRIMARY KEY,
	username text NOT NULL CONSTRAINT user_username UNIQUE,
	password text NOT NULL,
	first_name text NOT NULL,
	last_name text,
	user_created_date date NOT NULL
);
CREATE TABLE public.clothing_item (
    item_id SERIAL PRIMARY KEY, -- Auto-incrementing primary key
    user_id UUID NOT NULL,
    -- Automatically deletes all clothing_item rows associated with a user if that user is deleted from the users table
    FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE,
    brand VARCHAR(255),
    color VARCHAR(50),
    date_bought DATE,
    price NUMERIC(10, 2),
    item_name VARCHAR(255),
    clothing_category VARCHAR(100) NOT NULL,
    clothing_type VARCHAR(100) NOT NULL,
    is_favorite BOOLEAN DEFAULT FALSE,
    image_path1 VARCHAR(1024) NOT NULL,
    image_path2 VARCHAR(1024),
    image_path3 VARCHAR(1024),
);
CREATE TABLE public.outfit (
    outfit_id SERIAL PRIMARY KEY,  -- Auto-incrementing primary key
    -- Automatically deletes all outfit rows associated with a user if that user is deleted from the users table
    FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE,
    outfit_name VARCHAR(255),
    creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    is_favorite BOOLEAN DEFAULT FALSE
);
CREATE TABLE public.outfit_items (
    outfit_item_id SERIAL PRIMARY KEY, -- Auto-incrementing unique ID for this link
    outfit_id INT NOT NULL,            -- Foreign key to the outfit table
    item_id INT NOT NULL,              -- Foreign key to the clothing_item table
    FOREIGN KEY (outfit_id) REFERENCES public.outfit(outfit_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES public.clothing_item(item_id) ON DELETE CASCADE
);
CREATE TABLE public.clothing_item_stats (
    item_tracked_stats_id SERIAL PRIMARY KEY, -- Auto-incrementing primary key
    item_id INT NOT NULL,                     -- Foreign key to the clothing_item table
    FOREIGN KEY (item_id) REFERENCES public.users(item_id) ON DELETE CASCADE,
    outfit_id INT,                            -- Foreign key to the outfit table
    FOREIGN KEY (outfit_id) REFERENCES public.users(outfit_id) ON DELETE SET NULL,
    date_worn DATE NOT NULL DEFAULT CURRENT_DATE,
    high_temp NUMERIC(10, 2),
    low_temp NUMERIC(10, 2)
);
CREATE TABLE public.outfit_stats (
    outfit_tracked_stats_id SERIAL PRIMARY KEY, -- Auto-incrementing primary key
    outfit_id INT NOT NULL,                     -- Foreign key to the outfit table
    FOREIGN KEY (outfit_id) REFERENCES public.users(outfit_id),
    date_worn DATE NOT NULL DEFAULT CURRENT_DATE,
    high_temp NUMERIC(10, 2),
    low_temp NUMERIC(10, 2)
);