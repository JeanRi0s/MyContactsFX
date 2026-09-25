# MyContactsFX

Evolução do MyContacts (agenda de contatos em CLI): agora com **interface gráfica em JavaFX**, **persistência em banco de dados SQLite** e **validação de campos em tempo real**.

## O que mudou em relação ao MyContacts original

| | MyContacts (CLI) | MyContactsFX |
|---|---|---|
| Interface | Linha de comando | Interface gráfica (JavaFX) |
| Persistência | Em memória (perdida ao fechar) | Banco de dados SQLite (`mycontacts.db`) |
| Edição de contato | Não disponível | Formulário de edição pré-preenchido |
| Busca | Por nome exato | Filtro dinâmico (em tempo real, enquanto digita) |
| Validação | Só de e-mail | Nome, telefone, e-mail e empresa, com mensagens de erro por campo |
| Build | Sem gerenciador de dependências | Maven |

Estrutura de pacotes, modelos (`Contato` / `ContatoComercial`), interface `Buscavel` e exceção `ContatoNaoEncontradoException` foram mantidos do projeto original.

## Funcionalidades

- Cadastrar contato **Pessoal** ou **Comercial**, escolhido por rádio button no formulário
- Listar todos os contatos em uma tabela (ID, Nome, Telefone, E-mail, Empresa, Tipo)
- Buscar contatos por nome, com filtragem instantânea na tabela
- Editar contato existente, com formulário pré-preenchido
- Remover contato, com diálogo de confirmação
- Validação de nome, telefone, e-mail e empresa antes de salvar, com mensagem de erro exibida abaixo de cada campo
- Persistência automática em SQLite — o banco é criado na primeira execução, sem necessidade de instalação

## Estrutura do projeto

```
MyContactsFX/
├── pom.xml
└── src/mycontacts/
    ├── app/
    │   └── Main.java                  # Ponto de entrada (Application.launch)
    ├── controller/
    │   ├── Agenda.java                # Camada de negócio, delega ao DAO
    │   └── Buscavel.java              # Interface de busca (mantida do original)
    ├── db/
    │   ├── ConexaoDB.java             # Conexão JDBC e criação da tabela
    │   └── ContatoDAO.java            # CRUD (inserir, listar, buscar, atualizar, remover)
    ├── exceptions/
    │   └── ContatoNaoEncontradoException.java
    ├── model/
    │   ├── Contato.java                # Contato pessoal
    │   └── ContatoComercial.java       # Contato comercial (herda de Contato)
    ├── utils/
    │   ├── Validador.java              # Validação de nome, telefone, e-mail, empresa
    │   └── ValidadorEmail.java         # Regex de e-mail (mantido do original)
    └── view/
        ├── MainView.java               # Tela principal (busca, tabela, botão novo contato)
        ├── FormularioView.java         # Formulário de cadastro/edição, com validação
        ├── TabelaContatos.java         # Configuração das colunas e ações da tabela
        └── Alertas.java                # Diálogos de informação e erro
```

## Requisitos

- Java 21+
- Maven 3.8+

## Como executar

```bash
# Entrar na pasta do projeto
cd MyContactsFX

# Executar diretamente com Maven
mvn javafx:run

# OU gerar o JAR e rodar
mvn package
java -jar target/MyContactsFX-1.0.jar
```

## Banco de dados

O arquivo `mycontacts.db` (SQLite) é criado automaticamente na pasta onde a aplicação é executada — não é necessária nenhuma instalação adicional.

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

## Tecnologias

- Java 21
- JavaFX 21 (`javafx-controls`, `javafx-fxml`)
- SQLite JDBC (`org.xerial:sqlite-jdbc`)
- Maven (`javafx-maven-plugin`, `maven-compiler-plugin`)

## Possíveis melhorias futuras

- Ordenação e paginação da tabela
- Exportação de contatos (CSV/PDF)
- Testes unitários para `Validador` e `ContatoDAO`
- Empacotamento como executável nativo (jpackage)

## Autor

Desenvolvido por Jean — Engenharia de Mecatrônica, Instituto Federal do Ceará.
