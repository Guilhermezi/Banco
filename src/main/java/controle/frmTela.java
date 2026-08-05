package controle;

import conexao.Conexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel; // para reconhecer a Jtable
import java.sql.*; // Para reconhecer os comandos do SQL

public class frmTela extends javax.swing.JFrame{
    Conexao con_cliente;

    public frmTela() {
//        initComponents();
        con_cliente = new Conexao(); // inicialização do objeto como instância
        con_cliente.conecta(); // Chama o metodo que conecta
        con_cliente.executaSQL("select * from tbclientes order by cod");
        preencherTabela();
        posicionarRegistro();
        tblClientes.setAutoCreateRowSorter(true);// ativa a classificação ordenada da tabela
    }

    public void preencherTabela(){
        tblClientes.getColumnModel().getColumn(0).setPreferredWidth(4);
        tblClientes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblClientes.getColumnModel().getColumn(2).setPreferredWidth(11);
        tblClientes.getColumnModel().getColumn(3).setPreferredWidth(14);
        tblClientes.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
        modelo.setNumRows(0);

        try {
            con_cliente.resultSet.beforeFirst();
            while (con_cliente.resultSet.next()){
                modelo.addRow(new Object[]{
                        con_cliente.resultSet.getString("cod"),
                        con_cliente.resultSet.getString("nome"),
                        con_cliente.resultSet.getString("dt_nasc"),
                        con_cliente.resultSet.getString("telefone"),
                        con_cliente.resultSet.getString("email")
                });
            }
        }catch (SQLException erro){
            JOptionPane.showMessageDialog(null,"\n Erro ao listar dados da tabela!! :\n "+erro,"Mensagem do Programa",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void posicionarRegistro(){
        try {
            con_cliente.resultSet.first(); //posiciona na 1º registro da tabela
            mostrar_Dados(); // chama o metodo que irá buscar o dado da tabela
        }catch (SQLException erro){
            JOptionPane.showMessageDialog(null,"Não foi possível posicionar no primeiro registro: "+erro,"Mensagem do Programa",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mostrar_Dados(){
        try {
            txtCod.setText(con_cliente.resultSet.getString("cod"));
            txtNome.setText(con_cliente.resultSet.getString("nome"));
            txtDtNasc.setText(con_cliente.resultSet.getString("dt_nasc"));
            txtTelefone.setText(con_cliente.resultSet.getString("telefone"));
            txtEmail.setText(con_cliente.resultSet.getString("email"));
        }catch (SQLException erro){
            JOptionPane.showMessageDialog(null,"Não localizou dados: "+erro,"Mensagem do Programa",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void tblClientesMouseClied(java.awt.event.MouseEvent evt){
        int linha_selecionada = tblClientes.getSelectedRow();
        txtCod.setText(tblClientes.getValueAt(linha_selecionada,0).toString());
        txtNome.setText(tblClientes.getValueAt(linha_selecionada,1).toString());
        txtDtNasc.setText(tblClientes.getValueAt(linha_selecionada,2).toString());
        txtTelefone.setText(tblClientes.getValueAt(linha_selecionada,3).toString());
        txtEmail.setText(tblClientes.getValueAt(linha_selecionada,4).toString());
    }

    private void tblClientesKeyPressed(java.awt.event.KeyEvent evt){
        // evento que sincroniza a grid com as setas do teclado
        int linha_selecionada = tblClientes.getSelectedRow();
        txtCod.setText(tblClientes.getValueAt(linha_selecionada,0).toString());
        txtNome.setText(tblClientes.getValueAt(linha_selecionada,1).toString());
        txtDtNasc.setText(tblClientes.getValueAt(linha_selecionada,2).toString());
        txtTelefone.setText(tblClientes.getValueAt(linha_selecionada,3).toString());
        txtEmail.setText(tblClientes.getValueAt(linha_selecionada,4).toString());
    }

    private void btnPrimeiroRegestroActionPerformed(java.awt.event.ActionEvent evt){
        try {
            con_cliente.resultSet.first();
            mostrar_Dados();
        }catch (SQLException erro){

        }
    }

    private void btnUltimoRegestroActionPerformed(java.awt.event.ActionEvent evt){
        try {
            con_cliente.resultSet.last();
            mostrar_Dados();
        }catch (SQLException erro){

        }
    }

    private void btnVoltarUmRegestroActionPerformed(java.awt.event.ActionEvent evt){
        try {
            con_cliente.resultSet.previous();
            mostrar_Dados();
        }catch (SQLException erro){

        }
    }

    private void btnAvancarUmRegestroActionPerformed(java.awt.event.ActionEvent evt){
        try {
            con_cliente.resultSet.next();
            mostrar_Dados();
        }catch (SQLException erro){

        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

    }
}
