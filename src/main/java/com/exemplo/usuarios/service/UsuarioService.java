package com.exemplo.usuarios.service;

import com.exemplo.usuarios.dto.UsuarioRequest;
import com.exemplo.usuarios.dto.UsuarioResponse;
import com.exemplo.usuarios.dto.UsuarioUpdateRequest;
import com.exemplo.usuarios.exception.EmailJaCadastradoException;
import com.exemplo.usuarios.exception.RecursoNaoEncontradoException;
import com.exemplo.usuarios.model.Usuario;
import com.exemplo.usuarios.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest req) {
        String email = req.email().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new EmailJaCadastradoException(email);
        }
        Usuario u = new Usuario();
        u.setNome(req.nome().trim());
        u.setEmail(email);
        u.setSenha(passwordEncoder.encode(req.senha()));
        return UsuarioResponse.de(repository.save(u));
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(String nome, Pageable pageable) {
        Page<Usuario> pagina = (nome == null || nome.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNomeContainingIgnoreCase(nome, pageable);
        return pagina.map(UsuarioResponse::de);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return UsuarioResponse.de(buscar(id));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioUpdateRequest req) {
        Usuario u = buscar(id);
        String email = req.email().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new EmailJaCadastradoException(email);
        }
        u.setNome(req.nome().trim());
        u.setEmail(email);
        return UsuarioResponse.de(repository.save(u));
    }

    @Transactional
    public void deletar(Long id) {
        repository.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));
    }
}
