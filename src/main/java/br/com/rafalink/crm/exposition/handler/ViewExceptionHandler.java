package br.com.rafalink.crm.exposition.handler;

import br.com.rafalink.crm.domain.exception.BusinessException;
import br.com.rafalink.crm.domain.exception.InvalidStatusException;
import br.com.rafalink.crm.domain.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Tratamento global de exceções para os ViewControllers (/view/**).
 *
 * Ao invés de retornar JSON (como o GlobalExceptionHandler faz para /api/**),
 * este handler captura exceções lançadas em @Controller e redireciona o usuário
 * de volta à página anterior com uma mensagem de erro no flash scope.
 *
 * A separação entre handlers é possível porque:
 * - GlobalExceptionHandler usa @RestControllerAdvice limitado ao pacote "json"
 * - ViewExceptionHandler usa @ControllerAdvice limitado ao pacote "view"
 */
@ControllerAdvice(basePackages = "br.com.rafalink.crm.exposition.controller.view")
public class ViewExceptionHandler {

    /**
     * Recurso não encontrado (ex: /view/leads/999 quando ID não existe).
     * Redireciona para a listagem do domínio com mensagem de erro.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(
            ResourceNotFoundException ex,
            RedirectAttributes ra,
            HttpServletRequest request
    ) {
        ra.addFlashAttribute("erro", ex.getMessage());
        return "redirect:" + resolverListagem(request);
    }

    /**
     * Regra de negócio violada (ex: desativar administrador, cancelar
     * agendamento já cancelado, encerrar campanha já encerrada).
     * Redireciona de volta ao referer (página onde estava) com mensagem.
     */
    @ExceptionHandler(BusinessException.class)
    public String handleBusiness(
            BusinessException ex,
            RedirectAttributes ra,
            HttpServletRequest request
    ) {
        ra.addFlashAttribute("erro", ex.getMessage());
        return "redirect:" + resolverRefererOuListagem(request);
    }

    /**
     * Status inválido enviado via form (ex: valor inválido no select de status).
     */
    @ExceptionHandler(InvalidStatusException.class)
    public String handleInvalidStatus(
            InvalidStatusException ex,
            RedirectAttributes ra,
            HttpServletRequest request
    ) {
        ra.addFlashAttribute("erro", "Status inválido: " + ex.getMessage());
        return "redirect:" + resolverRefererOuListagem(request);
    }

    /**
     * Exceção genérica não mapeada — redireciona para /view com mensagem.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneric(
            Exception ex,
            RedirectAttributes ra
    ) {
        ra.addFlashAttribute("erro",
                "Ocorreu um erro inesperado: " + ex.getMessage());
        return "redirect:/view";
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Extrai a URL base do domínio atual para redirecionar à listagem.
     * Ex: /view/leads/5/status → /view/leads
     *     /view/campanhas/3/encerrar → /view/campanhas
     */
    private String resolverListagem(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Pega os dois primeiros segmentos após /view/
        // ex: "/view/leads/5" → "/view/leads"
        String[] partes = uri.split("/");
        if (partes.length >= 3) {
            return "/" + partes[1] + "/" + partes[2];
        }
        return "/view";
    }

    /**
     * Usa o header Referer para voltar exatamente à página anterior.
     * Se não houver referer, cai na listagem do domínio.
     */
    private String resolverRefererOuListagem(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            // Extrai apenas o path (sem host/port) para evitar open redirect
            try {
                java.net.URI uri = java.net.URI.create(referer);
                String path = uri.getPath();
                if (path != null && path.startsWith("/view")) {
                    return path;
                }
            } catch (IllegalArgumentException ignored) {
                // referer malformado — cai no fallback
            }
        }
        return resolverListagem(request);
    }
}
