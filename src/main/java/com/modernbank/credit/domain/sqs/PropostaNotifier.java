package com.modernbank.credit.domain.sqs;

import com.modernbank.credit.domain.model.Proposta;

public interface PropostaNotifier {

    void notificarCriacao(Proposta proposta);
}
