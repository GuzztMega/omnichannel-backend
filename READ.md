# Omnichannel API

A API Omnichannel é um sistema de gerenciamento de canais de comunicação e interações com clientes. Esta API permite criar canais, clientes e interações, fornecendo uma interface fácil de usar para gerenciar a comunicação efetivamente.

---

## Configuração do Banco de Dados

Esta aplicação usa o PostgreSQL como banco de dados. Certifique-se de ter o PostgreSQL instalado e configurado em sua máquina. As configurações de conexão podem ser encontradas e alteradas no arquivo `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/omnichannel_api
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Crie um banco de dados chamado `omnichannel_api` no PostgreSQL e ajuste o `username` e `password` conforme a configuração do seu ambiente de banco de dados.

---
## Documentação da API com Swagger

A documentação da API está disponível via Swagger UI, que fornece uma interface interativa para explorar e testar os endpoints da API. Após iniciar a aplicação, acesse a documentação em:

```
http://localhost:8080/swagger-ui.html
```

Você também pode acessar a documentação da API em formato JSON em:

```
http://localhost:8080/api-docs
```
---
## Uso da API

A API oferece endpoints para gerenciar canais, clientes e interações. Aqui estão alguns exemplos de uso:

### Canais

- **Criar um Canal:** POST `/channels`
- **Obter um Canal por ID:** GET `/channels/{id}`
- **Listar Todos os Canais:** GET `/channels`

### Clientes

- **Criar um Cliente:** POST `/customers`
- **Obter um Cliente por ID:** GET `/customers/{id}`
- **Listar Histórico de Interações do Cliente:** GET `/customers/{id}/historic`

### Interações

- **Criar uma Interação:** POST `/interactions`
- **Obter uma Interação por ID:** GET `/interactions/{id}`

---

## Decisões de Design

- **Swagger para Documentação:** Escolhemos o Swagger para documentar a API devido à sua capacidade de gerar documentação interativa e legível.
- **Spring Boot e Spring MVC:** Utilizamos Spring Boot para simplificar a configuração e o lançamento da aplicação, e Spring MVC para modelar os endpoints da API.
- **Metodologias de Teste:** Usamos JUnit para validar a lógica de negócios de maneira isolada, e Mockito para simular dependências externas e comportamentos. 
