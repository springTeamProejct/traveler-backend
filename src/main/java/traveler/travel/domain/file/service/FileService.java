package traveler.travel.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.account.repository.UserImgRepository;
import traveler.travel.domain.file.utils.ImageUtils;
import traveler.travel.domain.post.entity.File;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final UserImgRepository userImgRepository;

//    프로필 파일 저장
    public String saveFile(MultipartFile file) throws IOException{
        UUID uuid = UUID.randomUUID();

        //수정된 파일 이름 -> 이미지의 고유성 보존을 위해 'UUID_이미지 원래 이름'으로 저장
        String storedFileName = uuid + "_" + file.getOriginalFilename();
        //파일 고유의 원래 이름
        String originFileName = file.getOriginalFilename();

        //새로 저장한 파일 이름을 어떻게 해야될까?
        //String imageFilePath = uploadFolder + "/" + originFileName;

        String profileExtension = file.getContentType();
        Long profileSize = file.getSize();

        File fileData = userImgRepository.save(
                File.builder()
                        .originName(originFileName)
                        .storedName(storedFileName)
                        .size(profileSize)
                        .extension(profileExtension)
                        .file(ImageUtils.compressImg(file.getBytes()))
                        .build()
        );

        if(fileData != null){
            return "file upload successfully : " + originFileName;
        }

        return null;
    }

    public byte[] downloadImage(String fileName){

        File imageData = userImgRepository.findByOriginName(fileName)
                .orElseThrow(RuntimeException::new);

        return ImageUtils.decompressImage(imageData.getFile());
    }
}
