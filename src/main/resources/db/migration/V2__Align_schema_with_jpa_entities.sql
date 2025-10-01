-- Align users table with JPA entity
ALTER TABLE IF EXISTS users RENAME COLUMN password_hash TO password;
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS first_name VARCHAR(50) DEFAULT 'Unknown';
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS last_name VARCHAR(50) DEFAULT 'User';
UPDATE users SET first_name = COALESCE(NULLIF(split_part(full_name, ' ', 1), ''), first_name)
             , last_name  = COALESCE(NULLIF(split_part(full_name, ' ', 2), ''), last_name)
WHERE first_name = 'Unknown' AND last_name = 'User';
ALTER TABLE IF EXISTS users DROP COLUMN IF EXISTS full_name;
ALTER TABLE IF EXISTS users ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE IF EXISTS users ALTER COLUMN last_name SET NOT NULL;
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS phone VARCHAR(255);
ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;
UPDATE users SET active = TRUE WHERE active IS NULL;
ALTER TABLE IF EXISTS users ALTER COLUMN active SET NOT NULL;

-- Align contact_messages naming
ALTER TABLE IF EXISTS contact_messages RENAME COLUMN from_name TO sender_name;
ALTER TABLE IF EXISTS contact_messages RENAME COLUMN from_email TO sender_email;
ALTER TABLE IF EXISTS contact_messages ADD COLUMN IF NOT EXISTS is_read BOOLEAN DEFAULT FALSE;
ALTER TABLE IF EXISTS contact_messages ADD COLUMN IF NOT EXISTS read_at TIMESTAMP WITH TIME ZONE;
UPDATE contact_messages SET is_read = FALSE WHERE is_read IS NULL;
ALTER TABLE IF EXISTS contact_messages ALTER COLUMN sender_name SET NOT NULL;
ALTER TABLE IF EXISTS contact_messages ALTER COLUMN sender_email SET NOT NULL;
ALTER TABLE IF EXISTS contact_messages ALTER COLUMN message SET NOT NULL;
ALTER TABLE IF EXISTS contact_messages ALTER COLUMN is_read SET NOT NULL;

-- Align pet_images metadata
ALTER TABLE IF EXISTS pet_images RENAME COLUMN s3_key TO image_url;
ALTER TABLE IF EXISTS pet_images ADD COLUMN IF NOT EXISTS is_primary BOOLEAN DEFAULT FALSE;
UPDATE pet_images SET is_primary = COALESCE(is_primary, FALSE);
ALTER TABLE IF EXISTS pet_images ALTER COLUMN is_primary SET NOT NULL;
ALTER TABLE IF EXISTS pet_images ADD COLUMN IF NOT EXISTS alt_text VARCHAR(255);
ALTER TABLE IF EXISTS pet_images DROP COLUMN IF EXISTS order_index;
ALTER TABLE IF EXISTS pet_images ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF EXISTS pet_images ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;

-- Align pets table
ALTER TABLE IF EXISTS pets RENAME COLUMN age_months TO age;
ALTER TABLE IF EXISTS pets RENAME COLUMN sex TO gender;
ALTER TABLE IF EXISTS pets ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE IF EXISTS pets ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF EXISTS pets ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
UPDATE pets SET species = UPPER(species);
UPDATE pets SET gender = UPPER(gender);
UPDATE pets SET size = UPPER(size);
UPDATE pets SET status = CASE LOWER(status)
    WHEN 'available' THEN 'AVAILABLE'
    WHEN 'reserved' THEN 'PENDING'
    WHEN 'adopted' THEN 'ADOPTED'
    ELSE UPPER(status)
END;
ALTER TABLE IF EXISTS pets DROP CONSTRAINT IF EXISTS pets_species_check;
ALTER TABLE IF EXISTS pets DROP CONSTRAINT IF EXISTS pets_sex_check;
ALTER TABLE IF EXISTS pets DROP CONSTRAINT IF EXISTS pets_size_check;
ALTER TABLE IF EXISTS pets DROP CONSTRAINT IF EXISTS pets_status_check;
ALTER TABLE IF EXISTS pets ADD CONSTRAINT pets_species_check CHECK (species IN ('DOG','CAT','RABBIT','BIRD','HAMSTER','GUINEA_PIG','FISH','REPTILE','OTHER'));
ALTER TABLE IF EXISTS pets ADD CONSTRAINT pets_gender_check CHECK (gender IN ('MALE','FEMALE','UNKNOWN'));
ALTER TABLE IF EXISTS pets ADD CONSTRAINT pets_size_check CHECK (size IN ('SMALL','MEDIUM','LARGE','EXTRA_LARGE'));
ALTER TABLE IF EXISTS pets ADD CONSTRAINT pets_status_check CHECK (status IN ('AVAILABLE','PENDING','ADOPTED','UNAVAILABLE'));

-- Align adoption_requests table
ALTER TABLE IF EXISTS adoption_requests ADD COLUMN IF NOT EXISTS message TEXT;
ALTER TABLE IF EXISTS adoption_requests ADD COLUMN IF NOT EXISTS experience TEXT;
ALTER TABLE IF EXISTS adoption_requests ADD COLUMN IF NOT EXISTS living_situation TEXT;
ALTER TABLE IF EXISTS adoption_requests ADD COLUMN IF NOT EXISTS review_notes TEXT;
ALTER TABLE IF EXISTS adoption_requests ADD COLUMN IF NOT EXISTS reviewed_at TIMESTAMP WITH TIME ZONE;
UPDATE adoption_requests SET message = COALESCE(message, 'Pending message');
ALTER TABLE IF EXISTS adoption_requests ALTER COLUMN message SET NOT NULL;
ALTER TABLE IF EXISTS adoption_requests DROP COLUMN IF EXISTS answers_json;
ALTER TABLE IF EXISTS adoption_requests DROP CONSTRAINT IF EXISTS adoption_requests_status_check;
UPDATE adoption_requests SET status = CASE LOWER(status)
    WHEN 'submitted' THEN 'PENDING'
    WHEN 'in_review' THEN 'PENDING'
    WHEN 'approved' THEN 'APPROVED'
    WHEN 'rejected' THEN 'REJECTED'
    ELSE UPPER(status)
END;
ALTER TABLE IF EXISTS adoption_requests ADD CONSTRAINT adoption_requests_status_check CHECK (status IN ('PENDING','APPROVED','REJECTED','CANCELLED'));

-- Foundations already contain required fields; ensure timestamps exist
ALTER TABLE IF EXISTS foundations ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE IF EXISTS foundations ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF EXISTS foundations ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE IF EXISTS foundations ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE IF EXISTS foundations ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);

-- Ensure indexes exist to match JPA definitions
CREATE INDEX IF NOT EXISTS idx_users_phone ON users (phone);
CREATE INDEX IF NOT EXISTS idx_pet_images_primary ON pet_images (is_primary);
CREATE INDEX IF NOT EXISTS idx_adoption_requests_user_pet ON adoption_requests (user_id, pet_id);
