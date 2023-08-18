package com.cadastrocliente.cadastroclienteapp.helpers;

import com.cadastrocliente.cadastroclienteapp.model.dto.ClienteDTO;

public class ClienteDTOMockFactory {

    public static ClienteDTO novoClienteMath(){
        return definirMath().build();
    }

    private static ClienteDTO.ClienteDTOBuilder definirMath(){
        return ClienteDTO.builder()
                .primeiroNome("Math")
                .ultimoNome("Carv")
                .usuario("math.carv@email.com")
                .cidade("Princesa")
                .estado("PAraiba");
    }

    public static ClienteDTO clienteCdastradoMathId9(){
        return definirMath().id(9).build();
    }

    public static ClienteDTO clienteCdastradoMathId10(){
        return definirMath().id(10).build();
    }

}
