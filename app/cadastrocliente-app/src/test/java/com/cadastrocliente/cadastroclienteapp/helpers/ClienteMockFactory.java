package com.cadastrocliente.cadastroclienteapp.helpers;

import com.cadastrocliente.cadastroclienteapp.model.entity.Cliente;

public class ClienteMockFactory {

    public static Cliente novoClienteMath(){
        return definirMath().build();
    }

    private static Cliente.ClienteBuilder definirMath(){
        return Cliente.builder()
                .primeiroNome("Math")
                .ultimoNome("Carv")
                .usuario("math.carv@email.com")
                .cidade("Princesa")
                .estado("PAraiba");
    }

    public static  Cliente clienteCadastradoMathId9(){
        return definirMath().id(9).build();
    }

}
