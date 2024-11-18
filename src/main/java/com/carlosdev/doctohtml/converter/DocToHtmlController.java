package com.carlosdev.doctohtml.converter;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class DocToHtmlController {

    private final DocToHtmlService docToHtmlService;
    private final ExtractInfoService extractInfoService;

    public DocToHtmlController(DocToHtmlService docToHtmlService,
                               ExtractInfoService extractInfoService) {
        this.docToHtmlService = docToHtmlService;
        this.extractInfoService = extractInfoService;
    }

    @PostMapping(value = "/convert", produces = MediaType.TEXT_HTML_VALUE)
    public String convertDocToHtml(@RequestParam("file") MultipartFile file) {
        return docToHtmlService.convertDocxToHtml(file);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<AtoLegislativoDTO> extractInfo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(extractInfoService.extrairInformacoes(file));
    }

    @PostMapping(value = "/convert-doc", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> convertDocToHtmlTeste(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            HWPFDocument document = new HWPFDocument(inputStream);
            Range range = document.getRange();
            StringBuilder htmlContent = new StringBuilder();

            // Process paragraphs and tables
            TableIterator tableIterator = new TableIterator(range);

            for (int i = 0; i < range.numParagraphs(); i++) {
                Paragraph paragraph = range.getParagraph(i);

                // Check if the current paragraph is part of a table
                if (tableIterator.hasNext()) {
                    Table table = tableIterator.next();
                    htmlContent.append(convertTableToHtml(table));
                    // Skip paragraphs that are part of the current table
                    i += table.numParagraphs() - 1;
                } else {

                    System.out.println("\n\n\n");
                    StyleSheet style = document.getStyleSheet();
                    for (char ch: paragraph.text().trim().toCharArray()) {
                        var intChar = (int) ch;
                        System.out.println(ch + "---> [%d]".formatted(intChar));
                    }
                    System.out.println("\n\n\n");

                    /*System.out.println("\n\n\n\n");
                    System.out.println(paragraph.text().trim() +" ----> [%s]".formatted(style.getStyleDescription(paragraph.getStyleIndex()).getName()));
                    System.out.println("\n\n\n\n");*/
                    String styleName = paragraph.getStyleIndex() != -1 ? style.getStyleDescription(paragraph.getStyleIndex()).getName() : "";
                    htmlContent.append("<p class=\"").append(styleName).append("\">")
                            .append(processHyperlinks(paragraph.text().trim()))
                            .append("</p>");
                }
            }

            return ResponseEntity.ok(htmlContent.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }

    public static String processHyperlinks(String input) {
        if (!input.contains("HYPERLINK")) {
            return input;
        }

        // Remove o símbolo ASCII 19 da string
        input = input.replace("\u0013", "");

        // Expressão regular para encontrar hyperlinks no formato especificado
        String regex = "HYPERLINK \"(.*?)\".*?(.*?)";
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

    private String convertTableToHtml(Table table) {
        StringBuilder tableHtml = new StringBuilder("<table>");

        for (int rowIndex = 0; rowIndex < table.numRows(); rowIndex++) {
            TableRow row = table.getRow(rowIndex);
            tableHtml.append("<tr>");

            for (int colIndex = 0; colIndex < row.numCells(); colIndex++) {
                tableHtml.append("<td>")
                        .append(row.getCell(colIndex).text().trim())
                        .append("</td>");
            }

            tableHtml.append("</tr>");
        }

        tableHtml.append("</table>");
        return tableHtml.toString();
    }



}