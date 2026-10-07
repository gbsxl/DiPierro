CREATE TABLE public_procurement( --A.K.A licitação
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    public_procurement_number VARCHAR(50) UNIQUE NOT NULL,
    process_number VARCHAR(50) UNIQUE,
    object TEXT NOT NULL,
    modality VARCHAR(100) NOT NULL,
    situation VARCHAR(100),
    legal_instrument VARCHAR(100),
    estimated_value NUMERIC(15, 2) DEFAULT 0.00,
    publication_date DATE,
    opening_date DATE,
    designated_contact VARCHAR(255), -- pregoeiro
    ibge_city_code VARCHAR(7),
    federative_unit_acronym CHAR(2),
    managing_unity_code VARCHAR(20),
    cnpj_government_agency VARCHAR(14),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actors_id UUID UNIQUE NOT NULL,
    constraint fk_actors_public_procurement
        foreign key (actors_id) references actors(id) on delete cascade
);

CREATE TABLE documents(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(255),
   type VARCHAR(50),
   file_path TEXT,
   hash VARCHAR(64),
   extracted BOOLEAN NOT NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   public_procurement_id UUID,
   constraint fk_public_procurement_documents
       foreign key (public_procurement_id) references public_procurement(id) on delete cascade
);

CREATE TABLE document_mentions (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   document_id UUID NOT NULL,
   actors_id UUID NOT NULL,
   role VARCHAR(50),
   confidence INT CHECK (confidence >= 1 AND confidence <= 10),
   extracted_name TEXT,
   constraint fk_documents_document_mentions
       foreign key (document_id) references documents(id) on delete cascade,
   constraint fk_actors_document_mentions
       foreign key (actors_id) references actors(id) on delete cascade
);

CREATE TABLE assets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    people_id UUID NOT NULL,
    type VARCHAR(30) NOT NULL,
    description TEXT,
    estimated_value NUMERIC(15, 2),
    source TEXT,
    still_have_it BOOLEAN DEFAULT TRUE,
    acquired_at DATE,
    mapped_at DATE,
    constraint fk_people_assets
        foreign key (people_id) references people(id) on delete cascade
);

CREATE TABLE actor_indicators (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actors_id UUID NOT NULL,
    indicator_type VARCHAR(150) NOT NULL,
    value VARCHAR(150) NOT NULL,
    source TEXT,
    date DATE,
    constraint fk_actors_actor_indicators
        foreign key (actors_id) references actors(id) on delete cascade
);

CREATE TABLE red_flags(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actors_id UUID,
    public_procurement_id UUID,
    transaction_id UUID,
    association_id UUID,
    type VARCHAR(255),
    severity INT NOT NULL CHECK (severity >= 1 AND severity <= 10),
    description TEXT,
    detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (
        actors_id IS NOT NULL OR
        public_procurement_id IS NOT NULL OR
        transaction_id IS NOT NULL OR
        association_id IS NOT NULL
    )
);

ALTER TABLE transactions
ALTER COLUMN value TYPE numeric(15, 2);

ALTER TABLE people
ALTER COLUMN cpf DROP NOT NULL;

ALTER TABLE people
ALTER COLUMN complete_name TYPE varchar(255);

ALTER TABLE actors
DROP COLUMN address;

ALTER TABLE people
ADD COLUMN address text;

ALTER TABLE business
ADD COLUMN address text;

ALTER TABLE business
ALTER COLUMN legal_name TYPE varchar(255);

ALTER TABLE business
ALTER COLUMN fantasy_name TYPE varchar(255);

ALTER TABLE business
ADD COLUMN capital_stock numeric(15, 2);

ALTER TABLE business
ADD COLUMN estimated_net_worth numeric(15, 2);

ALTER TABLE associations
ADD CHECK (actor_id_first <> actor_id_second);

ALTER TABLE associations
ADD CHECK (association_end IS NULL OR association_end >= association_start);

ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'PARTICIPOU_LICITACAO';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'RESPONSAVEL_TECNICO';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'PREGOEIRO';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'HOMOLOGADOR';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'AUTOR_ETP';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'MEMBRO_EQUIPE_APOIO';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'VENCEDOR_LICITACAO';
ALTER TYPE association_type ADD VALUE IF NOT EXISTS 'PARTICIPANTE_LICITACAO';

CREATE INDEX idx_association_first
    ON associations(actor_id_first);

CREATE INDEX idx_association_second
    ON associations(actor_id_second);

CREATE INDEX idx_transaction_sender
    ON transactions(sender_actor_id);

CREATE INDEX idx_transaction_receiver
    ON transactions(receiver_actor_id);

CREATE INDEX idx_document_mentions_actor
    ON document_mentions(actors_id);

CREATE INDEX idx_redflags_actor
    ON red_flags(actors_id);