package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.model.Business;
import Di.Pierro.domain.model.RedFlag;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class BusinessAnalyzer {
    private final BusinessUseCases businessUseCases;
    private final RedFlagUseCases redFlagUseCases;

    public BusinessAnalyzer(BusinessUseCases businessUseCases, RedFlagUseCases redFlagUseCases) {
        this.businessUseCases = businessUseCases;
        this.redFlagUseCases = redFlagUseCases;
    }

    public void identifyRecentCompanyRedFlag(PublicProcurementInvestigationContext context){
        Business winner = context.getPublicProcurementWinner();

        if (winner != null && winner.getOpenDate() != null) {
            LocalDate oneYearAgo = LocalDate.now().minusYears(1);
            boolean businessWasOpenedLessThanOneYear = winner.getOpenDate().isAfter(oneYearAgo);

            if(businessWasOpenedLessThanOneYear){
                CreateRedFlagInput createRedFlagInput = new CreateRedFlagInput(
                        List.of(winner.getActor().getId()),
                        context.getProcurement().getId(),
                        null,
                        null,
                        "Recently established company",
                        3,
                        "A newly established business may raise suspicions due to its brief operating history"
                );
                RedFlag redFlag = redFlagUseCases.createRedFlag(createRedFlagInput);
                context.getRedFlags().add(redFlag);
            }
        }
    }

    public void identifyRecentlyCompanyRedFlag(PublicProcurementInvestigationContext context) {
        identifyRecentCompanyRedFlag(context);
    }

    public void identifyInsufficientCapitalStockRedFlag(PublicProcurementInvestigationContext context){
        Business winner = context.getPublicProcurementWinner();
        if (winner == null || context.getProcurement() == null) {
            return;
        }

        BigDecimal capitalStock = winner.getCapitalStock();
        BigDecimal estimatedPublicProcurementContractValue = context.getProcurement().getEstimatedValue();

        if (capitalStock == null || estimatedPublicProcurementContractValue == null) {
            return;
        }

        boolean isCapitalStockSmallerPublicProcurementContractValue = capitalStock.compareTo(estimatedPublicProcurementContractValue) < 0;

        if (isCapitalStockSmallerPublicProcurementContractValue){
            CreateRedFlagInput createRedFlagInput = new CreateRedFlagInput(
                    List.of(winner.getActor().getId()),
                    context.getProcurement().getId(),
                    null,
                    null,
                    "The winning business's capital stock is less than the contract value of the public procurement",
                    3,
                    "Capital stock below the contract value may raise concerns about the bidder's ability to meet the economic and financial qualification requirements set forth in the public procurement notice."
            );
            RedFlag redFlag = redFlagUseCases.createRedFlag(createRedFlagInput);
            context.getRedFlags().add(redFlag);
        }
    }

    public void identifyCapitalStockSmallerPublicProcurementContractValue(PublicProcurementInvestigationContext context) {
        identifyInsufficientCapitalStockRedFlag(context);
    }


}
