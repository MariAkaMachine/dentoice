package com.mariakamachine.dentoice.util.invoice.xml;

import com.mariakamachine.dentoice.config.properties.InvoiceProperties;
import com.mariakamachine.dentoice.data.entity.InvoiceEntity;
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import com.mariakamachine.dentoice.model.xml.Laborabrechnung;
import com.mariakamachine.dentoice.model.xml.MwstGruppe;
import com.mariakamachine.dentoice.model.xml.Position;
import com.mariakamachine.dentoice.model.xml.Rechnung;
import com.mariakamachine.dentoice.util.invoice.InvoiceSum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mariakamachine.dentoice.model.xml.Art.BEL;
import static com.mariakamachine.dentoice.model.xml.Art.MAT;
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice;
import static java.lang.Integer.valueOf;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.util.Collections.singletonList;

public class InvoiceConverter {

    public Laborabrechnung convertToXmlModel(InvoiceEntity invoice, InvoiceProperties invoiceProperties) {
        InvoiceSum invoiceSum = calculateInvoice(invoice);
        // RECHNUNG
        Rechnung rechnung = new Rechnung();
        rechnung.setLaborSoftwareVersion(invoiceProperties.getSoftwareVersion());
        rechnung.setLaborLieferDatum(invoice.getDate());
        rechnung.setLaborRechnungsNummer(invoice.getId());
        rechnung.setXmlNummer(invoice.getXmlNumber());
        rechnung.setNetto(convertToXsdConformInteger(invoiceSum.getNetto()));
        rechnung.setMwst(convertToXsdConformInteger(invoiceSum.getMwst()));
        rechnung.setBrutto(convertToXsdConformInteger(invoiceSum.getBrutto()));
        // MWST GRUPPE
        MwstGruppe mwstGruppe = new MwstGruppe();
        mwstGruppe.setNetto(convertToXsdConformInteger(invoiceSum.getNetto()));
        mwstGruppe.setMwstSatz(convertToXsdConformInteger(new Double(invoice.getMwst()) / 10));
        mwstGruppe.setMwstBetrag(convertToXsdConformInteger(invoiceSum.getMwst()));
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
        return new Position(BEL, effort.getPosition(), effort.getName(), convertToXsdConformInteger(effort.getPricePerUnit()), effort.getQuantity().intValue());
    }

    private Position materialToPosition(MaterialJsonb material) {
        return new Position(MAT, null, material.getName(), convertToXsdConformInteger(material.getPricePerUnit()), convertToXsdConformInteger(material.getQuantity() * 10));
    }

    private int convertToXsdConformInteger(BigDecimal sum) {
        return valueOf(sum.toPlainString().replace(".", ""));
    }

    private int convertToXsdConformInteger(double sum) {
        return valueOf(new BigDecimal(sum)
                .setScale(2, ROUND_HALF_DOWN)
                .toPlainString()
                .replace(".", ""));
    }

}
