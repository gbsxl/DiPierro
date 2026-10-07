package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.model.Business;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.domain.model.RedFlag;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class BusinessAnalyzer {
    private final RedFlagUseCases redFlagUseCases;

    public BusinessAnalyzer(RedFlagUseCases redFlagUseCases) {
        this.redFlagUseCases = redFlagUseCases;
    }

    public void identifyRecentCompanyRedFlag(PublicProcurementInvestigationContext context){
        PublicProcurement procurement = context.getProcurement();
        List<RedFlag> redFlags = context.getRedFlags();
        checkRecentCompany(context.getAllBusinessList(), procurement, redFlags);
    }

    private void checkRecentCompany(List<Business> businessList, PublicProcurement procurement, List<RedFlag> redFlags) {
        if(procurement == null) return;
        LocalDate referenceDate = procurement.getPublicationDate() != null
                ? procurement.getPublicationDate()
                : procurement.getOpeningDate();

        Set<UUID> recentlyFoundCompanies = new HashSet<>();
        for (Business business : businessList) {
            if (business == null) continue;
            Boolean isRecentCompany = isRecentCompany(business, referenceDate);
            if(Boolean.TRUE.equals(isRecentCompany)) recentlyFoundCompanies.add(business.getActor().getId());
        }
        if(recentlyFoundCompanies.isEmpty()) return;
        redFlags.add(createRecentCompanyRedFlag(recentlyFoundCompanies, procurement.getId()));
    }

    private RedFlag createRecentCompanyRedFlag(Set<UUID> recentlyFoundCompanies, UUID publicProcurementUUID) {
        boolean isGreaterThanAnElement = recentlyFoundCompanies.size() > 1;
        String description = isGreaterThanAnElement
                ? "Some newly established businesses may raise suspicions due to their brief operating history"
                : "A newly established business may raise suspicions due to its brief operating history";

        CreateRedFlagInput createRedFlagInput = new CreateRedFlagInput(
                new ArrayList<>(recentlyFoundCompanies),
                publicProcurementUUID,
                null,
                null,
                "Recently established compan" + (isGreaterThanAnElement ? "ies" : "y"),
                8,
                description
        );
        return redFlagUseCases.createRedFlag(createRedFlagInput);
    }

    private static Boolean isRecentCompany(Business businessParticipant, LocalDate referenceDate) {
        LocalDate openDate = businessParticipant.getOpenDate();
        if (openDate == null) {
            return null;
        }
        LocalDate oneYearBeforeReference = (referenceDate != null ? referenceDate : LocalDate.now()).minusYears(1);
        return openDate.isAfter(oneYearBeforeReference);
    }

    public void identifyInsufficientCapitalStockRedFlag(PublicProcurementInvestigationContext context){
        PublicProcurement procurement = context.getProcurement();
        List<RedFlag> redFlags = context.getRedFlags();
        checkInsufficientCapitalStock(context.getAllBusinessList(), procurement, redFlags);
    }

    private void checkInsufficientCapitalStock(List<Business> businessList, PublicProcurement procurement, List<RedFlag> redFlags) {
        if(procurement == null) return;
        Set<UUID> companiesWithInsufficientCapital = new HashSet<>();
        for (Business business : businessList) {
            if (business == null) continue;
            Boolean isCapitalStockSmallerPublicProcurementContractValue = isCapitalStockSmallerPublicProcurementContractValue(business, procurement.getEstimatedValue());
            if(Boolean.TRUE.equals(isCapitalStockSmallerPublicProcurementContractValue)) companiesWithInsufficientCapital.add(business.getActor().getId());
        }
        if(companiesWithInsufficientCapital.isEmpty()) return;
        redFlags.add(createCapitalStockTooSmallRedflag(companiesWithInsufficientCapital, procurement.getId()));
    }

    private RedFlag createCapitalStockTooSmallRedflag(Set<UUID> companiesWithInsufficientCapital, UUID publicProcurementUUID) {
        boolean isGreaterThanAnElement = companiesWithInsufficientCapital.size() > 1;
        CreateRedFlagInput createRedFlagInput = new CreateRedFlagInput(
                new ArrayList<>(companiesWithInsufficientCapital),
                publicProcurementUUID,
                null,
                null,
                "The " + (isGreaterThanAnElement ? "businesses' capital stock is" : "winning business's capital stock is") + " less than the contract value of the public procurement",
                6,
                "Capital stock below the contract value may raise concerns about the bidder" + (isGreaterThanAnElement ? "s'" : "'s") + " ability to meet the economic and financial qualification requirements set forth in the public procurement notice."
        );
        return redFlagUseCases.createRedFlag(createRedFlagInput);
    }

    private static Boolean isCapitalStockSmallerPublicProcurementContractValue(Business businessParticipant, BigDecimal publicProcurementEstimatedValue) {
        if (businessParticipant == null) {
            return null;
        }
        BigDecimal capitalStock = businessParticipant.getCapitalStock();

        if (capitalStock == null || publicProcurementEstimatedValue == null || capitalStock.compareTo(BigDecimal.ZERO) < 1) {
            return null;
        }

        return capitalStock.compareTo(publicProcurementEstimatedValue) < 0;
    }
}
