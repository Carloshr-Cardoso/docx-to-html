package com.carlosdev.doctohtml.converter;

import com.carlosdev.doctohtml.converter.utils.ConvertDoc;
import com.carlosdev.doctohtml.converter.utils.ConvertDocx;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocToHtmlService {

    public String convertDocxToHtml(MultipartFile file) {
        return ConvertDocx.toHtml(file);
    }

    public String convertDocToHtml(MultipartFile file) {
        try{
            return ConvertDocx.toHtml(file);
        } catch (OLE2NotOfficeXmlFileException e){
            return ConvertDoc.toHtml(file);
        }
    }



}
