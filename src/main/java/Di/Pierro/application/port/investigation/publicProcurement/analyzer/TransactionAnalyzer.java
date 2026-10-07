package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.input.TransactionUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.*;
import Di.Pierro.domain.model.Business;
import Di.Pierro.domain.model.Person;
import Di.Pierro.domain.model.Transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class TransactionAnalyzer {
    private final RedFlagUseCases redFlagUseCases;
    private final TransactionUseCases transactionUseCases;

    public TransactionAnalyzer(RedFlagUseCases redFlagUseCases, TransactionUseCases transactionUseCases) {
        this.redFlagUseCases = redFlagUseCases;
        this.transactionUseCases = transactionUseCases;
    }

    public void identifyTransactionsBetweenPeopleOnTheGovernmentSideAndPublicProcurementParticipants(PublicProcurementInvestigationContext context) {
        Set<TransactionConnection> transactionConnections = getTransactionsCrossingGovernmentAndBusiness(context);

        if (transactionConnections.isEmpty()) {
            return;
        }

        Map<UUID, Transaction> uuidTransactionMap = getTransactions(transactionConnections);
        LocalDate publicProcurementDate = context.getProcurement() != null ? context.getProcurement().getOpeningDate() : null;
        UUID procurementId = context.getProcurement() != null ? context.getProcurement().getId() : null;

        for (TransactionConnection connection : transactionConnections) {
            Transaction transaction = uuidTransactionMap.get(connection.transactionId());
            if (transaction == null || transaction.getTransactionDate() == null) continue;

            LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();
            CrossFinancialData crossFinancialData = getCrossFinancialData(transactionDate, publicProcurementDate);

            String fromTrail = buildActorTrail(connection.fromEntityId(), context);
            String toTrail = buildActorTrail(connection.toEntityId(), context);
            BigDecimal value = transaction.getValue() != null ? transaction.getValue() : BigDecimal.ZERO;

            String description = String.format(
                    "Bilateral financial transaction crossing business and government-linked networks detected: "
                            + "Flow: [%s] -> [%s], Value: R$ %s on %s (%s). "
                            + "Note: The government-linked network comprises public officials and connected entities discovered through corporate, personal, or transactional links.",
                    fromTrail,
                    toTrail,
                    value,
                    transactionDate,
                    crossFinancialData.temporalDescription
            );

            context.getRedFlags().add(redFlagUseCases.createRedFlag(new CreateRedFlagInput(
                    List.of(connection.fromEntityId(), connection.toEntityId()),
                    procurementId,
                    connection.transactionId(),
                    null,
                    "Financial transaction between a government-affiliated individual and public procurement participants",
                    crossFinancialData.severity,
                    description
            )));
        }
    }

    public void identifyWinnerFinancialPatterns(PublicProcurementInvestigationContext context) {
        if (context == null || context.getPublicProcurementWinner() == null) {
            return;
        }
        checkIfWinnerQuicklyMovedALargeAmountOfMoney(context);
        identifyTransfersToCompetitors(context);
        identifySmurfingPatterns(context);
    }

    public void identifyPossibleMoneyLaundering(PublicProcurementInvestigationContext context) {
        identifyWinnerFinancialPatterns(context);
    }

    private void identifyTransfersToCompetitors(PublicProcurementInvestigationContext context) {
        if (context.getPublicProcurementWinner() == null || context.getBusinessSide() == null) {
            return;
        }

        UUID winnerUUID = context.getPublicProcurementWinner().getActor().getId();
        UUID procurementId = context.getProcurement() != null ? context.getProcurement().getId() : null;

        Map<UUID, Business> competitorMap = new HashMap<>();
        for (Business b : context.getBusinessSide()) {
            if (b != null && b.getActor() != null && !winnerUUID.equals(b.getActor().getId())) {
                competitorMap.put(b.getActor().getId(), b);
            }
        }

        if (competitorMap.isEmpty()) return;

        List<TransactionConnection> allConnections = new ArrayList<>();
        if (context.getBusinessTransactionsList() != null) {
            context.getBusinessTransactionsList().forEach(t -> allConnections.add(t.transactionConnection()));
        }

        Map<UUID, List<TransactionConnection>> connectionsByCompetitor = allConnections.stream()
                .filter(c -> c != null && winnerUUID.equals(c.fromEntityId()) && competitorMap.containsKey(c.toEntityId()))
                .collect(Collectors.groupingBy(TransactionConnection::toEntityId));

        if (connectionsByCompetitor.isEmpty()) return;

        Set<TransactionConnection> allUniqueConnections = connectionsByCompetitor.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        Map<UUID, Transaction> transactionMap = getTransactions(allUniqueConnections);

        String winnerName = getActorDisplayName(winnerUUID, context);

        connectionsByCompetitor.forEach((competitorId, connList) -> {
            Business competitor = competitorMap.get(competitorId);
            String competitorName = competitor.getFantasyName() != null && !competitor.getFantasyName().isBlank()
                    ? competitor.getFantasyName()
                    : competitor.getLegalName();

            List<Transaction> competitorTransactions = connList.stream()
                    .map(c -> transactionMap.get(c.transactionId()))
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted(Comparator.comparing(Transaction::getTransactionDate))
                    .toList();

            if (competitorTransactions.isEmpty()) return;

            BigDecimal totalTransferred = competitorTransactions.stream()
                    .map(Transaction::getValue)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String description = String.format(
                    "The winning bidder (%s) executed %d financial transfer(s) totaling R$ %s to competing bidder (%s). "
                            + "Direct financial transfers between the winner and participating competitors in the same procurement "
                            + "strongly indicate bid rigging, cartel agreements, kickbacks, or illicit compensatory subcontracting.",
                    winnerName,
                    competitorTransactions.size(),
                    totalTransferred,
                    competitorName
            );

            context.getRedFlags().add(redFlagUseCases.createRedFlag(new CreateRedFlagInput(
                    List.of(winnerUUID, competitorId),
                    procurementId,
                    competitorTransactions.get(0).getId(),
                    null,
                    "Public procurement winner transferred funds to competing bidder (potential cartel/collusion)",
                    10,
                    description
            )));
        });
    }

    private void identifySmurfingPatterns(PublicProcurementInvestigationContext context) {
        if (context.getPublicProcurementWinner() == null) {
            return;
        }

        UUID winnerUUID = context.getPublicProcurementWinner().getActor().getId();
        UUID procurementId = context.getProcurement() != null ? context.getProcurement().getId() : null;

        List<TransactionConnection> allConnections = new ArrayList<>();
        if (context.getBusinessTransactionsList() != null) {
            context.getBusinessTransactionsList().forEach(t -> allConnections.add(t.transactionConnection()));
        }

        Set<TransactionConnection> winnerSentConnections = allConnections.stream()
                .filter(c -> c != null && winnerUUID.equals(c.fromEntityId()))
                .collect(Collectors.toSet());

        if (winnerSentConnections.isEmpty()) return;

        Map<UUID, Transaction> transactionMap = getTransactions(winnerSentConnections);

        Map<UUID, List<Transaction>> transactionsByReceiver = transactionMap.values().stream()
                .filter(t -> t != null && t.getActorReceiver() != null && t.getActorReceiver().getId() != null)
                .collect(Collectors.groupingBy(t -> t.getActorReceiver().getId()));

        BigDecimal coafThreshold = BigDecimal.valueOf(50000);
        BigDecimal minSmurfingValue = BigDecimal.valueOf(10000);

        String winnerName = getActorDisplayName(winnerUUID, context);

        transactionsByReceiver.forEach((receiverId, txList) -> {
            List<Transaction> subThresholdTransactions = txList.stream()
                    .filter(t -> t.getValue() != null
                            && t.getValue().compareTo(minSmurfingValue) >= 0
                            && t.getValue().compareTo(coafThreshold) < 0)
                    .sorted(Comparator.comparing(Transaction::getTransactionDate))
                    .toList();

            if (subThresholdTransactions.size() >= 2) {
                BigDecimal totalStructured = subThresholdTransactions.stream()
                        .map(Transaction::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalStructured.compareTo(coafThreshold) >= 0) {
                    String receiverName = getActorDisplayName(receiverId, context);
                    LocalDate firstDate = subThresholdTransactions.get(0).getTransactionDate().toLocalDate();
                    LocalDate lastDate = subThresholdTransactions.get(subThresholdTransactions.size() - 1).getTransactionDate().toLocalDate();

                    String description = String.format(
                            "Suspected financial structuring (smurfing) detected: The winning bidder (%s) executed %d transactions "
                                    + "each valued just below the regulatory reporting threshold of R$ 50,000 (values: %s) to recipient [%s] "
                                    + "between %s and %s, totaling R$ %s. Structuring multiple transactions just below reporting thresholds "
                                    + "is a recognized indicator of intentional evasion of financial intelligence controls (anti-money laundering).",
                            winnerName,
                            subThresholdTransactions.size(),
                            subThresholdTransactions.stream().map(t -> "R$ " + t.getValue()).collect(Collectors.joining(", ")),
                            receiverName,
                            firstDate,
                            lastDate,
                            totalStructured
                    );

                    context.getRedFlags().add(redFlagUseCases.createRedFlag(new CreateRedFlagInput(
                            List.of(winnerUUID, receiverId),
                            procurementId,
                            subThresholdTransactions.get(0).getId(),
                            null,
                            "Suspected financial structuring (smurfing) by procurement winner",
                            9,
                            description
                    )));
                }
            }
        });
    }

    private void checkIfWinnerQuicklyMovedALargeAmountOfMoney(PublicProcurementInvestigationContext context) {
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

        Set<TransactionConnection> transactionConnectionSet = new HashSet<>();
        if (context.getTransactionGraphIntersection() != null && context.getTransactionGraphIntersection().getGraph() != null) {
            Set<TransactionConnection> fromIntersection = context.getTransactionGraphIntersection().getGraph().get(winnerUUID);
            if (fromIntersection != null) transactionConnectionSet.addAll(fromIntersection);
        }
        if (context.getBusinessTransactionsList() != null) {
            context.getBusinessTransactionsList().stream()
                    .map(TransactionItemBySide::transactionConnection)
                    .filter(c -> c != null && winnerUUID.equals(c.fromEntityId()))
                    .forEach(transactionConnectionSet::add);
        }

        if (transactionConnectionSet.isEmpty()) {
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

        if (!transactionsFiltered.isEmpty()) {
            Map<UUID, TooMoneyMovedQuickly> suspiciousTransactions = getSuspiciousTransactions(transactionsFiltered, publicProcurementValue, suspiciousPercentage);

            UUID procurementActorUUID = context.getProcurement().getActor() != null ? context.getProcurement().getActor().getId() : context.getProcurement().getId();
            if (!suspiciousTransactions.isEmpty()) {
                createTooManyMoneyMovedRedFlags(suspiciousTransactions, openingDate, publicProcurementValue, procurementActorUUID, winnerUUID);
            }
        }
    }

    private void createTooManyMoneyMovedRedFlags(Map<UUID, TooMoneyMovedQuickly> suspiciousTransactions, LocalDate openingDate, BigDecimal publicProcurementValue, UUID publicProcurementUUID, UUID winnerUUID) {
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

    private Map<UUID, TooMoneyMovedQuickly> getSuspiciousTransactions(Set<Transaction> transactions, BigDecimal publicProcurementValue, BigDecimal suspiciousPercentage) {
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
            if (isMinimumToBeSuspicious) tooMoneyMovedQuicklyMap.put(receiverUUID, new TooMoneyMovedQuickly(transactionsByRecipient, totalValue));
        });

        return tooMoneyMovedQuicklyMap;
    }

    private BigDecimal getTotalValue(Set<Transaction> transactions) {
        BigDecimal totalValue = BigDecimal.ZERO;
        if (transactions == null) return totalValue;
        for (Transaction transaction : transactions) {
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

    private Set<TransactionConnection> getTransactionsCrossingGovernmentAndBusiness(PublicProcurementInvestigationContext context) {
        Set<TransactionConnection> crossingTransactions = new LinkedHashSet<>();

        Set<UUID> directBidders = new HashSet<>();
        if (context.getBusinessSide() != null) {
            context.getBusinessSide().forEach(b -> directBidders.add(b.getActor().getId()));
        }

        Set<UUID> businessActors = new HashSet<>(directBidders);
        if (context.getBusinessPersonGraph() != null && context.getBusinessPersonGraph().getPersonGraphItemList() != null) {
            context.getBusinessPersonGraph().getPersonGraphItemList().forEach(item -> businessActors.add(item.actorId()));
        }
        if (context.getBusinessTransactionsList() != null) {
            context.getBusinessTransactionsList().forEach(item -> {
                businessActors.add(item.transactionConnection().fromEntityId());
                businessActors.add(item.transactionConnection().toEntityId());
            });
        }

        Set<UUID> governmentActors = new HashSet<>();
        if (context.getGovernmentSide() != null) {
            context.getGovernmentSide().forEach(b -> governmentActors.add(b.getActor().getId()));
        }
        if (context.getGovernmentPersonHopZero() != null) {
            context.getGovernmentPersonHopZero().forEach(p -> governmentActors.add(p.getActor().getId()));
        }
        if (context.getGovernmentPersonGraph() != null && context.getGovernmentPersonGraph().getPersonGraphItemList() != null) {
            context.getGovernmentPersonGraph().getPersonGraphItemList().forEach(item -> governmentActors.add(item.actorId()));
        }
        if (context.getGovernmentTransactionsList() != null) {
            context.getGovernmentTransactionsList().forEach(item -> {
                governmentActors.add(item.transactionConnection().fromEntityId());
                governmentActors.add(item.transactionConnection().toEntityId());
            });
        }

        Set<TransactionConnection> allCandidateConnections = new LinkedHashSet<>();
        if (context.getBusinessTransactionsList() != null) {
            context.getBusinessTransactionsList().forEach(t -> allCandidateConnections.add(t.transactionConnection()));
        }
        if (context.getGovernmentTransactionsList() != null) {
            context.getGovernmentTransactionsList().forEach(t -> allCandidateConnections.add(t.transactionConnection()));
        }

        Set<UUID> processedTransactionIds = new HashSet<>();

        for (TransactionConnection connection : allCandidateConnections) {
            if (connection == null || connection.transactionId() == null) continue;
            if (!processedTransactionIds.add(connection.transactionId())) continue;

            UUID from = connection.fromEntityId();
            UUID to = connection.toEntityId();

            // Direct bidder-to-bidder transactions are analyzed as cartel/collusion
            if (directBidders.contains(from) && directBidders.contains(to)) {
                continue;
            }

            boolean fromInBusiness = businessActors.contains(from);
            boolean toInGovernment = governmentActors.contains(to);
            boolean fromInGovernment = governmentActors.contains(from);
            boolean toInBusiness = businessActors.contains(to);

            boolean crossesSides = (fromInBusiness && toInGovernment) || (fromInGovernment && toInBusiness);

            if (crossesSides) {
                crossingTransactions.add(connection);
            }
        }

        return crossingTransactions;
    }

    private String getActorDisplayName(UUID actorId, PublicProcurementInvestigationContext context) {
        if (actorId == null) return "Unknown";
        if (context.getAllBusinessList() != null) {
            for (Business b : context.getAllBusinessList()) {
                if (b != null && b.getActor() != null && actorId.equals(b.getActor().getId())) {
                    return b.getFantasyName() != null && !b.getFantasyName().isBlank()
                            ? b.getFantasyName()
                            : b.getLegalName();
                }
            }
        }
        if (context.getAllPersonList() != null) {
            for (Person p : context.getAllPersonList()) {
                if (p != null && p.getActor() != null && actorId.equals(p.getActor().getId())) {
                    return p.getCompleteName();
                }
            }
        }
        return actorId.toString();
    }

    private String buildActorTrail(UUID actorId, PublicProcurementInvestigationContext context) {
        if (actorId == null) return "Unknown";

        if (context.getBusinessSide() != null) {
            for (Business b : context.getBusinessSide()) {
                if (b != null && b.getActor() != null && actorId.equals(b.getActor().getId())) {
                    boolean isWinner = context.getPublicProcurementWinner() != null
                            && actorId.equals(context.getPublicProcurementWinner().getActor().getId());
                    return getActorDisplayName(actorId, context) + (isWinner ? " (Licitante Vencedora)" : " (Licitante Participante)");
                }
            }
        }

        if (context.getGovernmentPersonHopZero() != null) {
            for (Person p : context.getGovernmentPersonHopZero()) {
                if (p != null && p.getActor() != null && actorId.equals(p.getActor().getId())) {
                    return getActorDisplayName(actorId, context) + " (Agente Público / Semente Governamental)";
                }
            }
        }

        if (context.getGovernmentPersonGraph() != null && context.getGovernmentPersonGraph().getPersonGraphItemList() != null) {
            for (PersonGraphItem item : context.getGovernmentPersonGraph().getPersonGraphItemList()) {
                if (item.actorId().equals(actorId) && item.actorIdConnectionsOrder() != null && item.actorIdConnectionsOrder().size() > 1) {
                    return item.actorIdConnectionsOrder().stream()
                            .map(node -> getActorDisplayName(node.actorId(), context))
                            .collect(Collectors.joining(" -> "));
                }
            }
        }

        if (context.getBusinessPersonGraph() != null && context.getBusinessPersonGraph().getPersonGraphItemList() != null) {
            for (PersonGraphItem item : context.getBusinessPersonGraph().getPersonGraphItemList()) {
                if (item.actorId().equals(actorId) && item.actorIdConnectionsOrder() != null && item.actorIdConnectionsOrder().size() > 1) {
                    return item.actorIdConnectionsOrder().stream()
                            .map(node -> getActorDisplayName(node.actorId(), context))
                            .collect(Collectors.joining(" -> "));
                }
            }
        }

        return getActorDisplayName(actorId, context);
    }

    private CrossFinancialData getCrossFinancialData(LocalDate transactionLocalDate, LocalDate publicProcurementDate) {
        if (publicProcurementDate == null || transactionLocalDate == null) {
            return new CrossFinancialData("on an unrecorded date relative to opening", 5);
        }

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

    private Map<UUID, Transaction> getTransactions(Set<TransactionConnection> transactionConnectionSet) {
        if (transactionConnectionSet == null || transactionConnectionSet.isEmpty()) {
            return Collections.emptyMap();
        }
        List<UUID> uuidList = transactionConnectionSet.stream().map(TransactionConnection::transactionId).distinct().toList();
        List<Transaction> transactionList = transactionUseCases.findAllByIds(uuidList);
        return transactionList.stream().collect(Collectors.toMap(
                Transaction::getId,
                transaction -> transaction,
                (a, b) -> a
        ));
    }

    private record CrossFinancialData(
            String temporalDescription,
            int severity
    ) {}

    private record TooMoneyMovedQuickly(Set<Transaction> transactions, BigDecimal totalValue) {}

    private record DateWithDays(LocalDate date, int daysOfDifference) {}
}
