//package com.mariakamachine.dentoice.util.invoice.pdf;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfPageEventHelper;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import static com.itextpdf.text.Element.ALIGN_CENTER;
//import static com.itextpdf.text.FontFactory.COURIER;
//import static com.itextpdf.text.FontFactory.getFont;
//import static com.itextpdf.text.PageSize.A4;
//import static java.lang.String.format;
//
//public class PdfPageNumberEvent extends PdfPageEventHelper {
//
//    private String parameter;
//    private boolean isMonthly;
//
//    PdfPageNumberEvent(String parameter, boolean isMonthly) {
//        this.parameter = parameter;
//        this.isMonthly = isMonthly;
//    }
//
//    @Override
//    public void onEndPage(PdfWriter writer, Document document) {
//        PdfPTable table = new PdfPTable(1);
//        table.setTotalWidth(A4.getWidth() - 100);
//        table.setLockedWidth(true);
//        table.setHorizontalAlignment(ALIGN_CENTER);
//        table.addCell(cell(format(isMonthly ? "Monatsaufstellung %s - Seite %d" : "Rechnung %s - Seite %d", parameter, writer.getCurrentPageNumber())));
//        table.writeSelectedRows(0, -1, document.left(), 40, writer.getDirectContent());
//    }
//
//    private PdfPCell cell(String text) {
//        PdfPCell cell = new PdfPCell();
//        cell.setHorizontalAlignment(ALIGN_CENTER);
//        cell.setPhrase(new Phrase(text, getFont(COURIER, 8)));
//        cell.setBorder(0);
//        return cell;
//    }
//
//}
