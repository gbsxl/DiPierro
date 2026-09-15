package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.input.TransactionUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.application.port.investigation.publicProcurement.model.Side;
import Di.Pierro.application.port.investigation.publicProcurement.model.TransactionConnection;
import Di.Pierro.application.port.investigation.publicProcurement.model.TransactionGraph;
import Di.Pierro.domain.model.Transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    public void identifyPossibleMoneyLaundering(PublicProcurementInvestigationContext context){
        if(context != null && context.getPublicProcurementWinner() != null){
            checkIfWinnerQuicklyMovedALargeAmountOfMoney(context);
        }
    }

    private void checkIfWinnerQuicklyMovedALargeAmountOfMoney(PublicProcurementInvestigationContext context){
        if (context == null || context.getPublicProcurementWinner() == null || context.getPublicProcurementWinner().getActor() == null) {
            return;
        }
        if (context.getProcurement() == null || context.getProcurement().getOpeningDate() == null || context.getProcurement().getEstimatedValue() == null) {
            return;
        }

        UUID winnerUUID = context.getPublicProcurementWinner().getActor().getId();
        if (winnerUUID == null) {
            return;
        }

        TransactionGraph transactionGraphIntersection = context.getTransactionGraphIntersection();
        if (transactionGraphIntersection == null || transactionGraphIntersection.getUuidSet() == null) {
            return;
        }

        boolean hasWinnerTransactions = transactionGraphIntersection.getUuidSet().contains(winnerUUID);
        if(hasWinnerTransactions){
            Set<TransactionConnection> transactionConnectionSet = transactionGraphIntersection.getGraph().get(winnerUUID);
            if (transactionConnectionSet == null || transactionConnectionSet.isEmpty()) {
                return;
            }

            BigDecimal suspiciousPercentage = BigDecimal.valueOf(0.35);
            BigDecimal publicProcurementValue = context.getProcurement().getEstimatedValue();
            int daysBefore = 30;
            int daysAfter = 90;
            LocalDate openingDate = context.getProcurement().getOpeningDate();
            LocalDate oneMonthBefore = openingDate.minusDays(daysBefore);
            LocalDate threeMonthsAfter = openingDate.plusDays(daysAfter);

            Set<Transaction> transactionsFiltered = getTransactionsBetweenDatesAndWinnerBeingSender(transactionConnectionSet, winnerUUID, oneMonthBefore, threeMonthsAfter);

            if(!transactionsFiltered.isEmpty()){
                Map<UUID, TooMoneyMovedQuickly> suspiciousTransactions = getSuspiciousTransactions(transactionsFiltered, publicProcurementValue, suspiciousPercentage);

                UUID procurementActorUUID = context.getProcurement().getActor() != null ? context.getProcurement().getActor().getId() : context.getProcurement().getId();
                if(!suspiciousTransactions.isEmpty()) createTooManyMoneyMovedRedFlags(suspiciousTransactions, openingDate, publicProcurementValue, procurementActorUUID, winnerUUID);
            }
        }
    }

    private void createTooManyMoneyMovedRedFlags(Map<UUID, TooMoneyMovedQuickly> suspiciousTransactions, LocalDate openingDate, BigDecimal publicProcurementValue, UUID publicProcurementUUID, UUID winnerUUID){

        suspiciousTransactions.forEach((uuid, tooMoneyMovedQuickly) -> {
            DateWithDays nearestDateAndDays = getNearestDateWithDays(tooMoneyMovedQuickly.transactions, openingDate);
            int nearestDateSeverity = getNearestDateSeverity(nearestDateAndDays);
            int nearestPublicProcurementValueSeverity = getNearestPublicProcurementValueSeverity(tooMoneyMovedQuickly.totalValue, publicProcurementValue);
            boolean nearestDateSeverityIsEqualOrBiggerThanSeven = nearestDateSeverity >= 7;
            int severity = Math.max(nearestDateSeverity, nearestPublicProcurementValueSeverity);
            int finalSeverity = nearestDateSeverityIsEqualOrBiggerThanSeven ? severity + 1 : severity;

            String description = buildTooManyMoneyMovedDescription(
                    tooMoneyMovedQuickly.transactions,
                    tooMoneyMovedQuickly.totalValue,
                    publicProcurementValue,
                    nearestDateAndDays,
                    openingDate,
                    finalSeverity
            );

            redFlagUseCases.createRedFlag(new CreateRedFlagInput(
                    List.of(winnerUUID, uuid),
                    publicProcurementUUID,
                    null,
                    null,
                    "Public procurement winner quickly moved a large amount of money",
                    finalSeverity,
                    description
            ));
        });
    }

    private String buildTooManyMoneyMovedDescription(
            Set<Transaction> transactions,
            BigDecimal totalValue,
            BigDecimal publicProcurementValue,
            DateWithDays nearestDateAndDays,
            LocalDate openingDate,
            int finalSeverity
    ) {
        int transactionCount = transactions.size();

        String valueText;
        if (publicProcurementValue != null && publicProcurementValue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal percentage = totalValue
                    .multiply(BigDecimal.valueOf(100))
                    .divide(publicProcurementValue, 2, RoundingMode.HALF_UP);
            valueText = "totaling " + totalValue + " (" + percentage + "% of the public procurement estimated value of " + publicProcurementValue + ")";
        } else {
            valueText = "totaling " + totalValue;
        }

        long daysDiff = ChronoUnit.DAYS.between(openingDate, nearestDateAndDays.date);
        long absDays = Math.abs(daysDiff);
        String temporalText;
        if (daysDiff > 0) {
            temporalText = "occurred " + (absDays == 1 ? "1 day" : absDays + " days") + " after";
        } else if (daysDiff < 0) {
            temporalText = "occurred " + (absDays == 1 ? "1 day" : absDays + " days") + " before";
        } else {
            temporalText = "occurred on";
        }

        String severityText;
        if (finalSeverity >= 9) {
            severityText = "Critical concern (severity level " + finalSeverity + "/10)";
        } else if (finalSeverity >= 7) {
            severityText = "High concern (severity level " + finalSeverity + "/10)";
        } else {
            severityText = "Moderate concern (severity level " + finalSeverity + "/10)";
        }

        if (transactionCount == 1) {
            return "The public procurement winner executed 1 transaction " + valueText + " on " + nearestDateAndDays.date + ", "
                    + "which " + temporalText + " the public procurement opening date. "
                    + severityText + ": rapid movement of significant funds around the procurement opening date.";
        }

        LocalDate minDate = transactions.stream().map(t -> t.getTransactionDate().toLocalDate()).min(LocalDate::compareTo).orElse(nearestDateAndDays.date);
        LocalDate maxDate = transactions.stream().map(t -> t.getTransactionDate().toLocalDate()).max(LocalDate::compareTo).orElse(nearestDateAndDays.date);

        if (minDate.equals(maxDate)) {
            return "The public procurement winner executed " + transactionCount + " transactions " + valueText + " on " + minDate + ", "
                    + "which " + temporalText + " the public procurement opening date. "
                    + severityText + ": rapid movement of significant funds around the procurement opening date.";
        }

        return "The public procurement winner executed " + transactionCount + " transactions " + valueText + " between " + minDate + " and " + maxDate + ", "
                + "where the closest transaction " + temporalText + " the public procurement opening date. "
                + severityText + ": rapid movement of significant funds around the procurement opening date.";
    }

    private int getNearestPublicProcurementValueSeverity(BigDecimal totalValue, BigDecimal publicProcurementValue) {
        if (publicProcurementValue == null || publicProcurementValue.compareTo(BigDecimal.ZERO) <= 0 || totalValue == null || totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            return 5;
        }

        BigDecimal totalValuePercentage = totalValue.divide(publicProcurementValue, 4, RoundingMode.HALF_UP);

        BigDecimal severitySixPercentage = BigDecimal.valueOf(0.60);
        BigDecimal severitySevenPercentage = BigDecimal.valueOf(0.85);
        BigDecimal entireValuePercentage = BigDecimal.ONE;
        BigDecimal severityNinePercentage = BigDecimal.valueOf(1.20);

        if (totalValuePercentage.compareTo(severityNinePercentage) >= 0) {
            return 9;
        }
        if (totalValuePercentage.compareTo(entireValuePercentage) >= 0) {
            return 8;
        }
        if (totalValuePercentage.compareTo(severitySevenPercentage) >= 0) {
            return 7;
        }
        if (totalValuePercentage.compareTo(severitySixPercentage) >= 0) {
            return 6;
        }

        return 5;
    }

    private int getNearestDateSeverity(DateWithDays nearestDateAndDays) {
        int severityEightDifferenceDays = 15;
        int severitySevenDifferenceDays = 30;
        int daysOfDifference = Math.abs(nearestDateAndDays.daysOfDifference);
        return daysOfDifference <= severityEightDifferenceDays ? 8 :
                        (daysOfDifference <= severitySevenDifferenceDays ? 7 : 6);
    }

    private DateWithDays getNearestDateWithDays(Set<Transaction> transactions, LocalDate openingDate) {
        AtomicReference<Long> nearestAbsDifference = new AtomicReference<>(Long.MAX_VALUE);
        AtomicReference<Long> nearestRawDifference = new AtomicReference<>(0L);
        AtomicReference<LocalDate> nearestDate = new AtomicReference<>(openingDate);

        transactions.forEach(transaction -> {
            LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();
            long daysFromOpening = ChronoUnit.DAYS.between(
                    openingDate,
                    transactionDate
            );
            long absDifference = Math.abs(daysFromOpening);
            if (absDifference < nearestAbsDifference.get()) {
                nearestAbsDifference.set(absDifference);
                nearestRawDifference.set(daysFromOpening);
                nearestDate.set(transactionDate);
            }
        });
        return new DateWithDays(nearestDate.get(), nearestRawDifference.get().intValue());
    }

    private Map<UUID, TooMoneyMovedQuickly> getSuspiciousTransactions(Set<Transaction> transactions, BigDecimal publicProcurementValue, BigDecimal suspiciousPercentage){
        if (transactions == null || transactions.isEmpty() || publicProcurementValue == null || suspiciousPercentage == null) {
            return Collections.emptyMap();
        }

        BigDecimal minimumToBeSuspicious = publicProcurementValue.multiply(suspiciousPercentage);
        Map<UUID, TooMoneyMovedQuickly> tooMoneyMovedQuicklyMap = new HashMap<>();

        Map<UUID, Set<Transaction>> transactionsByReceiver = transactions.stream()
                .filter(t -> t != null && t.getActorReceiver() != null && t.getActorReceiver().getId() != null)
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getActorReceiver().getId(),
                        Collectors.toSet()
                ));

        transactionsByReceiver.forEach((receiverUUID, transactionsByRecipient) -> {
            BigDecimal totalValue = getTotalValue(transactionsByRecipient);
            boolean isMinimumToBeSuspicious = totalValue.compareTo(minimumToBeSuspicious) > 0;
            if(isMinimumToBeSuspicious) tooMoneyMovedQuicklyMap.put(receiverUUID, new TooMoneyMovedQuickly(transactionsByRecipient, totalValue));
        });

        return tooMoneyMovedQuicklyMap;
    }

    private BigDecimal getTotalValue(Set<Transaction> transactions){
        BigDecimal totalValue = BigDecimal.ZERO;
        if (transactions == null) return totalValue;
        for (Transaction transaction : transactions){
            if (transaction != null && transaction.getValue() != null && transaction.getValue().compareTo(BigDecimal.ZERO) > 0) {
                totalValue = totalValue.add(transaction.getValue());
            }
        }
        return totalValue;
    }

    private Set<Transaction> getTransactionsBetweenDatesAndWinnerBeingSender(Set<TransactionConnection> transactionConnectionSet, UUID winnerUUID, LocalDate earlierDate, LocalDate laterDate) {
        if (transactionConnectionSet == null || transactionConnectionSet.isEmpty() || winnerUUID == null || earlierDate == null || laterDate == null || laterDate.isBefore(earlierDate)) {
            return Collections.emptySet();
        }

        List<UUID> winnerBeingSenderTransactionsUUIDs = transactionConnectionSet.stream()
                .filter(transactionConnection -> transactionConnection.fromEntityId().equals(winnerUUID))
                .map(TransactionConnection::transactionId)
                .distinct()
                .toList();

        List<Transaction> winnerBeingSenderTransactions = transactionUseCases.findAllByIds(winnerBeingSenderTransactionsUUIDs)
                .stream()
                .distinct()
                .toList();

        return winnerBeingSenderTransactions.stream()
                .filter(transaction -> transaction != null
                        && transaction.getTransactionDate() != null
                        && !transaction.getTransactionDate().toLocalDate().isBefore(earlierDate)
                        && !transaction.getTransactionDate().toLocalDate().isAfter(laterDate))
                .collect(Collectors.toSet());
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

    private record TooMoneyMovedQuickly(Set<Transaction> transactions, BigDecimal totalValue){}

    private record DateWithDays(LocalDate date, int daysOfDifference){}
}
