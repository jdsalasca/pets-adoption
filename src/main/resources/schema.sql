-- PETFRIENDLY Database Schema
-- Based on the MVP requirements document

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('VISITOR', 'USER', 'FOUNDATION_ADMIN', 'SUPER_ADMIN')),
    city VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Foundations table
CREATE TABLE foundations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    description TEXT,
    contact_email VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Pets table
CREATE TABLE pets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    foundation_id UUID NOT NULL REFERENCES foundations(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    species VARCHAR(50) NOT NULL CHECK (species IN ('dog', 'cat', 'other')),
    breed VARCHAR(100),
    sex VARCHAR(10) NOT NULL CHECK (sex IN ('male', 'female')),
    age_months INTEGER NOT NULL CHECK (age_months >= 0),
    size VARCHAR(20) NOT NULL CHECK (size IN ('small', 'medium', 'large')),
    temperament VARCHAR(255),
    health TEXT,
    city VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'available' CHECK (status IN ('available', 'reserved', 'adopted')),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Pet images table
CREATE TABLE pet_images (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pet_id UUID NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    s3_key VARCHAR(500) NOT NULL,
    order_index INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Adoption requests table
CREATE TABLE adoption_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pet_id UUID NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'submitted' CHECK (status IN ('submitted', 'in_review', 'approved', 'rejected')),
    answers_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Contact messages table
CREATE TABLE contact_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    foundation_id UUID NOT NULL REFERENCES foundations(id) ON DELETE CASCADE,
    from_name VARCHAR(255) NOT NULL,
    from_email VARCHAR(255) NOT NULL,
    subject VARCHAR(255),
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_pets_foundation_id ON pets(foundation_id);
CREATE INDEX idx_pets_city ON pets(city);
CREATE INDEX idx_pets_species ON pets(species);
CREATE INDEX idx_pets_status ON pets(status);
CREATE INDEX idx_pet_images_pet_id ON pet_images(pet_id);
CREATE INDEX idx_adoption_requests_pet_id ON adoption_requests(pet_id);
CREATE INDEX idx_adoption_requests_user_id ON adoption_requests(user_id);
CREATE INDEX idx_adoption_requests_status ON adoption_requests(status);
CREATE INDEX idx_contact_messages_foundation_id ON contact_messages(foundation_id);

-- Update trigger for updated_at columns
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_adoption_requests_updated_at BEFORE UPDATE ON adoption_requests
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();