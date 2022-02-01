/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import modelo.Animal;
import modelo.Cidade;
import modelo.Estado;
import modelo.Pessoa;

/**
 *
 * @author Rafael
 */
public class PessoaAnimalDAO {

    public void gravar(Pessoa pessoa) throws Exception {
        if (pessoa.getId() == 0) {
            inserir(pessoa);
        } else {
            alterar(pessoa);
        }
        for (Animal animal : pessoa.getAnimais()) {
            if (animal.getId() == 0) {
                inserir(animal);
            } else {
                alterar(animal);
            }
        }
    }

    private void inserir(Pessoa pessoa) throws Exception {
        String sql = "insert into pessoa "
                + "(nome, data_nascimento, endereco, "
                + "numero, bairro, cidade_id, registro_ativo) "
                + "values "
                + "(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        consulta.setString(1, pessoa.getNome());

        if (pessoa.getDataNascimento() != null) {
            consulta.setDate(2,
                    //Converte java.util.Date em java.sql.Date
                    new java.sql.Date(pessoa.getDataNascimento().getTime()));
        } else {
            consulta.setDate(2, null);
        }

        consulta.setString(3, pessoa.getEndereco());
        consulta.setInt(4, pessoa.getNumero());
        consulta.setString(5, pessoa.getBairro());
        consulta.setInt(6, pessoa.getCidade().getId());
        consulta.setBoolean(7, pessoa.isAtivo());
        consulta.executeUpdate();//Roda o insert no BD
        ResultSet retornoId = consulta.getGeneratedKeys();
        if (retornoId.next()) {
            pessoa.setId(retornoId.getInt(1));
        }
    }

    private void alterar(Pessoa pessoa) throws Exception {
        String sql = "update pessoa set "
                + "nome = ?, data_nascimento = ?, endereco = ?, "
                + "numero = ?, bairro = ?, cidade_id = ?, registro_ativo = ? "
                + "where id = ? ";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, pessoa.getNome());
        consulta.setDate(2,
                new java.sql.Date(pessoa.getDataNascimento().getTime())); //Converte java.util.Date em java.sql.Date
        consulta.setString(3, pessoa.getEndereco());
        consulta.setInt(4, pessoa.getNumero());
        consulta.setString(5, pessoa.getBairro());
        consulta.setInt(6, pessoa.getCidade().getId());
        consulta.setBoolean(7, pessoa.isAtivo());
        consulta.setInt(8, pessoa.getId());
        consulta.executeUpdate();//Roda o insert no BD
    }

    public ArrayList<Pessoa> pesquisar(String filtro) throws Exception {
        ArrayList<Pessoa> pessoas = new ArrayList();
        String sql = "select p.*, c.nome as cidade, e.sigla from pessoa p "
                + "left join cidade c on c.id = p.cidade_id "
                + "left join estado e on e.id = c.estado_id "
                + "where registro_ativo and "
                + "(lower(p.nome) like ? or lower(c.nome) like ?) ";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, "%" + filtro.toLowerCase() + "%");//nome
        consulta.setString(2, "%" + filtro.toLowerCase() + "%");//cidade
        ResultSet resultadoConsulta = consulta.executeQuery();

        //Faz a varredura na tabelinha de resultado da consulta
        while (resultadoConsulta.next()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(resultadoConsulta.getInt("id"));
            pessoa.setNome(resultadoConsulta.getString("nome"));
            pessoa.setDataNascimento(resultadoConsulta.getDate("data_nascimento"));
            pessoa.setEndereco(resultadoConsulta.getString("endereco"));
            pessoa.setNumero(resultadoConsulta.getInt("numero"));
            pessoa.setBairro(resultadoConsulta.getString("bairro"));
            pessoa.setAtivo(resultadoConsulta.getBoolean("registro_ativo"));

            Cidade cidade = new Cidade();
            cidade.setId(resultadoConsulta.getInt("cidade_id"));
            cidade.setNome(resultadoConsulta.getString("cidade"));

            Estado estado = new Estado();
            estado.setSigla(resultadoConsulta.getString("sigla"));
            cidade.setEstado(estado);

            pessoa.setCidade(cidade);
            pessoa.setAnimais(carregarAnimais(pessoa));

            pessoas.add(pessoa);
        }

        return pessoas;
    }

    public ArrayList<Pessoa> listar() throws Exception {
        ArrayList<Pessoa> pessoas = new ArrayList();
        String sql = "select id, nome from pessoa "
                + "where registro_ativo";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        ResultSet resultadoConsulta = consulta.executeQuery();

        //Faz a varredura na tabelinha de resultado da consulta
        while (resultadoConsulta.next()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(resultadoConsulta.getInt("id"));
            pessoa.setNome(resultadoConsulta.getString("nome"));
            pessoas.add(pessoa);
        }

        return pessoas;
    }

    //Não utiliza porque não exclui registros no BD, apenas inativa
//    private void excluir(Pessoa pessoa) throws Exception {
//        String sql = "delete from pessoa where id = ?";
//        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
//        consulta.setInt(1, pessoa.getId());
//        consulta.executeUpdate();//Roda o insert no BD
//    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void inserir(Animal animal) throws Exception {
        String sql = "insert into animal "
                + "(nome, pessoa_id, registro_ativo) "
                + "values "
                + "(?, ?, ?)";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, animal.getNome());
        consulta.setInt(2, animal.getDono().getId());
        consulta.setBoolean(3, animal.isAtivo());
        consulta.executeUpdate();//Roda o insert no BD
    }

    private void alterar(Animal animal) throws Exception {
        String sql = "update animal set "
                + "nome = ?, pessoa_id = ?, registro_ativo = ? "
                + "where id = ?";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setString(1, animal.getNome());
        consulta.setInt(2, animal.getDono().getId());
        consulta.setBoolean(3, animal.isAtivo());
        consulta.setInt(4, animal.getId());
        consulta.executeUpdate();//Roda o insert no BD
    }

    public ArrayList<Animal> carregarAnimais(Pessoa pessoa) throws Exception {
        ArrayList<Animal> animais = new ArrayList();
        String sql = "select * from animal where registro_ativo and pessoa_id = ?";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, pessoa.getId());
        ResultSet resultadoConsulta = consulta.executeQuery();

        //Faz a varredura na tabelinha de resultado da consulta
        while (resultadoConsulta.next()) {
            Animal animal = new Animal();
            animal.setId(resultadoConsulta.getInt("id"));
            animal.setNome(resultadoConsulta.getString("nome_animal"));
            animal.setDono(pessoa);
            animal.setAtivo(resultadoConsulta.getBoolean("registro_ativo"));
            animais.add(animal);
        }

        return animais;
    }
    
    public ArrayList<Animal> pesquisarAnimal(int filtro) throws Exception{
        ArrayList<Animal> animais = new ArrayList();
        String sql = "select * from animal where registro_ativo and pessoa_id = ?";

        PreparedStatement consulta = Conexao.getConexao().prepareStatement(sql);
        consulta.setInt(1, filtro);
        ResultSet resultadoConsulta = consulta.executeQuery();

        //Faz a varredura na tabelinha de resultado da consulta
        while (resultadoConsulta.next()) {
            Animal animal = new Animal();
            animal.setId(resultadoConsulta.getInt("id"));
            animal.setNome(resultadoConsulta.getString("nome_animal"));
            animal.setAtivo(resultadoConsulta.getBoolean("registro_ativo"));
            animais.add(animal);
        }

        return animais;
    }
}
