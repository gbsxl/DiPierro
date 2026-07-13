package Di.Pierro.application.port.usecase.redflag;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.output.RedFlagRepository;
import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RedFlagUseCasesImplementation implements RedFlagUseCases {
    RedFlagRepository redFlagRepository;

    public RedFlagUseCasesImplementation(RedFlagRepository redFlagRepository) {
        this.redFlagRepository = redFlagRepository;
    }

    @Override
    public void createRedFlag(CreateRedFlagInput createRedFlagInput) {
        RedFlag redFlag = RedFlag.createRedFlag(
                createRedFlagInput.type(),
                createRedFlagInput.severity(),
                createRedFlagInput.description(),
                null,
                null,
                createRedFlagInput.transactionId(),
                createRedFlagInput.associationId()
        );
        redFlagRepository.save(redFlag, createRedFlagInput.actorId(), createRedFlagInput.publicProcurementId());
    }

    @Override
    public List<RedFlag> findAll() {
        return redFlagRepository.findAll();
    }

    @Override
    public Optional<RedFlag> findById(UUID id) {
        return redFlagRepository.findById(id);
    }
}
