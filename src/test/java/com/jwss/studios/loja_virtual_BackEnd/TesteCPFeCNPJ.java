package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.util.ValidaCNPJ;
import com.jwss.studios.loja_virtual_BackEnd.util.ValidaCPF;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TesteCPFeCNPJ {
    @Test
    public static void main(String[] args){
       boolean isCNPJ = ValidaCNPJ.isCNPJ("86.275.669/0001-01");
       boolean isCPF = ValidaCPF.isCPF("356.150.200-83");

      if(isCNPJ == true){
          System.out.println("Cnpj Válido: "+isCNPJ);
      }else {
          System.out.println("Cnpj Inválido: "+isCNPJ);
      }
        if(isCPF == true){
            System.out.println("Cpf Válido: "+isCPF);
        }else {
            System.out.println("Cpf Inválido: "+isCPF);
        }
    }
}
