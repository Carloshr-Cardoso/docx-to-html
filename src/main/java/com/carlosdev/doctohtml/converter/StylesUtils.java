package com.carlosdev.doctohtml.converter;

import org.springframework.lang.NonNull;

public class StylesUtils {
    private StylesUtils() {}

    public static final String ATTR_CLASS = "class";
    public static final String CONTAINER_STYLE = "width: 70%; margin: 0 auto; font-family:'arial';";
    public static final String EPIGRAFE_STYLE = "font-size: 13pt; font-weight: bold; text-align: center";
    public static final String DATA_PUBLICACAO_STYLE = "font-size: 8pt; display: list-item; list-style-position: inside;";
    public static final String EMENTA_STYLE = "font-size: 10pt; text-align: justify; width: 50%; margin-left: auto; margin-right: 0;";
    public static final String NOVA_REDACAO_STYLE = "font-size: 10pt; text-align: justify; text-indent: 4rem;";
    public static final String TEXTO_SIMPLES_STYLE = "font-size: 10pt; text-align: justify; text-indent: 2rem;";
    public static final String CARGO_STYLE = "font-size: 9pt; text-align: justify; text-indent: 2rem;";
    public static final String ORDEM_PREAMBULO_STYLE = "font-size: 12pt; font-weight: bold; text-indent: 2rem;";

    public static final String TAB_CENTRO_8 = "font-size: 8pt; text-align: center;";
    public static final String TAB_JUSTIFICA_8 = "font-size: 8pt; text-align: justify;";
    public static final String TAB_DIREITA_8 = "font-size: 8pt; text-align: justify;";


    public static String getClassFromId(@NonNull String idEstilo) {
        if (idEstilo.contains("epigrafe")) {
            return "epigrafe";
        } else if (idEstilo.contains("publicacao")) {
            return "publicacao";
        } else if (idEstilo.contains("ementa")) {
            return "ementa";
        } else if (idEstilo.contains("novaredacao")) {
            return "novaredacao";
        } else if (idEstilo.contains("preambulo")) {
            return "preambulo";
        } else if (idEstilo.contains("ordempreamb")) {
            return "ordempreamb";
        } else if (idEstilo.contains("texto")) {
            return "texto";
        } else if (idEstilo.contains("tabdireita8")) {
            return "tabdireita8";
        } else if (idEstilo.contains("tabjustifica8")) {
            return "tabjustifica8";
        } else if (idEstilo.contains("tabcentro8")) {
            return "tabcentro8";
        } else if (idEstilo.contains("TableParagraph")) {
            return "TableParagraph";
        } else if (idEstilo.contains("autoridade")) {
            return "autoridade";
        } else if (idEstilo.contains("cargo")) {
            return "cargo";
        }
        return null;

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
                ".tabcentro8 {" + TAB_CENTRO_8 + "}" +
                ".TableParagraph {" + TAB_CENTRO_8 + "}" +
                ".autoridade {" + TEXTO_SIMPLES_STYLE + "}" +
                ".cargo {" + CARGO_STYLE + "}";

    }
}
