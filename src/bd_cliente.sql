-- Script de criação do banco de dados do sistema (cadastro de clientes).
-- Este script é usado para criar o banco do zero:
--  - manualmente:  mariadb < src/bd_cliente.sql
--  - pelo Docker:  é montado em /docker-entrypoint-initdb.d/ e roda sozinho
--    na primeira vez em que o container é criado.
--
-- Obs.: tudo usa "IF NOT EXISTS" para poder rodar mais de uma vez sem erro.

CREATE DATABASE IF NOT EXISTS clientes DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE clientes;

-- Tabela de clientes (o código é gerado sozinho pelo banco = AUTO_INCREMENT)
CREATE TABLE IF NOT EXISTS tbclientes (
    cod int NOT NULL AUTO_INCREMENT,
    nome varchar(255) NOT NULL,
    dt_nasc varchar(10) NOT NULL,
    telefone varchar(14) NOT NULL,
    email varchar(255) NOT NULL,
    PRIMARY KEY (cod)
);

-- Tabela de usuários do login (a senha é guardada como hash SHA-256)
CREATE TABLE IF NOT EXISTS tbusuario (
    id int NOT NULL AUTO_INCREMENT,
    usuario varchar(50) NOT NULL,
    senha varchar(64) NOT NULL,
    PRIMARY KEY (id)
);

-- Dados de exemplo
INSERT INTO tbclientes (cod, nome, dt_nasc, telefone, email) VALUES
(1, 'Antonio', '17/03/1939', '1111-1111', 'antonio@antonio.com.br'),
(2, 'Edna', '27/03/1969', '2222-2222', 'edna@edna.com.br'),
(3, 'Gustavo', '27/05/2000', '3333-3333', 'hustavo@gustavo.com.br'),
(4, 'aaaaaaa', '01/01/2001', '4444-4444', 'aaa@aaa.com.br'),
(5, 'bbbbb', '02/02/2002', '5555-5555', 'bbb@bbb.com.br');
