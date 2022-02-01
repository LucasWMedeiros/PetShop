/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Rafael
 */
public class Conexao {

    private static Connection conexao;

    public static Connection getConexao() throws Exception {
        if (conexao == null){
           Class.forName("org.postgresql.Driver"); 
           conexao = DriverManager.getConnection(
                   "jdbc:postgresql://localhost:5432/petshop", //URL
                   "postgres", //LOGIN
                   "abc123*"); //SENHA
        }
        return conexao;
    }

}
