package com.carlosdev.doctohtml.converter;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return docToHtmlService.convertDocxToHtml(file);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<AtoLegislativoDTO> extractInfo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(docToHtmlService.extrairInformacoes(file));
    }



}