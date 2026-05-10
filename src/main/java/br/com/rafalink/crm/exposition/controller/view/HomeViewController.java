package br.com.rafalink.crm.exposition.controller.view;
import br.com.rafalink.crm.application.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor
public class HomeViewController {
    private final UsuarioService usuarioService;
    private final LeadService leadService;
    private final CampanhaService campanhaService;

    @GetMapping("/view")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalLeads", leadService.listarTodos().size());
        model.addAttribute("totalCampanhas", campanhaService.listarTodas().size());
        model.addAttribute("campannhasAtivas", campanhaService.listarAtivas().size());
        model.addAttribute("paginaAtiva", "home");
        return "home";
    }

    @GetMapping("/")
    public String raiz() { return "redirect:/view"; }
}
