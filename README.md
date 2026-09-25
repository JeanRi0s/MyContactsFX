# MyContacts FX

Agenda de contatos com **JavaFX** + **JDBC (SQLite)** + **Validação**.

## Pré-requisitos
- Java 21+
- Maven 3.8+

## Como executar

```bash
# 1. Entrar na pasta do projeto
cd MyContactsFX

# 2. Executar diretamente com Maven
mvn javafx:run

# OU gerar o JAR e rodar
mvn package
java -jar target/MyContactsFX-1.0.jar
```

## Estrutura do projeto

```
MyContactsFX/
├── pom.xml
└── src/mycontacts/
    ├── app/
    │   └── Main.java                  ← ponto de entrada
    ├── controller/
    │   ├── Agenda.java                ← lógica de negócio
    │   └── Buscavel.java              ← interface (mantida do original)
    ├── db/
    │   ├── ConexaoDB.java             ← conexão JDBC / SQLite
    │   └── ContatoDAO.java            ← CRUD no banco
    ├── exceptions/
    │   └── ContatoNaoEncontradoException.java
    ├── model/
    │   ├── Contato.java               ← contato pessoal
    │   └── ContatoComercial.java      ← contato comercial (herda Contato)
    ├── utils/
    │   ├── Validador.java             ← validações (nome, telefone, email, empresa)
    │   └── ValidadorEmail.java        ← regex de e-mail (mantido do original)
    └── view/
        ├── MainView.java              ← tela principal
        ├── FormularioView.java        ← formulário de cadastro/edição
        ├── TabelaContatos.java        ← componente da tabela
        └── Alertas.java               ← utilitário de diálogos
```

## Funcionalidades

| Funcionalidade         | Descrição                                          |
|------------------------|----------------------------------------------------|
| Adicionar contato      | Pessoal ou Comercial, com validação em tempo real  |
| Listar contatos        | Tabela com todas as informações                    |
| Buscar por nome        | Filtragem dinâmica enquanto digita                 |
| Editar contato         | Formulário pré-preenchido                          |
| Remover contato        | Com confirmação antes de excluir                   |
| Persistência           | Banco SQLite criado automaticamente (mycontacts.db)|

## Banco de dados

O banco `mycontacts.db` é criado automaticamente na pasta onde o JAR é executado.
Nenhuma instalação adicional é necessária (SQLite é embutido).

### Esquema da tabela

```sql
CREATE TABLE contatos (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     TEXT    NOT NULL,
    telefone TEXT    NOT NULL,
    email    TEXT,
    tipo     TEXT    NOT NULL DEFAULT 'Pessoal',
    empresa  TEXT
);
```
