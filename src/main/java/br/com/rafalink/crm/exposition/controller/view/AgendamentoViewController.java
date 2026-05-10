package br.com.rafalink.crm.exposition.controller.view;
import br.com.rafalink.crm.application.service.*;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.Agendamento;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

/**
 * ViewController — Agendamentos
 * GET  /view/agendamentos              → lista (filtro por usuário opcional)
 * GET  /view/agendamentos/{id}         → detalhe
 * GET  /view/agendamentos/novo         → formulário
 * POST /view/agendamentos              → criar
 * POST /view/agendamentos/{id}/cancelar → cancelar
 */
@Controller @RequestMapping("/view/agendamentos") @RequiredArgsConstructor
public class AgendamentoViewController {
    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;
    private final LeadService leadService;

    @GetMapping
    public String listar(@RequestParam(required=false) Long usuarioId, Model model) {
        if (usuarioId != null) {
            model.addAttribute("agendamentos", agendamentoService.listarPorUsuario(usuarioId));
            model.addAttribute("usuarioFiltro", usuarioId);
        } else {
            model.addAttribute("agendamentos",
                usuarioService.listarTodos().stream()
                    .flatMap(u -> agendamentoService.listarPorUsuario(u.getId()).stream())
                    .distinct().toList());
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("paginaAtiva", "agendamentos");
        return "agendamentos/lista";
    }

    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("leads", leadService.listarTodos());
        model.addAttribute("paginaAtiva", "agendamentos");
        return "agendamentos/formulario";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("agendamento", agendamentoService.buscarPorId(id));
            model.addAttribute("paginaAtiva", "agendamentos");
            return "agendamentos/detalhe";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/agendamentos";
        }
    }

    @PostMapping
    public String criar(@RequestParam String titulo, @RequestParam(required=false) String descricao,
                        @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
                        @RequestParam Long usuarioId, @RequestParam(required=false) Long leadId,
                        RedirectAttributes ra) {
        try {
            Agendamento a = Agendamento.builder().titulo(titulo).descricao(descricao).dataHora(dataHora)
                    .usuario(usuarioService.buscarPorId(usuarioId))
                    .lead(leadId != null ? leadService.buscarPorId(leadId) : null).build();
            agendamentoService.criar(a);
            ra.addFlashAttribute("sucesso", "Agendamento '" + titulo + "' criado!");
        } catch (BusinessException | ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/agendamentos/novo";
        }
        return "redirect:/view/agendamentos";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            agendamentoService.cancelar(id);
            ra.addFlashAttribute("sucesso", "Agendamento cancelado.");
        } catch (BusinessException | ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view/agendamentos/" + id;
    }
}
