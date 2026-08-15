#!/usr/bin/env bash
# Script para compilar e executar o sistema sem precisar do IntelliJ.
# Pré-requisitos: JDK instalado e o banco de dados rodando (Docker ou MariaDB local).
#
# Como usar:
#   1) Suba o banco:        docker compose up -d
#   2) Rode o programa:     ./run.sh

set -e

# URLs e pastas usadas pelo script
JDBC_URL="https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.31/mysql-connector-j-8.0.31.jar"
LIB_DIR="lib"
DRIVER_JAR="$LIB_DIR/mysql-connector-j-8.0.31.jar"
OUT_DIR="target/classes"

# 1) Verifica se o Java está instalado
if ! command -v javac >/dev/null 2>&1 || ! command -v java >/dev/null 2>&1; then
    echo "Erro: JDK não encontrado. Instale o JDK (versão 26) e tente de novo."
    exit 1
fi

# 2) Baixa o driver MySQL (só na primeira vez)
if [ ! -f "$DRIVER_JAR" ]; then
    echo "Baixando o driver MySQL..."
    mkdir -p "$LIB_DIR"
    if command -v curl >/dev/null 2>&1; then
        curl -fL -o "$DRIVER_JAR" "$JDBC_URL"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$DRIVER_JAR" "$JDBC_URL"
    else
        echo "Erro: instale o curl (ou wget) para baixar o driver MySQL."
        exit 1
    fi
fi

# 3) Compila o projeto (todos os arquivos .java)
echo "Compilando o projeto..."
mkdir -p "$OUT_DIR"
javac -encoding UTF-8 -cp "$DRIVER_JAR" -d "$OUT_DIR" $(find src/main/java -name "*.java")

# 4) Executa o sistema
echo "Executando o sistema..."
java -cp "$OUT_DIR:$DRIVER_JAR" controle.Main
