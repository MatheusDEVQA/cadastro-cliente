package com.cadastrocliente.cadastroclienteapp.model.dto;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteDTO {

    private long id;

    @NotBlank(message = "O nome é obrigatório")
    private String primeiroNome;

    @NotBlank(message = "o sobrenome é obrigatório")
    private String ultimoNome;

    @NotBlank(message = "o e-mail é obrigatório")
    @Email(message = "Formato incorreto")
    private String usuario;

    @NotBlank(message = " obrigatória")
    private String cidade;

    @NotBlank(message = " obrigatório")
    private String estado;


}
