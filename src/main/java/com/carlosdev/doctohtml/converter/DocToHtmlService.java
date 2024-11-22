package com.carlosdev.doctohtml.converter;

import com.carlosdev.doctohtml.converter.utils.ConvertDocx;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocToHtmlService {

    public String convertDocxToHtml(MultipartFile file) {
        return ConvertDocx.toHtml(file);
    }

}
