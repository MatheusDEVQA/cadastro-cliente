package com.cadastrocliente.cadastroclienteapp.model.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import javax.annotation.processing.Generated;

public class Cliente {



    private long id;

    @NotBlank(message = " obrigatório")
    private String primeiroNome;

    @NotBlank(message = "")
    private String ultimoNome;

    @NotBlank(message = "")
    @Email(message = "Formato de email incorreto")
    private String usuario;

    @NotBlank(message = "")
    private String cidade;

    @NotBlank(message = "")
    private String estado;

}
