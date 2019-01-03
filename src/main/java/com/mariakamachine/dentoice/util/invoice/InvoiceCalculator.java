package com.mariakamachine.dentoice.util.invoice;

import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

@Slf4j
public class InvoiceCalculator {

    public static InvoiceSum calculateInvoice(InvoiceEntity invoice) {
        log.info("calculating costs for invoice {}", invoice.getId());
        CostWrapperEntity costs = invoice.getCosts();
        // calc efforts
        BigDecimal efforts = new BigDecimal(0.0);
        for (EffortJsonb effort : costs.getEfforts()) {
            efforts = efforts.add(new BigDecimal(effort.getQuantity()).multiply(new BigDecimal(effort.getPricePerUnit())));
        }
        // calc materials
        BigDecimal materials = new BigDecimal(0.0);
        BigDecimal metals = new BigDecimal(0.0);
        for (MaterialJsonb material : costs.getMaterials()) {
            BigDecimal materialCost = new BigDecimal(material.getQuantity()).multiply(new BigDecimal(material.getPricePerUnit()));
            if (material.getIsMetal()) {
                metals = metals.add(materialCost);
            }
            materials = materials.add(materialCost);
        }
        // netto
        BigDecimal netto = round(efforts.add(materials));
        // fraction
        BigDecimal mwst = calculatePercentage(netto, invoice.getMwst());
        return new InvoiceSum(round(efforts), round(materials), round(metals), netto, mwst, netto.add(mwst));
    }

    public static MonthlyInvoiceSum calculateMonthlyInvoiceSum(List<InvoiceEntity> invoices, double skontoPercentage) {
        BigDecimal subtotal = new BigDecimal(0.0);
        BigDecimal efforts = new BigDecimal(0.0);
        for (InvoiceEntity invoice : invoices) {
            InvoiceSum invoiceSum = calculateInvoice(invoice);
            subtotal = subtotal.add(invoiceSum.getBrutto());
            efforts = efforts.add(invoiceSum.getEfforts().add(calculatePercentage(invoiceSum.getEfforts(), invoice.getMwst())));
        }
        BigDecimal skonto = calculatePercentage(efforts, skontoPercentage);
        return new MonthlyInvoiceSum(round(subtotal), round(efforts), skonto, subtotal.subtract(skonto));
    }

    public static BigDecimal calculateProduct(Double quantity, Double pricePerUnit) {
        return round(new BigDecimal(quantity).multiply(new BigDecimal(pricePerUnit)));
    }

    private static BigDecimal calculatePercentage(BigDecimal sum, double percentage) {
        return round(sum.multiply(new BigDecimal(percentage)).divide(new BigDecimal(100)));
    }

    private static BigDecimal round(BigDecimal sum) {
        return sum.setScale(2, ROUND_HALF_DOWN);
    }

}
