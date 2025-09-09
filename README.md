# Projeto de Processamento de Usuários com Spring Batch

Este projeto é um exemplo prático de um job ETL (Extract, Transform, Load) construído com Spring Batch. O objetivo é ler dados de usuários de um arquivo JSON, validar e enriquecer essas informações, e persistir os dados válidos em um banco de dados.

Este projeto foi desenvolvido durante um encontro do grupo de estudos da comunidade **Brazil JUG Community { Devs Java }**.

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3**
* **Spring Batch** - Para o processamento em lote.
* **Spring Data JPA** - Para a persistência de dados.
* **H2 Database** - Banco de dados em memória para fins de demonstração.
* **Maven** - Gerenciador de dependências.

## ▶️ Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    ```
2.  **Navegue até a pasta do projeto:**
    ```bash
    cd nome-do-projeto
    ```
3.  **Execute a aplicação com o Maven:**
    ```bash
    mvn spring-boot:run
    ```
    O job do Spring Batch será executado automaticamente na inicialização.

4.  **Verifique o resultado:**
    * Após a execução, acesse o console do banco H2 no seu navegador: `http://localhost:8080/h2-console`
    * Use a URL JDBC: `jdbc:h2:mem:testdb`
    * Execute a query `SELECT * FROM USUARIO;` para ver os usuários válidos que foram inseridos.
