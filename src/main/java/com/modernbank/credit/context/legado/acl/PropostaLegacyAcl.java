package com.modernbank.credit.context.legado.acl;

import com.modernbank.credit.context.legado.adapter.PropostaLegacyDTO;
import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.model.PropostaStatus;
import com.modernbank.credit.context.propostas.domain.factory.PropostaFactory;
import com.modernbank.credit.sharedkernel.vo.Cpf;
import com.modernbank.credit.sharedkernel.vo.Dinheiro;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Anti-Corruption Layer (ACL) que protege o modelo de domínio das peculiaridades
 * do formato legado do mainframe. Faz parsing/normalização e devolve uma Proposta
 * consistente com a Linguagem Ubíqua do domínio.
 */
public final class PropostaLegacyAcl {

    private PropostaLegacyAcl() {}

    public static Proposta mapearParaProposta(PropostaLegacyDTO dto) {
        if (dto == null) throw new NullPointerException("DTO legado não pode ser nulo");

        // Normaliza CPF e cria Value Object
        Cpf cpf = new Cpf(dto.getCpf());

        // Converte centavos (string) -> BigDecimal (reais)
        BigDecimal valor = parseCentavos(dto.getValorEmCentavos());
        Dinheiro dinheiro = new Dinheiro(valor);

        // Mapeia status legado para enum do domínio
        PropostaStatus status = mapearStatus(dto.getStatusCodigo());

        // Para id usamos um UUID gerado, mas mantemos o idLegado em logs (ou criar mapeamento adicional se necessário)
        UUID id = UUID.randomUUID();

        // Utiliza a factory de domínio para manter invariantes
        return PropostaFactory.reconstruir(id, cpf, dinheiro, status);
    }

    private static BigDecimal parseCentavos(String centavosStr) {
        if (centavosStr == null || centavosStr.isBlank()) {
            throw new IllegalArgumentException("Valor em centavos inválido");
        }
        String digits = centavosStr.replaceAll("\\D", "");
        if (digits.isEmpty()) throw new IllegalArgumentException("Valor em centavos inválido");
        BigDecimal centavos = new BigDecimal(digits);
        // Usa movePointLeft para evitar necessidade de especificar rounding mode
        return centavos.movePointLeft(2);
    }

    private static PropostaStatus mapearStatus(String codigo) {
        if (codigo == null) return PropostaStatus.PENDENTE;
        switch (codigo.toUpperCase()) {
            case "S": // Submetida
            case "1":
                return PropostaStatus.SUBMETIDA;
            case "A": // Aprovada
            case "2":
                return PropostaStatus.APROVADA;
            case "R": // Rejeitada
            case "0":
                return PropostaStatus.REJEITADA;
            default:
                return PropostaStatus.PENDENTE; // padrão defensivo
        }
    }
}
