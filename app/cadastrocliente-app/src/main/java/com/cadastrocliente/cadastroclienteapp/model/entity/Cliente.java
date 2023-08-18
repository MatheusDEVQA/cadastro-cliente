package com.cadastrocliente.cadastroclienteapp.model.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Cliente {


    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private long id;

    @NotBlank(message = " obrigatório")
    private String primeiroNome;

    @NotBlank(message = "")
    private String ultimoNome;

    @NotBlank(message = "")
    @Email(message = "Formato de email incorreto")
    private String usuario;

    @NotBlank(message = "A cidade é obrigatória!")
    private String cidade;

    @NotBlank(message = "")
    private String estado;

}
