package Di.Pierro.infrastructure.persistence.publicprocurement;

import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PublicProcurementSpecifications {

    public static Specification<PublicProcurementEntity> hasPublicProcurementNumber(String publicProcurementNumber) {
        return (root, query, builder) ->
                builder.equal(root.get("publicProcurementNumber"), publicProcurementNumber);
    }

    public static Specification<PublicProcurementEntity> hasProcessNumber(String processNumber) {
        return (root, query, builder) ->
                builder.equal(root.get("processNumber"), processNumber);
    }

    public static Specification<PublicProcurementEntity> hasObject(String object) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("object")), "%" + object.toLowerCase() + "%");
    }

    public static Specification<PublicProcurementEntity> hasModality(String modality) {
        return (root, query, builder) ->
                builder.equal(root.get("modality"), modality);
    }

    public static Specification<PublicProcurementEntity> hasSituation(String situation) {
        return (root, query, builder) ->
                builder.equal(root.get("situation"), situation);
    }

    public static Specification<PublicProcurementEntity> hasLegalInstrument(String legalInstrument) {
        return (root, query, builder) ->
                builder.equal(root.get("legalInstrument"), legalInstrument);
    }

    public static Specification<PublicProcurementEntity> hasEstimatedValue(BigDecimal estimatedValue) {
        return (root, query, builder) ->
                builder.equal(root.get("estimatedValue"), estimatedValue);
    }

    public static Specification<PublicProcurementEntity> hasPublicationDate(LocalDate publicationDate) {
        return (root, query, builder) ->
                builder.equal(root.get("publicationDate"), publicationDate);
    }

    public static Specification<PublicProcurementEntity> hasOpeningDate(LocalDate openingDate) {
        return (root, query, builder) ->
                builder.equal(root.get("openingDate"), openingDate);
    }

    public static Specification<PublicProcurementEntity> hasDesignatedContact(String designatedContact) {
        return (root, query, builder) ->
                builder.equal(root.get("designatedContact"), designatedContact);
    }

    public static Specification<PublicProcurementEntity> hasIbgeCityCode(String ibgeCityCode) {
        return (root, query, builder) ->
                builder.equal(root.get("ibgeCityCode"), ibgeCityCode);
    }

    public static Specification<PublicProcurementEntity> hasFederativeUnitAcronym(String federativeUnitAcronym) {
        return (root, query, builder) ->
                builder.equal(root.get("federativeUnitAcronym"), federativeUnitAcronym);
    }

    public static Specification<PublicProcurementEntity> hasManagingUnityCode(String managingUnityCode) {
        return (root, query, builder) ->
                builder.equal(root.get("managingUnityCode"), managingUnityCode);
    }

    public static Specification<PublicProcurementEntity> hasCnpjGovernmentAgency(String cnpjGovernmentAgency) {
        return (root, query, builder) ->
                builder.equal(root.get("cnpjGovernmentAgency"), cnpjGovernmentAgency);
    }
}
