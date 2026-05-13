create table actors (
    id UUID primary key DEFAULT gen_random_uuid(),
    address text,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active boolean default true
);

create table people (
    id UUID primary key DEFAULT gen_random_uuid(),
    complete_name varchar(70) NOT NULL,
    cpf char(11) UNIQUE NOT NULL,
    gender varchar(20),
    phone_number varchar(20),
    email varchar(150),
    actors_id UUID UNIQUE NOT NULL,
    constraint fk_actors
        foreign key (actors_id) references actors(id) on delete cascade
);

create table business (
    id UUID primary key DEFAULT gen_random_uuid(),
    legal_name varchar(70) NOT NULL,
    cnpj char(14) UNIQUE NOT NULL,
    fantasy_name varchar(70),
    phone_number varchar(20),
    email varchar(150),
    is_public_company boolean default false,
    actors_id UUID UNIQUE NOT NULL,
    constraint fk_actors
        foreign key (actors_id) references actors(id) on delete cascade
);

create TYPE association_type AS ENUM (
    'TRABALHISTA',
    'PARENTESCO',
    'SOCIO',
    'AMIZADE',
    'PROPIETARIO',
    'NAO_ESPECIFICADO'
);

create table associations(
    id UUID primary key DEFAULT gen_random_uuid(),
    actor_id_first UUID NOT NULL,
    actor_id_second UUID NOT NULL,
    type association_type NOT NULL,
    font text,
    confidence_level int CHECK(confidence_level >= 0 AND confidence_level <= 10),
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    association_start timestamp NOT NULL,
    is_association_ended BOOLEAN DEFAULT false,
    association_end timestamp,

    constraint fk_actor_first
        foreign key (actor_id_first) references actors(id) on delete cascade,

    constraint fk_actor_second
        foreign key (actor_id_second) references actors(id) on delete cascade
);

create table transactions(
    id UUID primary key DEFAULT gen_random_uuid(),
    value numeric NOT NULL,
    sender_actor_id UUID NOT NULL,
    receiver_actor_id UUID NOT NULL,
    currency char(3) NOT NULL,
    transaction_date timestamptz NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,

     constraint fk_actor_sender
         foreign key (sender_actor_id) references actors(id) on delete cascade,

     constraint fk_actor_receiver
         foreign key (receiver_actor_id) references actors(id) on delete cascade
);