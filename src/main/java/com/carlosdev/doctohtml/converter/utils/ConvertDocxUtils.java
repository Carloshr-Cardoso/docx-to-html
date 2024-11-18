package com.carlosdev.doctohtml.converter.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.carlosdev.doctohtml.converter.utils.StylesUtils.addCssStylesToHtml;
import static com.carlosdev.doctohtml.converter.utils.StylesUtils.getClassFromId;

public class ConvertDocxUtils {
    private ConvertDocxUtils() {
    }


    public static String convertDocxToHtml(MultipartFile file) {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            StringBuilder htmlBuilder = new StringBuilder();

            Element styleTag = new Element(Tag.valueOf("style"), "");
            styleTag.append(addCssStylesToHtml());

            htmlBuilder.append(styleTag);

            Element htmlDiv = new Element(Tag.valueOf("div"), "");
            htmlDiv.attr("class", "container");

            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph paragraph) {
                    htmlDiv.appendChild(generateHtmlFromParagraph(paragraph));
                } else if (element instanceof XWPFTable table) {
                    htmlDiv.appendChild(generateHtmlFromTable(table));
                }
            }

            htmlBuilder.append(htmlDiv);
            return htmlBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static Element generateHtmlFromParagraph(XWPFParagraph paragraph) {
        Element htmlParagraph = new Element(Tag.valueOf("p"), "");

        var classe = getClassFromId(paragraph.getStyle());
        if (classe != null)
            htmlParagraph.attr("class", classe);

        for (XWPFRun run : paragraph.getRuns()) {
            if (run instanceof XWPFHyperlinkRun hyperlinkRun) {
                htmlParagraph.appendChild(generateHyperlinkFromParagraph(hyperlinkRun));
            } else {
                if (run.isBold()) {
                    Element bElement = new Element(Tag.valueOf("b"), "");
                    bElement.text(run.text());
                    htmlParagraph.appendChild(bElement);
                } else {
                    htmlParagraph.appendText(run.text());
                }
            }
        }

        return htmlParagraph;
    }

    private static Element generateHyperlinkFromParagraph(XWPFHyperlinkRun hyperlinkRun) {
        String hyperlinkText = hyperlinkRun.getText(0);
        String hyperlinkUrl = hyperlinkRun.getHyperlink(hyperlinkRun.getDocument()).getURL();

        Element linkElement = new Element(Tag.valueOf("a"), "");
        linkElement.attr("href", hyperlinkUrl);
        linkElement.text(hyperlinkText);

        return linkElement;
    }

    private static Element generateHtmlFromTable(XWPFTable table) {
        Element htmlTable = new Element(Tag.valueOf("table"), "");
        htmlTable.attr("border", "1");

        for (XWPFTableRow row : table.getRows()) {
            Element htmlRow = new Element(Tag.valueOf("tr"), "");

            for (XWPFTableCell cell : row.getTableCells()) {
                Element htmlCell = new Element(Tag.valueOf("td"), "");
                htmlCell.text(cell.getText());

                checkHyperlinkFromTable(cell, htmlCell);
                htmlRow.appendChild(htmlCell);
            }

            htmlTable.appendChild(htmlRow);
        }
        return htmlTable;
    }

    private static void checkHyperlinkFromTable(XWPFTableCell cell, Element htmlCell) {
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            var classe = getClassFromId(paragraph.getStyle());
            if (classe != null)
                htmlCell.attr("class", classe);

            for (XWPFRun run : paragraph.getRuns()) {
                if (run instanceof XWPFHyperlinkRun hyperlinkRun) {
                    htmlCell.appendChild(generateHyperlinkFromParagraph(hyperlinkRun));
                }
            }
        }
    }

}
