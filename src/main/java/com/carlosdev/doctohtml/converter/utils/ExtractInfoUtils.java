package com.carlosdev.doctohtml.converter.utils;

import com.carlosdev.doctohtml.converter.AtoLegislativoDTO;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class ExtractInfoUtils {
    private ExtractInfoUtils() {
    }

    public static AtoLegislativoDTO extrairInformacoes(MultipartFile file){
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

    private static void preencheInformacoesFromParagraph(XWPFParagraph paragraph, AtoLegislativoDTO ato) {

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

}
