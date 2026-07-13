package Di.Pierro.presentation.asset;

import Di.Pierro.application.dto.asset.CreateAssetInput;
import Di.Pierro.application.port.input.AssetUseCases;
import Di.Pierro.domain.model.Asset;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/asset")
public class AssetController {
    private final AssetUseCases assetUseCases;

    public AssetController(AssetUseCases assetUseCases) {
        this.assetUseCases = assetUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateAssetInput assetInput) {
        assetUseCases.createAsset(assetInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> findById(@PathVariable @NotNull UUID id) {
        Optional<Asset> optionalAsset = assetUseCases.findById(id);
        return optionalAsset.map(asset -> ResponseEntity.ok(optionalAsset.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Asset>> findAll() {
        return ResponseEntity.ok(assetUseCases.findAll());
    }
}
