package com.carlosdev.doctohtml.extract;

import com.carlosdev.doctohtml.converter.AtoLegislativoDTO;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.carlosdev.doctohtml.converter.utils.ExtractInfoUtils.extrairLocalEData;
import static java.util.Objects.isNull;

public class ExtractInfoFromDoc {
    private ExtractInfoFromDoc() {
    }

    public static AtoLegislativoDTO extract(MultipartFile file){
        AtoLegislativoDTO ato = new AtoLegislativoDTO();

        try (HWPFDocument document = new HWPFDocument(file.getInputStream())) {
            preencheInformacoesFromParagraph(document, ato);
            return ato;
        } catch (IOException e) {
            return null;
        }
    }

    private static void preencheInformacoesFromParagraph(HWPFDocument document, AtoLegislativoDTO ato) {
        Range range = document.getRange();

        for (int i = 0; i < range.numParagraphs(); i++) {
            Paragraph paragraph = range.getParagraph(i);
            StyleSheet style = document.getStyleSheet();

            String idEstilo = paragraph.getStyleIndex() != -1 ? style.getStyleDescription(paragraph.getStyleIndex()).getName() : "";
            String textoParagrafo = paragraph.text().trim();

            if(!StringUtils.isBlank(idEstilo)){
                if (idEstilo.contains("epigrafe")) {
                    ato.setEpigrafe(textoParagrafo);
                } else if (idEstilo.contains("publicacao") && isNull(ato.getLocalPublicacao())) {
                    extrairLocalEData(textoParagrafo, ato);
                } else if (idEstilo.contains("ementa")) {
                    ato.setEmenta(textoParagrafo);
                }
            }

        }
    }

}
