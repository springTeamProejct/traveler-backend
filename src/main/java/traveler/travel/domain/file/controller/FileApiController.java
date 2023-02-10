package traveler.travel.domain.file.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.file.service.FileService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileApiController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException {
        String uploadImage = fileService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @GetMapping(value = "/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") Long fileId) throws IOException {
        byte[] downloadImg = fileService.downloadImgFromFileSystem(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(downloadImg);
    }
}
