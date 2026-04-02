package br.com.rafalink.crm.exposition.controller;

import br.com.rafalink.crm.application.service.LeadService;
import br.com.rafalink.crm.domain.model.Lead;
import br.com.rafalink.crm.domain.model.StatusLead;
import br.com.rafalink.crm.exposition.dto.LeadRequest;
import br.com.rafalink.crm.exposition.dto.LeadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<LeadResponse> cadastrar(@RequestBody @Valid LeadRequest request) {
        Lead lead = Lead.builder()
                .nome(request.nome())
                .email(request.email())
                .telefone(request.telefone())
                .origem(request.origem())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LeadResponse.from(leadService.cadastrar(lead)));
    }

    @GetMapping
    public ResponseEntity<List<LeadResponse>> listar() {
        List<LeadResponse> lista = leadService.listarTodos()
                .stream().map(LeadResponse::from).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(LeadResponse.from(leadService.buscarPorId(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeadResponse>> listarPorStatus(@PathVariable StatusLead status) {
        List<LeadResponse> lista = leadService.listarPorStatus(status)
                .stream().map(LeadResponse::from).toList();
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusLead novoStatus
    ) {
        return ResponseEntity.ok(LeadResponse.from(leadService.atualizarStatus(id, novoStatus)));
    }

    @PatchMapping("/arquivar-inativos")
    public ResponseEntity<String> arquivarInativos() {
        int total = leadService.arquivarLeadsInativos();
        return ResponseEntity.ok(total + " lead(s) arquivado(s) por inatividade.");
    }
}