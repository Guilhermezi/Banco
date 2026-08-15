package controle;

// Classe que representa UM registro da tabela tbusuario (só dados, sem lógica)
public class Usuario {
    private int id; // código do usuário (PK)
    private String usuario; // nome de login
    private String senha; // senha (armazenada como hash)

    public Usuario() { // construtor vazio (usado quando criamos e preenchemos depois)
    }

    public Usuario(int id, String usuario, String senha) { // construtor completo
        this.id = id; // guarda o id recebido no atributo id
        this.usuario = usuario; // guarda o usuário recebido
        this.senha = senha; // guarda a senha recebida
    }

    public int getId() {
        return id; // devolve o id
    }

    public void setId(int id) {
        this.id = id; // define o id
    }

    public String getUsuario() {
        return usuario; // devolve o nome de login
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario; // define o nome de login
    }

    public String getSenha() {
        return senha; // devolve a senha
    }

    public void setSenha(String senha) {
        this.senha = senha; // define a senha
    }
}