package kfu.itis.service;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal convertRubToUsd(BigDecimal amountRub);
}