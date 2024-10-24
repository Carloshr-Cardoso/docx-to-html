package com.carlosdev.doctohtml.converter;

import java.util.Objects;

public class StylesUtils {
    private StylesUtils() {}

    public static final String CONTAINER_STYLE = "width: 70%; margin: 0 auto; font-family:'arial';";
    public static final String EPIGRAFE_STYLE = "font-size: 13pt; font-weight: bold; text-align: center";
    public static final String DATA_PUBLICACAO_STYLE = "font-size: 8pt; display: list-item; list-style-position: inside;";
    public static final String EMENTA_STYLE = "font-size: 10pt; text-align: justify; width: 50%; margin-left: auto; margin-right: 0;";
    public static final String NOVA_REDACAO_STYLE = "font-size: 10pt; text-align: justify; text-indent: 4rem;";
    public static final String TEXTO_SIMPLES_STYLE = "font-size: 10pt; text-align: justify; text-indent: 2rem;";
    public static final String ORDEM_PREAMBULO_STYLE = "font-size: 12pt; font-weight: bold; text-indent: 2rem;";

    public static String getEstiloFromId(String idEstilo){
        if(Objects.nonNull(idEstilo)){
            if(idEstilo.contains("epigrafe")){
                return EPIGRAFE_STYLE;
            } else if(idEstilo.contains("publicacao")){
                return DATA_PUBLICACAO_STYLE;
            } else if(idEstilo.contains("ementa")){
                return EMENTA_STYLE;
            } else if(idEstilo.contains("novaredacao")){
                return NOVA_REDACAO_STYLE;
            } else if(idEstilo.contains("preambulo")){
                return "";
            } else if(idEstilo.contains("ordempreamb")){
                return ORDEM_PREAMBULO_STYLE;
            } else if(idEstilo.contains("texto")){
                return TEXTO_SIMPLES_STYLE;
            } else {
                return "";
            }
        }
        return "";
    }
}
