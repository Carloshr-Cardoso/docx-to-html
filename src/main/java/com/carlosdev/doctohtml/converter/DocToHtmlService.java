package com.carlosdev.doctohtml.converter;

import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocToHtmlService {

    public String convertDoc(MultipartFile file) {
        try {
            return convertDocxToHtml(file);
        } catch (IOException e) {
            return null;
        }
    }

    private String convertDocxToHtml(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            StringBuilder htmlBuilder = new StringBuilder();

            // Processar todos os elementos na ordem em que aparecem no documento
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph paragraph) {
                    htmlBuilder.append(generateHtmlFromParagraph(paragraph));
                } else if (element instanceof XWPFTable table) {
                    htmlBuilder.append(generateHtmlFromTable(table));
                }
            }

            return htmlBuilder.toString();
        }
    }

    private Element generateHtmlFromParagraph(XWPFParagraph paragraph) {
        boolean isHeading = paragraph.getStyle() != null && paragraph.getStyle().equals("Heading1");
        String tagName = isHeading ? "h1" : "p";
        Element htmlParagraph = new Element(Tag.valueOf(tagName), "");

        System.out.println("Estilo -> ["+paragraph.getStyleID() +"] ----> " + paragraph.getText() + "|| " + paragraph.getNumFmt());
        htmlParagraph.attr("style", StylesUtils.getSpigrafeStyle());
        htmlParagraph.attr("class", paragraph.getStyle());

        // Iterar sobre os runs para adicionar texto e hyperlinks
        for (XWPFRun run : paragraph.getRuns()) {
            if (run instanceof XWPFHyperlinkRun hyperlinkRun) {
                htmlParagraph.appendChild(generateHyperlinkFromParagraph(hyperlinkRun));
            } else {
                String text = run.text();
                htmlParagraph.appendText(text);
            }
        }

        return htmlParagraph;
    }

    private String generateHtmlFromTable(XWPFTable table) {
        Element htmlTable = new Element(Tag.valueOf("table"), "");
        htmlTable.attr("border", "1");

        // Processar as linhas e células da tabela
        for (XWPFTableRow row : table.getRows()) {
            Element htmlRow = new Element(Tag.valueOf("tr"), "");

            for (XWPFTableCell cell : row.getTableCells()) {
                Element htmlCell = new Element(Tag.valueOf("td"), "");
                htmlCell.text(cell.getText());

                // Verificar hyperlinks nas células e adicionar ao conteúdo
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    for (XWPFRun run : paragraph.getRuns()) {
                        if (run instanceof XWPFHyperlinkRun hyperlinkRun) {
                            htmlCell.appendChild(generateHyperlinkFromParagraph(hyperlinkRun));
                        }
                    }
                }
                htmlRow.appendChild(htmlCell);
            }

            htmlTable.appendChild(htmlRow);
        }
        return htmlTable.outerHtml();
    }

    private Element generateHyperlinkFromParagraph(XWPFHyperlinkRun hyperlinkRun) {
        String hyperlinkText = hyperlinkRun.getText(0);
        String hyperlinkUrl = hyperlinkRun.getHyperlink(hyperlinkRun.getDocument()).getURL();

        Element linkElement = new Element(Tag.valueOf("a"), "");
        linkElement.attr("href", hyperlinkUrl);
        linkElement.text(hyperlinkText);

        return linkElement;
    }




}
