package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.input.TransactionUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.application.port.investigation.publicProcurement.model.Side;
import Di.Pierro.application.port.investigation.publicProcurement.model.TransactionConnection;
import Di.Pierro.domain.model.Transaction;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TransactionAnalyzer {
    private final RedFlagUseCases redFlagUseCases;
    private final TransactionUseCases transactionUseCases;

    public TransactionAnalyzer(RedFlagUseCases redFlagUseCases, TransactionUseCases transactionUseCases) {
        this.redFlagUseCases = redFlagUseCases;
        this.transactionUseCases = transactionUseCases;
    }

    public void identifyTransactionsBetweenPeopleOnTheGovernmentSideAndPublicProcurementParticipants(PublicProcurementInvestigationContext context) {
        Set<TransactionConnection> transactionConnectionSetRedFlags = getTransactionsForIntersectionUUIDs(context);

        Map<UUID, Transaction> uuidTransactionMap = getTransactions(transactionConnectionSetRedFlags);
        LocalDate publicProcurementDate = context.getProcurement().getOpeningDate();

        for (TransactionConnection transactionConnection : transactionConnectionSetRedFlags) {
            LocalDate transactionLocalDate = uuidTransactionMap.get(transactionConnection.transactionId()).getTransactionDate().toLocalDate();
            CrossFinancialData crossFinancialData = getCrossFinancialData(transactionLocalDate, publicProcurementDate);

            context.getRedFlags().add(redFlagUseCases.createRedFlag(new CreateRedFlagInput(
                    List.of(
                            transactionConnection.fromEntityId(),
                            transactionConnection.toEntityId()
                    ),
                    context.getProcurement().getId(),
                    transactionConnection.transactionId(),
                    null,
                    "Financial transaction between a government-affiliated individual and public procurement participants",
                    crossFinancialData.severity,
                    "Bilateral financial transactions in public procurement raise concerns due to "
                            + "the possibility of an improper link between the two parties. In this case, "
                            + "the transaction " + crossFinancialData.temporalDescription + "."
            )));
        }

    }

    private Set<TransactionConnection> getTransactionsForIntersectionUUIDs(PublicProcurementInvestigationContext context) {
        Set<TransactionConnection> redFlagTransactions = new HashSet<>();

        for (int count = 1; count <= 6; count++) {
            Set<UUID> uuidSetBusiness = context.getTransactionListByTypeNumber(Side.BUSINESS, count).stream()
                    .flatMap(transactionItemBySide -> Stream.of(
                            transactionItemBySide.transactionConnection().fromEntityId(),
                            transactionItemBySide.transactionConnection().toEntityId()
                    )).collect(Collectors.toSet());

            Set<UUID> previousUUIDSetBusiness = context.getTransactionListByTypeNumber(Side.BUSINESS, count-1).stream()
                    .flatMap(transactionItemBySide -> Stream.of(
                            transactionItemBySide.transactionConnection().fromEntityId(),
                            transactionItemBySide.transactionConnection().toEntityId()
                    )).collect(Collectors.toSet());

            Set<UUID> uuidSetGovernment = context.getTransactionListByTypeNumber(Side.GOVERNMENT, count).stream()
                    .flatMap(transactionItemBySide -> Stream.of(
                            transactionItemBySide.transactionConnection().fromEntityId(),
                            transactionItemBySide.transactionConnection().toEntityId()
                    )).collect(Collectors.toSet());

            Set<UUID> previousUUIDSetGovernment = context.getTransactionListByTypeNumber(Side.GOVERNMENT, count-1).stream()
                    .flatMap(transactionItemBySide -> Stream.of(
                            transactionItemBySide.transactionConnection().fromEntityId(),
                            transactionItemBySide.transactionConnection().toEntityId()
                    )).collect(Collectors.toSet());

            Set<UUID> uuidDiscoveredBusiness = getDiffBetweenFirstToSecond(uuidSetBusiness, previousUUIDSetBusiness);
            Set<UUID> uuidDiscoveredGovernment = getDiffBetweenFirstToSecond(uuidSetGovernment, previousUUIDSetGovernment);
            Set<UUID> uuidsIntersection = getIntersection(uuidSetBusiness, uuidSetGovernment);

            for (UUID uuid : uuidsIntersection){
                Set<TransactionConnection> businessConnections = context.getTransactionGraphBySide(Side.BUSINESS).getGraph().getOrDefault(uuid, Collections.emptySet());
                Set<TransactionConnection> governmentConnections = context.getTransactionGraphBySide(Side.GOVERNMENT).getGraph().getOrDefault(uuid, Collections.emptySet());

                Set<TransactionConnection> transactionConnectionSetBusiness = businessConnections.stream().filter(transactionConnection -> hasDiscoveredUUID(transactionConnection, uuidDiscoveredBusiness)).collect(Collectors.toSet());

                Set<TransactionConnection> transactionConnectionSetGovernment = governmentConnections.stream().filter(transactionConnection -> hasDiscoveredUUID(transactionConnection, uuidDiscoveredGovernment)).collect(Collectors.toSet());

                if(!transactionConnectionSetBusiness.isEmpty()) redFlagTransactions.addAll(transactionConnectionSetBusiness);
                if(!transactionConnectionSetGovernment.isEmpty()) redFlagTransactions.addAll(transactionConnectionSetGovernment);
            }

        }
        return redFlagTransactions;
    }
    private boolean hasDiscoveredUUID(TransactionConnection connection, Set<UUID> uuidDiscovered){
        boolean containsSender = hasUUID(uuidDiscovered, connection.fromEntityId());
        boolean containsReceiver = hasUUID(uuidDiscovered, connection.toEntityId());
        return containsReceiver || containsSender;
    }

    private CrossFinancialData getCrossFinancialData(LocalDate transactionLocalDate, LocalDate publicProcurementDate) {
        long daysDifference = ChronoUnit.DAYS.between(
                publicProcurementDate,
                transactionLocalDate
        );

        long absoluteDays = Math.abs(daysDifference);

        int severity;

        if (absoluteDays <= 90) {
            severity = 10;
        } else if (absoluteDays <= 365) {
            severity = 8;
        } else if (absoluteDays <= 730) {
            severity = 6;
        } else {
            severity = 5;
        }

        String temporalDescription;

        if (daysDifference > 0) {
            temporalDescription = "occurred " + absoluteDays
                    + " days after the public procurement opening date";
        } else if (daysDifference < 0) {
            temporalDescription = "occurred " + absoluteDays
                    + " days before the public procurement opening date";
        } else {
            temporalDescription = "occurred on the public procurement opening date";
        }
        return new CrossFinancialData(temporalDescription, severity);
    }

    private Map<UUID, Transaction> getTransactions(Set<TransactionConnection> transactionConnectionSet){
        List<UUID> uuidList = transactionConnectionSet.stream().map(TransactionConnection::transactionId).toList();
        List<Transaction> transactionList = transactionUseCases.findAllByIds(uuidList);
        return transactionList.stream().collect(Collectors.toMap(
                Transaction::getId,
                transaction -> transaction
        ));
    }

    private boolean hasUUID(Set<UUID> intersectionUUIDSet, UUID uuid){
        return intersectionUUIDSet.contains(uuid);
    }

    private Set<UUID> getIntersection(Set<UUID> uuidBusinessParticipantsSet, Set<UUID> uuidGovernmentParticipantsSet) {
        Set<UUID> intersectionUUIDSet = new HashSet<>(uuidBusinessParticipantsSet);
        intersectionUUIDSet.retainAll(uuidGovernmentParticipantsSet);
        return intersectionUUIDSet;
    }

    private Set<UUID> getDiffBetweenFirstToSecond(Set<UUID> uuidFirstSet, Set<UUID> uuidSecondSet){
        Set<UUID> diffUUIDSet = new HashSet<>(uuidFirstSet);
        diffUUIDSet.removeAll(uuidSecondSet);
        return diffUUIDSet;
    }

    private record CrossFinancialData(
            String temporalDescription,
            int severity
    ){}
}
