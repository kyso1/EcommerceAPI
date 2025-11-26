# 🛒 E-commerce API - Backend

Uma API RESTful profissional e robusta para gerenciamento completo de e-commerce, desenvolvida como projeto de portfólio. O sistema é focado em integridade de dados, transações seguras, e boas práticas de engenharia de software.

## 📋 Sumário Executivo

Esta API implementa um sistema de e-commerce com funcionalidades de:

- **Catálogo de Produtos** com gerenciamento de estoque
- **Carrinho de Compras** inteligente com validação em tempo real
- **Checkout Seguro** com transações ACID garantidas
- **Autenticação e Autorização** de usuários
- **Documentação Interativa** com Swagger/OpenAPI

---

## 🚀 Tecnologias & Stack

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Linguagem** | Java | 17+ |
| **Framework** | Spring Boot | 4.0.0 |
| **Banco de Dados** | PostgreSQL | 15+ |
| **ORM** | Hibernate/Spring Data JPA | - |
| **Testes** | JUnit 5 & Mockito | - |
| **API Docs** | SpringDoc OpenAPI 3.0 | 2.3.0 |
| **Build** | Maven | 3.8+ |
| **Utils** | Lombok | - |

---

## 🏗️ Arquitetura & Estrutura do Projeto

```
ecommerce/
├── src/main/java/com/portfolio/ecommerce/
│   ├── config/              # Configuração da API (Swagger/OpenAPI)
│   ├── controller/          # Controllers REST (CartController, CheckoutController, ProductController)
│   ├── dto/                 # Data Transfer Objects
│   ├── model/               # Entidades JPA (User, Product, Order, CartItem, OrderItem)
│   ├── repository/          # Interfaces de acesso a dados (Spring Data JPA)
│   ├── service/             # Lógica de negócio (CheckoutService)
│   └── EcommerceApplication.java  # Classe principal
├── src/main/resources/
│   ├── application.properties     # Configurações da aplicação
│   ├── static/                    # Arquivos estáticos
│   └── templates/                 # Templates (se houver frontend integrado)
├── src/test/                      # Testes unitários
├── pom.xml                        # Configuração Maven
├── db.sql                         # Script de criação do banco
├── seed.sql                       # Script de dados iniciais
└── README.md                      # Este arquivo
```

---

## ⚙️ Funcionalidades & Destaques Técnicos

### 1️⃣ Gestão de Produtos

- **Listagem de Produtos**: Recupera todos os produtos disponíveis
- **Busca por ID**: Obtém detalhes específicos de um produto
- **Validação de Estoque**: Garante que apenas produtos em estoque sejam ofertados
- **Preços Históricos**: Armazena o preço no momento da compra para auditoria

**Endpoint Principal**: `GET /api/products` | `GET /api/products/{id}`

### 2️⃣ Carrinho de Compras Inteligente

- **Lógica de Upsert**: Ao adicionar o mesmo item duas vezes, apenas a quantidade é atualizada (sem duplicação)
- **Validação em Tempo Real**: Verifica estoque antes de adicionar ao carrinho
- **Remoção de Itens**: Remove produtos do carrinho facilmente
- **Limpeza Automática**: Carrinho é limpo após checkout bem-sucedido

**Destaques Técnicos**:

- Usa `findByUserIdAndProductId()` para identificar itens existentes
- Implementa `@Query` customizado para operações eficientes
- Validação de disponibilidade de estoque

**Endpoints Principais**:

- `POST /api/cart/add` - Adicionar ao carrinho
- `GET /api/cart/{userId}` - Ver carrinho
- `DELETE /api/cart/{cartItemId}` - Remover item

### 3️⃣ Checkout Transacional (Coração do Sistema)

O checkout é a operação mais crítica. Implementa transações ACID completas com `@Transactional`:

**Fluxo Transacional**:

1. Verifica disponibilidade de estoque para todos os itens
2. Calcula totais **no backend** (segurança contra manipulação de preços)
3. Cria a entidade `Order` com dados do usuário
4. Cria `OrderItem` para cada produto (com preço congelado do momento)
5. Deduz quantidade do estoque (`stock_quantity`)
6. Limpa o carrinho do usuário
7. Retorna confirmação com número do pedido

**Mecanismo de Segurança**:

- Se **qualquer etapa falhar**, o banco faz **ROLLBACK** automático
- Garante **consistência de dados** (não há pedido sem estoque reduzido, nem carrinho fantasma)
- Evita **race conditions** com locks de transação do Hibernate

**Exemplo de Falha Tratada**:

```java
Se estoque insuficiente → Transação cancelada
Se erro ao criar Order → Estoque não é alterado
Se erro ao limpar carrinho → Tudo volta ao estado inicial
```

**Endpoint**: `POST /api/checkout` - Processa pedido

### 4️⃣ Qualidade de Código & Testes

- **Testes Unitários**: Cobertura completa da camada de serviço (`CheckoutService`)
- **Mocks com Mockito**: Isolamento de dependências externas (repositories, etc.)
- **Tratamento de Exceções**: Mapeamento correto para HTTP Status Codes
- **Logging Estruturado**: Facilita debugging e auditoria

