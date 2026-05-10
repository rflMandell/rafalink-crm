package br.com.rafalink.crm.exposition.controller.json;
import br.com.rafalink.crm.application.service.CampanhaService;
import br.com.rafalink.crm.domain.model.Campanha;
import br.com.rafalink.crm.exposition.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * JSONController — Campanhas
 * POST   /api/campanhas              → 201 Created
 * GET    /api/campanhas              → 200 OK (ativas)
 * GET    /api/campanhas/todas        → 200 OK (todas)
 * GET    /api/campanhas/{id}         → 200 OK | 404
 * PATCH  /api/campanhas/{id}/encerrar → 200 OK | 422
 */
@RestController @RequestMapping("/api/campanhas") @RequiredArgsConstructor
public class CampanhaController {
    private final CampanhaService campanhaService;

    @PostMapping
    public ResponseEntity<CampanhaResponse> criar(@RequestBody @Valid CampanhaRequest req) {
        Campanha c = Campanha.builder().nome(req.nome()).descricao(req.descricao())
                .canal(req.canal()).inicio(req.inicio()).fim(req.fim()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(CampanhaResponse.from(campanhaService.criar(c)));
    }

    @GetMapping
    public ResponseEntity<List<CampanhaResponse>> listarAtivas() {
        return ResponseEntity.ok(campanhaService.listarAtivas().stream().map(CampanhaResponse::from).toList());
    }

    @GetMapping("/todas")
    public ResponseEntity<List<CampanhaResponse>> listarTodas() {
        return ResponseEntity.ok(campanhaService.listarTodas().stream().map(CampanhaResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(CampanhaResponse.from(campanhaService.buscarPorId(id)));
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<CampanhaResponse> encerrar(@PathVariable Long id) {
        return ResponseEntity.ok(CampanhaResponse.from(campanhaService.encerrar(id)));
    }
}
