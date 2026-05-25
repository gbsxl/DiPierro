package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.application.usecase.business.CreateBusinessUseCase;
import Di.Pierro.application.usecase.business.GetBusinessByIdUseCase;
import Di.Pierro.application.usecase.business.SearchBusinessUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessBeanConfig {

    @Bean
    public CreateBusinessUseCase createBusinessUseCase(BusinessRepository businessRepository){
        return new CreateBusinessUseCase(businessRepository);
    }

    @Bean
    public GetBusinessByIdUseCase getBusinessByIdUseCase(BusinessRepository businessRepository){
        return new GetBusinessByIdUseCase(businessRepository);
    }

    @Bean
    public SearchBusinessUseCase searchBusinessUseCase(BusinessRepository businessRepository){
        return new SearchBusinessUseCase(businessRepository);
    }

}
