package Di.Pierro.application.port.investigation.publicProcurement;

import Di.Pierro.application.port.investigation.publicProcurement.analyzer.BusinessAnalyzer;
import Di.Pierro.application.port.investigation.publicProcurement.analyzer.PersonAnalyzer;
import Di.Pierro.application.port.investigation.publicProcurement.analyzer.TransactionAnalyzer;
import Di.Pierro.application.port.investigation.publicProcurement.dataFetcher.*;
import Di.Pierro.application.port.investigation.publicProcurement.model.*;
import Di.Pierro.domain.model.RedFlag;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckPublicProcurementImplementation implements CheckPublicProcurementUseCase {
    private final PublicProcurementDataFetcher publicProcurementDataFetcher;
    private final BusinessDataFetcher businessDataFetcher;
    private final PersonDataFetcher personDataFetcher;
    private final AssociationDataFetcher associationDataFetcher;
    private final TransactionDataFetcher transactionDataFetcher;
    private final BusinessAnalyzer businessAnalyzer;
    private final PersonAnalyzer personAnalyzer;
    private final TransactionAnalyzer transactionAnalyzer;

    public CheckPublicProcurementImplementation(
            PublicProcurementDataFetcher publicProcurementDataFetcher,
            BusinessDataFetcher businessDataFetcher,
            PersonDataFetcher personDataFetcher,
            AssociationDataFetcher associationDataFetcher,
            TransactionDataFetcher transactionDataFetcher,
            BusinessAnalyzer businessAnalyzer,
            PersonAnalyzer personAnalyzer,
            TransactionAnalyzer transactionAnalyzer) {
        this.publicProcurementDataFetcher = publicProcurementDataFetcher;
        this.businessDataFetcher = businessDataFetcher;
        this.personDataFetcher = personDataFetcher;
        this.associationDataFetcher = associationDataFetcher;
        this.transactionDataFetcher = transactionDataFetcher;
        this.businessAnalyzer = businessAnalyzer;
        this.personAnalyzer = personAnalyzer;
        this.transactionAnalyzer = transactionAnalyzer;
    }

    @Override
    public InvestigationResult investigatePublicProcurement(String publicProcurementNumber) {
        PublicProcurementInvestigationContext context = new PublicProcurementInvestigationContext();

        publicProcurementDataFetcher.findPublicProcurement(publicProcurementNumber, context);

        if (context.getProcurement() == null) {
            throw new ResourceNotFoundException("404", "Public procurement not found: " + publicProcurementNumber);
        }

        businessDataFetcher.getBusinessSeed(context);
        personDataFetcher.fetchPersonData(context);
        associationDataFetcher.fetchAllAssociations(context);
        businessDataFetcher.fetchPublicProcurementWinner(context);
        transactionDataFetcher.fetchTransactionData(context);

        businessAnalyzer.identifyRecentCompanyRedFlag(context);
        businessAnalyzer.identifyInsufficientCapitalStockRedFlag(context);
        personAnalyzer.identifySharedQsaBetweenGovernmentAndBusiness(context);
        personAnalyzer.identifyLinkBetweenPeopleOnTheGovernmentSideAndPublicProcurementParticipants(context);
        personAnalyzer.identifyProbableFraudulentCpfUsage(context);
        transactionAnalyzer.identifyTransactionsBetweenPeopleOnTheGovernmentSideAndPublicProcurementParticipants(context);

        List<RedFlag> redFlags = context.getRedFlags();
        int critical = 0;
        int high = 0;
        int medium = 0;
        int low = 0;

        for (RedFlag rf : redFlags) {
            int severity = rf.getSeverity() != null ? rf.getSeverity() : 0;
            if (severity >= 9) {
                critical++;
            } else if (severity >= 7) {
                high++;
            } else if (severity >= 4) {
                medium++;
            } else {
                low++;
            }
        }

        InvestigationSummary summary = new InvestigationSummary(
                redFlags.size(),
                critical,
                high,
                medium,
                low
        );

        InvestigationStatus status = redFlags.isEmpty()
                ? InvestigationStatus.COMPLETED
                : InvestigationStatus.COMPLETED_WITH_RED_FLAGS;

        return new InvestigationResult(
                context.getProcurement().getId(),
                status,
                summary,
                redFlags
        );
    }
}
