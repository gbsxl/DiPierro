package Di.Pierro.application.port.investigation.publicProcurement.model;

import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.UUID;

public record QSA(
        UUID businessId,
        UUID businessActorId,
        List<Person> personList
) {
}
