package Di.Pierro.domain.enums;

public enum AssociationType {

    // ==========================================
    // VÍNCULOS SOCIETÁRIOS E CORPORATIVOS
    // ==========================================
    SOCIO("Sócio", ActorScope.PERSON_BUSINESS),
    SOCIO_ADMINISTRADOR("Sócio Administrador", ActorScope.PERSON_BUSINESS),
    DIRETOR("Diretor", ActorScope.PERSON_BUSINESS),
    ACIONISTA("Acionista", ActorScope.PERSON_BUSINESS),
    PROPRIETARIO("Proprietário", ActorScope.PERSON_BUSINESS),
    CONSELHEIRO("Conselheiro", ActorScope.PERSON_BUSINESS),
    REPRESENTANTE_LEGAL("Representante Legal", ActorScope.PERSON_BUSINESS),
    CONTROLADORA("Empresa Controladora", ActorScope.BUSINESS_BUSINESS),
    SUBSIDIARIA("Empresa Subsidiária", ActorScope.BUSINESS_BUSINESS),
    PROCURADOR("Procurador", ActorScope.PERSON_BUSINESS),
    FIADOR("Fiador", ActorScope.PERSON_BUSINESS),

    // ==========================================
    // VÍNCULOS FAMILIARES
    // ==========================================
    CONJUGE("Cônjuge", ActorScope.PERSON_PERSON),
    COMPANHEIRO("Companheiro(a)", ActorScope.PERSON_PERSON),
    PAI_MAE("Pai ou Mãe", ActorScope.PERSON_PERSON),
    FILHO_FILHA("Filho ou Filha", ActorScope.PERSON_PERSON),
    IRMAO_IRMA("Irmão ou Irmã", ActorScope.PERSON_PERSON),
    AVO_AVO("Avô ou Avó", ActorScope.PERSON_PERSON),
    NETO_NETA("Neto ou Neta", ActorScope.PERSON_PERSON),
    TIO_TIA("Tio ou Tia", ActorScope.PERSON_PERSON),
    SOBRINHO_SOBRINHA("Sobrinho ou Sobrinha", ActorScope.PERSON_PERSON),
    PRIMO_PRIMA("Primo ou Prima", ActorScope.PERSON_PERSON),
    SOGRO_SOGRA("Sogro ou Sogra", ActorScope.PERSON_PERSON),
    GENRO_NORA("Genro ou Nora", ActorScope.PERSON_PERSON),
    CUNHADO_CUNHADA("Cunhado ou Cunhada", ActorScope.PERSON_PERSON),
    PARENTE_SECUNDARIO("Parente Secundário", ActorScope.PERSON_PERSON),
    PARENTESCO("Parentesco", ActorScope.PERSON_PERSON),

    // ==========================================
    // VÍNCULOS POLÍTICOS
    // ==========================================
    FILIADO_PARTIDO("Filiado a Partido Político", ActorScope.PERSON_PERSON),
    DIRIGENTE_PARTIDARIO("Dirigente Partidário", ActorScope.PERSON_PERSON),
    MEMBRO_DIRETORIO_PARTIDARIO("Membro de Diretório Partidário", ActorScope.PERSON_PERSON),
    CANDIDATO("Candidato", ActorScope.PERSON_PERSON),
    EX_CANDIDATO("Ex-Candidato", ActorScope.PERSON_PERSON),
    DOADOR_CAMPANHA("Doador de Campanha", ActorScope.PERSON_PERSON),
    RECEBEDOR_DOACAO("Recebedor de Doação", ActorScope.PERSON_PERSON),
    COORDENADOR_CAMPANHA("Coordenador de Campanha", ActorScope.PERSON_PERSON),
    ASSESSOR_POLITICO("Assessor Político", ActorScope.PERSON_PERSON),
    CABO_ELEITORAL("Cabo Eleitoral", ActorScope.PERSON_PERSON),
    MANDATARIO("Mandatário", ActorScope.PERSON_PERSON),
    AGENTE_POLITICO("Agente Político", ActorScope.PERSON_PERSON),

