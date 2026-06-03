ALTER TABLE associations
    RENAME COLUMN font TO source;

ALTER TABLE people RENAME CONSTRAINT fk_actors TO fk_actors_people;
ALTER TABLE business RENAME CONSTRAINT fk_actors TO fk_actors_business;

ALTER TYPE association_type RENAME TO association_type_old;

CREATE TYPE association_type AS ENUM (
    'SOCIO',
    'SOCIO_ADMINISTRADOR',
    'DIRETOR',
    'CONSELHEIRO',
    'REPRESENTANTE_LEGAL',
    'CONTROLADORA',
    'SUBSIDIARIA',
    'CONJUGE',
    'PAI_MAE',
    'FILHO_FILHA',
    'IRMAO_IRMA',
    'PARENTE_SECUNDARIO',
    'EMPREGADOR',
    'EMPREGADO',
    'PRESTADOR_SERVICO',
    'PROCURADOR',
    'FIADOR',
    'COMPARTILHA_ENDERECO',
    'COMPARTILHA_CONTATO',
    'DOADOR_CAMPANHA',
    'ASSESSOR_POLITICO',
    'LIGACAO_SEM_CLASSIFICACAO'
    );

ALTER TABLE associations
    ALTER COLUMN type TYPE association_type
        USING type::text::association_type;

DROP TYPE association_type_old;