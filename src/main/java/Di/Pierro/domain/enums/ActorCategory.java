package Di.Pierro.domain.enums;

public enum ActorCategory {
    PERSON("Pessoa Física"),
    BUSINESS("Pessoa Jurídica / Empresa / Órgão"),
    PUBLIC_PROCUREMENT("Licitação"),
    UNKNOWN("Ator Não Identificado");

    private final String description;

    ActorCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
