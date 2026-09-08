package Di.Pierro.application.port.investigation.publicProcurement.model;

import Di.Pierro.domain.model.*;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Stream;

public class PublicProcurementInvestigationContext {
    private PublicProcurement procurement;
    private List<Business> allBusinessList = new ArrayList<>();
    private List<Person> allPersonList = new ArrayList<>();
    private List<Association> associationList = new ArrayList<>();
    private List<Transaction> transactionList = new ArrayList<>();
    private List<Asset> assetList = new ArrayList<>();
    private List<ActorIndicator> indicatorList = new ArrayList<>();
    private List<RedFlag> redFlags = new ArrayList<>();

    private Map<UUID, Person> allGovernmentSidePersonList = new HashMap<>();
    private Map<UUID, Person> allBusinessSidePersonList = new HashMap<>();

    private Business publicProcurementWinner;

    private PersonGraph businessPersonGraph = new PersonGraph();
    private PersonGraph governmentPersonGraph = new PersonGraph();
    private PersonGraphIntersection personGraphIntersection = new PersonGraphIntersection();

    private TransactionGraph transactionGraphBusiness = new TransactionGraph();
    private TransactionGraph transactionGraphGovernment = new TransactionGraph();
    private TransactionGraph transactionGraphIntersection = new TransactionGraph();

    private List<Person> governmentPersonHopZero = new ArrayList<>();
    private List<Person> governmentPersonHopOne = new ArrayList<>();
    private List<Person> governmentPersonHopTwo = new ArrayList<>();
    private List<Person> governmentPersonHopThree = new ArrayList<>();

    private List<Person> businessPersonHopZero = new ArrayList<>();
    private List<Person> businessPersonHopOne = new ArrayList<>();
    private List<Person> businessPersonHopTwo = new ArrayList<>();
    private List<Person> businessPersonHopThree = new ArrayList<>();


    private List<TransactionItemBySide> governmentTransactionsList = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopZero = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopOne = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopTwo = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopThree = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopFour = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopFive = new ArrayList<>();
    private List<TransactionItemBySide> governmentTransactionHopSix = new ArrayList<>();

    private List<TransactionItemBySide> businessTransactionsList = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopZero = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopOne = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopTwo = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopThree = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopFour = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopFive = new ArrayList<>();
    private List<TransactionItemBySide> businessTransactionHopSix = new ArrayList<>();

    private List<QSA> qsaList = new ArrayList<>();

    public List<TransactionItemBySide> getTransactionListByTypeNumber(Side side, int number) {
        if (side == null || side == Side.WITHOUT_CLASSIFICATION || number < 0 || number > 6) {
            return new ArrayList<>();
        }

        if (side == Side.BUSINESS) {
            return switch (number) {
                case 0 -> businessTransactionHopZero;
                case 1 -> businessTransactionHopOne;
                case 2 -> businessTransactionHopTwo;
                case 3 -> businessTransactionHopThree;
                case 4 -> businessTransactionHopFour;
                case 5 -> businessTransactionHopFive;
                case 6 -> businessTransactionHopSix;
                default -> new ArrayList<>();
            };
        }

        if (side == Side.GOVERNMENT) {
            return switch (number) {
                case 0 -> governmentTransactionHopZero;
                case 1 -> governmentTransactionHopOne;
                case 2 -> governmentTransactionHopTwo;
                case 3 -> governmentTransactionHopThree;
                case 4 -> governmentTransactionHopFour;
                case 5 -> governmentTransactionHopFive;
                case 6 -> governmentTransactionHopSix;
                default -> new ArrayList<>();
            };
        }

        return new ArrayList<>();
    }
    
