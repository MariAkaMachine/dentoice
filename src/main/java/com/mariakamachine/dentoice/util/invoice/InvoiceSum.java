package com.mariakamachine.dentoice.util.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceSum {

    private BigDecimal efforts;
    private BigDecimal materials;
    private BigDecimal metal;
    private BigDecimal netto;
    private BigDecimal fraction;
    private BigDecimal brutto;

}
