package br.com.rafalink.crm.exposition.controller.view;
import br.com.rafalink.crm.application.service.UsuarioService;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ViewController — Usuários
 * GET  /view/usuarios           → lista
 * GET  /view/usuarios/{id}      → detalhe
 * GET  /view/usuarios/novo      → formulário
 * POST /view/usuarios           → criar
 * POST /view/usuarios/{id}/desativar → desativar
 */
@Controller @RequestMapping("/view/usuarios") @RequiredArgsConstructor
public class UsuarioViewController {
    private final UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("paginaAtiva", "usuarios");
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("perfis", PerfilUsuario.values());
        model.addAttribute("paginaAtiva", "usuarios");
        return "usuarios/formulario";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("usuario", usuarioService.buscarPorId(id));
            model.addAttribute("paginaAtiva", "usuarios");
            return "usuarios/detalhe";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/usuarios";
        }
    }

    @PostMapping
    public String criar(@RequestParam String nome, @RequestParam String email,
                        @RequestParam String senha, @RequestParam PerfilUsuario perfil,
                        RedirectAttributes ra) {
        try {
            usuarioService.cadastrar(Usuario.builder().nome(nome).email(email).senha(senha).perfil(perfil).build());
            ra.addFlashAttribute("sucesso", "Usuário '" + nome + "' cadastrado com sucesso!");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/usuarios/novo";
        }
        return "redirect:/view/usuarios";
    }

    @PostMapping("/{id}/desativar")
    public String desativar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            usuarioService.desativar(id);
            ra.addFlashAttribute("sucesso", "Usuário desativado com sucesso.");
        } catch (BusinessException | ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view/usuarios";
    }
}
