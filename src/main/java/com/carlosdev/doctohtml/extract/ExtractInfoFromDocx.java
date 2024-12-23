package com.carlosdev.doctohtml.extract;

import com.carlosdev.doctohtml.converter.AtoLegislativoDTO;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.carlosdev.doctohtml.converter.utils.ExtractInfoUtils.extrairLocalEData;
import static java.util.Objects.isNull;

public class ExtractInfoFromDocx {
    private ExtractInfoFromDocx() {
    }

    public static AtoLegislativoDTO extract(MultipartFile file){
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
}
