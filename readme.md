# 🍽️ Sistema de Gestão de Restaurante

### Trabalho Semestral 2025 — Programação Orientada a Objetos (POO)

**Universidade Eduardo Mondlane (UEM)**

---

## 📌 Descrição do Projeto

Este projeto consiste no desenvolvimento de um sistema de gestão de restaurante utilizando **Java 25**, com interface gráfica em **JavaFX**, persistência de dados com **Hibernate** e base de dados **MySQL**.

O sistema permite a interação de diferentes tipos de utilizadores (anónimo, cliente autenticado, funcionário e gestor), cada um com funcionalidades específicas.

---

## 🛠️ Tecnologias Utilizadas

* ☕ Java 25
* 🖥️ JavaFX (Interface Gráfica)
* 🗄️ Hibernate (ORM)
* 🐬 MySQL (Base de Dados)

---

## 🚀 Funcionalidades

### 👤 Utilizador Anónimo

* Visualizar página inicial (Home)
* Consultar menu
* Aceder à página "Sobre Nós"
* Navegar por um menu interativo com imagens dos pratos

---

### 🔐 Utilizador Autenticado (Cliente)

* Fazer pedidos *takeaway*
* Reservar mesas

---

### 🧑‍🍳 Funcionário

* Registar pedidos
* Gerir mesas (ocupação, reservas, etc.)

---

### 📊 Gestor

* Visualizar relatórios financeiros

---

## 🗂️ Estrutura de Dados Inicial (Seed)

Na primeira execução do sistema, é necessário executar o seguinte ficheiro:

```
src/tmp.java
```

Este ficheiro é responsável por:

* Inserir pratos iniciais na base de dados
* Criar utilizadores e clientes de teste (*dummy data*)

⚠️ **Nota:** Este passo deve ser executado apenas na primeira utilização do sistema.

---

## 🖼️ Screenshots

![Inicio](https://raw.githubusercontent.com/NelsonGuiamba/TrabalhoSemestralPoo2025/master/screenshots/inicio.png)
![Login](https://raw.githubusercontent.com/NelsonGuiamba/TrabalhoSemestralPoo2025/master/screenshots/login.png)
![Pedido](https://raw.githubusercontent.com/NelsonGuiamba/TrabalhoSemestralPoo2025/master/screenshots/pedido.png)
![Reserva](https://raw.githubusercontent.com/NelsonGuiamba/TrabalhoSemestralPoo2025/master/screenshots/reserva.png)

---

## ⚙️ Como Executar o Projeto

1. Configurar a base de dados MySQL
2. Atualizar as credenciais no ficheiro de configuração do Hibernate
3. Executar o script inicial (`tmp.java`)
4. Iniciar a aplicação JavaFX

---

## 📌 Observações

* O sistema segue princípios de **Programação Orientada a Objetos (POO)**
* Utiliza padrão de persistência com Hibernate
* Interface gráfica desenvolvida com JavaFX

---

## 👨‍💻 Autor

Trabalho desenvolvido no âmbito académico da disciplina de POO — UEM (2025)

---
