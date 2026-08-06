package controle;

import conexao.Conexao;

import javax.swing.JOptionPane; // para mostrar as mensagens de erro
import java.sql.*; // Connection, PreparedStatement, ResultSet, SQLException

// Classe responsável SOMENTE pelos comandos SQL na tabela tbusuario (acesso aos dados)
public class UsuarioDAO {

    // Recebe usuário e senha digitados na tela; devolve true se o login for válido
    public boolean autenticar(String usuario, String senha) {
        try {
            String senhaHash = Criptografia.hash(senha); // transforma a senha digitada em hash (o banco guarda o hash)

            String sql = "select * from tbusuario where usuario = ? and senha = ?"; // SELECT comparando usuário e senha-hash

            // try-with-resources fecha a conexão e o statement automaticamente
            try (Connection con = Conexao.getConexao(); // abre a conexão
                 PreparedStatement ps = con.prepareStatement(sql)) { // prepara o comando SQL
                ps.setString(1, usuario); // preenche o 1º "?" com o usuário digitado
                ps.setString(2, senhaHash); // preenche o 2º "?" com o hash da senha digitada

                try (ResultSet rs = ps.executeQuery()) { // executa o SELECT e guarda o resultado
                    return rs.next(); // true = encontrou 1 linha (login OK); false = não encontrou
                }
            }
        } catch (SQLException erro) { // se der erro no banco...
            JOptionPane.showMessageDialog(null, "Erro na autenticação: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...mostra o erro
            return false; // e considera o login inválido
        }
    }
}
