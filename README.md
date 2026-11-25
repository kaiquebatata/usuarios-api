# 👤 Usuários API

API REST para gerenciamento de usuários, construída com **Java 17** e **Spring Boot 3**, seguindo arquitetura em camadas e boas práticas de mercado.

## ✨ Funcionalidades

- CRUD completo de usuários
- Validação de dados com Bean Validation (mensagens em português)
- Senhas armazenadas com hash **BCrypt** (nunca retornadas pela API)
- E-mail único (case-insensitive) com resposta `409 Conflict`
- Paginação, ordenação e filtro por nome
- Tratamento global de erros com respostas padronizadas
- Documentação interativa com **Swagger / OpenAPI**
- Testes de integração com **MockMvc**

## 🛠️ Tecnologias

Java 17 · Spring Boot 3.3 · Spring Web · Spring Data JPA · Bean Validation · Spring Security Crypto · H2 · springdoc-openapi · JUnit 5

## 🏗️ Estrutura

```
src/main/java/com/exemplo/usuarios
├── controller   # Endpoints REST
├── service      # Regras de negócio
├── repository   # Acesso a dados (JPA)
├── model        # Entidades
├── dto          # Records de entrada/saída
├── exception    # Exceções e handler global
└── config       # Beans (BCrypt, OpenAPI)
```

## 🚀 Como executar

Pré-requisitos: JDK 17+ e Maven 3.9+

```bash
mvn spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- Console H2: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:usuariosdb`, usuário `sa`)

Rodar os testes:

```bash
mvn test
```

## 📌 Endpoints

| Método | Rota                 | Descrição                                   |
|--------|----------------------|---------------------------------------------|
| POST   | `/api/usuarios`      | Cria usuário                                |
| GET    | `/api/usuarios`      | Lista (`?nome=&page=0&size=10&sort=nome,asc`) |
| GET    | `/api/usuarios/{id}` | Busca por id                                |
| PUT    | `/api/usuarios/{id}` | Atualiza nome e e-mail                      |
| DELETE | `/api/usuarios/{id}` | Remove usuário                              |

### Exemplo

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Silva","email":"maria@exemplo.com","senha":"senha123"}'
```

Resposta `201 Created`:

```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria@exemplo.com",
  "criadoEm": "2026-11-25T16:50:00",
  "atualizadoEm": "2026-11-25T16:50:00"
}
```

Erro de validação `400`:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Erro de validação",
  "campos": { "email": "E-mail inválido" },
  "timestamp": "2026-11-25T16:50:00"
}
```

## 🔭 Próximos passos

- Autenticação com JWT (Spring Security)
- Banco PostgreSQL + Flyway
- Docker / Docker Compose
- CI com GitHub Actions

---

Desenvolvido por **Kaique Mattochek**
