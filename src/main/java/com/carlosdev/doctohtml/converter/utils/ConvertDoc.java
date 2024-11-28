package com.carlosdev.doctohtml.converter.utils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.carlosdev.doctohtml.converter.utils.StylesUtils.addCssStylesToHtml;
import static com.carlosdev.doctohtml.converter.utils.StylesUtils.getClassFromId;

public class ConvertDoc {
    private ConvertDoc() {}

    public static String toHtml(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            StringBuilder htmlContent = new StringBuilder();
            htmlContent
                    .append("<style type='text/css'>")
                    .append(addCssStylesToHtml())
                    .append("</style>")
                    .append("<div class='container'>");

            processaCorpoDocumento(htmlContent, new HWPFDocument(inputStream));

            htmlContent.append("</div>");
            return htmlContent.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static void processaCorpoDocumento(StringBuilder htmlContent, HWPFDocument document){
        Range range = document.getRange();
        TableIterator tableIterator = new TableIterator(range);

        for (int i = 0; i < range.numParagraphs(); i++) {
            Paragraph paragraph = range.getParagraph(i);
            StyleSheet style = document.getStyleSheet();

            // Verifica se o parágrafo é parte de uma tabela
            if (tableIterator.hasNext() && paragraph.isInTable()) {
                Table table = tableIterator.next();
                String styleName = paragraph.getStyleIndex() != -1 ? style.getStyleDescription(paragraph.getStyleIndex()).getName() : "";

                htmlContent.append(convertTableToHtml(table, getClassFromId(styleName)));
                // Pula os parágrafos pertencentes à tabela
                while (i < range.numParagraphs() && range.getParagraph(i).isInTable()) {
                    i++;
                }
                i--;
            } else {
                String styleName = paragraph.getStyleIndex() != -1 ? style.getStyleDescription(paragraph.getStyleIndex()).getName() : "";

                htmlContent
                        .append("<p class='%s' >".formatted(getClassFromId(styleName)))
                        .append(processHyperlinks(paragraph))
                        .append("</p>");
            }

        }
    }

    private static String processaParagrafos(Paragraph paragraph) {
        StringBuilder paragraphContent = new StringBuilder();
        for (int j=0; j< paragraph.numCharacterRuns(); j++){

            if (paragraph.getCharacterRun(j).isBold()){
                paragraphContent
                        .append("<b>")
                        .append(paragraph.getCharacterRun(j).text().trim())
                        .append("</b>");
            } else {
                paragraphContent.append(paragraph.getCharacterRun(j).text().trim());
            }
        }

        return paragraphContent.toString();
    }

    private static String processHyperlinks(Paragraph paragraph) {
        String input = paragraph.text().trim();
        if (!input.contains("HYPERLINK")) {
            return processaParagrafos(paragraph);
        }

        // Remove o símbolo ASCII 19 da string
        input = input.replace("\u0013", "");

        // Expressão regular para encontrar hyperlinks no formato especificado
        String regex = "HYPERLINK \"(.*?)\".*?\u0014(.*?)\u0015";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // StringBuilder para construir a saída formatada
        StringBuilder output = new StringBuilder();

        // Índice de início para copiar texto não correspondente
        int lastEnd = 0;

        // Itera sobre todas as correspondências encontradas
        while (matcher.find()) {
            // Adiciona texto não correspondente antes do hyperlink
            output.append(input, lastEnd, matcher.start());

            // Formata o hyperlink encontrado
            String url = matcher.group(1);
            String text = matcher.group(2);
            output.append("<a href=\"").append(url).append("\">").append(text).append("</a>");

            // Atualiza o índice de fim da última correspondência
            lastEnd = matcher.end();
        }

        // Adiciona qualquer texto restante após o último hyperlink
        output.append(input.substring(lastEnd));

        return output.toString();
    }

    private static String convertTableToHtml(Table table, String style) {
        StringBuilder html = new StringBuilder("<table border='1' class='%s'>".formatted(style));

        for (int rowIdx = 0; rowIdx < table.numRows(); rowIdx++) {
            TableRow row = table.getRow(rowIdx);
            html.append("<tr>");

            for (int colIdx = 0; colIdx < row.numCells(); colIdx++) {
                TableCell cell = row.getCell(colIdx);
                html.append("<td>");

                for (int p = 0; p < cell.numParagraphs(); p++) {
                    Paragraph cellParagraph = cell.getParagraph(p);
                    html.append(processaParagrafos(cellParagraph));
                }

                html.append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");
        return html.toString();
    }
}
