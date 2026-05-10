package br.com.rafalink.crm.exposition.controller.json;
import br.com.rafalink.crm.application.service.UsuarioService;
import br.com.rafalink.crm.domain.model.Usuario;
import br.com.rafalink.crm.exposition.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * JSONController — Usuários
 * POST   /api/usuarios               → 201 Created
 * GET    /api/usuarios               → 200 OK
 * GET    /api/usuarios/{id}          → 200 OK | 404
 * PATCH  /api/usuarios/{id}/desativar → 200 OK | 422
 */
@RestController @RequestMapping("/api/usuarios") @RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid UsuarioRequest req) {
        Usuario u = Usuario.builder().nome(req.nome()).email(req.email()).senha(req.senha()).perfil(req.perfil()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(usuarioService.cadastrar(u)));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos().stream().map(UsuarioResponse::from).toList());
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
