package Di.Pierro.presentation.investigation;

import Di.Pierro.application.port.investigation.publicProcurement.CheckPublicProcurementUseCase;
import Di.Pierro.application.port.investigation.publicProcurement.model.InvestigationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investigation")
public class InvestigationController {
    private final CheckPublicProcurementUseCase checkPublicProcurementUseCase;

    public InvestigationController(CheckPublicProcurementUseCase checkPublicProcurementUseCase) {
        this.checkPublicProcurementUseCase = checkPublicProcurementUseCase;
    }

    @PostMapping("/public-procurement/{id}")
    public ResponseEntity<InvestigationResult> publicProcurementInvestigation(@PathVariable("id") String id) {
        InvestigationResult result = checkPublicProcurementUseCase.investigatePublicProcurement(id);
        return ResponseEntity.ok(result);
    }
}
