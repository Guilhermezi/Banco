package controle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // modelo de dados da tabela (linhas/colunas)
import javax.swing.text.MaskFormatter; // máscara de digitação dos campos (data, telefone)
import java.text.ParseException; // erro se a máscara estiver errada
import java.sql.*; // para capturar erros do banco
import java.util.*; // para a lista de clientes carregada pelo DAO

// A tela é uma janela (JFrame). "extends" faz esta classe herdar tudo do JFrame.
// Responsabilidade desta classe: SOMENTE a interface gráfica e chamar os métodos do DAO.
public class FrmTela extends javax.swing.JFrame {
    private ClienteDAO dao; // acesso aos dados (listar, inserir, alterar, excluir)
    private List<Cliente> clientes; // clientes carregados (usada na navegação e na grid)
    private int indiceAtual; // posição atual na lista (qual registro está sendo mostrado)
    private int totalRegistros = 0; // total de registros carregados
    private int codAtual = 0; // código do registro atual (auto-increment; não aparece na tela)

    // --- Campos (variáveis) da tela, criados no initComponents() ---
    private JTextField txtNome;            // caixa de texto do nome
    private JFormattedTextField txtDtNasc; // caixa de texto da data (máscara ##/##/####)
    private JFormattedTextField txtTelefone; // caixa de texto do telefone (máscara (##) ####-####)
    private JTextField txtEmail;           // caixa de texto do e-mail
    private JTextField txtPesquisa;        // caixa de texto da pesquisa por nome

    private JTable tblClientes;       // tabela (grid) que mostra os registros
    private DefaultTableModel modelo; // "conteúdo" da tabela: colunas e linhas

    private JButton btnPrimeiro;      // botão ir ao primeiro registro
    private JButton btnAnterior;      // botão voltar um registro
    private JButton btnProximo;       // botão avançar um registro
    private JButton btnUltimo;        // botão ir ao último registro

    private JButton btnNovo;          // botão limpar os campos para cadastro novo
    private JButton btnGravar;        // botão inserir o registro no banco
    private JButton btnAlterar;       // botão atualizar o registro
    private JButton btnExcluir;       // botão apagar o registro

    private JLabel lblStatus;         // texto que mostra em qual registro estamos

    // Construtor: roda automaticamente quando criamos "new frmTela()"
    public FrmTela() {
        initComponents(); // monta a tela (cria e posiciona todos os componentes)
        dao = new ClienteDAO(); // cria o objeto que acessa o banco
        carregarDados(); // busca os clientes e mostra na tela
        tblClientes.setAutoCreateRowSorter(true);// permite ordenar a tabela clicando no cabeçalho
    }

