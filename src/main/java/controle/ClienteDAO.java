package controle;

import conexao.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Classe responsável SOMENTE pelos comandos SQL na tabela tbclientes (acesso aos dados)
public class ClienteDAO {

    // Retorna todos os clientes em ordem de código
    public List<Cliente> listarTodos() throws SQLException {
        return consultar("select * from tbclientes order by cod");
    }

    // Retorna os clientes cujo nome COMEÇA com o texto digitado
    public List<Cliente> buscarPorNome(String nome) throws SQLException {
        return consultar("select * from tbclientes where nome like '" + nome + "%' order by cod");
    }

    // Método privado: executa um SELECT e transforma cada linha em um objeto Cliente
    private List<Cliente> consultar(String sql) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        // try-with-resources fecha conexão/statement/resultSet automaticamente
        try (Connection con = Conexao.getConexao();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { // para cada linha retornada...
                clientes.add(new Cliente( // ...cria um objeto Cliente com os dados
                        rs.getInt("cod"),
                        rs.getString("nome"),
                        rs.getString("dt_nasc"),
                        rs.getString("telefone"),
                        rs.getString("email")));
            }
        }
        return clientes;
    }

    // Insere um novo cliente (INSERT). "?" são preenchidos via setString/setInt
    public void inserir(Cliente cliente) throws SQLException {
        String sql = "insert into tbclientes (nome, telefone, email, dt_nasc) values (?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDtNasc());
            ps.executeUpdate();
        }
    }

    // Atualiza um cliente existente pelo código (UPDATE)
    public void alterar(Cliente cliente) throws SQLException {
        String sql = "update tbclientes set nome=?, telefone=?, email=?, dt_nasc=? where cod=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDtNasc());
            ps.setInt(5, cliente.getCod());
            ps.executeUpdate();
        }
    }

    // Exclui um cliente pelo código (DELETE)
    public void excluir(int cod) throws SQLException {
        String sql = "delete from tbclientes where cod=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cod);
            ps.executeUpdate();
        }
    }
}
