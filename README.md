# Projeto Case-Backend

## Descrição
Este projeto é um exemplo de aplicação backend utilizando Spring Boot, com implementação de rotas CRUD para gerenciamento de dados de usuários. O sistema usa um `HashMap` para armazenar temporariamente os dados dos usuários, com campos como `id`, `nome`, `e-mail` e `idade`. Além disso, testes unitários são realizados para garantir a validade das operações.

## Tecnologias Utilizadas
- **Spring Boot 3.4.1**
- **Java 17**
- **JUnit** (para testes unitários)
- **Mockito** (para para criação de mocks nos testes)
- **Maven** (para gerenciamento de dependências)
- **Javadoc** (para documentação do código)
- **Jacoco** (para verificar a cobertura de testes)

## Estrutura de Dados
Os dados dos usuários são armazenados em um `HashMap` temporário. Cada usuário possui os seguintes campos:
- `id`: identificador único do usuário
- `nome`: nome do usuário
- `e-mail`: endereço de e-mail do usuário
- `idade`: idade do usuário

## Funcionalidades
O sistema implementa as operações básicas de CRUD (Criar, Ler, Atualizar e Deletar) para gerenciar os dados dos usuários.

### Endpoints da API:
- `POST /users`: Cria um novo usuário.
- `GET /users`: Retorna a lista de todos os usuários.
- `GET /users/{id}`: Retorna os dados de um usuário pelo ID.
- `PUT /users/{id}`: Atualiza os dados de um usuário existente.
- `PATCH /users/{id}`: Atualiza parcialmente os dados de um usuário existente.
- `DELETE /users/{id}`: Remove um usuário pelo ID.

## Como Executar

1. Clone o repositório:
   ```bash
   git clone git@github.com:leaozim/case-backend.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
    cd case-backend
   ```
Execute o projeto usando o Maven:
 ```bash
    mvn spring-boot:run
   ```
