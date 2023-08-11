package com.cadastrocliente.cadastroclienteapp.repository;

import com.cadastrocliente.cadastroclienteapp.model.dto.ReponseEmailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface MailingRepository {

    ReponseEmailDTO sendEmail(String email) throws JsonProcessingException;
}