### 5️⃣ Documentação Interativa

- **Swagger UI**: Disponível em `/swagger-ui.html` quando a aplicação está rodando
- **OpenAPI 3.0**: Especificação completa em `/v3/api-docs`
- **Configuração Custom**: Em `config/OpenApiConfig.java`

---

## 🛠️ Como Rodar Localmente

### Pré-requisitos

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **PostgreSQL 15+** ([Download](https://www.postgresql.org/download/))
- **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- **Git** ([Download](https://git-scm.com/))

### Passo 1: Clone o Repositório

```bash
git clone https://github.com/SEU-USUARIO/ecommerce-backend.git
cd ecommerce
```

### Passo 2: Configurar Banco de Dados

```bash
# Conecte ao PostgreSQL
psql -U postgres

# Crie o banco de dados
CREATE DATABASE ecommerce_api;

# Saia do psql
\q
```

### Passo 3: Execute Scripts de Inicialização

```bash
# Crie as tabelas
psql -U postgres -d ecommerce_api -f db.sql

# Insira dados de teste
psql -U postgres -d ecommerce_api -f seed.sql
```

### Passo 4: Configure a Aplicação

Edite `src/main/resources/application.properties`:

```properties
# URL do Banco
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_api

# Usuário PostgreSQL (padrão: postgres)
spring.datasource.username=postgres

# Senha PostgreSQL
spring.datasource.password=sua_senha_aqui

# Mostrar SQL no console (opcional, útil para debug)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# DDL Strategy: 'validate' = apenas valida | 'update' = altera banco
spring.jpa.hibernate.ddl-auto=validate
```

### Passo 5: Compilar & Executar

```bash
# Compilar
mvn clean compile

# Executar aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### Passo 6: Acessar a Documentação da API

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## 📚 API Endpoints

### Produtos

```http
GET /api/products                 # Listar todos os produtos
GET /api/products/{id}            # Obter produto específico
```

### Carrinho

```http
POST /api/cart/add                # Adicionar produto ao carrinho
GET /api/cart/{userId}            # Ver carrinho do usuário
DELETE /api/cart/{cartItemId}     # Remover item do carrinho
```

### Checkout

```http
POST /api/checkout                # Processar pedido (transação segura)
```

---

## 🧪 Executar Testes

```bash
# Rodar todos os testes
mvn test

# Rodar com cobertura
mvn test jacoco:report

# Rodar teste específico
mvn test -Dtest=CheckoutServiceTest
```

---

## 🔐 Segurança & Boas Práticas

1. **Transações ACID**: Checkout implementado com `@Transactional` para garantir consistência
2. **Validação de Entrada**: Todos os inputs são validados antes de processar
3. **Cálculo de Preços no Backend**: Evita manipulação de preços pelo cliente
4. **Congelamento de Preço**: Preços dos pedidos são armazenados no histórico
5. **Controle de Estoque**: Verifica disponibilidade antes de vender
6. **Tratamento de Exceções**: Erros mapeados para respostas HTTP apropriadas

---

## 📊 Modelos de Dados

### User

```java
- id (PK)
- name
- email
- created_at
```

### Product

```java
- id (PK)
- name
- description
- price
- stock_quantity
- created_at
```

### Order

```java
- id (PK)
- user_id (FK)
- total_price
- status
- created_at
```

### OrderItem

```java
- id (PK)
- order_id (FK)
- product_id (FK)
- quantity
- unit_price (preço congelado)
```

### CartItem

```java
- id (PK)
- user_id (FK)
- product_id (FK)
- quantity
- created_at
```

---

## 🚨 Tratamento de Erros

A API retorna respostas HTTP apropriadas:

| Status | Cenário |
|--------|---------|
| `200 OK` | Requisição bem-sucedida |
| `400 Bad Request` | Dados inválidos |
| `404 Not Found` | Recurso não encontrado |
| `409 Conflict` | Erro de negócio (ex: estoque insuficiente) |
| `500 Internal Server Error` | Erro do servidor |

---

## 📝 Exemplos de Uso

### Adicionar ao Carrinho

```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 5, "quantity": 2}'
```

### Processar Checkout

```bash
curl -X POST http://localhost:8080/api/checkout \
  -H "Content-Type: application/json" \
  -d '{"userId": 1}'
```

---

## 📈 Performance & Optimizações

- **Lazy Loading**: Entidades relacionadas carregadas sob demanda
- **Query Customizado**: `@Query` para operações específicas e eficientes
- **Índices de Banco**: Criados em campos frequentemente consultados
- **Connection Pooling**: HikariCP (padrão do Spring Boot)

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para alterações maiores:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 👨‍💻 Autor

**Seu Nome**
- LinkedIn: [linkedin.com/in/seu-usuario](https://linkedin.com/in/)
- GitHub: [@seu-usuario](https://github.com/)
- Email: seu.email@exemplo.com

---

## 📞 Suporte

Para dúvidas ou problemas, abra uma [Issue](https://github.com/SEU-USUARIO/ecommerce-backend/issues).

---

**Última atualização**: Novembro de 2025
