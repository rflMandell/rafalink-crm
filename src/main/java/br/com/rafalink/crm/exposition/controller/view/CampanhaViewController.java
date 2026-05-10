package br.com.rafalink.crm.exposition.controller.view;
import br.com.rafalink.crm.application.service.CampanhaService;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

/**
 * ViewController — Campanhas
 * GET  /view/campanhas              → ativas
 * GET  /view/campanhas/todas        → todas
 * GET  /view/campanhas/{id}         → detalhe
 * GET  /view/campanhas/nova         → formulário
 * POST /view/campanhas              → criar
 * POST /view/campanhas/{id}/encerrar → encerrar
 */
@Controller @RequestMapping("/view/campanhas") @RequiredArgsConstructor
public class CampanhaViewController {
    private final CampanhaService campanhaService;

    @GetMapping
    public String listarAtivas(Model model) {
        model.addAttribute("campanhas", campanhaService.listarAtivas());
        model.addAttribute("exibindoAtivas", true);
        model.addAttribute("paginaAtiva", "campanhas");
        return "campanhas/lista";
    }

    @GetMapping("/todas")
    public String listarTodas(Model model) {
        model.addAttribute("campanhas", campanhaService.listarTodas());
        model.addAttribute("exibindoAtivas", false);
        model.addAttribute("paginaAtiva", "campanhas");
        return "campanhas/lista";
    }

    @GetMapping("/nova")
    public String formulario(Model model) {
        model.addAttribute("canais", CanalCampanha.values());
        model.addAttribute("paginaAtiva", "campanhas");
        return "campanhas/formulario";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("campanha", campanhaService.buscarPorId(id));
            model.addAttribute("paginaAtiva", "campanhas");
            return "campanhas/detalhe";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/campanhas";
        }
    }

    @PostMapping
    public String criar(@RequestParam String nome, @RequestParam(required=false) String descricao,
                        @RequestParam CanalCampanha canal,
                        @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate inicio,
                        @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fim,
                        RedirectAttributes ra) {
        try {
            campanhaService.criar(Campanha.builder().nome(nome).descricao(descricao).canal(canal).inicio(inicio).fim(fim).build());
            ra.addFlashAttribute("sucesso", "Campanha '" + nome + "' criada!");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/view/campanhas/nova";
        }
        return "redirect:/view/campanhas";
    }

    @PostMapping("/{id}/encerrar")
    public String encerrar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            campanhaService.encerrar(id);
            ra.addFlashAttribute("sucesso", "Campanha encerrada.");
        } catch (BusinessException | ResourceNotFoundException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view/campanhas/" + id;
    }
}
