package com.mariakamachine.dentoice.util.invoice.xml;

import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.xml.Laborabrechnung;
import com.mariakamachine.dentoice.xml.MwstGruppe;
import com.mariakamachine.dentoice.xml.Position;
import com.mariakamachine.dentoice.xml.Rechnung;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mariakamachine.dentoice.xml.Art.BEL;
import static com.mariakamachine.dentoice.xml.Art.MAT;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.util.Collections.singletonList;

@NoArgsConstructor
public class InvoiceConverter {

    public Laborabrechnung convertToXmlModel(InvoiceEntity invoice, InvoiceProperties invoiceProperties) {
        double netto = calculateInvoiceNetto(invoice.getCosts());
        double mwst = netto * invoiceProperties.getMwstInPercentage() / 100;
        double brutto = netto + mwst;
        // RECHNUNG
        Rechnung rechnung = new Rechnung();
        rechnung.setLaborSoftwareVersion(invoiceProperties.getSoftwareVersion());
        rechnung.setLaborLieferDatum(invoice.getDate());
        rechnung.setLaborRechnungsNummer(invoice.getId());
        rechnung.setXmlNummer(invoice.getXmlNumber());
        rechnung.setNetto(convertToXsdConformInteger(netto));
        rechnung.setMwst(convertToXsdConformInteger(mwst));
        rechnung.setBrutto(convertToXsdConformInteger(brutto));
        // MWST GRUPPE
        MwstGruppe mwstGruppe = new MwstGruppe();
        mwstGruppe.setNetto(convertToXsdConformInteger(netto));
        mwstGruppe.setMwstSatz(convertToXsdConformInteger(invoiceProperties.getMwstInPercentage() / 10));
        mwstGruppe.setMwstBetrag(convertToXsdConformInteger(mwst));
        // POSITIONEN - EFFORT
        List<Position> positionen = new ArrayList<>();
        for (EffortJsonb effort : invoice.getCosts().getEfforts()) {
            positionen.add(effortToPosition(effort));
        }
        // POSITIONEN - MATERIAL
        for (MaterialJsonb material : invoice.getCosts().getMaterials()) {
            positionen.add(materialToPosition(material));
        }

        mwstGruppe.setPositionen(positionen);
        rechnung.setMwstGruppe(singletonList(mwstGruppe));
        return new Laborabrechnung(invoiceProperties.getXsdVersion(), rechnung);
    }

    private Position effortToPosition(EffortJsonb effort) {
        return new Position(BEL, effort.getPosition(), effort.getDescription(), convertToXsdConformInteger(effort.getPricePerUnit()), effort.getQuantity().intValue());
    }

    private Position materialToPosition(MaterialJsonb material) {
        return new Position(MAT, null, material.getDescription(), convertToXsdConformInteger(material.getPricePerUnit()), convertToXsdConformInteger(material.getQuantity() * 10));
    }

    private double calculateInvoiceNetto(CostWrapperEntity costs) {
        double result = 0;
        for (EffortJsonb effort : costs.getEfforts()) {
            result += effort.getQuantity() * effort.getPricePerUnit();
        }
        for (MaterialJsonb material : costs.getMaterials()) {
            result += material.getQuantity() * material.getPricePerUnit();
        }
        return result;
    }

    private int convertToXsdConformInteger(double sum) {
        return Integer.valueOf(new BigDecimal(sum)
                .setScale(2, ROUND_HALF_DOWN)
                .toPlainString()
                .replace(".", ""));
    }

}
