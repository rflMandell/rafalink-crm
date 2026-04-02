package br.com.rafalink.crm.exposition.controller;

import br.com.rafalink.crm.application.service.AgendamentoService;
import br.com.rafalink.crm.application.service.LeadService;
import br.com.rafalink.crm.application.service.UsuarioService;
import br.com.rafalink.crm.domain.model.Agendamento;
import br.com.rafalink.crm.exposition.dto.AgendamentoRequest;
import br.com.rafalink.crm.exposition.dto.AgendamentoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;
    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoRequest request) {
        Agendamento agendamento = Agendamento.builder()
                .Titulo(request.titulo())
                .descricao(request.descricao())
                .dataHora(request.dataHora())
                .usuario(usuarioService.buscarPorId(request.usuarioId()))
                .lead(request.leadId() != null ? leadService.buscarPorId(request.leadId()) : null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AgendamentoResponse.from(agendamentoService.criar(agendamento)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(agendamentoService.buscarPorId(id)));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<AgendamentoResponse> lista = agendamentoService.listarPorUsuario(usuarioId)
                .stream().map(AgendamentoResponse::from).toList();
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(agendamentoService.cancelar(id)));
    }
}