package com.carlosdev.doctohtml.fileupload;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FolderReadService folderReadServiceService;

    public FileController(FolderReadService folderReadServiceService) {
        this.folderReadServiceService = folderReadServiceService;
    }

    @GetMapping
    public List<FileInfo> getFiles() throws IOException {
        return folderReadServiceService.listFiles();
    }
}