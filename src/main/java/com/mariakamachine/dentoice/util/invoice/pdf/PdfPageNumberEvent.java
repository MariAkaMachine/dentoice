package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.fineCell;
import static java.lang.String.format;

public class PdfPageNumberEvent implements IEventHandler {

    private Document doc;
    private String parameter;
    private boolean isMonthly;

    PdfPageNumberEvent(Document doc, String parameter, boolean isMonthly) {
        this.doc = doc;
        this.parameter = parameter;
        this.isMonthly = isMonthly;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        Table table = new Table(1);
        table.addCell(fineCell((format(isMonthly ? "Monatsaufstellung %s - Seite %d" : "Rechnung %s - Seite %d", parameter, docEvent.getDocument().getPageNumber(page)))));
        table.setFixedPosition(page.getPageSize().getWidth() / 2, 50, 200);
        doc.add(table);
    }

}
