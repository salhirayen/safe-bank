package tn.isetkebili.transaction.dto;

import java.math.BigDecimal;

public record AccountDTO(Long id, Long customerId, BigDecimal balance, String status) {}