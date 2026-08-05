package conexao;

import javax.swing.JOptionPane;
import java.sql.*;

public class Conexao {
    final private String driver = "com.mysql.cj.jdbc.Driver"; // definição do driver MySql para acesso aos dados
    final private String url = "jdbc:mysql://localhost/clientes"; // acesso ao bd clientes no servidor - easyphp
    final private String usuario = "root"; // usuário do MySql - easyphp
    final private String senha = ""; // senha do MySql
    private Connection conexao; // variável que armazenará a conexão aberta
    public Statement statement; // variável para execução dos comandos SQL dentro do ambiente Java
    public ResultSet resultSet; // variável que aramzenará o resultado da execução de um comando SQL

    public boolean conecta() {
        boolean result = true;
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, usuario, senha);
            JOptionPane.showMessageDialog(null, "Conexão estabelecida", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Fonte de dados não localizada" + driver, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            result = false;
        } catch (SQLException Fonte) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco!!" + Fonte, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            result = false;
        }
        return result;
    }

    public void desconecta(){
        try {
            conexao.close();
            JOptionPane.showMessageDialog(null,"Conexão com o banco fechada","Mensagem do Programa",JOptionPane.INFORMATION_MESSAGE);
        }catch (SQLException fecha){

        }
    }

    public void executaSQL(String sql){
        try{
            statement = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);
        }catch (SQLException excecao){
            JOptionPane.showMessageDialog(null,"Erro no comando SQL! \n Erro: "+excecao,"Mensagem do Programa",JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
