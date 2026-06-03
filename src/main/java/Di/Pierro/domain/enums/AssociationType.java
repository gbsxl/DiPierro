package Di.Pierro.domain.enums;

public enum AssociationType {

    // ==========================================
    // VÍNCULOS SOCIETÁRIOS E CORPORATIVOS
    // ==========================================
    SOCIO("Sócio"),
    SOCIO_ADMINISTRADOR("Sócio Administrador"),
    DIRETOR("Diretor"),
    CONSELHEIRO("Conselheiro"),
    REPRESENTANTE_LEGAL("Representante Legal"),
    CONTROLADORA("Empresa Controladora"),
    SUBSIDIARIA("Empresa Subsidiária"),

    // ==========================================
    // VÍNCULOS FAMILIARES
    // ==========================================
    CONJUGE("Cônjuge"),
    PAI_MAE("Pai ou Mãe"),
    FILHO_FILHA("Filho ou Filha"),
    IRMAO_IRMA("Irmão ou Irmã"),
    PARENTE_SECUNDARIO("Parente Secundário"),

    // ==========================================
    // VÍNCULOS TRABALHISTAS E FINANCEIROS
    // ==========================================
    EMPREGADOR("Empregador"),
    EMPREGADO("Empregado"),
    PRESTADOR_SERVICO("Prestador de Serviço"),
    PROCURADOR("Procurador"),
    FIADOR("Fiador"),

    // ==========================================
    // VÍNCULOS INDIRETOS / OSINT
    // ==========================================
    COMPARTILHA_ENDERECO("Compartilha Endereço"),
    COMPARTILHA_CONTATO("Compartilha Contato"),
    DOADOR_CAMPANHA("Doador de Campanha"),
    ASSESSOR_POLITICO("Assessor Político"),

    // ==========================================
    // NÃO CLASSIFICADO
    // ==========================================
    LIGACAO_SEM_CLASSIFICACAO("Ligação Sem Classificação");

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