package Di.Pierro.domain.enums;

public enum Currency {

    BRL("Real Brasileiro"),
    USD("Dólar Americano"),
    EUR("Euro"),
    GBP("Libra Esterlina"),
    JPY("Iene Japonês"),
    CNY("Yuan Chinês"),
    CAD("Dólar Canadense"),
    AUD("Dólar Australiano"),
    CHF("Franco Suíço"),
    INR("Rúpia Indiana"),

    UNDEFINED("Indefinido");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Currency fromCode(String code) {
        if (code == null || code.isBlank()) {
            return UNDEFINED;
        }

        try {
            return Currency.valueOf(code.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNDEFINED;
        }
    }
}
