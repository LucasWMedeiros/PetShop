/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao_controle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Animal;
import modelo.Cidade;
import modelo.Estado;
import modelo.Pessoa;
import modelo.dao.CidadeEstadoDAO;
import modelo.dao.PessoaAnimalDAO;

/**
 *
 * @author Rafael
 */
public class CadastroClientes extends javax.swing.JDialog {

    private Pessoa pessoaEmEdicao;
    private Animal animalEmEdicao;
    private ArrayList<Estado> estados;
    private ArrayList<Cidade> cidades;
    private PessoaAnimalDAO pessoaDAO = new PessoaAnimalDAO();
    private CidadeEstadoDAO cidadeEstadoDAO = new CidadeEstadoDAO();

    public CadastroClientes() {
        initComponents();
        setModal(true);
        this.pessoaEmEdicao = new Pessoa();
        carregarEstados();
    }

    public void setPessoaEmEdicao(Pessoa pessoaEmEdicao) {
        this.pessoaEmEdicao = pessoaEmEdicao;
        preencherDadosTela();
    }

    private void carregarEstados() {
        try {
            this.estados = cidadeEstadoDAO.carregarEstados();
            jcbEstado.removeAllItems();
            if (this.estados != null) {
                for (Estado e : this.estados) {
                    jcbEstado.addItem(e.getSigla());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar estados.", "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCidades(String siglaEstado) {
        try {
            this.cidades = cidadeEstadoDAO.carregarCidades(siglaEstado);
            jcbCidade.removeAllItems();
            if (this.cidades != null) {
                for (Cidade c : this.cidades) {
                    jcbCidade.addItem(c.getNome());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cidades.", "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean preencheuTodosCamposObrigatorios() {
        if (jtfNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente.", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            jtfNome.requestFocus();
            return false;
        }

        //Mais validações aqui...
        return true;
    }

    private void preencherDadosTela() {
        jtfNome.setText(pessoaEmEdicao.getNome());
        jtfEndereco.setText(pessoaEmEdicao.getEndereco());

        if (pessoaEmEdicao.getNumero() > 0) {
            jtfNumero.setText(String.valueOf(pessoaEmEdicao.getNumero()));
        }

        jtfBairro.setText(pessoaEmEdicao.getBairro());

        if (pessoaEmEdicao.getDataNascimento() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            jftfDataNascimento.setText(sdf.format(pessoaEmEdicao.getDataNascimento()));
        }

        if (pessoaEmEdicao.getCidade() != null) {
            jcbEstado.setSelectedItem(pessoaEmEdicao.getCidade().getEstado().getSigla());
            jcbCidade.setSelectedItem(pessoaEmEdicao.getCidade().getNome());
        }

//        int indiceCidadeSelecionada = jcbCidade.getSelectedIndex();
//        Cidade cidadeSelecionada = this.cidades.get(indiceCidadeSelecionada);
//        pessoaEmEdicao.setCidade(cidadeSelecionada);
        atualizarListaAnimais();
    }

    private void lerDadosTela() {
        pessoaEmEdicao.setNome(jtfNome.getText());

        pessoaEmEdicao.setEndereco(jtfEndereco.getText());

        if (!jtfNumero.getText().trim().equals("")) {
            int numero = Integer.parseInt(jtfNumero.getText());
            pessoaEmEdicao.setNumero(numero);
        }

        pessoaEmEdicao.setBairro(jtfBairro.getText());

        if (!jftfDataNascimento.getText().replace("/", "").trim().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date dataNascimento = sdf.parse(jftfDataNascimento.getText());
                pessoaEmEdicao.setDataNascimento(dataNascimento);
            } catch (ParseException ex) {
                Logger.getLogger(CadastroClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int indiceCidadeSelecionada = jcbCidade.getSelectedIndex();
        Cidade cidadeSelecionada = this.cidades.get(indiceCidadeSelecionada);
        pessoaEmEdicao.setCidade(cidadeSelecionada);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbSalvar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtfNome = new javax.swing.JTextField();
        jtfEndereco = new javax.swing.JTextField();
        jtfBairro = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtfNumero = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jftfDataNascimento = new javax.swing.JFormattedTextField();
        jcbEstado = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jcbCidade = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtListaAnimais = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jtfNomeAnimal = new javax.swing.JTextField();
        jbAdicionarAnimal = new javax.swing.JButton();
        jbRemoverAnimal = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jbSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/save.png"))); // NOI18N
        jbSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalvarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("NOME:");

        jLabel5.setText("ENDEREÇO:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("BAIRRO:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("ESTADO:");

        jLabel8.setText("NÚMERO:");

        jtfNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfNumeroKeyTyped(evt);
            }
        });

        jLabel9.setText("DATA NASC.:");

        try {
            jftfDataNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jcbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbEstadoActionPerformed(evt);
            }
        });

        jLabel10.setText("CIDADE:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jcbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbCidade, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jtfBairro, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jtfEndereco)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtfNome))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jtfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jftfDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jcbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("DADOS PESSOAIS", jPanel3);

        jtListaAnimais.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOME"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtListaAnimais.setFillsViewportHeight(true);
        jtListaAnimais.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtListaAnimaisMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtListaAnimais);

        jLabel11.setText("NOME ANIMAL:");

        jbAdicionarAnimal.setText("Adicionar / Salvar");
        jbAdicionarAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAdicionarAnimalActionPerformed(evt);
            }
        });

        jbRemoverAnimal.setText("Remover Animal");
        jbRemoverAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoverAnimalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbRemoverAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNomeAnimal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbAdicionarAnimal)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtfNomeAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAdicionarAnimal))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRemoverAnimal)
                .addContainerGap())
        );

        jTabbedPane1.addTab("ANIMAIS", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CADASTRO DE CLIENTES");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/clients.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(55, 55, 55))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbSalvar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(jbSalvar)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jcbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbEstadoActionPerformed
        if (jcbEstado.getSelectedItem() != null) {
            String siglaSelecionada = jcbEstado.getSelectedItem().toString();
            carregarCidades(siglaSelecionada);
        }
    }//GEN-LAST:event_jcbEstadoActionPerformed

    private void jbSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarActionPerformed
        if (preencheuTodosCamposObrigatorios()) {
            try {
                lerDadosTela();
                //Grava no banco
                PessoaAnimalDAO dao = new PessoaAnimalDAO();
                dao.gravar(this.pessoaEmEdicao);
                JOptionPane.showMessageDialog(this, "Operação realizada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                //Fecha a tela
                this.setVisible(false);
            } catch (Exception ex) {
//                if (ex instanceof MeuErro) {
//                    MeuErro erro = (MeuErro) ex;
//                    JOptionPane.showMessageDialog(this, erro.getMensagem(), "ERRO", JOptionPane.ERROR_MESSAGE);
//                }
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gravar cliente!", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jbSalvarActionPerformed

    private void jtfNumeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNumeroKeyTyped
        if (!String.valueOf(evt.getKeyChar()).matches("[0-9]")) {
            evt.consume();
        }
    }//GEN-LAST:event_jtfNumeroKeyTyped

    private void jbAdicionarAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAdicionarAnimalActionPerformed
        if (jtfNomeAnimal.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Informe o nome do animal.", "ATENÇÃO",
                    JOptionPane.WARNING_MESSAGE);
            jtfNomeAnimal.requestFocus();
            return;
        }

        if (this.animalEmEdicao != null) {
            animalEmEdicao.setNome(jtfNomeAnimal.getText().trim());
        } else {
            Animal animal = new Animal();
            animal.setNome(jtfNomeAnimal.getText().trim());
            animal.setDono(pessoaEmEdicao);
            pessoaEmEdicao.getAnimais().add(animal);
        }

        atualizarListaAnimais();
        jtfNomeAnimal.setText("");
        this.animalEmEdicao = null;
    }//GEN-LAST:event_jbAdicionarAnimalActionPerformed

    private void jbRemoverAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoverAnimalActionPerformed
        int linhaSelecionada = jtListaAnimais.getSelectedRow();
        if (linhaSelecionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um animal da lista.", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este animal?", "Atenção",
                    JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                int indiceAnimalLista = getIndiceAnimalListaBD(linhaSelecionada);
                Animal animal = pessoaEmEdicao.getAnimais().get(indiceAnimalLista);
                if (animal.getId() == 0) {//Animal nunca foi gravado no BD
                    pessoaEmEdicao.getAnimais().remove(indiceAnimalLista);
                } else {
                    animal.setAtivo(false);
                }
                atualizarListaAnimais();
            }
        }
    }//GEN-LAST:event_jbRemoverAnimalActionPerformed

    private void jtListaAnimaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtListaAnimaisMouseClicked
        if (evt.getClickCount() == 2) {
            int linhaSelecionada = jtListaAnimais.getSelectedRow();
            int indiceObjArrayList = getIndiceAnimalListaBD(linhaSelecionada);

            this.animalEmEdicao = this.pessoaEmEdicao.getAnimais().get(indiceObjArrayList);
            jtfNomeAnimal.setText(this.animalEmEdicao.getNome());
            jtfNomeAnimal.requestFocus();
        }
    }//GEN-LAST:event_jtListaAnimaisMouseClicked

    private int getIndiceAnimalListaBD(int linhaSelecionada) {
        int indiceAnimaisAtivos = -1;
        for (int i = 0; i < pessoaEmEdicao.getAnimais().size(); i++) {
            if (pessoaEmEdicao.getAnimais().get(i).isAtivo()) {
                indiceAnimaisAtivos++;
            }
            if (indiceAnimaisAtivos == linhaSelecionada) {
                return i;
            }
        }
        return -1;
    }

    private void atualizarListaAnimais() {
        DefaultTableModel modelo = (DefaultTableModel) jtListaAnimais.getModel();
        modelo.setNumRows(0);
        for (Animal animal : pessoaEmEdicao.getAnimais()) {
            if (animal.isAtivo()) {
                Object[] dados = new Object[]{
                    animal.getNome()
                };
                modelo.addRow(dados);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CadastroClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadastroClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbAdicionarAnimal;
    private javax.swing.JButton jbRemoverAnimal;
    private javax.swing.JButton jbSalvar;
    private javax.swing.JComboBox<String> jcbCidade;
    private javax.swing.JComboBox<String> jcbEstado;
    private javax.swing.JFormattedTextField jftfDataNascimento;
    private javax.swing.JTable jtListaAnimais;
    private javax.swing.JTextField jtfBairro;
    private javax.swing.JTextField jtfEndereco;
    private javax.swing.JTextField jtfNome;
    private javax.swing.JTextField jtfNomeAnimal;
    private javax.swing.JTextField jtfNumero;
    // End of variables declaration//GEN-END:variables
}
