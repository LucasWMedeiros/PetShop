/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao_controle;

import java.util.ArrayList;
import java.util.Date;
import modelo.Cidade;
import modelo.Pessoa;
import modelo.dao.PessoaAnimalDAO;

/**
 *
 * @author Rafael
 */
public class Teste {
    public static void main(String[] args) throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Rogerio");
        pessoa.setEndereco("Rua tamandaré");
        pessoa.setNumero(11);
        pessoa.setDataNascimento(new Date());
        
        Cidade cidade = new Cidade();
        cidade.setId(5);
        cidade.setNome("Capixaba");
        pessoa.setCidade(cidade);
        
        PessoaAnimalDAO dao = new PessoaAnimalDAO();
//        dao.gravar(pessoa);
        ArrayList<Pessoa> pessoas = dao.pesquisar("ael");
        for (Pessoa p : pessoas){
            System.out.println(p.getNome());
        }
    }
}
