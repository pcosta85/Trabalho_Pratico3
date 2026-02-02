# 📘 Relatório Final  
## Sistema de Gestão de Restaurante

## 🎯 Objetivos

O objetivo principal deste trabalho foi desenvolver um **sistema de gestão de restaurante em Java**, com interface gráfica, que permita:

- Gerir clientes;
- Registar, alterar e eliminar pedidos;
- Consultar pedidos associados a um cliente;
- Gerar faturas em ficheiro de texto;
- Garantir persistência dos dados através de ficheiros CSV;
- Aplicar conceitos de **Programação Orientada a Objetos**, **estruturas de dados** e **interfaces gráficas**.

O sistema pretende simular o funcionamento básico de um restaurante real, fornecendo uma solução simples, organizada e funcional.

## 📖 Introdução

No contexto da disciplina de **Programação Orientada a Objetos**, este projeto explora conceitos fundamentais da linguagem **Java**, integrando lógica de negócio, persistência de dados e interface gráfica.

Foram utilizadas **estruturas de dados dinâmicas**, nomeadamente `ArrayList`, para armazenar clientes e pedidos em memória, permitindo operações de inserção, remoção e alteração de forma eficiente.

A aplicação faz uso de **ficheiros CSV** para garantir que os dados persistem mesmo após o encerramento do programa, evitando a perda de informação.  
A interface gráfica foi desenvolvida com **Java Swing**, proporcionando uma interação intuitiva ao utilizador através de menus laterais e formulários.

Este tipo de sistema pode ser aplicado em pequenos restaurantes, cafés ou ambientes académicos como exercício prático de modelação e implementação de sistemas de informação.

## 🧠 Algoritmos

### 🔹 Gestão de Clientes

- **Inserção de cliente**  
  - Recebe o nome do cliente;
  - Cria um objeto `Cliente`;
  - Armazena em memória (`ArrayList`);
  - Guarda no ficheiro `clientes.csv`.

- **Pesquisa de cliente**  
  - Percorre a lista de clientes;
  - Compara nomes ignorando maiúsculas/minúsculas;
  - Retorna o cliente encontrado ou `null`.

- **Alteração de cliente**  
  - Valida existência do cliente;
  - Atualiza o nome;
  - Reescreve os ficheiros CSV (`clientes.csv` e `pedidos.csv`).

- **Eliminação de cliente**  
  - Remove o cliente da lista;
  - Remove automaticamente os seus pedidos;
  - Atualiza os ficheiros CSV.

### 🔹 Gestão de Pedidos

- **Registo de pedido**  
  - Associa um pedido a um cliente existente;
  - Cria um objeto `Pedido` com produto, preço e quantidade;
  - Guarda no ficheiro `pedidos.csv`.

- **Alteração de pedido**  
  - Identifica o pedido pelo índice;
  - Atualiza produto, preço e quantidade;
  - Reescreve o ficheiro `pedidos.csv`.

- **Eliminação de pedido**  
  - Remove o pedido pelo índice;
  - Atualiza o ficheiro `pedidos.csv`.

### 🔹 Geração de Fatura

- Percorre os pedidos do cliente;
- Calcula o total por item e o total geral;
- Gera um ficheiro `.pdf` com a fatura detalhada;
- Guarda a fatura numa pasta própria (`faturas`).

## 🖥️ Interface

A interface gráfica foi desenvolvida utilizando **Java Swing**, com as seguintes características:

- **Barra lateral fixa** com menu de navegação;
- **Painel central dinâmico** usando `CardLayout`;
- Organização clara das funcionalidades.

### 🔸 Funcionalidades

- Inserir Cliente  
- Registar Pedido  
- Consultar Cliente (com tabela de pedidos)  
- Gerar Fatura  
- Alterar Cliente  
- Eliminar Cliente  
- Alterar Pedido  
- Eliminar Pedido  

### 🔸 Usabilidade

- Mensagens de aviso, erro e sucesso (`JOptionPane`);
- Confirmação antes de eliminar dados;
- Validação de campos para evitar dados inválidos;
- Interface simples e intuitiva.

## 📊 Resultados e Conclusões

### ✔️ Resultados Obtidos

- O sistema cumpre todos os objetivos propostos;
- A interface gráfica facilita a interação do utilizador;
- Os dados são corretamente persistidos em ficheiros CSV;
- As operações de CRUD funcionam corretamente;
- A geração de faturas apresenta valores corretos e formatados.

### 🧾 Conclusões

A realização deste trabalho permitiu consolidar conhecimentos de:

- Programação Orientada a Objetos;
- Estruturas de dados dinâmicas (`ArrayList`);
- Manipulação de ficheiros em Java (leitura/escrita CSV);
- Desenvolvimento de interfaces gráficas com Swing;
- Separação entre lógica de negócio e interface.

O projeto demonstra uma abordagem estruturada e modular, podendo ser expandido no futuro para incluir uma base de dados, autenticação de utilizadores e geração de relatórios em PDF.

## 🛠️ Tecnologias Utilizadas

- Java SE  
- Java Swing  
- Estruturas de dados (`ArrayList`)  
- Ficheiros CSV  
- IDE: VS Code / IntelliJ / Eclipse  
