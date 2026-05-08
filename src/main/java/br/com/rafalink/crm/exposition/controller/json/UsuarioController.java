package br.com.rafalink.crm.exposition.controller.json;

import br.com.rafalink.crm.application.service.UsuarioService;
import br.com.rafalink.crm.domain.model.Usuario;
import br.com.rafalink.crm.exposition.dto.UsuarioRequest;
import br.com.rafalink.crm.exposition.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * JSONController — Usuários
 *
 * Endpoints disponíveis:
 *   POST   /api/usuarios               → cadastrar usuário         → 201 Created
 *   GET    /api/usuarios               → listar todos os usuários  → 200 OK
 *   GET    /api/usuarios/{id}          → buscar por ID             → 200 OK | 404 Not Found
 *   PATCH  /api/usuarios/{id}/desativar → desativar usuário        → 200 OK | 422 Unprocessable
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid UsuarioRequest request) {
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .perfil(request.perfil())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UsuarioResponse.from(usuarioService.cadastrar(usuario)));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> lista = usuarioService.listarTodos()
                .stream().map(UsuarioResponse::from).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.buscarPorId(id)));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponse> desativar(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.desativar(id)));
    }
}