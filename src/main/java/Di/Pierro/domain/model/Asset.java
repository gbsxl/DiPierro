package Di.Pierro.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Asset {
    private UUID id;
    private String type;
    private String description;
    private BigDecimal estimatedValue;
    private String source;
    private boolean stillHaveIt;
    private LocalDate acquiredAt;
    private LocalDate mappedAt;
    private Person person;

    public static Asset createAsset(
            String type,
            String description,
            BigDecimal estimatedValue,
            String source,
            boolean stillHaveIt,
            LocalDate acquiredAt,
            LocalDate mappedAt,
            Person person
    ) {
        return new Asset(
                UUID.randomUUID(),
                type,
                description,
                estimatedValue,
                source,
                stillHaveIt,
                acquiredAt,
                mappedAt,
                person
        );
    }

    public Asset() {
    }

    public Asset(UUID id, String type, String description, BigDecimal estimatedValue, String source, boolean stillHaveIt, LocalDate acquiredAt, LocalDate mappedAt, Person person) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.estimatedValue = estimatedValue;
        this.source = source;
        this.stillHaveIt = stillHaveIt;
        this.acquiredAt = acquiredAt;
        this.mappedAt = mappedAt;
        this.person = person;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isStillHaveIt() {
        return stillHaveIt;
    }

    public void setStillHaveIt(boolean stillHaveIt) {
        this.stillHaveIt = stillHaveIt;
    }

    public LocalDate getAcquiredAt() {
        return acquiredAt;
    }

    public void setAcquiredAt(LocalDate acquiredAt) {
        this.acquiredAt = acquiredAt;
    }

    public LocalDate getMappedAt() {
        return mappedAt;
    }

    public void setMappedAt(LocalDate mappedAt) {
        this.mappedAt = mappedAt;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
