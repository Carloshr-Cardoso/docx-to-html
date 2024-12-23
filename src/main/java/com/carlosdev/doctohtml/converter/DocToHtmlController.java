package com.carlosdev.doctohtml.converter;

import com.carlosdev.doctohtml.converter.utils.ConvertDoc;
import com.carlosdev.doctohtml.converter.utils.ExtractInfoUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class DocToHtmlController {

    private final DocToHtmlService docToHtmlService;

    public DocToHtmlController(DocToHtmlService docToHtmlService) {
        this.docToHtmlService = docToHtmlService;
    }

    @PostMapping(value = "/convert", produces = MediaType.TEXT_HTML_VALUE)
    public String convertDocToHtml(@RequestParam("file") MultipartFile file) {
        return docToHtmlService.convertDocToHtml(file);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<AtoLegislativoDTO> extractInfo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ExtractInfoUtils.extrairInformacoes(file));
    }

    @PostMapping(value = "/convert-doc", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> convertDocToHtmlTeste(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ConvertDoc.toHtml(file));
    }

}