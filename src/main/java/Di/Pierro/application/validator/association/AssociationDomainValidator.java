package Di.Pierro.application.validator.association;

import Di.Pierro.domain.enums.ActorCategory;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.infrastructure.exception.custom.UnprocessableEntityException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AssociationDomainValidator {

    public void validateAssociationScope(
            UUID firstActorId,
            ActorCategory firstCategory,
            UUID secondActorId,
            ActorCategory secondCategory,
            AssociationType associationType
    ) {
        if (associationType == null || associationType.getActorScope() == AssociationType.ActorScope.ANY) {
            return;
        }

        AssociationType.ActorScope scope = associationType.getActorScope();

        boolean isValid = switch (scope) {
            case PERSON_PERSON ->
                    firstCategory == ActorCategory.PERSON && secondCategory == ActorCategory.PERSON;

            case PERSON_BUSINESS ->
                    (firstCategory == ActorCategory.PERSON && secondCategory == ActorCategory.BUSINESS) ||
                    (firstCategory == ActorCategory.BUSINESS && secondCategory == ActorCategory.PERSON);

            case BUSINESS_BUSINESS ->
                    firstCategory == ActorCategory.BUSINESS && secondCategory == ActorCategory.BUSINESS;

            case PERSON_PROCUREMENT ->
                    (firstCategory == ActorCategory.PERSON && secondCategory == ActorCategory.PUBLIC_PROCUREMENT) ||
                    (firstCategory == ActorCategory.PUBLIC_PROCUREMENT && secondCategory == ActorCategory.PERSON);

            case BUSINESS_PROCUREMENT ->
                    (firstCategory == ActorCategory.BUSINESS && secondCategory == ActorCategory.PUBLIC_PROCUREMENT) ||
                    (firstCategory == ActorCategory.PUBLIC_PROCUREMENT && secondCategory == ActorCategory.BUSINESS);

            case ANY -> true;
        };

        if (!isValid) {
            throw new UnprocessableEntityException(
                    "422",
                    String.format(
                            "Incompatible association: type '%s' requires scope '%s' (%s), but received first actor [%s: %s] and second actor [%s: %s]",
                            associationType.name(),
                            scope.name(),
                            scope.getDescription(),
                            firstActorId,
                            firstCategory.getDescription(),
                            secondActorId,
                            secondCategory.getDescription()
                    )
            );
        }
    }
}
