package br.com.rafalink.crm.application.service;

import br.com.rafalink.crm.domain.exception.BusinessException;
import br.com.rafalink.crm.domain.exception.ResourceNotFoundException;
import br.com.rafalink.crm.domain.model.Usuario;
import br.com.rafalink.crm.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // E-mail deve ser único no sistema. Não permitir cadastro duplicado.
    @Transactional
    public Usuario cadastrar(Usuario usuario) {
        log.debug("Tentando cadastrar usuário com email: {}", usuario.getEmail());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException(
                    "Já existe um usuário cadastrado com o e-mail: " + usuario.getEmail()
            );
        }

        validarEmailFormato(usuario.getEmail());

        Usuario salvo = usuarioRepository.save(usuario);
        log.debug("Usuário cadastrado com sucesso. ID: {}", salvo.getId());
        return salvo;
    }

    // Não permitir desativar usuário que não existe.
    // Administrador não pode ser desativado pelo sistema.
    @Transactional
    public Usuario desativar(Long id) {
        Usuario usuario = buscarPorId(id);

        if (usuario.getPerfil().name().equals("ADMINISTRADOR")) {
            throw new BusinessException(
                    "Administradores não podem ser desativados pelo sistema."
            );
        }

        usuario.setAtivo(false);
        log.debug("Usuário ID {} desativado.", id);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    private void validarEmailFormato(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BusinessException("E-mail inválido: " + email);
        }
    }
}