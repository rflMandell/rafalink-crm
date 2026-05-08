package br.com.rafalink.crm.exposition.controller.json;

import br.com.rafalink.crm.application.service.CampanhaService;
import br.com.rafalink.crm.domain.model.Campanha;
import br.com.rafalink.crm.exposition.dto.CampanhaRequest;
import br.com.rafalink.crm.exposition.dto.CampanhaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * JSONController — Campanhas
 *
 * Endpoints disponíveis:
 *   POST   /api/campanhas              → criar campanha           → 201 Created
 *   GET    /api/campanhas              → listar campanhas ativas  → 200 OK
 *   GET    /api/campanhas/todas        → listar todas campanhas   → 200 OK
 *   GET    /api/campanhas/{id}         → buscar por ID            → 200 OK | 404 Not Found
 *   PATCH  /api/campanhas/{id}/encerrar → encerrar campanha       → 200 OK | 422 Unprocessable
 */
@RestController
@RequestMapping("/api/campanhas")
@RequiredArgsConstructor
public class CampanhaController {

    private final CampanhaService campanhaService;

    @PostMapping
    public ResponseEntity<CampanhaResponse> criar(@RequestBody @Valid CampanhaRequest request) {
        Campanha campanha = Campanha.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .canal(request.canal())
                .inicio(request.inicio())
                .fim(request.fim())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CampanhaResponse.from(campanhaService.criar(campanha)));
    }

    @GetMapping
    public ResponseEntity<List<CampanhaResponse>> listarAtivas() {
        List<CampanhaResponse> lista = campanhaService.listarAtivas()
                .stream().map(CampanhaResponse::from).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<CampanhaResponse>> listarTodas() {
        List<CampanhaResponse> lista = campanhaService.listarTodas()
                .stream().map(CampanhaResponse::from).toList();
        return ResponseEntity.ok(lista);
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