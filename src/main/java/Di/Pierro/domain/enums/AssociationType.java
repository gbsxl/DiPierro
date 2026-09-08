package Di.Pierro.domain.enums;

public enum AssociationType {

    // ==========================================
    // VÍNCULOS SOCIETÁRIOS E CORPORATIVOS
    // ==========================================
    SOCIO("Sócio"),
    SOCIO_ADMINISTRADOR("Sócio Administrador"),
    DIRETOR("Diretor"),
    ACIONISTA("Acionista"),
    PROPRIETARIO("Proprietário"),
    CONSELHEIRO("Conselheiro"),
    REPRESENTANTE_LEGAL("Representante Legal"),
    CONTROLADORA("Empresa Controladora"),
    SUBSIDIARIA("Empresa Subsidiária"),
    PROCURADOR("Procurador"),
    FIADOR("Fiador"),

    // ==========================================
    // VÍNCULOS FAMILIARES
    // ==========================================
    CONJUGE("Cônjuge"),
    COMPANHEIRO("Companheiro(a)"),
    PAI_MAE("Pai ou Mãe"),
    FILHO_FILHA("Filho ou Filha"),
    IRMAO_IRMA("Irmão ou Irmã"),
    AVO_AVO("Avô ou Avó"),
    NETO_NETA("Neto ou Neta"),
    TIO_TIA("Tio ou Tia"),
    SOBRINHO_SOBRINHA("Sobrinho ou Sobrinha"),
    PRIMO_PRIMA("Primo ou Prima"),
    SOGRO_SOGRA("Sogro ou Sogra"),
    GENRO_NORA("Genro ou Nora"),
    CUNHADO_CUNHADA("Cunhado ou Cunhada"),
    PARENTE_SECUNDARIO("Parente Secundário"),
    PARENTESCO("Parentesco"),

    // ==========================================
    // VÍNCULOS POLÍTICOS
    // ==========================================
    FILIADO_PARTIDO("Filiado a Partido Político"),
    DIRIGENTE_PARTIDARIO("Dirigente Partidário"),
    MEMBRO_DIRETORIO_PARTIDARIO("Membro de Diretório Partidário"),
    CANDIDATO("Candidato"),
    EX_CANDIDATO("Ex-Candidato"),
    DOADOR_CAMPANHA("Doador de Campanha"),
    RECEBEDOR_DOACAO("Recebedor de Doação"),
    COORDENADOR_CAMPANHA("Coordenador de Campanha"),
    ASSESSOR_POLITICO("Assessor Político"),
    CABO_ELEITORAL("Cabo Eleitoral"),
    MANDATARIO("Mandatário"),
    AGENTE_POLITICO("Agente Político"),

    // ==========================================
    // VÍNCULOS TRABALHISTAS E FINANCEIROS
    // ==========================================
    EMPREGADOR("Empregador"),
    EMPREGADO("Empregado"),
    EX_EMPREGADOR("Ex-Empregador"),
    EX_EMPREGADO("Ex-Empregado"),
    COLEGA_TRABALHO("Colega de Trabalho"),
    SOCIO_PROFISSIONAL("Sócio Profissional"),
    PRESTADOR_SERVICO("Prestador de Serviço"),
    CONTRATANTE("Contratante"),
    CONTRATADO("Contratado"),
    CONSULTOR("Consultor"),
    ASSESSOR("Assessor"),
    GERENTE("Gerente"),
    ADMINISTRADOR("Administrador"),
    TRABALHISTA("Trabalhista"),

    // ==========================================
    // VÍNCULOS COM ONG / ASSOCIAÇÕES / ENTIDADES
    // ==========================================
    MEMBRO_ASSOCIACAO("Membro de Associação"),
    DIRETOR_ASSOCIACAO("Diretor de Associação"),
    PRESIDENTE_ASSOCIACAO("Presidente de Associação"),
    MEMBRO_ONG("Membro de ONG"),
    DIRETOR_ONG("Diretor de ONG"),
    PRESIDENTE_ONG("Presidente de ONG"),
    MEMBRO_INSTITUTO("Membro de Instituto"),
    DIRETOR_INSTITUTO("Diretor de Instituto"),
    MEMBRO_FUNDACAO("Membro de Fundação"),
    DIRETOR_FUNDACAO("Diretor de Fundação"),
    MEMBRO_CONSELHO_ENTIDADE("Membro de Conselho de Entidade"),
    DIRIGENTE_ENTIDADE("Dirigente de Entidade"),
    FUNDADOR_ENTIDADE("Fundador de Entidade"),
    VOLUNTARIO_ENTIDADE("Voluntário de Entidade"),
    BENEFICIARIO_ENTIDADE("Beneficiário de Entidade"),

