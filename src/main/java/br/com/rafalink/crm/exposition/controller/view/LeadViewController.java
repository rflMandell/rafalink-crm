package br.com.rafalink.crm.exposition.controller.view;
import br.com.rafalink.crm.application.service.LeadService;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ViewController — Leads
 * GET  /view/leads                    → lista
 * GET  /view/leads/{id}               → detalhe
 * GET  /view/leads/novo               → formulário
 * POST /view/leads                    → criar
 * POST /view/leads/{id}/status        → atualizar status
 * POST /view/leads/arquivar-inativos  → arquivar (RN06)
 */
@Controller @RequestMapping("/view/leads") @RequiredArgsConstructor
public class LeadViewController {
    private final LeadService leadService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("leads", leadService.listarTodos());
        model.addAttribute("statusOpcoes", StatusLead.values());
        model.addAttribute("paginaAtiva", "leads");
        return "leads/lista";
    }

    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("paginaAtiva", "leads");
        return "leads/formulario";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("lead", leadService.buscarPorId(id));
            model.addAttribute("statusOpcoes", StatusLead.values());
            model.addAttribute("paginaAtiva", "leads");
            return "leads/detalhe";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/leads";
        }
    }

    @PostMapping
    public String criar(@RequestParam String nome, @RequestParam String email,
                        @RequestParam(required=false) String telefone,
                        @RequestParam(required=false) String origem, RedirectAttributes ra) {
        try {
            leadService.cadastrar(Lead.builder().nome(nome).email(email).telefone(telefone).origem(origem).build());
            ra.addFlashAttribute("sucesso", "Lead '" + nome + "' cadastrado!");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/leads/novo";
        }
        return "redirect:/view/leads";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id, @RequestParam StatusLead novoStatus, RedirectAttributes ra) {
        try {
            leadService.atualizarStatus(id, novoStatus);
            ra.addFlashAttribute("sucesso", "Status atualizado para " + novoStatus + ".");
        } catch (BusinessException | ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view/leads/" + id;
    }

    @PostMapping("/arquivar-inativos")
    public String arquivarInativos(RedirectAttributes ra) {
        int total = leadService.arquivarLeadsInativos();
        ra.addFlashAttribute("sucesso", total + " lead(s) arquivado(s) por inatividade (RN06).");
        return "redirect:/view/leads";
    }
}
