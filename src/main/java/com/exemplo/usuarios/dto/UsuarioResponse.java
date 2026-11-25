package com.exemplo.usuarios.dto;

import com.exemplo.usuarios.model.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getCriadoEm(), u.getAtualizadoEm());
    }
}
