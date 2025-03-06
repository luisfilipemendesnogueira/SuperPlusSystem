# SuperPlus - Sistema de Gerenciamento de Supermercado 🛒

Sistema de gerenciamento de pontuação de acordo com as compras dos clientes de uma rede de supermercados fictício chamado “SuperPlus”

**⚠️ Este projeto em Java conecta-se a um banco de dados MySQL via JDBC. Para utilizá-lo corretamente, siga as instruções abaixo.**

## 📌 Pré-requisitos
- MySQL
- Eclipse (ou outra IDE compatível)

## 🛠️ Configuração do Banco de Dados
1. **Crie o banco de dados** copiando o conteúdo do arquivo **script.sql** e colando no MySQL.

## ⚙️ Configuração do Projeto
Você precisa criar manualmente um arquivo chamado `config.properties` para armazenar as credenciais do seu banco de dados.
### 📄 Criando o arquivo `config.properties`
1. No Eclipse (ou qualquer editor de texto), navegue até a pasta `src/` do projeto.
2. Crie um novo arquivo chamado `config.properties`. 
3. Abra este arquivo e adicione as seguintes linhas, substituindo **seu_usuario** e **sua_senha** com suas credenciais do MySQL:
   ```properties
   db.url=jdbc:mysql://localhost:3306/superplus
   db.user=seu_usuario
   db.password=sua_senha
4. Salve o arquivo.

## 🚀 Rodando o Projeto
1. Abra o projeto no Eclipse ou em outra IDE compatível.
2. Execute a classe **Main**.
