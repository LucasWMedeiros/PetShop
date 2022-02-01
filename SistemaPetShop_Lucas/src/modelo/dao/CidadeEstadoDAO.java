/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import modelo.Cidade;
import modelo.Estado;

/**
 *
 * @author Rafael
 */
public class CidadeEstadoDAO {

    public ArrayList<Estado> carregarEstados() throws Exception {
        ArrayList<Estado> estados = new ArrayList<>();
        String sql = "select id, sigla from estado order by sigla";
        PreparedStatement comandoSql = Conexao.getConexao().prepareStatement(sql);
        ResultSet retornoConsulta = comandoSql.executeQuery();
        while (retornoConsulta.next()) {
            Estado e = new Estado();
            e.setId(retornoConsulta.getInt("id"));
            e.setSigla(retornoConsulta.getString("sigla"));
            estados.add(e);
        }
        return estados;
    }

    public ArrayList<Cidade> carregarCidades(String siglaEstado) throws Exception {
        ArrayList<Cidade> cidades = new ArrayList<>();
        String sql = "select c.id, c.nome from cidade c "
                + "left join estado e on e.id = c.estado_id "
                + "where e.sigla = ? order by lower(c.nome)";
        PreparedStatement comandoSql = Conexao.getConexao().prepareStatement(sql);
        comandoSql.setString(1, siglaEstado);
        ResultSet retornoConsulta = comandoSql.executeQuery();
        while (retornoConsulta.next()) {
            Cidade c = new Cidade();
            c.setId(retornoConsulta.getInt("id"));
            c.setNome(retornoConsulta.getString("nome"));
            cidades.add(c);
        }
        return cidades;
    }
}