    // ==========================================
    // VÍNCULOS TRABALHISTAS E FINANCEIROS
    // ==========================================
    EMPREGADOR("Empregador", ActorScope.PERSON_BUSINESS),
    EMPREGADO("Empregado", ActorScope.PERSON_BUSINESS),
    EX_EMPREGADOR("Ex-Empregador", ActorScope.PERSON_BUSINESS),
    EX_EMPREGADO("Ex-Empregado", ActorScope.PERSON_BUSINESS),
    COLEGA_TRABALHO("Colega de Trabalho", ActorScope.PERSON_PERSON),
    SOCIO_PROFISSIONAL("Sócio Profissional", ActorScope.PERSON_BUSINESS),
    PRESTADOR_SERVICO("Prestador de Serviço", ActorScope.PERSON_BUSINESS),
    CONTRATANTE("Contratante", ActorScope.PERSON_BUSINESS),
    CONTRATADO("Contratado", ActorScope.PERSON_BUSINESS),
    CONSULTOR("Consultor", ActorScope.PERSON_BUSINESS),
    ASSESSOR("Assessor", ActorScope.PERSON_BUSINESS),
    GERENTE("Gerente", ActorScope.PERSON_BUSINESS),
    ADMINISTRADOR("Administrador", ActorScope.PERSON_BUSINESS),
    TRABALHISTA("Trabalhista", ActorScope.PERSON_BUSINESS),

    // ==========================================
    // VÍNCULOS COM ONG / ASSOCIAÇÕES / ENTIDADES
    // ==========================================
    MEMBRO_ASSOCIACAO("Membro de Associação", ActorScope.ANY),
    DIRETOR_ASSOCIACAO("Diretor de Associação", ActorScope.ANY),
    PRESIDENTE_ASSOCIACAO("Presidente de Associação", ActorScope.ANY),
    MEMBRO_ONG("Membro de ONG", ActorScope.ANY),
    DIRETOR_ONG("Diretor de ONG", ActorScope.ANY),
    PRESIDENTE_ONG("Presidente de ONG", ActorScope.ANY),
    MEMBRO_INSTITUTO("Membro de Instituto", ActorScope.ANY),
    DIRETOR_INSTITUTO("Diretor de Instituto", ActorScope.ANY),
    MEMBRO_FUNDACAO("Membro de Fundação", ActorScope.ANY),
    DIRETOR_FUNDACAO("Diretor de Fundação", ActorScope.ANY),
    MEMBRO_CONSELHO_ENTIDADE("Membro de Conselho de Entidade", ActorScope.ANY),
    DIRIGENTE_ENTIDADE("Dirigente de Entidade", ActorScope.ANY),
    FUNDADOR_ENTIDADE("Fundador de Entidade", ActorScope.ANY),
    VOLUNTARIO_ENTIDADE("Voluntário de Entidade", ActorScope.ANY),
    BENEFICIARIO_ENTIDADE("Beneficiário de Entidade", ActorScope.ANY),

    // ==========================================
    // VÍNCULOS INDIRETOS / OSINT E PESSOAIS
    // ==========================================
    COMPARTILHA_ENDERECO("Compartilha Endereço", ActorScope.ANY),
    COMPARTILHA_CONTATO("Compartilha Contato", ActorScope.ANY),
    COMPARTILHA_TELEFONE("Compartilha Telefone", ActorScope.ANY),
    COMPARTILHA_EMAIL("Compartilha E-mail", ActorScope.ANY),
    COMPARTILHA_DOMINIO("Compartilha Domínio", ActorScope.ANY),
    COMPARTILHA_EMPRESA("Compartilha Empresa", ActorScope.BUSINESS_BUSINESS),
    COMPARTILHA_ENTIDADE("Compartilha Entidade", ActorScope.ANY),
    RELACAO_COMUM("Possui Relação em Comum", ActorScope.ANY),
    MESMA_ORGANIZACAO("Pertence à Mesma Organização", ActorScope.ANY),
    MESMO_CONSELHO("Pertence ao Mesmo Conselho", ActorScope.ANY),
    MESMA_DIRETORIA("Pertence à Mesma Diretoria", ActorScope.ANY),
    AMIZADE("Amizade", ActorScope.PERSON_PERSON),

    // ==========================================
    // VÍNCULOS ACADÊMICOS / INSTITUCIONAIS
    // ==========================================
    COLEGA_ACADEMICO("Colega Acadêmico", ActorScope.PERSON_PERSON),
    PROFESSOR("Professor", ActorScope.PERSON_PERSON),
    ALUNO("Aluno", ActorScope.PERSON_PERSON),

