package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;

import Di.Pierro.application.dto.transaction.TransactionFilter;
import Di.Pierro.application.port.input.TransactionUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.*;
import Di.Pierro.domain.model.Transaction;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TransactionDataFetcher {
    private final TransactionUseCases transactionUseCases;

    public TransactionDataFetcher(TransactionUseCases transactionUseCases) {
        this.transactionUseCases = transactionUseCases;
    }

    public void fetchTransactionData(PublicProcurementInvestigationContext context){
        getAllTransaction(context);
        setTransactionGraph(context, Side.BUSINESS);
        setTransactionGraph(context, Side.GOVERNMENT);
        fillTransactionIntersection(context);
    }

    private void fillTransactionIntersection(PublicProcurementInvestigationContext context) {
        TransactionGraph transactionGraphBusiness = context.getTransactionGraphBusiness();
        TransactionGraph transactionGraphGovernment = context.getTransactionGraphGovernment();
        TransactionGraph transactionGraphIntersection = context.getTransactionGraphIntersection();
        addTransactionGraphIntersection(transactionGraphBusiness, transactionGraphIntersection);
        addTransactionGraphIntersection(transactionGraphGovernment, transactionGraphIntersection);
    }

    private void addTransactionGraphIntersection(TransactionGraph transactionGraph, TransactionGraph transactionGraphIntersection){
        for (UUID uuid : transactionGraph.getUuidSet()){
            for (TransactionConnection connection : transactionGraph.getGraph().get(uuid)){
                transactionGraphIntersection.addGraphItem(connection);
            }
        }
    }

    private void getAllTransaction(PublicProcurementInvestigationContext context){
        List<UUID> uuidBusinessSideList = new ArrayList<>();

        List<UUID> uuidBusinessList = context.getAllBusinessList().stream().map(business -> business.getActor().getId()).toList();
        List<UUID> uuidPersonListBusinessSide = context.getBusinessPersonGraph().getPersonGraphItemList().stream().map(PersonGraphItem::actorId).toList();

        List<UUID> uuidPersonListGovernmentSide = context.getGovernmentPersonGraph().getPersonGraphItemList().stream().map(PersonGraphItem::actorId).toList();

        uuidBusinessSideList.addAll(uuidBusinessList);
        uuidBusinessSideList.addAll(uuidPersonListBusinessSide);

        List<UUID> uuidGovernmentSideList = new ArrayList<>(uuidPersonListGovernmentSide);

        getTransactionItemBySideList(uuidBusinessSideList, Side.BUSINESS, context);
        getTransactionItemBySideList(uuidGovernmentSideList, Side.GOVERNMENT, context);
    }

    private void getTransactionItemBySideList(List<UUID> uuidList, Side side, PublicProcurementInvestigationContext context){
        Set<UUID> oldUUIDSet = new HashSet<>(uuidList);
        List<UUID> discoveredUUIDs = setZeroHop(uuidList, side, context);
        oldUUIDSet.addAll(discoveredUUIDs);

        for (int count = 1; count <= 6; count++) {
            if (discoveredUUIDs.isEmpty()) break;

            List<Transaction> foundTransactionList = getTransactions(discoveredUUIDs);
            if (foundTransactionList.isEmpty()) break;

            discoveredUUIDs = searchNewUUIDs(oldUUIDSet, foundTransactionList);

            oldUUIDSet.addAll(addToOldUUIDSet(foundTransactionList));

            context.getTransactionListByTypeNumber(side, count).addAll(castTransactionItemList(foundTransactionList, side));
        }

        context.setGovernmentTransactionsListByAllLists(side);
    }


    private Set<UUID> addToOldUUIDSet(List<Transaction> foundTransactionList){
        return foundTransactionList.stream()
                .flatMap(transaction -> Stream.of(transaction.getActorSender().getId(), transaction.getActorReceiver().getId()))
                .collect(Collectors.toSet());
    }

    private List<TransactionItemBySide> castTransactionItemList(List<Transaction> foundTransactionList, Side side){
        return foundTransactionList.stream().map(transaction ->
                new TransactionItemBySide(
                        new TransactionConnection(
                                transaction.getId(),
                                transaction.getActorSender().getId(),
                                transaction.getActorReceiver().getId()
                        ),
                        side
                )
        )
        .toList();
    }

    private List<UUID> setZeroHop(List<UUID> oldUUIDList, Side side, PublicProcurementInvestigationContext context) {
        List<Transaction> foundTransactionList = getTransactions(oldUUIDList);
        context.getTransactionListByTypeNumber(side, 0).addAll(
                foundTransactionList.stream()
                        .map(transaction ->
                                new TransactionItemBySide(
                                        new TransactionConnection(
                                                transaction.getId(),
                                                transaction.getActorSender().getId(),
                                                transaction.getActorReceiver().getId()
                                        ),
                                        side
                                )
                        )
                        .toList()
        );
        return searchNewUUIDs(new HashSet<>(oldUUIDList), foundTransactionList);
    }

    private List<UUID> searchNewUUIDs(Set<UUID> oldUUIDSet, List<Transaction> foundTransactionList){
        Set<UUID> foundTransactionsUUIDs = foundTransactionList.stream()
                .flatMap(transaction -> Stream.of(transaction.getActorSender().getId(), transaction.getActorReceiver().getId()))
                .collect(Collectors.toSet());

        return getNewUUIDs(oldUUIDSet, foundTransactionsUUIDs);
    }

    private List<UUID> getNewUUIDs(Set<UUID> uuidOldSet, Set<UUID> foundUUIDs){
        Set<UUID> newsUUIDSet = new HashSet<>(foundUUIDs);
        newsUUIDSet.removeAll(uuidOldSet);
        return new ArrayList<>(newsUUIDSet);
    }


    private List<Transaction> getTransactions(List<UUID> uuidList){
        return transactionUseCases.findByFilter(
                new TransactionFilter(
                        null,
                        null,
                        null,
                        null,
                        uuidList,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    private void setTransactionGraph(PublicProcurementInvestigationContext context, Side side){
        context.getTransactionListBySide(side).forEach(
                transactionItemBySide ->
                        context.getTransactionGraphBySide(side).addGraphItem(transactionItemBySide.transactionConnection())
        );
    }
}
