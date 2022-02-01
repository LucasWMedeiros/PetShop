/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao_controle;

import java.util.ArrayList;
import modelo.ProdutoServico;
import modelo.dao.ProdutoServicoDAO;

/**
 *
 * @author lucas
 */
public class testeProdutoServico {
    public static void main(String[] args) throws Exception {
        ProdutoServico produtoServico = new ProdutoServico();
        
        produtoServico.setNome("vacina de raiva");
        produtoServico.setServico(true);
        produtoServico.setValor((float) 50);
        produtoServico.setFrequenciaAplicacao(180);
        produtoServico.setAtivo(true);
        
        ProdutoServicoDAO dao = new ProdutoServicoDAO();
        //dao.gravar(produtoServico);
        
        ArrayList<ProdutoServico> produtoServicos = dao.pesquisar("");
        for (ProdutoServico p: produtoServicos){
            System.out.println(p.getNome());
        }
    }
   
}
