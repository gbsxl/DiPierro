package Di.Pierro.application.port.usecase.redflag;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.dto.redflag.RedFlagFilter;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.output.RedFlagRepository;
import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RedFlagUseCasesImplementation implements RedFlagUseCases {
    private final RedFlagRepository redFlagRepository;

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

    @Override
    public List<RedFlag> findByActorId(UUID id) {
        return redFlagRepository.findByActorId(id);
    }

    @Override
    public List<RedFlag> findByPublicProcurementId(UUID id) {
        return redFlagRepository.findByPublicProcurementId(id);
    }

    @Override
    public List<RedFlag> findByTransactionId(UUID id) {
        return redFlagRepository.findByTransactionId(id);
    }

    @Override
    public List<RedFlag> findByAssociationId(UUID id) {
        return redFlagRepository.findByAssociationId(id);
    }

    @Override
    public List<RedFlag> findByType(String string) {
        return redFlagRepository.findByType(string);
    }

    @Override
    public List<RedFlag> findBySeverity(Integer severity) {
        return redFlagRepository.findBySeverity(severity);
    }

    @Override
    public List<RedFlag> findByFilter(RedFlagFilter filter) {
        return redFlagRepository.findByFilter(filter);
    }

    @Override
    public RedFlag updateById(UUID id, CreateRedFlagInput redFlag) {
        return redFlagRepository.updateById(id, redFlag);
    }

    @Override
    public void deleteById(UUID id) {
        redFlagRepository.deleteById(id);
    }
}
