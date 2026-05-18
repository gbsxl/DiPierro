package Di.Pierro.application.port.input;

import Di.Pierro.domain.entity.Actor;
import java.util.List;

public interface FindAllActorsUseCase {
    List<Actor> execute();
}