    public void setGovernmentTransactionsListByAllLists(Side side){
        List<TransactionItemBySide> transactionItemBySides = Stream.of(
                        getTransactionListByTypeNumber(side, 0),
                        getTransactionListByTypeNumber(side, 1),
                        getTransactionListByTypeNumber(side, 2),
                        getTransactionListByTypeNumber(side, 3),
                        getTransactionListByTypeNumber(side, 4),
                        getTransactionListByTypeNumber(side, 5),
                        getTransactionListByTypeNumber(side, 6)

                )
                .flatMap(List::stream)
                .toList();

        if(side == Side.GOVERNMENT) getGovernmentTransactionsList().addAll(transactionItemBySides);
        if(side == Side.BUSINESS) getBusinessTransactionsList().addAll(transactionItemBySides);
    }

    public TransactionGraph getTransactionGraphBySide(Side side){
        if(side == Side.BUSINESS) return getTransactionGraphBusiness();
        if(side == Side.GOVERNMENT) return getTransactionGraphGovernment();
        throw new IllegalArgumentException("Unsupported side: " + side);
    }

    public List<TransactionItemBySide> getTransactionListBySide(Side side){
        if(side == Side.BUSINESS) return getBusinessTransactionsList();
        if(side == Side.GOVERNMENT) return getGovernmentTransactionsList();
        throw new IllegalArgumentException("Unsupported side: " + side);
    }

    public void addGovernmentPersons(List<Person> personList, int hop){
        allPersonList.addAll(personList);

        for (Person person : personList){
            allGovernmentSidePersonList.put(person.getActor().getId(), person);
        }

        if(hop == 0){
            governmentPersonHopZero.addAll(personList);
        }
        if(hop == 1){
            governmentPersonHopOne.addAll(personList);
        }
        if(hop == 2){
            governmentPersonHopTwo.addAll(personList);
        }
        if(hop == 3){
            governmentPersonHopThree.addAll(personList);
        }
    }

    public void addBusinessPersons(List<Person> personList, int hop){
        allPersonList.addAll(personList);

        for (Person person : personList){
            allBusinessSidePersonList.put(person.getActor().getId(), person);
        }

        if(hop == 0){
            businessPersonHopZero.addAll(personList);
        }
        if(hop == 1){
            businessPersonHopOne.addAll(personList);
        }
        if(hop == 2){
            businessPersonHopTwo.addAll(personList);
        }
        if(hop == 3){
            businessPersonHopThree.addAll(personList);
        }
    }

    public void setTransactionGraphBusiness(TransactionGraph transactionGraphBusiness) {
        this.transactionGraphBusiness = transactionGraphBusiness;
    }

    public TransactionGraph getTransactionGraphIntersection() {
        return transactionGraphIntersection;
    }

    public TransactionGraph getTransactionGraphBusiness() {
        return transactionGraphBusiness;
    }

    public void setTransactionGraphIntersection(TransactionGraph transactionGraph) {
        this.transactionGraphIntersection = transactionGraph;
    }

    public List<TransactionItemBySide> getGovernmentTransactionsList() {
        return governmentTransactionsList;
    }

    public void setGovernmentTransactionsList(List<TransactionItemBySide> governmentTransactionsList) {
        this.governmentTransactionsList = governmentTransactionsList;
    }

    public List<TransactionItemBySide> getBusinessTransactionsList() {
        return businessTransactionsList;
    }

