package controle;

// Classe que representa UM registro da tabela tbclientes (só dados, sem lógica)
public class Cliente {
    private int cod;            // código (PK) do cliente
    private String nome;        // nome do cliente
    private String dtNasc;      // data de nascimento
    private String telefone;    // telefone
    private String email;       // e-mail

    public Cliente() {
    }

    public Cliente(int cod, String nome, String dtNasc, String telefone, String email) {
        this.cod = cod;
        this.nome = nome;
        this.dtNasc = dtNasc;
        this.telefone = telefone;
        this.email = email;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(String dtNasc) {
        this.dtNasc = dtNasc;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