    // Busca todos os clientes no banco e exibe na tela
    private void carregarDados() {
        try {
            clientes = dao.listarTodos(); // pede a lista ao DAO
            totalRegistros = clientes.size(); // guarda a quantidade
            preencherTabela(); // joga a lista dentro da grid
            posicionarRegistro(); // mostra o primeiro registro nos campos
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar os dados: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Monta a janela: cria cada componente, define posição e tamanho, e liga os eventos
    private void initComponents() {
        setTitle("Cadastro de Clientes"); // texto da barra de título da janela
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // fechar a janela encerra o programa
        setLayout(null); // posicionamento manual (usamos setBounds para cada componente)

        JLabel lblTitulo = new JLabel("Cadastro de Clientes"); // rótulo do título
        lblTitulo.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18)); // fonte: negrito, tamanho 18

        JLabel lblNome = new JLabel("Nome:");            // rótulo "Nome:"
        JLabel lblDtNasc = new JLabel("Data de Nascimento:"); // rótulo "Data de Nascimento:"
        JLabel lblTelefone = new JLabel("Telefone:");    // rótulo "Telefone:"
        JLabel lblEmail = new JLabel("E-mail:");         // rótulo "E-mail:"
        JLabel lblPesquisa = new JLabel("Pesquisar:");   // rótulo "Pesquisar:"

        txtNome = new JTextField();       // cria a caixa de texto do nome
        txtDtNasc = criarCampoMascarado("##/##/####"); // caixa de texto da data com máscara
        txtTelefone = criarCampoMascarado("(##) ####-####"); // caixa de texto do telefone com máscara
        txtEmail = new JTextField();      // cria a caixa de texto do e-mail
        txtPesquisa = new JTextField();   // cria a caixa de texto da pesquisa

        // setBounds(x, y, largura, altura) define a posição e o tamanho de cada componente
        lblTitulo.setBounds(20, 10, 300, 20);
        lblNome.setBounds(20, 40, 60, 20);
        txtNome.setBounds(20, 60, 250, 25);
        lblDtNasc.setBounds(290, 40, 130, 20);
        txtDtNasc.setBounds(290, 60, 110, 25);
        lblTelefone.setBounds(20, 95, 80, 20);
        txtTelefone.setBounds(20, 115, 160, 25);
        lblEmail.setBounds(200, 95, 60, 20);
        txtEmail.setBounds(200, 115, 260, 25);
        lblPesquisa.setBounds(480, 95, 80, 20);
        txtPesquisa.setBounds(480, 115, 120, 25);

        // Cria o modelo da tabela sobrescrevendo isCellEditable para sempre retornar false
        modelo = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false; // false = as células NÃO podem ser editadas direto na grid
            }
        };
        modelo.setColumnIdentifiers(new Object[]{"Código", "Nome", "Data de Nascimento", "Telefone", "E-mail"}); // títulos das 5 colunas
        tblClientes = new JTable(modelo); // cria a tabela usando o modelo criado acima
        JScrollPane scroll = new JScrollPane(tblClientes); // coloca a tabela dentro de uma barra de rolagem
        scroll.setBounds(20, 155, 580, 200); // posição/tamanho da tabela na janela
        add(scroll); // adiciona a tabela (com a barra de rolagem) na janela

        btnPrimeiro = new JButton("|<"); // botão "ir ao primeiro" (símbolo |< )
        btnAnterior = new JButton("<");  // botão "voltar um"
        btnProximo = new JButton(">");   // botão "avançar um"
        btnUltimo = new JButton(">|");   // botão "ir ao último"
        btnNovo = new JButton("Novo");   // botão "Novo"
        btnGravar = new JButton("Gravar"); // botão "Gravar"
        btnAlterar = new JButton("Alterar"); // botão "Alterar"
        btnExcluir = new JButton("Excluir"); // botão "Excluir"

        // posição/tamanho de cada botão
        btnPrimeiro.setBounds(20, 375, 50, 25);
        btnAnterior.setBounds(75, 375, 50, 25);
        btnProximo.setBounds(130, 375, 50, 25);
        btnUltimo.setBounds(185, 375, 50, 25);
        btnNovo.setBounds(260, 375, 80, 25);
        btnGravar.setBounds(345, 375, 80, 25);
        btnAlterar.setBounds(430, 375, 80, 25);
        btnExcluir.setBounds(515, 375, 80, 25);

        lblStatus = new JLabel("Registro: 1"); // texto inicial da barra de status
        lblStatus.setBounds(20, 410, 300, 20);  // posição da barra de status

        // --- add(...) coloca cada componente na janela. Sem isto nada aparece! ---
        add(lblTitulo);
        add(lblNome);
        add(lblDtNasc);
        add(lblTelefone);
        add(lblEmail);
        add(lblPesquisa);
        add(txtNome);
        add(txtDtNasc);
        add(txtTelefone);
        add(txtEmail);
        add(txtPesquisa);
        add(btnPrimeiro);
        add(btnAnterior);
        add(btnProximo);
        add(btnUltimo);
        add(btnNovo);
        add(btnGravar);
        add(btnAlterar);
        add(btnExcluir);
        add(lblStatus);

        // --- Liga os componentes aos métodos de evento (a "ponte" interface <-> lógica) ---

        // Campo de pesquisa: quando SOLTAR uma tecla, chama o método de busca
        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt); // pesquisa por nome "ao vivo"
            }
        });
        // Tabela: quando CLICAR com o mouse, mostra a linha clicada nos campos
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClied(evt);
            }
        });
        // Tabela: quando PRESSIONAR tecla (setas), sincroniza os campos com a linha atual
        tblClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblClientesKeyPressed(evt);
            }
        });
        // Cada botão: quando for clicado, chama o seu método correspondente
        btnPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeiroRegestroActionPerformed(evt); // vai ao primeiro registro
            }
        });
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarUmRegestroActionPerformed(evt); // volta um registro
            }
        });
        btnProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvancarUmRegestroActionPerformed(evt); // avança um registro
            }
        });
        btnUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimoRegestroActionPerformed(evt); // vai ao último registro
            }
        });
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerfomed(evt); // limpa os campos
            }
        });
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt); // grava (insert) no banco
            }
        });
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt); // altera (update) o registro
            }
        });
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt); // exclui (delete) o registro
            }
        });

        setSize(630, 460); // tamanho da janela (largura, altura)
        setLocationRelativeTo(null); // centraliza a janela na tela
        setResizable(false); // não deixa o usuário redimensionar a janela
    }

    // Preenche a grid com a lista de clientes carregada
    public void preencherTabela() {
        // Ajusta a largura de cada coluna da tabela
        tblClientes.getColumnModel().getColumn(0).setPreferredWidth(4);
        tblClientes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblClientes.getColumnModel().getColumn(2).setPreferredWidth(11);
        tblClientes.getColumnModel().getColumn(3).setPreferredWidth(14);
        tblClientes.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel(); // pega o modelo atual da tabela
        modelo.setNumRows(0); // limpa todas as linhas (para não duplicar ao re-preenchê-la)

        for (Cliente c : clientes) { // para cada cliente da lista...
            modelo.addRow(new Object[]{ // ...adiciona uma linha nova na grid
                    c.getCod(),          // coluna 0
                    c.getNome(),         // coluna 1
                    c.getDtNasc(),       // coluna 2
                    c.getTelefone(),     // coluna 3
                    c.getEmail()         // coluna 4
            });
        }
    }

    // Posiciona a navegação no primeiro registro e mostra seus dados
    public void posicionarRegistro() {
        if (clientes.isEmpty()) { // se não há clientes...
            indiceAtual = -1; // nenhum registro selecionado
            mostrarDados(); // limpa os campos
            return;
        }
        indiceAtual = 0; // aponta para o primeiro cliente da lista
        mostrarDados(); // mostra os dados
    }

    // Mostra o registro atual (posição indiceAtual da lista) nas caixas de texto
    public void mostrarDados() {
        if (clientes.isEmpty() || indiceAtual < 0) { // lista vazia = limpa os campos
            codAtual = 0; // sem registro selecionado
            txtNome.setText("");
            txtDtNasc.setValue(null);
            txtTelefone.setValue(null);
            txtEmail.setText("");
            lblStatus.setText("Nenhum registro");
            return;
        }
        Cliente c = clientes.get(indiceAtual); // pega o cliente da posição atual
        codAtual = c.getCod(); // guarda o código (não aparece na tela, usado em alterar/excluir)
        txtNome.setText(c.getNome());    // nome
        txtDtNasc.setValue(c.getDtNasc()); // data de nascimento
        txtTelefone.setValue(c.getTelefone()); // telefone
        txtEmail.setText(c.getEmail());  // e-mail
        lblStatus.setText("Registro: " + (indiceAtual + 1) + " de " + clientes.size()); // barra de status
    }

    // Cria um campo de texto com máscara de digitação (ex.: ##/##/####)
    private JFormattedTextField criarCampoMascarado(String mascara) {
        try {
            MaskFormatter formatador = new MaskFormatter(mascara); // máscara (define onde entram letras/números)
            formatador.setPlaceholderCharacter(' '); // enquanto vazio, mostra espaços no lugar dos caracteres
            return new JFormattedTextField(formatador); // cria o campo já com a máscara
        } catch (ParseException erro) { // se a máscara estiver escrita errada...
            return new JFormattedTextField(); // ...cria um campo normal (sem máscara)
        }
    }

    // Lê o texto do campo de data; se estiver vazio devolve "" (em vez de "  /  /    ")
    private String lerData() {
        if (txtDtNasc.getValue() == null) { // campo vazio (não digitado por completo)...
            return ""; // ...devolve vazio
        }
        return txtDtNasc.getText(); // devolve o texto com a máscara (ex.: 17/03/1939)
    }

    // Lê o texto do campo de telefone; se estiver vazio devolve "" (em vez da máscara com espaços)
    private String lerTelefone() {
        if (txtTelefone.getValue() == null) { // campo vazio...
            return ""; // ...devolve vazio
        }
        return txtTelefone.getText(); // devolve o texto com a máscara (ex.: (11) 1234-5678)
    }

    // Evento: clique do mouse em uma linha da tabela
    private void tblClientesMouseClied(java.awt.event.MouseEvent evt) {
        int linha_selecionada = tblClientes.getSelectedRow(); // pega o nº da linha clicada
        if (linha_selecionada >= 0) { // proteção: só age se houver uma linha selecionada
            indiceAtual = linha_selecionada; // a linha clicada vira o registro atual
            mostrarDados(); // mostra esse cliente nos campos
        }
    }

    // Evento: tecla pressionada na tabela (setas de navegação)
    private void tblClientesKeyPressed(java.awt.event.KeyEvent evt) {
        // evento que sincroniza a grid com as setas do teclado
        int linha_selecionada = tblClientes.getSelectedRow(); // pega o nº da linha atual
        if (linha_selecionada >= 0) { // proteção contra linha vazia
            indiceAtual = linha_selecionada; // a linha atual vira o registro atual
            mostrarDados(); // mostra esse cliente nos campos
        }
    }

    // Botão "|<": vai ao primeiro registro
    private void btnPrimeiroRegestroActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (!clientes.isEmpty()) { // se existe algum registro...
                indiceAtual = 0; // vai para a primeira posição
                mostrarDados(); // mostra os dados
            }
        } catch (Exception erro) { // proteção contra qualquer erro inesperado
            JOptionPane.showMessageDialog(null, "Erro ao ir ao primeiro registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Botão ">|": vai ao último registro
    private void btnUltimoRegestroActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (!clientes.isEmpty()) { // se existe algum registro...
                indiceAtual = clientes.size() - 1; // vai para a última posição
                mostrarDados(); // mostra os dados
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao ir ao último registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Botão "<": volta um registro
    private void btnVoltarUmRegestroActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (indiceAtual > 0) { // se não está no primeiro...
                indiceAtual--; // ...volta uma posição
                mostrarDados(); // mostra os dados
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao voltar um registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Botão ">": avança um registro
    private void btnAvancarUmRegestroActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (indiceAtual < clientes.size() - 1) { // se não está no último...
                indiceAtual++; // ...avança uma posição
                mostrarDados(); // mostra os dados
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao avançar um registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Botão "Novo": limpa os campos para digitar um novo cadastro
    private void btnNovoActionPerfomed(java.awt.event.ActionEvent evt) {
        codAtual = 0; // novo registro = ainda não existe código (o banco gera sozinho)
        txtNome.setText(""); // limpa o nome
        txtDtNasc.setValue(null); // limpa a data
        txtTelefone.setValue(null); // limpa o telefone
        txtEmail.setText(""); // limpa o e-mail
        txtNome.requestFocus(); // posiciona o cursor no campo nome para digitação
    }

    // Confere se todos os campos foram preenchidos; se faltar algum, avisa e devolve false
    private boolean camposPreenchidos() {
        if (txtNome.getText().trim().isEmpty() || lerData().isEmpty() || lerTelefone().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            return false; // não pode gravar
        }
        return true; // todos os campos preenchidos
    }

    // Botão "Gravar": insere um novo registro no banco
    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {
        if (!camposPreenchidos()) { // se faltou preencher algum campo...
            return; // ...não grava
        }
        Cliente cliente = new Cliente(); // cria um objeto Cliente com os dados digitados
        cliente.setNome(txtNome.getText()); // nome
        cliente.setDtNasc(lerData()); // data de nascimento
        cliente.setTelefone(lerTelefone()); // telefone
        cliente.setEmail(txtEmail.getText()); // e-mail

        try {
            dao.inserir(cliente); // manda o DAO fazer o INSERT
            JOptionPane.showMessageDialog(null, "Gravação realizada com sucesso!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // avisa que gravou
            carregarDados(); // recarrega a lista e a grid
        } catch (SQLException errosql) { // se der erro no banco...
            JOptionPane.showMessageDialog(null, "\n Erro na gravação :\n" + errosql, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...mostra o erro
        }
    }

    // Botão "Alterar": atualiza o registro atual (ou insere se for um cadastro novo)
    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {
        if (!camposPreenchidos()) { // se faltou preencher algum campo...
            return; // ...não grava
        }
        Cliente cliente = new Cliente(); // objeto com os dados atuais dos campos
        cliente.setNome(txtNome.getText());
        cliente.setDtNasc(lerData());
        cliente.setTelefone(lerTelefone());
        cliente.setEmail(txtEmail.getText());

        try {
            if (codAtual == 0) { // sem registro selecionado = cadastro NOVO
                dao.inserir(cliente); // faz um INSERT
            } else { // senão, existe código = ALTERAÇÃO de um registro já salvo
                cliente.setCod(codAtual); // usa o código guardado
                dao.alterar(cliente); // faz um UPDATE por esse código
            }
            JOptionPane.showMessageDialog(null, "Gravação realizada com sucesso!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // avisa que deu certo
            carregarDados(); // recarrega a lista e a grid
        } catch (SQLException errosql) { // se der erro no banco...
            JOptionPane.showMessageDialog(null, "\n Erro na gravação :\n" + errosql, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...mostra o erro
        }
    }

    // Botão "Excluir": apaga o registro atual (com confirmação)
    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Pergunta "Sim/Não" antes de apagar; guarda a resposta do usuário
            int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja excluir o registro: ", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) { // se o usuário escolheu "Sim"
                dao.excluir(codAtual); // manda o DAO fazer o DELETE pelo código guardado
                JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // avisa
                carregarDados(); // recarrega a lista e a grid
            } else { // se escolheu "Não"
                JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuario!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // avisa que cancelou
            }
        } catch (SQLException errosql) { // se der erro no banco...
            JOptionPane.showMessageDialog(null, "\n Erro na gravação :\n" + errosql, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...mostra o erro
        }
    }

    // Evento: digitou no campo de pesquisa (busca por nome que COMEÇA com o texto digitado)
    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {
        try {
            clientes = dao.buscarPorNome(txtPesquisa.getText()); // pede ao DAO os clientes que combinam
            totalRegistros = clientes.size(); // guarda a quantidade
            preencherTabela(); // recarrega a grid com o resultado
            posicionarRegistro(); // mostra o primeiro registro encontrado
            if (clientes.isEmpty()) { // se não encontrou nada...
                JOptionPane.showMessageDialog(null, "\n Não existe dados com este paramêtro!!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...avisa
            }
        } catch (SQLException errosql) { // se der erro no banco...
            JOptionPane.showMessageDialog(null, "\n Os dados digitados não foram localizados!! :\n " + errosql, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); // ...mostra o erro
        }
    }
}