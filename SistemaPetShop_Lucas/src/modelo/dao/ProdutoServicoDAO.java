/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import modelo.ProdutoServico;

/**
 *
 * @author lucas
 */
public class ProdutoServicoDAO {
    
    public void gravar(ProdutoServico produtoServico) throws Exception{
        if (produtoServico.getId() == 0){
            inserir(produtoServico);
        } else {
            alterar(produtoServico);
        }
    }
    
    private void inserir(ProdutoServico produtoServico) throws Exception{
        String sql = "insert into produtos_servicos "
                + "(nome, servico, valor, frequencia_aplicacao_dias, registro_ativo)"
                + "values "
                + "(?, ?, ?, ?, ?)";
        
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, produtoServico.getNome());
        consulta.setBoolean(2, produtoServico.isServico());
        consulta.setFloat(3, produtoServico.getValor());
        consulta.setInt(4, produtoServico.getFrequenciaAplicacao());
        consulta.setBoolean(5, produtoServico.isAtivo());
        consulta.executeUpdate();
    }
    
    private void alterar(ProdutoServico produtoServico) throws Exception{
        String sql = "update produtos_servicos set "
                + "nome = ?, servico = ?, valor = ?, "
                + "frequencia_aplicacao_dias = ?, registro_ativo = ? "
                + "where id = ?";
        
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, produtoServico.getNome());
        consulta.setBoolean(2, produtoServico.isServico());
        consulta.setFloat(3, produtoServico.getValor());
        consulta.setInt(4, produtoServico.getFrequenciaAplicacao());
        consulta.setBoolean(5, produtoServico.isAtivo());
        consulta.setInt(6, produtoServico.getId());
        consulta.executeUpdate();
    }
    
    public ArrayList<ProdutoServico> pesquisar(String filtro) throws Exception{
        ArrayList<ProdutoServico> produtoServicos = new ArrayList();
        String sql = "select * from produtos_servicos "
                + "where registro_ativo and "
                + "lower(nome) like ?";
        
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, "%" + filtro.toLowerCase() + "%");
        ResultSet resultadoConsulta = consulta.executeQuery();
        
        while (resultadoConsulta.next()){
            ProdutoServico produto_servico = new ProdutoServico();
            produto_servico.setId(resultadoConsulta.getInt("id"));
            produto_servico.setNome(resultadoConsulta.getString("nome"));
            produto_servico.setServico(resultadoConsulta.getBoolean("servico"));
            produto_servico.setValor(resultadoConsulta.getFloat("valor"));
            produto_servico.setFrequenciaAplicacao(resultadoConsulta.getInt("frequencia_aplicacao_dias"));
            produto_servico.setAtivo(resultadoConsulta.getBoolean("registro_ativo"));
            
            produtoServicos.add(produto_servico);
        }
        
        return produtoServicos;
    }
}
