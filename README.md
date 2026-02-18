# 📚 BookStore API

[![Java](https://img.shields.io/badge/Java-21-%23ED8B00.svg?logo=openjdk&logoColor=white)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-%236DB33F.svg?logo=springboot&logoColor=white)](#)
[![Postgres](https://img.shields.io/badge/Postgres-17-%23316192.svg?logo=postgresql&logoColor=white)](#)
[![Docker](https://img.shields.io/badge/Docker-Enabled-%232496ED.svg?logo=docker&logoColor=white)](#)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-GKE-%23326CE5.svg?logo=kubernetes&logoColor=white)](#)
[![CI/CD](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-2088FF?logo=github-actions&logoColor=white)](#)
[![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?logo=amazon-aws&logoColor=white)](#)
[![Google Cloud](https://img.shields.io/badge/GoogleCloud-%234285F4.svg?logo=google-cloud&logoColor=white)](#)

> # ❓ Sobre o projeto

O **BookStore API** é um sistema de back-end de alta performance desenvolvido para gerenciar o ecossistema de uma livraria moderna. Mais do que um simples CRUD, este projeto foi construído para demonstrar a aplicação prática de **Arquitetura de Microsserviços**, **DevOps** e **Design Patterns** avançados.

A aplicação segue os princípios do **RESTful**, atingindo o nível mais alto do Modelo de Maturidade de Richardson (HATEOAS), e está totalmente containerizada e preparada para escalar em ambientes de nuvem (AWS e Google Cloud) através de pipelines de CI/CD automatizados.

> # 🎯 Motivação e Aprendizado

Desenvolvi este projeto para consolidar e melhorar meus conhecimentos em engenharia de software, mas não sendo apenas mais um projeto sendo apenas CRUD, mas sim tendo implementações e tecnologias que o mercado de trabalho exige. As principais competências aplicadas incluem:

- **Clean Code & Design Patterns:** Uso de DTO, Builder, Strategy e injeção de dependência para um código limpo e testável.
- **Segurança Real:** Implementação de autenticação **Stateless com JWT** e Spring Security.
- **Integração Contínua (DevOps):** A aplicação não roda apenas localmente; ela possui um pipeline que testa, constrói a imagem Docker e faz o deploy automático na **AWS** e **Google Kubernetes Engine (GKE)**.
- **Qualidade de Software:** Testes automatizados cobrindo desde a unidade até a integração com banco de dados real (Testcontainers).

> # 🚀 Funcionalidades Principais

A API vai além do básico, oferecendo recursos avançados:

* 🔒 **Segurança:** Autenticação e Autorização via JWT (JSON Web Tokens).
* 📄 **Relatórios e Arquivos:**
    * Upload e Download de arquivos.
    * Exportação de dados para **Excel** e **CSV**.
    * Geração de relatórios PDF com **JasperReports**.
* 🌍 **Interoperabilidade:**
    * **Content Negotiation:** Suporta requisições/respostas em **JSON**, **XML** e **YAML**.
    * **CORS:** Configurado para permitir consumo por front-ends modernos.
* 🔄 **Versionamento:** Versionamento de API e Migrations de banco de dados com **Flyway**.
* 📚 **Documentação:** Swagger UI (OpenAPI) integrado para testes e documentação viva.

> # ☁️ Infraestrutura Multi-Cloud & DevOps

### 📄 Modelo conceitual
![Arquitetura Cloud](https://github.com/user-attachments/assets/43e4158a-0026-431a-bb88-c8e0b0643f98)

Um dos grandes diferenciais deste projeto é a sua capacidade de operar em múltiplos provedores de nuvem. A aplicação foi configurada e implantada com sucesso tanto na **Amazon Web Services (AWS)** quanto na **Google Cloud Platform (GCP)**, utilizando pipelines de CI/CD automatizados via **GitHub Actions**.

**Nota:** *Atualmente, os serviços de nuvem (AWS/GCP) foram interrompidos para evitar custos operacionais excessivos. No entanto, todo o código de infraestrutura (IaC) e pipelines de deploy continuam disponíveis no repositório para consulta e validação técnica.*

### 🔄 Fluxo de Continuous Deployment (CD)
A cada *push* na branch `main`, o GitHub Actions executa automaticamente os seguintes passos:
1.  **Testes:** Execução de suíte de testes unitários e de integração (JUnit/Testcontainers).
2.  **Build:** Compilação do projeto e geração da imagem Docker.
3.  **Registry:** Push da imagem para o repositório privado (Amazon ECR ou Google Artifact Registry).
4.  **Deploy:** Atualização sem downtime nos orquestradores de container.

### 🏛️ Arquitetura na Nuvem

| Provedor | Orquestração (Compute) | Banco de Dados | Container Registry |
| :--- | :--- | :--- | :--- |
| **Google Cloud (GCP)** | **Google Kubernetes Engine (GKE)** | Cloud SQL (PostgreSQL) | Artifact Registry |
| **AWS** | **Amazon ECS** (Fargate) | Amazon RDS (PostgreSQL) | Amazon ECR |
> # 📋 Tecnologias Utilizadas

### Core & Frameworks
- **Java 21** (LTS)
- **Spring Boot 3** (Web, Security, HATEOAS, Data JPA)
- **Maven** (Gerenciamento de dependências)

### Dados & Persistência
- **PostgreSQL 17** (Produção/Docker)
- **Flyway** (Migrations)

### Testes
- **JUnit 5** & **Mockito**
- **RestAssured** (Testes de API)
- **Testcontainers** (Testes de integração com containers reais)

### Ferramentas & Utils
- **Docker** & **Docker Compose**
- **JasperReports** & **Apache POI**
- **ModelMapper / Dozer**
- **Swagger / OpenAPI 3**

> # 💻 Aplicação Front-end (Web Client)

Para demonstrar a API em funcionamento, foi desenvolvida uma aplicação front-end básica utilizando **React** e **Vite**. Este projeto atua como o client da aplicação, consumindo os endpoints da BookStore API e ilustrando na prática a comunicação segura via JWT, controle de rotas e operações CRUD.

🔗 **[Clique aqui para acessar o repositório do Front-end](https://github.com/MuriloDias03/book-store-frontend)**

> # 👨🏻‍💻 Como executar o projeto

### Pré-requisitos
* Docker e Docker Compose instalados.

### Passo a passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/murilodias03/bookstore-api.git](https://github.com/murilodias03/bookstore-api.git)
    cd bookstore-api
    ```

2.  **Configure as Variáveis de Ambiente (Obrigatório ⚠️):**
    Para evitar que a aplicação feche sozinha (crash) ao tentar conectar no serviço de e-mail, crie um arquivo chamado `.env` na raiz do projeto.
    * Se não for testar o envio real de e-mails, use os valores fictícios abaixo:

    ```env
    EMAIL_USERNAME=fake@email.com
    EMAIL_PASSWORD=senhafake123
    ```
    *Nota: As credenciais do Banco de Dados já estão configuradas automaticamente no Docker.*


3.  **Suba o ambiente:**
    Execute o comando para construir a aplicação e subir os containers:
    ```bash
    docker compose up -d --build
    ```

4.  **🔐 Autenticação (Primeiro Passo):**
    A API é protegida via JWT. Antes de testar qualquer rota (exceto a documentação), você precisa obter um Token de Acesso.

    * **Usuário Padrão:** `leandro`
    * **Senha:** `admin123`

    **Como se autenticar:**
    1.  Faça uma requisição `POST` para `/auth/signin` com as credenciais acima.
    2.  Copie o `accessToken` retornado na resposta.
    3.  Use esse token no Header das próximas requisições (`Authorization: Bearer SEU_TOKEN`).


5.  **📖 Documentação Interativa (Swagger UI):**
    Acesse pelo navegador para visualizar os endpoints visualmente:
    `http://localhost:8080/swagger-ui/index.html`



6.  **🚀 Testes via Postman / Insomnia:**
    Você pode importar as rotas ou criar requisições manuais.
    * **URL Base:** `http://localhost:8080`
    * **Exemplo:** `GET http://localhost:8080/api/book/v1` (Lembre-se de adicionar o Token no header).

> # 🧙🏼‍♂️ Autor

**Murilo Cristovão Dias**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/murilo-cristovao-dias/)
