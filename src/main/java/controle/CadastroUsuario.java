package controle;

import javax.swing.*; // componentes gráficos (JFrame, JTextField, JPasswordField, etc.)
import java.sql.*; // para capturar os erros do banco (SQLException)

// A tela de cadastro de usuário. Responsabilidade: receber usuário, senha e confirmação
// e gravar um novo usuário no banco via UsuarioDAO.
public class CadastroUsuario extends javax.swing.JFrame {
    private UsuarioDAO dao; // acesso aos dados de usuário (inserir novo usuário)

    private JTextField txtUsuario;       // caixa de texto do usuário
    private JPasswordField txtSenha;     // caixa de texto da senha (mostra pontos, não o texto)
    private JPasswordField txtConfirma;  // caixa de texto da confirmação da senha
    private JButton btnCadastrar;        // botão para gravar o novo usuário
    private JButton btnVoltar;           // botão para voltar ao login

    // Construtor: roda automaticamente quando criamos "new CadastroUsuario()"
    public CadastroUsuario() {
        initComponents(); // monta a tela (cria e posiciona todos os componentes)
        dao = new UsuarioDAO(); // cria o objeto que acessa o banco de usuários
    }

    // Monta a janela: cria cada componente, define posição/tamanho e liga os eventos
    private void initComponents() {
        setTitle("Cadastro de Usuário"); // texto da barra de título da janela
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // fechar a janela só fecha esta tela
        setLayout(null); // posicionamento manual (usamos setBounds para cada componente)

        JLabel lblTitulo = new JLabel("Cadastro de Usuário:"); // rótulo do título
        lblTitulo.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18)); // fonte: negrito, tamanho 18
        JLabel lblUsuario = new JLabel("Usuário:"); // rótulo "Usuário:"
        JLabel lblSenha = new JLabel("Senha:"); // rótulo "Senha:"
        JLabel lblConfirma = new JLabel("Confirmar Senha:"); // rótulo "Confirmar Senha:"

        txtUsuario = new JTextField(); // cria a caixa de texto do usuário
        txtSenha = new JPasswordField(); // cria a caixa de senha
        txtConfirma = new JPasswordField(); // cria a caixa de confirmação da senha
        btnCadastrar = new JButton("Cadastrar"); // cria o botão Cadastrar
        btnVoltar = new JButton("Voltar"); // cria o botão Voltar

        // setBounds(x, y, largura, altura) define posição e tamanho de cada componente
        lblTitulo.setBounds(20, 10, 200, 20);
        lblUsuario.setBounds(20, 50, 80, 20);
        txtUsuario.setBounds(20, 70, 180, 25);
        lblSenha.setBounds(20, 105, 80, 20);
        txtSenha.setBounds(20, 125, 180, 25);
        lblConfirma.setBounds(20, 160, 120, 20);
        txtConfirma.setBounds(20, 180, 180, 25);
        btnCadastrar.setBounds(20, 225, 85, 30);
        btnVoltar.setBounds(115, 225, 85, 30);

        // --- add(...) coloca cada componente na janela. Sem isto nada aparece! ---
        add(lblTitulo);
        add(lblUsuario);
        add(lblSenha);
        add(lblConfirma);
        add(txtUsuario);
        add(txtSenha);
        add(txtConfirma);
        add(btnCadastrar);
        add(btnVoltar);

        // --- Liga os componentes aos métodos de evento ---

        // Botão Cadastrar: quando for clicado, chama o método que grava o usuário
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt); // tenta cadastrar o usuário
            }
        });
        // Botão Voltar: quando for clicado, chama o método que fecha a tela
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt); // volta para o login
            }
        });
        // Pressionar ENTER dentro do campo da confirmação também dispara o cadastro
        txtConfirma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) { // se a tecla foi ENTER...
                    btnCadastrarActionPerformed(null); // ...faz o cadastro
                }
            }
        });

        setSize(240, 300); // tamanho da janela (largura, altura)
        setLocationRelativeTo(null); // centraliza a janela na tela
        setResizable(false); // não deixa o usuário redimensionar a janela
    }

    // Botão "Cadastrar": valida os campos e grava o novo usuário no banco
    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario = txtUsuario.getText().trim(); // lê o usuário digitado (sem espaços nas pontas)
        String senha = new String(txtSenha.getPassword()); // lê a senha (getPassword devolve char[])
        String confirma = new String(txtConfirma.getPassword()); // lê a confirmação da senha

        if (usuario.isEmpty() || senha.isEmpty() || confirma.isEmpty()) { // se algum campo ficou em branco...
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!senha.equals(confirma)) { // se a senha não bate com a confirmação...
            JOptionPane.showMessageDialog(this, "As senhas não conferem!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            if (dao.existeUsuario(usuario)) { // se o nome de usuário já existe no banco...
                JOptionPane.showMessageDialog(this, "Este usuário já está cadastrado!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Usuario novo = new Usuario(); // cria um objeto Usuario com os dados digitados
            novo.setUsuario(usuario); // nome de login
            novo.setSenha(senha); // senha (o DAO grava como hash)

            dao.inserir(novo); // manda o DAO fazer o INSERT
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // avisa que deu certo
            dispose(); // fecha a tela de cadastro (volta ao login)
        } catch (SQLException erro) { // se der erro no banco...
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...mostra o erro
        }
    }

    // Botão "Voltar": fecha a tela de cadastro sem gravar nada
    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose(); // fecha a tela de cadastro (volta ao login)
    }
}
