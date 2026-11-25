package com.exemplo.usuarios;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exemplo.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired MockMvc mvc;
    @Autowired UsuarioRepository repository;

    @BeforeEach
    void limpar() {
        repository.deleteAll();
    }

    private static final String JSON_VALIDO = """
            {"nome":"Maria Silva","email":"maria@exemplo.com","senha":"senha123"}""";

    @Test
    void criaUsuarioSemExporSenha() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.email").value("maria@exemplo.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void rejeitaEmailDuplicado() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isConflict());
    }

    @Test
    void validaCamposObrigatorios() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"email\":\"invalido\",\"senha\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.senha").exists());
    }

    @Test
    void listaBuscaAtualizaEDeleta() throws Exception {
        String loc = mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        mvc.perform(get("/api/usuarios").param("nome", "zzz"))
                .andExpect(jsonPath("$.content", hasSize(0)));

        mvc.perform(put(loc).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria Souza\",\"email\":\"maria@exemplo.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Souza"));

        mvc.perform(delete(loc)).andExpect(status().isNoContent());
        mvc.perform(get(loc)).andExpect(status().isNotFound());
    }
}
