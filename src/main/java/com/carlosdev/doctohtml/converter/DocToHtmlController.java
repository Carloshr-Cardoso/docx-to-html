package com.carlosdev.doctohtml.converter;

import org.springframework.http.MediaType;
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
        return docToHtmlService.convertDoc(file);
    }


}