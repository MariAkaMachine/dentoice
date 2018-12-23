package com.mariakamachine.dentoice.util.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthlyInvoiceSum {

    BigDecimal subtotal;
    BigDecimal efforts;
    BigDecimal skonto;
    BigDecimal total;

}
