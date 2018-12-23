package com.mariakamachine.dentoice.util.invoice;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.FontFactory.COURIER;
import static com.itextpdf.text.FontFactory.getFont;
import static com.itextpdf.text.PageSize.A4;
import static java.lang.String.format;

public class PdfPageNumberEvent extends PdfPageEventHelper {

    private Long id;

    PdfPageNumberEvent(Long id) {
        this.id = id;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(A4.getWidth() - 100);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(ALIGN_CENTER);
        table.addCell(cell(format("Rechnung %d - Seite %d", id, writer.getCurrentPageNumber())));
        table.writeSelectedRows(0, -1, document.left(), 40, writer.getDirectContent());
    }

    private PdfPCell cell(String text) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(ALIGN_CENTER);
        cell.setPhrase(new Phrase(text, getFont(COURIER, 8)));
        cell.setBorder(0);
        return cell;
    }

}