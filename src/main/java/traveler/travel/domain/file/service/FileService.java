package traveler.travel.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.account.repository.UserImgRepository;
import traveler.travel.domain.post.entity.File;
import traveler.travel.global.exception.BadRequestException;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final UserImgRepository userImgRepository;

    private final String FOLDER_PATH = "/Users/gimjun-u/Desktop/test/temp/";

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

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {

        UUID uuid = UUID.randomUUID();

        //파일 고유의 원래 이름
        String originFileName = file.getOriginalFilename();

        String newFileName = createName();
        log.info("newFileName : " + newFileName);

        //수정된 파일 이름 -> 이미지의 고유성 보존을 위해 'UUID_이미지 원래 이름'으로 저장해야 하지만
        //db에 등록된 파일은 수정된 이름으로 uuid+_+db에 등록된 날짜 로 등록.
        String storedFileName = uuid + "_" + newFileName;

        String filePath = FOLDER_PATH + storedFileName;

        String profileExtension = file.getContentType();

        Long profileSize = file.getSize();

        File fileData = userImgRepository.save(
                File.builder()
                        .originName(originFileName)
                        .storedName(storedFileName)
                        .size(profileSize)
                        .extension(profileExtension)
                        .uploadDir(filePath)
                        .build()
        );

        file.transferTo(new java.io.File(filePath));

        if(fileData != null){
            return "file uploaded successfully! filePath : " + filePath;
        }
        return null;
    }


    public byte[] downloadImgFromFileSystem(Long fileId) throws IOException{

        File file = findOne(fileId);

        String filePath = file.getUploadDir();

        return Files.readAllBytes(new java.io.File(filePath).toPath());
    }

    //폴더, 파일 이름을 실행한 날짜 이름으로 바꾸는 기능
    private String createName(){
        //날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        //ex)저장할 폴더 이름을 20220210 이런 식으로 변환
        String str = sdf.format(date);

//        String newName = str.replace("-", java.io.File.separator);

        return str;
    }
}
