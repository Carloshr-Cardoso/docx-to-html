package com.carlosdev.doctohtml.converter;

import org.springframework.lang.NonNull;

import java.util.Map;

public class StylesUtils {
    private StylesUtils() {}

    public static final String ATTR_CLASS = "class";
    public static final String CONTAINER_STYLE = "width: 70%; margin: 0 auto; font-family:'arial';";
    public static final String EPIGRAFE_STYLE = "font-size: 13pt; font-weight: bold; text-align: center";
    public static final String DATA_PUBLICACAO_STYLE = "font-size: 8pt; display: list-item; list-style-position: inside;";
    public static final String EMENTA_STYLE = "font-size: 10pt; text-align: justify; width: 50%; margin-left: auto; margin-right: 0;";
    public static final String NOVA_REDACAO_STYLE = "font-size: 10pt; text-align: justify; text-indent: 4rem;";
    public static final String TEXTO_SIMPLES_STYLE = "font-size: 10pt; text-align: justify; text-indent: 2rem;";
    public static final String ORDEM_PREAMBULO_STYLE = "font-size: 12pt; font-weight: bold; text-indent: 2rem;";

    public static final String TAB_CENTRO_8 = "font-size: 8pt; text-align: center;";
    public static final String TAB_JUSTIFICA_8 = "font-size: 8pt; text-align: justify;";
    public static final String TAB_DIREITA_8 = "font-size: 8pt; text-align: justify;";

    private static final Map<String, String> ESTILOS = Map.of(
            "epigrafe", "epigrafe",
            "publicacao", "publicacao",
            "ementa", "ementa",
            "novaredacao", "novaredacao",
            "preambulo", "preambulo",
            "ordempreamb", "ordempreamb",
            "texto", "texto",
            "tabdireita8", "tabdireita8",
            "tabjustifica8", "tabjustifica8",
            "tabcentro8", "tabcentro8"
    );

    public static String getClassFromId(@NonNull String idEstilo){
        return ESTILOS.keySet().stream()
                .filter(idEstilo::contains)
                .findFirst()
                .map(ESTILOS::get)
                .orElse("");
    }

    public static String addCssStylesToHtml() {
        return ".container {" + CONTAINER_STYLE + "}" +
                ".epigrafe {" + EPIGRAFE_STYLE + "}" +
                ".ementa {" + EMENTA_STYLE + "}" +
                ".novaredacao {" + NOVA_REDACAO_STYLE + "}" +
                ".publicacao {" + DATA_PUBLICACAO_STYLE + "}" +
                ".ordempreamb {" + ORDEM_PREAMBULO_STYLE + "}" +
                ".texto {" + TEXTO_SIMPLES_STYLE + "}" +
                ".tabdireita8 {" + TAB_DIREITA_8 + "}" +
                ".tabjustifica8 {" + TAB_JUSTIFICA_8 + "}" +
                ".tabcentro8 {" + TAB_CENTRO_8 + "}";

    }
}
