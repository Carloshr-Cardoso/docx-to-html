package com.carlosdev.doctohtml.converter.utils;

import java.util.List;

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

    public static final String REMISSAO_STYLE = "font-size: 9pt; color: red; text-align: justify; text-align: justify; width: 80%; margin-left: auto; margin-right: 0;";

    public static final String RDANT_REMISSAO_STYLE = "font-size: 9pt; font-weight: bold; color: green; text-align: justify; text-align: justify; width: 65%; margin-left: auto; margin-right: 0;";

    public static final String RDANT_STYLE = "font-size: 9pt; color: green; text-align: justify; text-align: justify; width: 60%; margin-left: auto; margin-right: 0;";

    public static final String ORDEM_PREAMBULO_STYLE = "font-size: 12pt; font-weight: bold; text-indent: 2rem;";

    public static final String TAB_CENTRO_8 = "font-size: 8pt; text-align: center;";

    public static final String TAB_JUSTIFICA_8 = "font-size: 8pt; text-align: justify;";

    public static final String TAB_DIREITA_8 = "font-size: 8pt; text-align: justify;";

    private static final List<String> ESTILOS = List.of(
            "epigrafe",
            "publicacao",
            "ementa",
            "novaredacao",
            "preambulo",
            "ordem",
            "texto",
            "tabdireita8",
            "tabjustifica8",
            "tabcentro8",
            "TableParagraph",
            "autoridade",
            "cargo",
            "a05_nr",
            "a10_nr_remiss",
            "a11_rdant_remiss",
            "a12_rdant"
            );


    public static String getClassFromId(String idEstilo){
        return ESTILOS.stream().filter(idEstilo::contains).findFirst().orElse(idEstilo);
    }

    public static String addCssStylesToHtml() {
        return ".container {" + CONTAINER_STYLE + "}" +
                ".epigrafe {" + EPIGRAFE_STYLE + "}" +
                ".ementa {" + EMENTA_STYLE + "}" +
                ".novaredacao a05_nr {" + NOVA_REDACAO_STYLE + "}" +
                ".a05_nr {" + NOVA_REDACAO_STYLE + "}" +
                ".publicacao {" + DATA_PUBLICACAO_STYLE + "}" +
                ".ordem {" + ORDEM_PREAMBULO_STYLE + "}" +
                ".texto {" + TEXTO_SIMPLES_STYLE + "}" +
                ".tabdireita8 {" + TAB_DIREITA_8 + "}" +
                ".tabjustifica8 {" + TAB_JUSTIFICA_8 + "}" +
                ".tabcentro8 {" + TAB_CENTRO_8 + "}" +
                ".TableParagraph {" + TAB_CENTRO_8 + "}" +
                ".autoridade {" + TEXTO_SIMPLES_STYLE + "}" +
                ".cargo {" + CARGO_STYLE + "}" +
                ".a10_nr_remiss {" + REMISSAO_STYLE + "}" +
                ".a11_rdant_remiss {" + RDANT_REMISSAO_STYLE + "}" +
                ".a12_rdant {" + RDANT_STYLE + "}";

    }
}
