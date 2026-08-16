package conexao;

import java.sql.*;

// Classe responsável SOMENTE por fornecer a conexão com o banco de dados
public class Conexao {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // driver do MySQL 8
    private static final String URL = "jdbc:mysql://localhost/clientes"; // endereço do banco clientes
    private static final String USUARIO ="banco"; // usuário do MySQL
    private static final String SENHA = "banco123"; // senha do MySQL

    // Devolve uma conexão aberta. Quem chama (o DAO) é responsável por fechá-la.
    public static Connection getConexao() throws SQLException {
        try {
            Class.forName(DRIVER); // carrega o driver do MySQL na memória
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do MySQL não encontrado", e);
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA); // abre a conexão
    }
}
