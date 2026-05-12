# 🏦 Modern Credit Bridge

Aplicação enterprise-grade para gerenciamento de propostas de crédito, desenvolvida com **Modern Java 17** e **Best Practices**.

## 🎯 Características Principais

- ✅ **Domain-Driven Design (DDD)** - Arquitetura focada em domínio de negócio  
- ✅ **Clean Architecture** - Separação clara de responsabilidades  
- ✅ **Spring Boot 3.2.4** - Framework moderno com Java 17  
- ✅ **Spring Data JPA + H2** - Acesso simplificado ao banco de dados  
- ✅ **Validação com Jakarta** - Validações automáticas via anotações  
- ✅ **MapStruct** - Mapeamento automático entre camadas  
- ✅ **Lombok** - Redução de boilerplate de código  
- ✅ **29 Testes Unitários** - Cobertura completa com JUnit 5 + Mockito  
- ✅ **SOLID Principles** - Código extensível e manutenível  

---

## 🛠 Stack Tecnológico

| Componente | Versão | Propósito |
|-----------|--------|----------|
| Java | 17 LTS | Linguagem principal |
| Spring Boot | 3.2.4 | Framework web |
| Maven | 3.8+ | Build automation |
| H2 Database | Latest | Banco em memória (dev) |
| JUnit 5 | Latest | Testing framework |
| Mockito | Latest | Mock objects |
| Lombok | 1.18.30 | Boilerplate reduction |
| MapStruct | 1.5.5 | Object mapping |
| Jakarta Validation | Latest | Bean validation |

---

## 🚀 Quick Start

### 1. Pré-requisitos
- Java 17+
- Maven 3.8+

### 2. Compilar
```bash
mvn clean compile
```

### 3. Executar Testes
```bash
mvn test
```

### 4. Iniciar Aplicação
```bash
mvn spring-boot:run
```

A aplicação estará em:
- **API**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/api/h2-console

---

## 📡 Exemplo de Uso

### Criar Proposta
```bash
curl -X POST http://localhost:8080/api/v1/propostas \
  -H "Content-Type: application/json" \
  -d '{"cpf":"12345678900","valor":1000.00}'
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PENDENTE"
}
```

---

## 📁 Estrutura do Projeto

```
src/main/java/com/modernbank/credit/
├── domain/                          # Camada de Domínio
│   ├── model/Proposta.java         # Entidade com regras de negócio
│   ├── repository/                  # Interfaces de persistência
│   ├── usecase/                     # Lógica de negócio
│   └── exception/                   # Exceções customizadas
└── infrastructure/                  # Camada de Infraestrutura
    ├── input/rest/                  # Controllers REST
    └── persistence/                 # Implementação de persistência
```

---

## ✅ Testes

### Executar Todos
```bash
mvn test
```

### Testes Inclusos
- **PropostaTest** (17 testes) - Validações de modelo
- **CriarPropostaUseCaseTest** (6 testes) - Lógica de negócio
- **PropostaControllerTest** (6 testes) - Endpoints REST

---

## 🎯 Padrões de Arquitetura

- **Domain-Driven Design** - Modelo de domínio rico com validações
- **Clean Architecture** - Camadas independentes de frameworks
- **Repository Pattern** - Abstração de acesso a dados
- **Mapper Pattern** - Conversão entre DTOs e entidades
- **SOLID Principles** - Princípios de design

---

## 📚 Documentação

- [REFACTORING_REPORT.md](REFACTORING_REPORT.md) - Detalhes da refatoração inicial
- [IMPLEMENTATION_REPORT.md](IMPLEMENTATION_REPORT.md) - Documentação das implementações

---

## 📝 Versão

**v1.0.0** - Status: ✅ Production Ready

Última atualização: 12 de Maio de 2026
