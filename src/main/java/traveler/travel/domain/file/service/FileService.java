package traveler.travel.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.account.repository.UserImgRepository;
import traveler.travel.domain.file.utils.ImageUtils;
import traveler.travel.domain.post.entity.File;
import traveler.travel.global.exception.BadRequestException;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final UserImgRepository userImgRepository;

    //프로필 파일 저장
    public String saveFile(MultipartFile file) throws IOException{
        UUID uuid = UUID.randomUUID();

        //파일 고유의 원래 이름
        String originFileName = file.getOriginalFilename();

        //파일 이름은 영어만 등록 가능함.
        checkEng(originFileName);

        //수정된 파일 이름 -> 이미지의 고유성 보존을 위해 'UUID_이미지 원래 이름'으로 저장
        String storedFileName = uuid + "_" + originFileName;

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

    public byte[] downloadImage(Long fileId){
        File imgData = findOne(fileId);
        return ImageUtils.decompressImage(imgData.getFile());
    }

    //file 아이디 찾기
    public File findOne(Long fileId) {

        //파일이 존재하지 않는 경우
        File file = userImgRepository.findById(fileId).orElseThrow(() ->
                new BadRequestException("L00"));

        //이미 삭제된 파일일 경우 true, 삭제가 안된 경우 false
        boolean matches = file.isDeleted();

        if(matches == true){
            throw new BadRequestException("J07");
        }

        return file;
    }

    //파일 이름 영어인지 한글인지 체크
    public String checkEng(String fileName) {

        String pattern = "^[a-zA-Z0-9]+.[a-zA-Z]+$";
        boolean i = Pattern.matches(pattern, fileName);

        //true면 영어, false면 한글
        if (i == false) {
            throw new BadRequestException("F01");
        }
        return pattern;
    }
}
