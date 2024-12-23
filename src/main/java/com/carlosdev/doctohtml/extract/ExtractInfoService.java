package com.carlosdev.doctohtml.extract;

import com.carlosdev.doctohtml.converter.AtoLegislativoDTO;

import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExtractInfoService {

    public AtoLegislativoDTO extractInfo(final MultipartFile file) {
        try{
            return ExtractInfoFromDocx.extract(file);
        } catch (OLE2NotOfficeXmlFileException e){
            return ExtractInfoFromDoc.extract(file);
        }
    }
}
