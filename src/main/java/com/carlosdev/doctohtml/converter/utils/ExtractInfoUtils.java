package com.carlosdev.doctohtml.converter.utils;

import com.carlosdev.doctohtml.converter.AtoLegislativoDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractInfoUtils {
    private ExtractInfoUtils() {
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