    // ==========================================
    // VÍNCULOS COM ÓRGÃOS PÚBLICOS E LICITAÇÕES
    // ==========================================
    SERVIDOR_PUBLICO("Servidor Público", ActorScope.PERSON_PROCUREMENT),
    AGENTE_PUBLICO("Agente Público", ActorScope.PERSON_PROCUREMENT),
    OCUPANTE_CARGO_COMISSIONADO("Ocupante de Cargo Comissionado", ActorScope.PERSON_PROCUREMENT),
    OCUPANTE_FUNCAO_PUBLICA("Ocupante de Função Pública", ActorScope.PERSON_PROCUREMENT),
    AUTORIDADE_PUBLICA("Autoridade Pública", ActorScope.PERSON_PROCUREMENT),
    ORDENADOR_DESPESA("Ordenador de Despesa", ActorScope.PERSON_PROCUREMENT),
    GESTOR_CONTRATO("Gestor de Contrato", ActorScope.PERSON_PROCUREMENT),
    FISCAL_CONTRATO("Fiscal de Contrato", ActorScope.PERSON_PROCUREMENT),
    MEMBRO_COMISSAO_LICITACAO("Membro de Comissão de Licitação", ActorScope.PERSON_PROCUREMENT),

    // DOCUMENTO: ESTUDO TÉCNICO PRELIMINAR (ETP)
    AUTOR_ETP("Autor / Integrante da Equipe do ETP", ActorScope.PERSON_PROCUREMENT),

    // DOCUMENTO: TERMO DE REFERÊNCIA (TR)
    AUTOR_TR("Autor / Elaborador do Termo de Referência", ActorScope.PERSON_PROCUREMENT),

    // DOCUMENTO: ATA DO PREGÃO (ETAPA DE JULGAMENTO)
    PREGOEIRO("Pregoeiro / Agente de Contratação", ActorScope.PERSON_PROCUREMENT),
    MEMBRO_EQUIPE_APOIO("Membro da Equipe de Apoio", ActorScope.PERSON_PROCUREMENT),

    // DOCUMENTO: TERMO DE HOMOLOGAÇÃO
    HOMOLOGADOR("Autoridade Superior (Homologador)", ActorScope.PERSON_PROCUREMENT),

    // DOCUMENTO: PARECER JURÍDICO
    PROCURADOR_JURIDICO("Procurador / Assessor Jurídico", ActorScope.PERSON_PROCUREMENT),

    // DOCUMENTO: PORTARIA DE DESIGNAÇÃO
    AUTORIDADE_DESIGNANTE("Autoridade Designante / Emissor da Portaria", ActorScope.PERSON_PROCUREMENT),

    // ENTIDADE / EMPRESA CONTRATANTE (RECEBEDORA DOS ITENS/SERVIÇOS)
    CONTRATANTE_LICITACAO("Contratante da Licitação", ActorScope.BUSINESS_PROCUREMENT),

    // ATORES EXTERNOS (FORNECEDORES / DISPUTA DE LICITAÇÃO)
    VENCEDOR_LICITACAO("Vencedor da Licitação", ActorScope.BUSINESS_PROCUREMENT),
    RESPONSAVEL_TECNICO("Responsável Técnico da Empresa", ActorScope.PERSON_BUSINESS),
    PARTICIPOU_LICITACAO("Participou da licitação", ActorScope.BUSINESS_PROCUREMENT),
    PARTICIPANTE_LICITACAO("Participante da Licitação", ActorScope.PERSON_PROCUREMENT),

    // ==========================================
    // NÃO CLASSIFICADO
    // ==========================================
    LIGACAO_SEM_CLASSIFICACAO("Ligação Sem Classificação", ActorScope.ANY),
    NAO_ESPECIFICADO("Não Especificado", ActorScope.ANY);

    public enum ActorScope {
        PERSON_PERSON("Associação exclusiva entre duas Pessoas (Person <-> Person)"),
        PERSON_BUSINESS("Associação entre uma Pessoa e uma Empresa (Person <-> Business)"),
        BUSINESS_BUSINESS("Associação entre duas Empresas (Business <-> Business)"),
        PERSON_PROCUREMENT("Associação entre uma Pessoa e uma Licitação (Person <-> PublicProcurement)"),
        BUSINESS_PROCUREMENT("Associação entre uma Empresa e uma Licitação (Business <-> PublicProcurement)"),
        ANY("Associação permitida entre qualquer tipo de ator");

        private final String description;

        ActorScope(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final String description;
    private final ActorScope actorScope;

    AssociationType(String description) {
        this(description, ActorScope.ANY);
    }

    AssociationType(String description, ActorScope actorScope) {
        this.description = description;
        this.actorScope = actorScope;
    }

    public String getDescription() {
        return description;
    }

    public ActorScope getActorScope() {
        return actorScope;
    }

    public static AssociationType fromCode(String code) {
        if (code == null || code.isBlank()) {
            return LIGACAO_SEM_CLASSIFICACAO;
        }

        try {
            return AssociationType.valueOf(code.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return LIGACAO_SEM_CLASSIFICACAO;
        }
    }
}