    public void setBusinessTransactionsList(List<TransactionItemBySide> businessTransactionsList) {
        this.businessTransactionsList = businessTransactionsList;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopZero() {
        return governmentTransactionHopZero;
    }

    public void setGovernmentTransactionHopZero(List<TransactionItemBySide> governmentTransactionHopZero) {
        this.governmentTransactionHopZero = governmentTransactionHopZero;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopOne() {
        return governmentTransactionHopOne;
    }

    public void setGovernmentTransactionHopOne(List<TransactionItemBySide> governmentTransactionHopOne) {
        this.governmentTransactionHopOne = governmentTransactionHopOne;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopTwo() {
        return governmentTransactionHopTwo;
    }

    public void setGovernmentTransactionHopTwo(List<TransactionItemBySide> governmentTransactionHopTwo) {
        this.governmentTransactionHopTwo = governmentTransactionHopTwo;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopThree() {
        return governmentTransactionHopThree;
    }

    public void setGovernmentTransactionHopThree(List<TransactionItemBySide> governmentTransactionHopThree) {
        this.governmentTransactionHopThree = governmentTransactionHopThree;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopFour() {
        return governmentTransactionHopFour;
    }

    public void setGovernmentTransactionHopFour(List<TransactionItemBySide> governmentTransactionHopFour) {
        this.governmentTransactionHopFour = governmentTransactionHopFour;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopFive() {
        return governmentTransactionHopFive;
    }

    public void setGovernmentTransactionHopFive(List<TransactionItemBySide> governmentTransactionHopFive) {
        this.governmentTransactionHopFive = governmentTransactionHopFive;
    }

    public List<TransactionItemBySide> getGovernmentTransactionHopSix() {
        return governmentTransactionHopSix;
    }

    public void setGovernmentTransactionHopSix(List<TransactionItemBySide> governmentTransactionHopSix) {
        this.governmentTransactionHopSix = governmentTransactionHopSix;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopZero() {
        return businessTransactionHopZero;
    }

    public void setBusinessTransactionHopZero(List<TransactionItemBySide> businessTransactionHopZero) {
        this.businessTransactionHopZero = businessTransactionHopZero;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopOne() {
        return businessTransactionHopOne;
    }

    public void setBusinessTransactionHopOne(List<TransactionItemBySide> businessTransactionHopOne) {
        this.businessTransactionHopOne = businessTransactionHopOne;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopTwo() {
        return businessTransactionHopTwo;
    }

    public void setBusinessTransactionHopTwo(List<TransactionItemBySide> businessTransactionHopTwo) {
        this.businessTransactionHopTwo = businessTransactionHopTwo;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopThree() {
        return businessTransactionHopThree;
    }

    public void setBusinessTransactionHopThree(List<TransactionItemBySide> businessTransactionHopThree) {
        this.businessTransactionHopThree = businessTransactionHopThree;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopFour() {
        return businessTransactionHopFour;
    }

    public void setBusinessTransactionHopFour(List<TransactionItemBySide> businessTransactionHopFour) {
        this.businessTransactionHopFour = businessTransactionHopFour;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopFive() {
        return businessTransactionHopFive;
    }

    public void setBusinessTransactionHopFive(List<TransactionItemBySide> businessTransactionHopFive) {
        this.businessTransactionHopFive = businessTransactionHopFive;
    }

    public List<TransactionItemBySide> getBusinessTransactionHopSix() {
        return businessTransactionHopSix;
    }

    public void setBusinessTransactionHopSix(List<TransactionItemBySide> businessTransactionHopSix) {
        this.businessTransactionHopSix = businessTransactionHopSix;
    }

    public PersonGraphIntersection getPersonGraphIntersection() {
        return personGraphIntersection;
    }

    public void setPersonGraphIntersection(PersonGraphIntersection personGraphIntersection) {
        this.personGraphIntersection = personGraphIntersection;
    }

    public Map<UUID, Person> getAllGovernmentSidePersonList() {
        return allGovernmentSidePersonList;
    }

    public void setAllGovernmentSidePersonList(Map<UUID, Person> allGovernmentSidePersonList) {
        this.allGovernmentSidePersonList = allGovernmentSidePersonList;
    }

    public Map<UUID, Person> getAllBusinessSidePersonList() {
        return allBusinessSidePersonList;
    }

    public void setAllBusinessSidePersonList(Map<UUID, Person> allBusinessSidePersonList) {
        this.allBusinessSidePersonList = allBusinessSidePersonList;
    }

    public List<Person> getGovernmentPersonHopZero() {
        return governmentPersonHopZero;
    }

    public void setGovernmentPersonHopZero(List<Person> governmentPersonHopZero) {
        this.governmentPersonHopZero = governmentPersonHopZero;
    }

    public List<Person> getGovernmentPersonHopOne() {
        return governmentPersonHopOne;
    }

    public void setGovernmentPersonHopOne(List<Person> governmentPersonHopOne) {
        this.governmentPersonHopOne = governmentPersonHopOne;
    }

    public List<Person> getGovernmentPersonHopTwo() {
        return governmentPersonHopTwo;
    }

    public void setGovernmentPersonHopTwo(List<Person> governmentPersonHopTwo) {
        this.governmentPersonHopTwo = governmentPersonHopTwo;
    }

    public List<Person> getGovernmentPersonHopThree() {
        return governmentPersonHopThree;
    }

    public void setGovernmentPersonHopThree(List<Person> governmentPersonHopThree) {
        this.governmentPersonHopThree = governmentPersonHopThree;
    }

    public List<Person> getBusinessPersonHopZero() {
        return businessPersonHopZero;
    }

    public void setBusinessPersonHopZero(List<Person> businessPersonHopZero) {
        this.businessPersonHopZero = businessPersonHopZero;
    }

    public List<Person> getBusinessPersonHopOne() {
        return businessPersonHopOne;
    }

    public void setBusinessPersonHopOne(List<Person> businessPersonHopOne) {
        this.businessPersonHopOne = businessPersonHopOne;
    }

    public List<Person> getBusinessPersonHopTwo() {
        return businessPersonHopTwo;
    }

    public void setBusinessPersonHopTwo(List<Person> businessPersonHopTwo) {
        this.businessPersonHopTwo = businessPersonHopTwo;
    }

    public List<Person> getBusinessPersonHopThree() {
        return businessPersonHopThree;
    }

    public void setBusinessPersonHopThree(List<Person> businessPersonHopThree) {
        this.businessPersonHopThree = businessPersonHopThree;
    }

    public PersonGraph getGovernmentPersonGraph() {
        return governmentPersonGraph;
    }

    public void setGovernmentPersonGraph(PersonGraph governmentPersonGraph) {
        this.governmentPersonGraph = governmentPersonGraph;
    }

    public PersonGraph getBusinessPersonGraph() {
        return businessPersonGraph;
    }

    public void setBusinessPersonGraph(PersonGraph businessPersonGraph) {
        this.businessPersonGraph = businessPersonGraph;
    }

    public List<QSA> getQsaList() {
        return qsaList;
    }

    public void setQsaList(List<QSA> qsaList) {
        this.qsaList = qsaList;
    }

    public Business getPublicProcurementWinner() {
        return publicProcurementWinner;
    }

    public void setPublicProcurementWinner(Business publicProcurementWinner) {
        this.publicProcurementWinner = publicProcurementWinner;
    }

    public PublicProcurement getProcurement() {
        return procurement;
    }

    public void setProcurement(PublicProcurement procurement) {
        this.procurement = procurement;
    }

    public List<Business> getAllBusinessList() {
        return allBusinessList;
    }

    public void setAllBusinessList(List<Business> allBusinessList) {
        this.allBusinessList = allBusinessList;
    }

    public List<Person> getAllPersonList() {
        return allPersonList;
    }

    public void setAllPersonList(List<Person> allPersonList) {
        this.allPersonList = allPersonList;
    }

    public List<Association> getAssociationList() {
        return associationList;
    }

    public void setAssociationList(List<Association> associationList) {
        this.associationList = associationList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<Asset> getAssetList() {
        return assetList;
    }

    public void setAssetList(List<Asset> assetList) {
        this.assetList = assetList;
    }

    public List<ActorIndicator> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<ActorIndicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    public List<RedFlag> getRedFlags() {
        return redFlags;
    }

    public void setRedFlags(List<RedFlag> redFlags) {
        this.redFlags = redFlags;
    }

    public TransactionGraph getTransactionGraphGovernment() {
        return transactionGraphGovernment;
    }

    public void setTransactionGraphGovernment(TransactionGraph transactionGraphGovernment) {
        this.transactionGraphGovernment = transactionGraphGovernment;
    }
}