    // ==========================================
    // VÍNCULOS INDIRETOS / OSINT E PESSOAIS
    // ==========================================
    COMPARTILHA_ENDERECO("Compartilha Endereço"),
    COMPARTILHA_CONTATO("Compartilha Contato"),
    COMPARTILHA_TELEFONE("Compartilha Telefone"),
    COMPARTILHA_EMAIL("Compartilha E-mail"),
    COMPARTILHA_DOMINIO("Compartilha Domínio"),
    COMPARTILHA_EMPRESA("Compartilha Empresa"),
    COMPARTILHA_ENTIDADE("Compartilha Entidade"),
    RELACAO_COMUM("Possui Relação em Comum"),
    MESMA_ORGANIZACAO("Pertence à Mesma Organização"),
    MESMO_CONSELHO("Pertence ao Mesmo Conselho"),
    MESMA_DIRETORIA("Pertence à Mesma Diretoria"),
    AMIZADE("Amizade"),

    // ==========================================
    // VÍNCULOS ACADÊMICOS / INSTITUCIONAIS
    // ==========================================
    COLEGA_ACADEMICO("Colega Acadêmico"),
    PROFESSOR("Professor"),
    ALUNO("Aluno"),

    // ==========================================
    // VÍNCULOS COM ÓRGÃOS PÚBLICOS E LICITAÇÕES
    // ==========================================
    SERVIDOR_PUBLICO("Servidor Público"),
    AGENTE_PUBLICO("Agente Público"),
    OCUPANTE_CARGO_COMISSIONADO("Ocupante de Cargo Comissionado"),
    OCUPANTE_FUNCAO_PUBLICA("Ocupante de Função Pública"),
    AUTORIDADE_PUBLICA("Autoridade Pública"),
    ORDENADOR_DESPESA("Ordenador de Despesa"),
    GESTOR_CONTRATO("Gestor de Contrato"),
    FISCAL_CONTRATO("Fiscal de Contrato"),
    MEMBRO_COMISSAO_LICITACAO("Membro de Comissão de Licitação"),

    // DOCUMENTO: ESTUDO TÉCNICO PRELIMINAR (ETP)
    AUTOR_ETP("Autor / Integrante da Equipe do ETP"),

    // DOCUMENTO: TERMO DE REFERÊNCIA (TR)
    AUTOR_TR("Autor / Elaborador do Termo de Referência"),

    // DOCUMENTO: ATA DO PREGÃO (ETAPA DE JULGAMENTO)
    PREGOEIRO("Pregoeiro / Agente de Contratação"),
    MEMBRO_EQUIPE_APOIO("Membro da Equipe de Apoio"),

    // DOCUMENTO: TERMO DE HOMOLOGAÇÃO
    HOMOLOGADOR("Autoridade Superior (Homologador)"),

    // DOCUMENTO: PARECER JURÍDICO
    PROCURADOR_JURIDICO("Procurador / Assessor Jurídico"),

    // DOCUMENTO: PORTARIA DE DESIGNAÇÃO
    AUTORIDADE_DESIGNANTE("Autoridade Designante / Emissor da Portaria"),

    // ATORES EXTERNOS (FORNECEDORES / DISPUTA)
    VENCEDOR_LICITACAO("Vencedor da Licitação"),
    RESPONSAVEL_TECNICO("Responsável Técnico da Empresa"),
    PARTICIPOU_LICITACAO("Participou da licitação"),
    PARTICIPANTE_LICITACAO("Participante da Licitação"),

    // ==========================================
    // NÃO CLASSIFICADO
    // ==========================================
    LIGACAO_SEM_CLASSIFICACAO("Ligação Sem Classificação"),
    NAO_ESPECIFICADO("Não Especificado");

    private final String description;

    AssociationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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