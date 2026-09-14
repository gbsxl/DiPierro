package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;

import Di.Pierro.application.port.input.AssetUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.model.Asset;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AssetDataFetcher {
    private final AssetUseCases assetUseCases;

    public AssetDataFetcher(AssetUseCases assetUseCases) {
        this.assetUseCases = assetUseCases;
    }

    public void fetchAssets(PublicProcurementInvestigationContext context){
        Set<UUID> uuidSet = context.getAllPersonList().stream()
                .map(person -> person.getActor().getId())
                .collect(Collectors.toSet());
        context.getPersonAssets().putAll(getAllAssets(uuidSet));
    }

    private Map<UUID, List<Asset>> getAllAssets(Set<UUID> uuidSet){
        Map<UUID, List<Asset>> personAssets = new HashMap<>();
        List<Asset> assets = assetUseCases.findAllByActorsUUID(uuidSet.stream().toList());
        for(Asset asset : assets){
            UUID uuid = asset.getPerson().getActor().getId();
            personAssets
                    .computeIfAbsent(uuid, k -> new ArrayList<>())
                    .add(asset);
        }
        return personAssets;
    }
}
