package com.mariakamachine.dentoice.util.invoice;

import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class InvoiceSumCalculator {

    public InvoiceSum calculate(CostWrapperEntity costs) {
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
            if (material.isMetal()) {
                metals = metals.add(materialCost);
            }
            materials = materials.add(materialCost);
        }
        // total
        return new InvoiceSum(round(efforts), round(materials), round(metals), round(efforts.add(materials)));
    }

    private BigDecimal round(BigDecimal sum) {
        return sum.setScale(2, ROUND_HALF_DOWN);
    }

}
