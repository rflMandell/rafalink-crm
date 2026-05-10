package br.com.rafalink.crm.exposition.controller.json;
import br.com.rafalink.crm.application.service.*;
import br.com.rafalink.crm.domain.model.Agendamento;
import br.com.rafalink.crm.exposition.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * JSONController — Agendamentos
 * POST   /api/agendamentos              → 201 Created
 * GET    /api/agendamentos/{id}         → 200 OK | 404
 * GET    /api/agendamentos/usuario/{id} → 200 OK
 * GET    /api/agendamentos/lead/{id}    → 200 OK
 * PATCH  /api/agendamentos/{id}/cancelar → 200 OK | 422
 */
@RestController @RequestMapping("/api/agendamentos") @RequiredArgsConstructor
public class AgendamentoController {
    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;
    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoRequest req) {
        Agendamento a = Agendamento.builder()
                .titulo(req.titulo()).descricao(req.descricao()).dataHora(req.dataHora())
                .usuario(usuarioService.buscarPorId(req.usuarioId()))
                .lead(req.leadId() != null ? leadService.buscarPorId(req.leadId()) : null)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoResponse.from(agendamentoService.criar(a)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(agendamentoService.buscarPorId(id)));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(agendamentoService.listarPorUsuario(usuarioId).stream().map(AgendamentoResponse::from).toList());
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(agendamentoService.listarPorLead(leadId).stream().map(AgendamentoResponse::from).toList());
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(agendamentoService.cancelar(id)));
    }
}
