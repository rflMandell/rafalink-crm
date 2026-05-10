package br.com.rafalink.crm.application.service;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.*;
import br.com.rafalink.crm.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j @Service @RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario cadastrar(Usuario u) {
        if (usuarioRepository.existsByEmail(u.getEmail()))
            throw new BusinessException("Já existe um usuário com o e-mail: " + u.getEmail());
        return usuarioRepository.save(u);
    }

    @Transactional
    public Usuario desativar(Long id) {
        Usuario u = buscarPorId(id);
        if (PerfilUsuario.ADMINISTRADOR.equals(u.getPerfil()))
            throw new BusinessException("Administradores não podem ser desativados.");
        u.setAtivo(false);
        return usuarioRepository.save(u);
    }

    @Transactional(readOnly=true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    @Transactional(readOnly=true)
    public List<Usuario> listarTodos() { return usuarioRepository.findAll(); }
}
