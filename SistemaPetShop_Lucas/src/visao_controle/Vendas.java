/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao_controle;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Animal;
import modelo.ItemVenda;
import modelo.Pessoa;
import modelo.ProdutoServico;
import modelo.Venda;
import modelo.dao.PessoaAnimalDAO;
import modelo.dao.ProdutoServicoDAO;
import modelo.dao.VendasDAO;
import visao_controle.SelecionarPessoa;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import modelo.dao.Conexao;


/**
 *
 * @author lucas
 */
public class Vendas extends javax.swing.JFrame {

    private ArrayList<ProdutoServico> produtospesquisados;
    private ArrayList<ItemVenda> itenspesquisados;
    Pessoa clienteselecionado;
    int cod_cliente;
    private ArrayList<Animal> animalpesquisado;
    private ProdutoServico produto;
    private Venda venda;
    private int codpesquisa = 0;
    private Venda vendaPesquisada;
    private float valorTotal = 0;
    private ItemVenda item;
    private Animal animal;
    private int quantidade;

    /**
     * Creates new form Vendas
     */
    public Vendas() {
        initComponents();
        rodarPesquisa();
        venda = new Venda();
        setarVisibilidade();
        venda.setDataHora(LocalDateTime.now());
    }

    private void rodarPesquisa() {
        ProdutoServicoDAO dao = new ProdutoServicoDAO();

        try {
            this.produtospesquisados = dao.pesquisar(jtfFiltro.getText());
            DefaultTableModel modeloTabela = (DefaultTableModel) jtProdutos.getModel();
            modeloTabela.setNumRows(0);
            if (this.produtospesquisados != null) {
                for (ProdutoServico p : this.produtospesquisados) {
                    Object[] linha = new Object[2];
                    linha[0] = p.getNome();
                    linha[1] = p.getValor();
                    modeloTabela.addRow(linha);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PesquisaProdutosServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void preencherdadosclientes() {
        jtfNome.setText(clienteselecionado.getNome());
        jtfEndereco.setText(clienteselecionado.getEndereco());
        jtfBairro.setText(clienteselecionado.getBairro());
        jtfNumero.setText(String.valueOf(clienteselecionado.getNumero()));
        jtfCidade.setText(clienteselecionado.getCidade().getNome());
        jtfEstado.setText(clienteselecionado.getCidade().getEstado().getSigla());
        jtfId.setText(Integer.toString(clienteselecionado.getId()));
    }

    private void limparTela() {
        jtfNome.setText("");
        jtfEndereco.setText("");
        jtfBairro.setText("");
        jtfNumero.setText("");
        jtfCidade.setText("");
        jtfEstado.setText("");
        jtfId.setText("");
    }

    public int coletarId() {
        cod_cliente = clienteselecionado.getId();
        return cod_cliente;
    }

    public ProdutoServico getProduto() {
        int linhaselecionada = jtProdutos.getSelectedRow();
        produto = produtospesquisados.get(linhaselecionada);
        return produto;
    }

    public void adicionarProduto(int quantidade, Animal animal) {
        DefaultTableModel modeloTabela = (DefaultTableModel) jtProdutosVendidos.getModel();
        Object[] linha = new Object[4];
        linha[0] = quantidade;
        linha[1] = getProduto().getNome();
        linha[2] = getProduto().getValor();
        linha[3] = animal.getNome();
        modeloTabela.addRow(linha);

        ItemVenda itemvenda = new ItemVenda();
        itemvenda.setQuantidade(quantidade);
        itemvenda.setAnimal(animal);
        itemvenda.setProdutoServico(produto);
        itemvenda.setValorUnitario(produto.getValor());
        venda.getItens().add(itemvenda);
        atualizarTotal();
        this.animal = animal;
        this.quantidade = quantidade;
    }

    private void quantidadeAnimais() throws Exception {
        PessoaAnimalDAO dao = new PessoaAnimalDAO();
        animalpesquisado = dao.carregarAnimais(clienteselecionado);

        if (animalpesquisado.size() == 0) {
            limparTela();
            JOptionPane.showMessageDialog(this, "Este cliente não tem nenhum animal cadastrado", "ERRO", JOptionPane.ERROR_MESSAGE);
            clienteselecionado = null;
        } else {
            preencherdadosclientes();
        }
    }

    private void setarVisibilidade() {
        if (codpesquisa == 0) {
            jtfDataHora.setVisible(false);
            jlDataHora.setVisible(false);
            btnDeletar.setVisible(false);
            btnPesquisarPessoa.setVisible(true);
            btnRelatorio.setVisible(false);
        } else {
            jtfDataHora.setVisible(true);
            jlDataHora.setVisible(true);
            btnDeletar.setVisible(true);
            btnPesquisarPessoa.setVisible(false);
            btnRelatorio.setVisible(true);
        }
    }

    private void preencherPesquisa() throws Exception {
        clienteselecionado = venda.getCliente();
        preencherdadosclientes();
        pesquisarItens();
        
        setarVisibilidade();
        jtfDataHora.setText(venda.getDataHora().toString());
    }

    private void atualizarTotal() {
        float valorTotal = 0;
        for (ItemVenda item : venda.getItens()) {
            valorTotal = valorTotal + (item.getQuantidade() * item.getValorUnitario());
        }
        jtfValorTotal.setText(Float.toString(valorTotal));
    }

    private void venderItens() throws Exception {
        VendasDAO dao = new VendasDAO();

        for (ItemVenda item : venda.getItens()) {
            item.setAnimal(animal);
            item.setQuantidade(quantidade);
            item.setVenda(venda);
            dao.inserirItemVenda(item);
        }
    }
    private void realizarVenda() throws Exception{
        VendasDAO dao = new VendasDAO();
         dao.inserirVenda(venda);
            venderItens();
            JOptionPane.showMessageDialog(this, "Venda realizada com sucesso");
            this.setVisible(false);
    }
    private void atualizar() throws Exception{
        VendasDAO dao = new VendasDAO();
        dao.deletarItens(venda.getId());
        venda.setCliente(clienteselecionado);
        
        venderItens();
        JOptionPane.showMessageDialog(this, "Venda atualizada com sucesso");
        this.setVisible(false);
        dao.alterarVenda(venda);
    }

    private void pesquisarItens() throws Exception {
        VendasDAO dao = new VendasDAO();
        try {
            float valorTotal = 0;
            itenspesquisados = dao.pesquisarItens(venda.getId());
            DefaultTableModel modeloTabela = (DefaultTableModel) jtProdutosVendidos.getModel();
            for (ItemVenda i : itenspesquisados) {
                Object[] linha = new Object[4];
                linha[0] = i.getQuantidade();
                linha[1] = i.getProdutoServico().getNome();
                linha[2] = i.getValorUnitario();
                linha[3] = i.getAnimal().getNome();
                modeloTabela.addRow(linha);
                valorTotal = valorTotal + (i.getQuantidade() * i.getValorUnitario());
                venda.getItens().add(i);
            }
            jtfValorTotal.setText(Float.toString(valorTotal));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(PesquisaProdutosServicos.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void gerarRelatorio() throws Exception{
        try {
            // TODO add your handling code here:
            JasperReport report = JasperCompileManager.compileReport("Relatorios/Venda.jrxml");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id_venda", venda.getId());
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametros, Conexao.getConexao());
            
            JasperViewer viewer = new JasperViewer(jasperPrint);
            viewer.setTitle("Venda");
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setDefaultCloseOperation(0);
            viewer.setVisible(true); 
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, "RelatoriosVenda" + venda.getId() +".pdf");
        } catch (JRException ex) {
            Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnPesquisarPessoa = new javax.swing.JButton();
        jtfNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfEndereco = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtfNumero = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtfBairro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtfCidade = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtfEstado = new javax.swing.JTextField();
        jtfId = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProdutosVendidos = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jtfValorTotal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtProdutos = new javax.swing.JTable();
        btnExcluir = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jtfFiltro = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jtfNumeroVenda = new javax.swing.JTextField();
        btnPesquisarVenda = new javax.swing.JButton();
        jlDataHora = new javax.swing.JLabel();
        jtfDataHora = new javax.swing.JTextField();
        btnDeletar = new javax.swing.JButton();
        btnRelatorio = new javax.swing.JButton();

        jLabel10.setText("jLabel10");

        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vendas.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Vendas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Cliente");

        btnPesquisarPessoa.setText("...");
        btnPesquisarPessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarPessoaActionPerformed(evt);
            }
        });

        jtfNome.setEditable(false);

        jLabel4.setText("Endereço");

        jtfEndereco.setEditable(false);

        jLabel5.setText("Numero");

        jtfNumero.setEditable(false);
        jtfNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNumeroActionPerformed(evt);
            }
        });

        jLabel6.setText("Bairro");

        jtfBairro.setEditable(false);

        jLabel7.setText("Cidade");

        jtfCidade.setEditable(false);

        jLabel8.setText("Estado");

        jtfEstado.setEditable(false);

        jtfId.setEditable(false);

        jLabel14.setText("Cod.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesquisarPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jtfEndereco)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jtfBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jtfCidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtfEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(jtfNumero))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btnPesquisarPessoa)
                    .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jtfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtfBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jtfCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jtfEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jtProdutosVendidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "quant.", "Produto", "Preço", "Animal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtProdutosVendidos);
        if (jtProdutosVendidos.getColumnModel().getColumnCount() > 0) {
            jtProdutosVendidos.getColumnModel().getColumn(0).setResizable(false);
            jtProdutosVendidos.getColumnModel().getColumn(0).setPreferredWidth(12);
            jtProdutosVendidos.getColumnModel().getColumn(1).setResizable(false);
            jtProdutosVendidos.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel9.setText("Valor Total:");

        jtfValorTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfValorTotalActionPerformed(evt);
            }
        });

        jtProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produto", "Preço"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtProdutosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtProdutos);
        if (jtProdutos.getColumnModel().getColumnCount() > 0) {
            jtProdutos.getColumnModel().getColumn(0).setResizable(false);
            jtProdutos.getColumnModel().getColumn(1).setResizable(false);
        }

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Esquerda.png"))); // NOI18N
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Direita.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel11.setText("Filtro:");

        jtfFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfFiltroKeyPressed(evt);
            }
        });

        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/save.png"))); // NOI18N
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jLabel12.setText("Pesquisar Venda (Nº Venda):");

        btnPesquisarVenda.setText("Pesquisar");
        btnPesquisarVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarVendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtfNumeroVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPesquisarVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jtfNumeroVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisarVenda))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jlDataHora.setText("Data e Hora");

        btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/delete.png"))); // NOI18N
        btnDeletar.setToolTipText("");
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });

        btnRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imprimir.jpg"))); // NOI18N
        btnRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPesquisar))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnRelatorio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalvar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlDataHora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfDataHora, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtfFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtfValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlDataHora)
                            .addComponent(jtfDataHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRelatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisarPessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarPessoaActionPerformed
        // TODO add your handling code here:
        SelecionarPessoa selecionar = new SelecionarPessoa();
        selecionar.setVisible(true);
        clienteselecionado = selecionar.getPessoaselecionada();
        venda.setCliente(clienteselecionado);
        try {
            quantidadeAnimais();
        } catch (Exception ex) {
            Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPesquisarPessoaActionPerformed

    private void jtfNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNumeroActionPerformed

    private void jtfValorTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfValorTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfValorTotalActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (clienteselecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente", "ERRO", JOptionPane.ERROR_MESSAGE);
        } else if (jtProdutos.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto", "ERRO", JOptionPane.ERROR_MESSAGE);
        } else {
            QuantidadeAdicionada add = null;
            try {
                add = new QuantidadeAdicionada(this);
            } catch (Exception ex) {
                Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
            }
            add.setVisible(true);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        System.out.println(venda.getItens().size());
        int linhaselecionada = jtProdutosVendidos.getSelectedRow();
        if (linhaselecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione o produto que deseja remover", "ERRO", JOptionPane.ERROR_MESSAGE);
        } else {
            int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este produto?", "Atenção", JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                venda.getItens().remove(linhaselecionada);
                DefaultTableModel modeloTabela = (DefaultTableModel) jtProdutosVendidos.getModel();
                modeloTabela.removeRow(linhaselecionada);
            }
        }
        System.out.println(venda.getItens().size());
        atualizarTotal();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // TODO add your handling code here:
        rodarPesquisa();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void jtfFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFiltroKeyPressed
        // TODO add your handling code here:
        rodarPesquisa();
    }//GEN-LAST:event_jtfFiltroKeyPressed

    private void jtProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtProdutosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtProdutosMouseClicked

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        if(codpesquisa == 0){
            if (venda.getItens().isEmpty()){
                JOptionPane.showMessageDialog(this, "Selecione algum produto", "ERRO", JOptionPane.ERROR_MESSAGE);
            } else{
            try {
                realizarVenda();
                gerarRelatorio();
            } catch (Exception ex) {
                Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        } else {
            try {
                atualizar();
                this.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnPesquisarVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarVendaActionPerformed
        // TODO add your handling code here:
        if(jtfNumeroVenda.getText().isEmpty()){
            codpesquisa = 0;
        } else {
        codpesquisa = Integer.parseInt(jtfNumeroVenda.getText());
        }
        if (codpesquisa == 0){
            JOptionPane.showMessageDialog(this, "Insira uma venda valida", "ERRO", JOptionPane.ERROR_MESSAGE);
        } else {
        VendasDAO dao = new VendasDAO();
        try {
            venda = dao.pesquisar(codpesquisa);
            if(venda.isAtivo() == false){
                JOptionPane.showMessageDialog(this, "Venda não encontrada", "ERRO", JOptionPane.ERROR_MESSAGE);
            } else{
            preencherPesquisa();
            }
        } catch (Exception ex) {
            Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_btnPesquisarVendaActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        // TODO add your handling code here:
        try {
            int resp = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta venda?","Atencao", JOptionPane.YES_NO_OPTION);
            if(resp == JOptionPane.YES_OPTION){
               VendasDAO dao = new VendasDAO();
               dao.deletarVenda(vendaPesquisada.getId());
               dao.deletarItens(vendaPesquisada.getId());
               this.setVisible(false);
               JOptionPane.showMessageDialog(this, "Exclusão realizada com sucesso");
            }
        } catch (Exception e) {
            
        }
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void btnRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatorioActionPerformed
        try {
            gerarRelatorio();
        } catch (Exception ex) {
            Logger.getLogger(Vendas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRelatorioActionPerformed

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
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vendas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnPesquisarPessoa;
    private javax.swing.JButton btnPesquisarVenda;
    private javax.swing.JButton btnRelatorio;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlDataHora;
    private javax.swing.JTable jtProdutos;
    private javax.swing.JTable jtProdutosVendidos;
    private javax.swing.JTextField jtfBairro;
    private javax.swing.JTextField jtfCidade;
    private javax.swing.JTextField jtfDataHora;
    private javax.swing.JTextField jtfEndereco;
    private javax.swing.JTextField jtfEstado;
    private javax.swing.JTextField jtfFiltro;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfNome;
    private javax.swing.JTextField jtfNumero;
    private javax.swing.JTextField jtfNumeroVenda;
    private javax.swing.JTextField jtfValorTotal;
    // End of variables declaration//GEN-END:variables
}
