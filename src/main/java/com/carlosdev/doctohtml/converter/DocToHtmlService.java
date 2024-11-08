package com.carlosdev.doctohtml.converter;

import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.carlosdev.doctohtml.converter.StylesUtils.*;
import static java.util.Objects.isNull;

@Service
public class DocToHtmlService {

    public String convertDocxToHtml(MultipartFile file) {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            StringBuilder htmlBuilder = new StringBuilder();

            Element styleTag = new Element(Tag.valueOf("style"), "");
            styleTag.append(addCssStylesToHtml());

            htmlBuilder.append(styleTag);

            Element htmlDiv = new Element(Tag.valueOf("div"), "");
            htmlDiv.attr(ATTR_CLASS, "container");

            // Processar todos os elementos na ordem em que aparecem no documento
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

    public AtoLegislativoDTO extrairInformacoes(MultipartFile file){
        AtoLegislativoDTO ato = new AtoLegislativoDTO();

        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph paragraph) {
                    preencheInformacoesFromParagraph(paragraph, ato);
                }
            }
            return ato;
        } catch (IOException e) {
            return null;
        }
    }


    private void preencheInformacoesFromParagraph(XWPFParagraph paragraph, AtoLegislativoDTO ato) {

        var idEstilo = paragraph.getStyle();
        var textoParagrafo = paragraph.getText();

        if(!isNull(idEstilo)){
            if (idEstilo.contains("epigrafe")) {
                ato.setEpigrafe(textoParagrafo);
            } else if (idEstilo.contains("publicacao") && isNull(ato.getLocalPublicacao())) {
                extrairLocalEData(textoParagrafo, ato);
            } else if (idEstilo.contains("ementa")) {
                ato.setEmenta(textoParagrafo);
            }
        }
    }


    public static void extrairLocalEData(String texto, AtoLegislativoDTO ato) {
        String regex = "(Publicad[ao]|Publica[çc][ãa]o) no (.+?) de (\\d{2})\\.(\\d{2})\\.(\\d{2,4})";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            String localPublicacao = matcher.group(2).trim();

            String dia = matcher.group(3);
            String mes = matcher.group(4);
            String ano = matcher.group(5);

            if (ano.length() == 2) {
                ano = "20" + ano; // Ajusta o ano para o formato completo
            }
            LocalDate dataPublicacao = LocalDate.parse(dia + "/" + mes + "/" + ano, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            ato.setDataPublicacao(dataPublicacao);
            ato.setLocalPublicacao(localPublicacao);
        }
    }


    private Element generateHtmlFromParagraph(XWPFParagraph paragraph) {
        Element htmlParagraph = new Element(Tag.valueOf("p"), "");

        var classe = getClassFromId(paragraph.getStyle());
        if (!isNull(classe))
            htmlParagraph.attr(ATTR_CLASS, classe);

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

                checkHyperlinkFromTable(cell, htmlCell);
                htmlRow.appendChild(htmlCell);
            }

            htmlTable.appendChild(htmlRow);
        }
        return htmlTable;
    }

    private void checkHyperlinkFromTable(XWPFTableCell cell, Element htmlCell){
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            var classe = getClassFromId(paragraph.getStyle());
            if (!isNull(classe))
                htmlCell.attr(ATTR_CLASS, classe);

            for (XWPFRun run : paragraph.getRuns()) {
                if (run instanceof XWPFHyperlinkRun hyperlinkRun) {
                    htmlCell.appendChild(generateHyperlinkFromParagraph(hyperlinkRun));
                }
            }
        }
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
