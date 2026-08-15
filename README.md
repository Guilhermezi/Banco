# Banco — Cadastro de Clientes

Sistema desktop em Java Swing com login e cadastro de clientes, usando um banco MariaDB/MySQL.

---

## O que você precisa instalar

| Ferramenta | Por quê | Onde baixar |
|---|---|---|
| **JDK 26** | Compilar e rodar o Java | https://adoptium.net/ |
| **Docker** (opcional) | Subir o banco sem instalar nada | https://www.docker.com/products/docker-desktop/ |
| **MariaDB ou MySQL** (opcional) | Alternativa ao Docker | https://mariadb.org/ |
| **IntelliJ IDEA** (opcional) | Editar o código | https://www.jetbrains.com/idea/ |

> Você só precisa de **uma** das duas opções de banco: **Docker** (recomendado) **ou** MariaDB/MySQL instalado.

---

## Opção A — Banco com Docker (recomendado)

Rode na pasta do projeto:

```bash
docker compose up -d
```

Na primeira vez ele baixa a imagem do MariaDB e cria o banco `clientes` com as tabelas
`tbclientes` e `tbusuario`, além de inserir 5 clientes de exemplo.

> Se der erro de **porta 3306 já em uso**, é porque já existe um MariaDB/MySQL rodando.
> Pare ele (`sudo systemctl stop mariadb`) ou remova o `ports` no `docker-compose.yml`.

---

## Opção B — Banco local (MariaDB/MySQL instalado)

Sem Docker, instale o MariaDB e depois crie o banco com:

```bash
mariadb < src/bd_cliente.sql
```

No **Arch Linux** especificamente:

```bash
sudo pacman -S mariadb
sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
sudo systemctl enable --now mariadb
```

> Em alguns sistemas o root do MariaDB usa autenticação por socket e o programa (que conecta
> via TCP com senha vazia) não consegue logar. Nesse caso libere com:
>
> ```bash
> sudo mariadb -e "ALTER USER 'root'@'localhost' IDENTIFIED BY ''; FLUSH PRIVILEGES;"
> ```

---

## Como rodar o programa

### Jeito 1 — Script `run.sh` (sem IntelliJ)

Com o banco já rodando (Opção A ou B):

```bash
./run.sh
```

O script:
1. Baixa o driver MySQL na primeira execução (salva em `lib/`);
2. Compila todos os arquivos Java;
3. Abre a tela de Login.

> No Linux, se der **permissão negada**: `chmod +x run.sh`

### Jeito 2 — IntelliJ IDEA

1. Abra a pasta do projeto no IntelliJ (o `pom.xml` configura tudo);
2. Aguarde o Maven baixar as dependências;
3. Clique com o botão direito em `src/main/java/controle/Main.java` → **Run 'Main'**.

---

## Usando o sistema

1. **Login** — use um usuário cadastrado na tabela `tbusuario` (ou cadastre um novo pelo botão **Cadastrar**).
2. **Cadastro de Clientes** — tela com os botões:
   - `Novo` — limpa os campos;
   - `Gravar` — insere um registro novo (o código é gerado automaticamente pelo banco);
   - `Alterar` — atualiza o registro selecionado;
   - `Excluir` — apaga o registro atual;
   - `|<` `<` `>` `>|` — navegação entre registros;
   - `Pesquisar` — busca por nome.

> Os campos de **data** (`dd/mm/aaaa`) e **telefone** (`(xx) xxxx-xxxx`) têm máscara de digitação,
> e não é possível gravar com campos vazios.

---

## Estrutura do projeto

```
Banco/
├── docker-compose.yml          # Sobe o MariaDB (banco + dados de exemplo)
├── run.sh                      # Compila e roda o app sem IntelliJ
├── pom.xml                     # Configuração Maven (JDK 26 + driver MySQL)
├── src/
│   ├── bd_cliente.sql          # Script que cria o banco e as tabelas
│   └── main/java/
│       ├── conexao/Conexao.java        # Conexão com o banco
│       └── controle/
│           ├── Main.java               # Ponto de entrada (abre o Login)
│           ├── Login.java              # Tela de login
│           ├── CadastroUsuario.java    # Cadastro de usuário do login
│           ├── FrmTela.java            # Tela de cadastro de clientes
│           ├── Cliente.java            # Modelo "Cliente"
│           ├── Usuario.java            # Modelo "Usuario"
│           ├── ClienteDAO.java         # SQL da tabela tbclientes
│           ├── UsuarioDAO.java         # SQL da tabela tbusuario
│           └── Criptografia.java       # Hash SHA-256 da senha
```

---

## Problemas comuns

**A janela não aparece (Linux + Hyprland/Wayland)**
O app é uma janela X11 (Xwayland) e pode abrir fora da tela. Adicione esta regra em
`~/.config/hypr/hyprland/rules.lua` e rode `hyprctl reload`:

```lua
hl.window_rule({ match = { class = "controle-Main" }, center = true })
```

**"Usuário ou senha inválidos"**
Nenhum usuário foi cadastrado ainda. Clique em **Cadastrar** no login e crie um.

**Erro de porta 3306**
Já existe outro banco usando a porta. Pare ele ou mude a porta no `docker-compose.yml`.
