package controle;

import javax.swing.*; // componentes gráficos (JFrame, JTextField, JPasswordField, etc.)
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// A tela de login. Responsabilidade: receber usuário e senha e autenticar via UsuarioDAO.
public class Login extends JFrame {
    private UsuarioDAO dao; // acesso aos dados de usuário (autenticação)

    private JTextField txtUsuario;   // caixa de texto do usuário
    private JPasswordField txtSenha; // caixa de texto da senha (mostra pontos, não o texto)
    private JButton btnEntrar;       // botão para entrar no sistema
    private JButton btnSair;         // botão para sair do programa
    private JButton btnCadastrar;    // botão para abrir a tela de cadastro de usuário

    // Construtor: roda automaticamente quando criamos "new Login()"
    public Login() {
        initComponents(); // monta a tela (cria e posiciona todos os componentes)
        dao = new UsuarioDAO(); // cria o objeto que acessa o banco de usuários
    }

    // Monta a janela: cria cada componente, define posição/tamanho e liga os eventos
    private void initComponents() {
        setTitle("Login"); // texto da barra de título da janela
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // fechar a janela encerra o programa
        setLayout(null); // posicionamento manual (usamos setBounds para cada componente)

        JLabel lblTitulo = new JLabel("Login:"); // rótulo do título
        lblTitulo.setFont(new java.awt.Font("SansSerif", Font.BOLD, 18)); // fonte: negrito, tamanho 18
        JLabel lblUsuario = new JLabel("Usuário:"); // rótulo "Usuário:"
        JLabel lblSenha = new JLabel("Senha:"); // rótulo "Senha:"

        txtUsuario = new JTextField(); // cria a caixa de texto do usuário
        txtSenha = new JPasswordField(); // cria a caixa de senha
        btnEntrar = new JButton("Entrar"); // cria o botão Entrar
        btnSair = new JButton("Sair"); // cria o botão Sair
        btnCadastrar = new JButton("Cadastrar"); // cria o botão Cadastrar

        // setBounds(x, y, largura, altura) define posição e tamanho de cada componente
        lblTitulo.setBounds(20, 10, 120, 20);
        lblUsuario.setBounds(20, 50, 80, 20);
        txtUsuario.setBounds(20, 70, 180, 25);
        lblSenha.setBounds(20, 105, 80, 20);
        txtSenha.setBounds(20, 125, 180, 25);
        btnEntrar.setBounds(20, 170, 85, 30);
        btnSair.setBounds(115, 170, 85, 30);
        btnCadastrar.setBounds(20, 205, 180, 30);

        // --- add(...) coloca cada componente na janela. Sem isto nada aparece! ---
        add(lblTitulo);
        add(lblUsuario);
        add(lblSenha);
        add(txtUsuario);
        add(txtSenha);
        add(btnEntrar);
        add(btnSair);
        add(btnCadastrar);

        // --- Liga os componentes aos métodos de evento ---

        // Botão Entrar: quando for clicado, chama o método de autenticação
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnEntrarActionPerformed(evt); // tenta fazer o login
            }
        });
        // Botão Sair: quando for clicado, chama o método que encerra o programa
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSairActionPerformed(evt); // sai do sistema
            }
        });
        // Botão Cadastrar: quando for clicado, chama o método que abre a tela de cadastro
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarActionPerformed(evt); // abre a tela de cadastro de usuário
            }
        });
        // Pressionar ENTER dentro do campo da senha também dispara o login
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) { // se a tecla foi ENTER...
                    btnEntrarActionPerformed(null); // ...faz o login
                }
            }
        });

        setSize(240, 285); // tamanho da janela (largura, altura)
        setLocationRelativeTo(null); // centraliza a janela na tela
        setResizable(false); // não deixa o usuário redimensionar a janela
    }

    // Botão "Entrar": valida o usuário e a senha no banco
    private void btnEntrarActionPerformed(ActionEvent evt) {
        String usuario = txtUsuario.getText(); // lê o usuário digitado
        String senha = new String(txtSenha.getPassword()); // lê a senha (getPassword devolve char[])

        if (dao.autenticar(usuario, senha)) { // se a autenticação deu certo...
            FrmTela tela = new FrmTela(); // ...abre a tela de cadastro de clientes
            tela.setVisible(true); // mostra a tela
            dispose(); // fecha a janela de login
        } else { // se usuário ou senha estiverem errados...
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...avisa
        }
    }

    // Botão "Sair": encerra o programa
    private void btnSairActionPerformed(ActionEvent evt) {
        System.exit(0); // encerra a aplicação
    }

    // Botão "Cadastrar": abre a tela de cadastro de usuário
    private void btnCadastrarActionPerformed(ActionEvent evt) {
        CadastroUsuario cadastro = new CadastroUsuario(); // cria a tela de cadastro
        cadastro.setVisible(true); // mostra a tela de cadastro
    }
}