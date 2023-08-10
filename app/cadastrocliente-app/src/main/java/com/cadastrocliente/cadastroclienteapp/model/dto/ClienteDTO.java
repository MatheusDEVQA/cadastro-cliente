package com.cadastrocliente.cadastroclienteapp.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {

    private long id;

    @NotBlank(message = "O nome é obrigatório")
    private String primeiroNome;

    @NotBlank(message = "o sobrenome é obrigatório")
    private String ultimoNome;

    @NotBlank(message = "o e-mail é obrigatório")
    private String usuario;

    @NotBlank(message = " obrigatória")
    private String cidade;

    @NotBlank(message = " obrigatório")
    private String estado;


}
