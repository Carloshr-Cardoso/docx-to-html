package com.carlosdev.doctohtml.converter;

import com.carlosdev.doctohtml.converter.utils.ConvertDoc;
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
    private final ExtractInfoService extractInfoService;

    public DocToHtmlController(DocToHtmlService docToHtmlService,
                               ExtractInfoService extractInfoService) {
        this.docToHtmlService = docToHtmlService;
        this.extractInfoService = extractInfoService;
    }

    @PostMapping(value = "/convert", produces = MediaType.TEXT_HTML_VALUE)
    public String convertDocToHtml(@RequestParam("file") MultipartFile file) {
        return docToHtmlService.convertDocxToHtml(file);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<AtoLegislativoDTO> extractInfo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(extractInfoService.extrairInformacoes(file));
    }

    @PostMapping(value = "/convert-doc", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> convertDocToHtmlTeste(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ConvertDoc.toHtml(file));
    }

}