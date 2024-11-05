package com.carlosdev.doctohtml.fileupload;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

@Service
public class FolderReadService {

    @Value("${app.directory.path}")
    private String directoryPath;

    public List<FileInfo> listFiles() throws IOException{
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            return processarSubpastas(requireNonNull(directory.listFiles()));
        }
        return emptyList();
    }

    public List<FileInfo> processarSubpastas(File[] listaSubpasta) throws IOException{
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (File subpasta : listaSubpasta) {
            if (subpasta.isDirectory()){
                for (File subFile : requireNonNull(subpasta.listFiles())) {
                    if (subFile.isFile()) {
                        byte[] bytes = IOUtils.toByteArray(new FileInputStream(subFile));

                        FileInfo fileInfo = new FileInfo( subFile.getName(), subpasta.getName(), subFile.getAbsolutePath());
                        fileInfoList.add(fileInfo);
                    }
                }
            }
        }

        return fileInfoList;
    }
}
