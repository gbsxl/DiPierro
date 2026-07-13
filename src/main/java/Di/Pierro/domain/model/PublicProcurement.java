package Di.Pierro.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class PublicProcurement {
    private UUID id;
    private String publicProcurementNumber;
    private String processNumber;
    private String object;
    private String modality;
    private String situation;
    private String legalInstrument;
    private BigDecimal estimatedValue;
    private LocalDate publicationDate;
    private LocalDate openingDate;
    private String designatedContact;
    private String ibgeCityCode;
    private String federativeUnitAcronym;
    private String managingUnityCode;
    private String cnpjGovernmentAgency;
    private OffsetDateTime createdAt;
    private Actor actor;

    public static PublicProcurement createPublicProcurement(
            String publicProcurementNumber,
            String processNumber,
            String object,
            String modality,
            String situation,
            String legalInstrument,
            BigDecimal estimatedValue,
            LocalDate publicationDate,
            LocalDate openingDate,
            String designatedContact,
            String ibgeCityCode,
            String federativeUnitAcronym,
            String managingUnityCode,
            String cnpjGovernmentAgency
    ) {
        return new PublicProcurement(
                UUID.randomUUID(),
                publicProcurementNumber,
                processNumber,
                object,
                modality,
                situation,
                legalInstrument,
                estimatedValue,
                publicationDate,
                openingDate,
                designatedContact,
                ibgeCityCode,
                federativeUnitAcronym,
                managingUnityCode,
                cnpjGovernmentAgency,
                OffsetDateTime.now(ZoneOffset.UTC),
                new Actor()
        );
    }

    public PublicProcurement() {
    }

    public PublicProcurement(UUID id, String publicProcurementNumber, String processNumber, String object, String modality, String situation, String legalInstrument, BigDecimal estimatedValue, LocalDate publicationDate, LocalDate openingDate, String designatedContact, String ibgeCityCode, String federativeUnitAcronym, String managingUnityCode, String cnpjGovernmentAgency, OffsetDateTime createdAt, Actor actor) {
        this.id = id;
        this.publicProcurementNumber = publicProcurementNumber;
        this.processNumber = processNumber;
        this.object = object;
        this.modality = modality;
        this.situation = situation;
        this.legalInstrument = legalInstrument;
        this.estimatedValue = estimatedValue;
        this.publicationDate = publicationDate;
        this.openingDate = openingDate;
        this.designatedContact = designatedContact;
        this.ibgeCityCode = ibgeCityCode;
        this.federativeUnitAcronym = federativeUnitAcronym;
        this.managingUnityCode = managingUnityCode;
        this.cnpjGovernmentAgency = cnpjGovernmentAgency;
        this.createdAt = createdAt;
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPublicProcurementNumber() {
        return publicProcurementNumber;
    }

    public void setPublicProcurementNumber(String publicProcurementNumber) {
        this.publicProcurementNumber = publicProcurementNumber;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getLegalInstrument() {
        return legalInstrument;
    }

    public void setLegalInstrument(String legalInstrument) {
        this.legalInstrument = legalInstrument;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public String getDesignatedContact() {
        return designatedContact;
    }

    public void setDesignatedContact(String designatedContact) {
        this.designatedContact = designatedContact;
    }

    public String getIbgeCityCode() {
        return ibgeCityCode;
    }

    public void setIbgeCityCode(String ibgeCityCode) {
        this.ibgeCityCode = ibgeCityCode;
    }

    public String getFederativeUnitAcronym() {
        return federativeUnitAcronym;
    }

    public void setFederativeUnitAcronym(String federativeUnitAcronym) {
        this.federativeUnitAcronym = federativeUnitAcronym;
    }

    public String getManagingUnityCode() {
        return managingUnityCode;
    }

    public void setManagingUnityCode(String managingUnityCode) {
        this.managingUnityCode = managingUnityCode;
    }

    public String getCnpjGovernmentAgency() {
        return cnpjGovernmentAgency;
    }

    public void setCnpjGovernmentAgency(String cnpjGovernmentAgency) {
        this.cnpjGovernmentAgency = cnpjGovernmentAgency;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
