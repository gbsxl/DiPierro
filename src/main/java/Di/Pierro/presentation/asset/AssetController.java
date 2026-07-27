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

    @GetMapping("/{string}/type")
    public ResponseEntity<List<Asset>> findByType(@PathVariable String string) {
        return ResponseEntity.ok(assetUseCases.findByType(string));
    }

    @GetMapping("/stillHaveIt/{stillHaveIt}")
    public ResponseEntity<List<Asset>> findByStillHaveIt(@PathVariable boolean stillHaveIt) {
        return ResponseEntity.ok(assetUseCases.findByStillHaveIt(stillHaveIt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asset> update(@PathVariable @NotNull UUID id, @RequestBody CreateAssetInput asset) {
        return ResponseEntity.ok(assetUseCases.updateById(id, asset));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        assetUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
