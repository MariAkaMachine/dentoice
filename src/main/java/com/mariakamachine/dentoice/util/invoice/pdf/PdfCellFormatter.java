package com.mariakamachine.dentoice.util.invoice.pdf;


import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.math.BigDecimal;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.TextAlignment.*;


public class PdfCellFormatter {

    static Cell headerCell(String text, int colSpan) {
        return marginCell(text, LEFT, colSpan).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1));
    }

    static Cell headerCellRight(String text, int colSpan) {
        return marginCell(text, RIGHT, colSpan).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1));
    }

    private static Cell marginCell(String text, TextAlignment alignment, int colSpan) {
        Cell cell = cell(text, alignment, colSpan, NO_BORDER);
        cell.setPaddingTop(3);
        cell.setPaddingBottom(3);
        return cell;
    }

    static Cell cell(String text) {
        return cell(text, LEFT, 1, NO_BORDER);
    }

    static Cell cell(String text, int colSpan) {
        return cell(text, LEFT, colSpan, NO_BORDER);
    }

    static Cell cellRight(String text) {
        return cellRight(text, 1);
    }

    static Cell cellRight(String text, int colSpan) {
        return cell(text, RIGHT, colSpan, NO_BORDER);
    }

    static Cell fineCellCentered(String text) {
        return new Cell()
                .setTextAlignment(CENTER)
                .add(new Paragraph(text))
                .setFontSize(7f)
                .setItalic()
                .setBorder(NO_BORDER);
    }

    static Cell fineCell(String text) {
        return fineCell(text, 1);
    }

    static Cell fineCell(String text, int colSpan) {
        return new Cell(1, colSpan)
                .setTextAlignment(LEFT)
                .add(new Paragraph(text))
                .setFontSize(7f)
                .setItalic()
                .setBorder(NO_BORDER);
    }

    static Cell cell(String text, TextAlignment alignment, int colSpan, Border border) {
        return new Cell(1, colSpan)
                .add(new Paragraph(text))
                .setTextAlignment(alignment)
                .setFontSize(9f)
                .setBorder(border);
    }

    static void addEmptyRow(Table table) {
        Cell cell = cell(" ", table.getNumberOfColumns());
        table.addCell(cell);
    }

}
