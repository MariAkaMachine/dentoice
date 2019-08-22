package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.DentistEntity;

import static com.itextpdf.io.image.ImageDataFactory.create;
import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.UnitValue.createPercentValue;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.*;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

public class PdfPageEvent implements IEventHandler {

    private Document doc;
    private DentistEntity dentist;
    private String parameter;
    private boolean isMonthly;

    PdfPageEvent(Document doc, DentistEntity dentist, String parameter, boolean isMonthly) {
        this.doc = doc;
        this.dentist = dentist;
        this.parameter = parameter;
        this.isMonthly = isMonthly;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        doc.add(recipientDetails());
        doc.add(headerCell());
        doc.add(generatePageNumber(docEvent));
    }

    private Table recipientDetails() {
        Table table = new Table(1);
        table.setFixedPosition(1, 70, 600, createPercentValue(50));
        table.addCell(fineCell("Udo Baumann - Waldkapellenweg 7 - 72108 Rottenburg"));
        String recipient = new String()
                .concat(dentist.getTitle()).concat(lineSeparator())
                .concat(dentist.getFirstName()).concat(" ").concat(dentist.getLastName()).concat(lineSeparator())
                .concat(dentist.getStreet()).concat(lineSeparator())
                .concat(dentist.getZip()).concat(" ").concat(dentist.getCity()).concat(lineSeparator());
        table.addCell(cell(recipient));
        return table;
    }

    private Table headerCell() {
        Table table = new Table(2);
        table.setFixedPosition(310, 690, createPercentValue(121));
        table.addCell(cell("Udo Baumann")
                .setFontSize(18f)
                .setItalic()
                .setBold());
        table.addCell(cell("Dental-Technik")
                .setFontSize(18f)
                .setItalic());
        try {
            table.addCell(new Cell()
                    .add(new Image(create("./src/main/resources/static/img/tooth_icon.png")).scaleToFit(67, 67))
                    .setBorder(NO_BORDER)
                    .setPaddingLeft(15)
            );
        } catch (Exception e) {
            System.out.println("could not load pic");
            e.printStackTrace();
        }

        String address = new String()
                .concat("Waldkapellenweg 7").concat(lineSeparator())
                .concat("72108 Rottenburg").concat(lineSeparator())
                .concat("tel. 07073 / 919 718").concat(lineSeparator())
                .concat("fax 07073 / 919 719").concat(lineSeparator());
        table.addCell(cell(address));
        return table;
    }

    private Table generatePageNumber(PdfDocumentEvent docEvent) {
        PdfPage page = docEvent.getPage();
        return new Table(1)
                .useAllAvailableWidth()
                .addCell(fineCellCentered((format(isMonthly ? "Monatsaufstellung %s - Seite %d" : "Rechnung %s - Seite %d", parameter, docEvent.getDocument().getPageNumber(page)))));
    }

}
