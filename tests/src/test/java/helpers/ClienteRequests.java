package helpers;

import dto.ClienteDTO;

import java.util.UUID;

public class ClienteRequests {

    public static ClienteDTO clienteGenarate() {

        String uuid = UUID.randomUUID().toString() ;
        String usuarioAleatorio = uuid.replace("-","").substring(0,8).concat("@email.com");
        return ClienteDTO.builder()
                .primeiroNome("Math")
                .ultimoNome("Carv")
                .cidade("Princesa")
                .usuario(usuarioAleatorio)
                .estado("PB").build();
    }
}
