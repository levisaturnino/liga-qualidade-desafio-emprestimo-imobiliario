package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Solucao {

    private static String CREATED = "created";

    private static String ADD = "added";
    private static String proponent = "proponent";

    private static double VALOR_MAXIMO_EMPRESTIMO = 3000000;

    private static double VALOR_MINIMO_EMPRESTIMO = 30000;

    private static int IDADE_MINIMA = 18;

    private static int MAXIMO_MES = 180;

    private static int MINIMO_MES = 24;

    private static int GARANTIA = 0;

    private static int MAXIMO_15_ANOS_PAGA = 15;

    private static int MINIMO_2_ANOS_PAGA = 2;

    private static int PROPONENTE = 0;

    private static int valor = 2;

    private static Double valorGarantias = 0.0;
    private static Double valorEmprestimos = 0.0;
    public static final String[] UF = new String[] {"PR", "SC" ,"RS"};

    public static String processMessages(List<String> messages) {

      List<String> resposta = messages.stream().map(event -> {
        String[] proposta = event.split(",");

        switch (proposta[1]) {
            case "proposal" :
                return proposal(proposta);

            case "warranty" :
               return warranty(proposta);

            case "proponent" :
                return proponent(proposta);
        }
          return null;
      }).collect(Collectors.toList());
        resposta.removeIf(Objects::isNull);
        return String.join(",", resposta);
  }


    static String proposal(String[] proposta) {
        double valorEmprestimo =  Double.parseDouble(proposta[5]);

      // valorEmprestimos += valorEmprestimo;

        //regra:  O valor do empréstimo deve estar entre R$ 30.000,00 e R$ 3.000.000,00
       if (valorEmprestimo < VALOR_MINIMO_EMPRESTIMO || valorEmprestimo > VALOR_MAXIMO_EMPRESTIMO){
            return null;
        }
      //regra: O empréstimo deve ser pago em no mínimo 2 anos e no máximo 15 anos
        if (Integer.parseInt(proposta[6]) < MINIMO_MES || Integer.parseInt(proposta[6]) > MAXIMO_MES) {
            return null;
        }

        //regra: A soma do valor das garantias deve ser maior ou igual ao dobro do valor do empréstimo
       /* if(valorGarantias  >= valorEmprestimo * 2) {
            return null;
        }*/

        return proposta[4];
    }

    static String warranty(String[] proposta) {

         //regra:A soma do valor das garantias deve ser maior ou igual ao dobro do valor do empréstimo
        double valorGarantia =  Double.parseDouble(proposta[6]);

        valorGarantias += valorGarantia;

        //regra: Deve haver no mínimo 1 garantia de imóvel por proposta
        if (GARANTIA >= 1) {
            return null;
        }
        GARANTIA ++;

        //regra:As garantias de imóvel dos estados PR, SC e RS não são aceitas
        if (Arrays.stream(UF).anyMatch(proposta[7]::equals)) {
            return null;
        }

        return null;
    }

    static String proponent(String[] proposta) {
        //regra: Todos os proponentes devem ser maiores de 18 anos
        int  idade          =   Integer.parseInt(proposta[7]);
        double  emprestimo  =   Double.parseDouble(proposta[8]);

           if (idade  < IDADE_MINIMA){
                return null;
            }
        /*regra:  A renda do proponente principal deve ser pelo menos:
        4 vezes o valor da parcela do empréstimo, se a idade dele for entre 18 e 24 anos
        3 vezes o valor da parcela do empréstimo, se a idade dele for entre 24 e 50 anos
        2 vezes o valor da parcela do empréstimo, se a idade dele for acima de 50 anos*/

            if (idade > 18 && idade <= 24){
                proposta[8] = String.valueOf(emprestimo * 4);
                return null;
            }else  if (idade > 24 && idade <= 50){
                proposta[8] = String.valueOf(emprestimo * 3);
                return null;
            }else  if (idade > 50) {
                proposta[8] = String.valueOf(emprestimo * 2);
                return null;
            }
        return null;
    }

}