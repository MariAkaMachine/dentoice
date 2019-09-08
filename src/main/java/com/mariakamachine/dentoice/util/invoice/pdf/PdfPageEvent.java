package com.mariakamachine.dentoice.util.invoice.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.mariakamachine.dentoice.data.entity.DentistEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import static com.itextpdf.io.image.ImageDataFactory.create;
import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.TextAlignment.CENTER;
import static com.itextpdf.layout.property.TextAlignment.RIGHT;
import static com.mariakamachine.dentoice.util.invoice.pdf.PdfCellFormatter.*;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

@Slf4j
public class PdfPageEvent implements IEventHandler {

    private DentistEntity dentist;
    private String parameter;
    private boolean isMonthly;

    @Autowired
    private ResourceLoader resourceLoader;

    PdfPageEvent(DentistEntity dentist, String parameter, boolean isMonthly) {
        this.dentist = dentist;
        this.parameter = parameter;
        this.isMonthly = isMonthly;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument document = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), document);
        addRecipientDetails(document, page, canvas);
        addHeaderCell(document, page, canvas);
        addPageNumber(document, page, canvas);
        addPageFooter(document, page, canvas);
    }

    private void addRecipientDetails(PdfDocument document, PdfPage page, PdfCanvas canvas) {
        if (document.getPageNumber(page) != 1) {
            return;
        }
        Table table = new Table(1);
        table.addCell(fineCell("Udo Baumann • Waldkapellenweg 7 • 72108 Rottenburg"));
        String recipient = dentist.getTitle().concat(lineSeparator())
                .concat(dentist.getFirstName()).concat(" ").concat(dentist.getLastName()).concat(lineSeparator())
                .concat(dentist.getStreet()).concat(lineSeparator())
                .concat(dentist.getZip()).concat(" ").concat(dentist.getCity()).concat(lineSeparator());
        table.addCell(cell(recipient));
        new Canvas(canvas, document, new Rectangle(70, 600, page.getPageSize().getWidth() / 2, 100))
                .add(table);
    }

    private void addHeaderCell(PdfDocument document, PdfPage page, PdfCanvas canvas) {
        Table table = new Table(2);
        table.addCell(cell("Udo Baumann")
                .setFontSize(14f)
                .setItalic()
                .setBold());
        table.addCell(cell("Dental-Technik")
                .setFontSize(14f)
                .setItalic());
        try {
//            String path = ResourceUtils.getFile("classpath:BOOT-INF/classes/static/img/tooth_icon.png").getAbsolutePath();
            table.addCell(new Cell()
                    .add(new Image(create("classpath:BOOT-INF/classes/static/img/tooth_icon.png")).scaleToFit(50, 50))
                    .setBorder(NO_BORDER)
                    .setPaddingLeft(25));
        } catch (Exception e) {
            log.error("could not load golden tooth logo", e);
        }

        String address = "Waldkapellenweg 7".concat(lineSeparator())
                .concat("72108 Rottenburg").concat(lineSeparator())
                .concat("tel. 07073 / 919 718").concat(lineSeparator())
                .concat("fax 07073 / 919 719").concat(lineSeparator());
        table.addCell(cell(address));
        new Canvas(canvas, document, new Rectangle(page.getPageSize().getWidth() - 240, 600, page.getPageSize().getWidth() / 2, 200))
                .add(table);
    }

    private void addPageNumber(PdfDocument document, PdfPage page, PdfCanvas canvas) {
        Table table = new Table(1)
                .useAllAvailableWidth()
                .addCell(fineCellCentered((format(isMonthly ? "Monatsaufstellung %s - Seite %d" : "Rechnung %s - Seite %d", parameter, document.getPageNumber(page)))));
        new Canvas(canvas, document, new Rectangle(0, 40, page.getPageSize().getWidth(), 20))
                .add(table);
    }


    private void addPageFooter(PdfDocument document, PdfPage page, PdfCanvas canvas) {
        Table bank = new Table(1)
                .useAllAvailableWidth()
                .addCell(cell("BW-Bank Tübingen • BIC: SOLA DEST • IBAN: DE 41 6005 0101 7477 5120 38")
                        .setTextAlignment(CENTER)
                        .setItalic());
        new Canvas(canvas, document, new Rectangle(0, 27, page.getPageSize().getWidth(), 20))
                .add(bank);

        Table ziw_logo = new Table(1).useAllAvailableWidth();
        try {
//            String uri = ResourceUtils.getFile("classpath:BOOT-INF/classes/static/img/logo-ziw-bw.png").getAbsolutePath();
            ziw_logo.addCell(new Cell()
                    .add(new Image(create("classpath:BOOT-INF/classes/static/img/logo-ziw-bw.png")).scaleToFit(65, 65))
                    .setBorder(NO_BORDER)
                    .setTextAlignment(RIGHT));
        } catch (Exception e) {
            log.error("could not load ziw logo", e);
        }
        new Canvas(canvas, document, new Rectangle(page.getPageSize().getWidth() - 100, 50, page.getPageSize().getWidth() / 4, 20))
                .add(ziw_logo);
    }

}
