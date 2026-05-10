package br.com.rafalink.crm.exposition.controller.json;
import br.com.rafalink.crm.application.service.LeadService;
import br.com.rafalink.crm.domain.model.*;
import br.com.rafalink.crm.exposition.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * JSONController — Leads
 * POST   /api/leads                       → 201 Created
 * GET    /api/leads                       → 200 OK
 * GET    /api/leads/{id}                  → 200 OK | 404
 * GET    /api/leads/status/{status}       → 200 OK
 * PATCH  /api/leads/{id}/status           → 200 OK | 422
 * PATCH  /api/leads/arquivar-inativos     → 200 OK (RN06)
 */
@RestController @RequestMapping("/api/leads") @RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<LeadResponse> cadastrar(@RequestBody @Valid LeadRequest req) {
        Lead l = Lead.builder().nome(req.nome()).email(req.email()).telefone(req.telefone()).origem(req.origem()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(LeadResponse.from(leadService.cadastrar(l)));
    }

    @GetMapping
    public ResponseEntity<List<LeadResponse>> listar() {
        return ResponseEntity.ok(leadService.listarTodos().stream().map(LeadResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(LeadResponse.from(leadService.buscarPorId(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeadResponse>> listarPorStatus(@PathVariable StatusLead status) {
        return ResponseEntity.ok(leadService.listarPorStatus(status).stream().map(LeadResponse::from).toList());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadResponse> atualizarStatus(@PathVariable Long id, @RequestParam StatusLead novoStatus) {
        return ResponseEntity.ok(LeadResponse.from(leadService.atualizarStatus(id, novoStatus)));
    }

    @PatchMapping("/arquivar-inativos")
    public ResponseEntity<String> arquivarInativos() {
        return ResponseEntity.ok(leadService.arquivarLeadsInativos() + " lead(s) arquivado(s).");
    }
}
