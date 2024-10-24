package com.carlosdev.doctohtml.converter;

import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.carlosdev.doctohtml.converter.StylesUtils.CONTAINER_STYLE;

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
            Element htmlDiv = new Element(Tag.valueOf("div"), "");
            htmlDiv.attr("style", CONTAINER_STYLE);

            // Processar todos os elementos na ordem em que aparecem no documento
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph paragraph) {
                    htmlDiv.appendChild(generateHtmlFromParagraph(paragraph));
                } else if (element instanceof XWPFTable table) {
                    htmlDiv.appendChild(generateHtmlFromTable(table));
                }
            }

            return htmlDiv.outerHtml();
        }
    }

    private Element generateHtmlFromParagraph(XWPFParagraph paragraph) {
        Element htmlParagraph = new Element(Tag.valueOf("p"), "")
                .attr("style", StylesUtils.getEstiloFromId(paragraph.getStyle()));

        for (XWPFRun run : paragraph.getRuns()) {
            if (run instanceof XWPFHyperlinkRun hyperlinkRun) {
                htmlParagraph.appendChild(generateHyperlinkFromParagraph(hyperlinkRun));
            } else {
                if(run.isBold()) {
                    Element bElement = new Element(Tag.valueOf("b"), "");
                    bElement.text(run.text());
                    htmlParagraph.appendChild(bElement);
                }else{
                    htmlParagraph.appendText(run.text());
                }
            }
        }

        return htmlParagraph;
    }

    private Element generateHtmlFromTable(XWPFTable table) {
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
                    htmlCell.attr("style", StylesUtils.getEstiloFromId(paragraph.getStyle()));
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
        return htmlTable;
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
