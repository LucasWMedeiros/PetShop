/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import modelo.Animal;
import modelo.Cidade;
import modelo.Estado;
import modelo.ItemVenda;
import modelo.Pessoa;
import modelo.ProdutoServico;
import modelo.Venda;

/**
 *
 * @author lucas
 */
public class VendasDAO {

    public void inserirVenda(Venda venda) throws SQLException, Exception {
        String sql = "insert into venda "
                + "(pessoa_id, data_hora, registro_ativo) "
                + "values "
                + "(?, ?, ? )";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        consulta.setInt(1, venda.getCliente().getId());
            consulta.setTimestamp(2,
                    Timestamp.valueOf(venda.getDataHora()));
        consulta.setBoolean(3, true);
        consulta.executeUpdate();
        ResultSet genKey = consulta.getGeneratedKeys();
        if (genKey.next()){
            venda.setId(genKey.getInt(1));
        }
            
    }

    public void inserirItemVenda(ItemVenda item) throws Exception {
        String sql = "insert into itens_venda "
                + "(venda_id, produtos_servicos_id, animal_id, "
                + "quantidade, valor_unitario ) "
                + "values "
                + "(?, ?, ?, ?, ?)";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, item.getVenda().getId());
        consulta.setInt(2, item.getProdutoServico().getId());
        consulta.setInt(3, item.getAnimal().getId());
        consulta.setFloat(4, item.getQuantidade());
        consulta.setFloat(5, item.getValorUnitario());
        consulta.executeUpdate();
    }

    public void alterarVenda(Venda venda) throws Exception {
        String sql = "update venda set "
                + "pessoa_id = ?, data_hora = ?, registro_ativo = ?"
                + "where id = ?";
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, venda.getCliente().getId());
        consulta.setTimestamp(2,
                    Timestamp.valueOf(venda.getDataHora()));
        consulta.setBoolean(3, true);
        consulta.setInt(4, venda.getId());
        consulta.executeUpdate();
    }

    public void aterarItemVenda(ItemVenda item) throws Exception {
        String sql = "update itens_venda set "
                + "venda_id = ?, produto_serviço_id = ?, animal_id = ?, "
                + "quantidade = ?, valor_unitario = ?";
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, item.getVenda().getId());
        consulta.setInt(2, item.getProdutoServico().getId());
        consulta.setInt(3, item.getAnimal().getId());
        consulta.setFloat(4, item.getQuantidade());
        consulta.setFloat(5, item.getValorUnitario());
        consulta.setInt(6, item.getId());
        consulta.executeUpdate();
    }

    public Venda pesquisar(int filtro) throws Exception {
        Venda venda = new Venda();
        String sql = "select "
                + "v.id as venda_id, "
                + "v.data_hora, "
                + "v.registro_ativo as venda_ativo,"
                + "v.pessoa_id, "
                + "p.id as id_pessoa, "
                + "p.nome as nome_pessoa, "
                + "p.endereco, "
                + "p. numero, "
                + "p.bairro, "
                + "p.cidade_id, "
                + "c.nome as cidade, "
                + "e.sigla "
                + "from venda v "
                + "left join pessoa p on p.id = v.pessoa_id "
                + "left join cidade c on c.id = p.cidade_id "
                + "left join estado e on e.id = c.estado_id "
                + "where v.id = ? and v.registro_ativo";

        
        
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, filtro);
        ResultSet resultadoConsulta = consulta.executeQuery();

        while (resultadoConsulta.next()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(resultadoConsulta.getInt("id_pessoa"));
            pessoa.setNome(resultadoConsulta.getString("nome_pessoa"));
            pessoa.setEndereco(resultadoConsulta.getString("endereco"));
            pessoa.setNumero(resultadoConsulta.getInt("numero"));
            pessoa.setBairro(resultadoConsulta.getString("bairro"));
            
            Cidade cidade = new Cidade();
            cidade.setId(resultadoConsulta.getInt("cidade_id"));
            cidade.setNome(resultadoConsulta.getString("cidade"));
            
            Estado estado = new Estado();
            estado.setSigla(resultadoConsulta.getString("sigla"));
            cidade.setEstado(estado);
            
            pessoa.setCidade(cidade);


            venda.setCliente(pessoa);
            venda.setDataHora(resultadoConsulta.getTimestamp("data_hora").toLocalDateTime());
            venda.setId(resultadoConsulta.getInt("venda_id"));
            venda.setAtivo(resultadoConsulta.getBoolean("venda_ativo"));

        }

        return venda;
    }

    public ArrayList<ItemVenda> pesquisarItens(int filtro) throws Exception {
        ArrayList<ItemVenda> itens = new ArrayList();
        String sql = "select i.*, p.nome as produto_servico, a.nome_animal from itens_venda i "
                + "left join animal a on a.id = i.animal_id "
                + "left join produtos_servicos p on p.id = produtos_servicos_id "
                + "where venda_id = ?";
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, filtro);
        ResultSet resultadoConsulta = consulta.executeQuery();
         
        while (resultadoConsulta.next()){
            ItemVenda item = new ItemVenda();
            
            ProdutoServico produto_servico = new ProdutoServico();
            produto_servico.setId(resultadoConsulta.getInt("produtos_servicos_id"));
            produto_servico.setNome(resultadoConsulta.getString("produto_servico"));
            
            Animal animal = new Animal();
            animal.setId(resultadoConsulta.getInt("animal_id"));
            animal.setNome(resultadoConsulta.getString("nome_animal"));
            
            
            item.setProdutoServico(produto_servico);
            item.setId(resultadoConsulta.getInt("id"));
            item.setAnimal(animal);
            item.setQuantidade(resultadoConsulta.getInt("quantidade"));
            item.setValorUnitario(resultadoConsulta.getFloat("valor_unitario"));
            
            
            itens.add(item);
        }
         
        return itens;
    }
    
    public void deletarItens(int filtro) throws Exception {
    String sql = "Delete from itens_venda where venda_id = ?";
    
    PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
    consulta.setInt(1, filtro);
    consulta.executeUpdate();
}
    
    public void deletarVenda(int filtro) throws Exception{
        String sql = "update venda set registro_ativo = false where id = ?";
        
        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, filtro);
        consulta.executeUpdate();
    }

}
