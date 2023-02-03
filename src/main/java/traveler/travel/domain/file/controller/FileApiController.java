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
        String uploadImage = fileService.saveFile(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }


    //다운로드
    //id를 통해서 일치하는 내용의 파일을 찾기.
    @GetMapping(value = "/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") Long fileId) {
        byte[] downloadImage = fileService.downloadImage(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(downloadImage);
    }
}
