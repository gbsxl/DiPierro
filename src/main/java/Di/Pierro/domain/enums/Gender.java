package Di.Pierro.domain.enums;

import java.util.Arrays;

public enum Gender {

    MALE("male"),
    FEMALE("female"),
    OTHER("other"),
    UNDEFINED("undefined");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isMale() {
        return this == MALE;
    }

    public boolean isFemale() {
        return this == FEMALE;
    }

    public boolean isOther() {
        return this == OTHER;
    }

    public boolean isUndefined() {
        return this == UNDEFINED;
    }

    public static Gender from(String value) {
        if (value == null || value.isBlank()) {
            return UNDEFINED;
        }

        String normalized = value.trim().toLowerCase();

        return Arrays.stream(values())
                .filter(gender -> gender.value.equals(normalized))
                .findFirst()
                .orElseGet(() -> switch (normalized) {
                    case "m", "masculino" -> MALE;
                    case "f", "feminino" -> FEMALE;
                    case "o", "outro" -> OTHER;
                    default -> UNDEFINED;
                });
    }
}