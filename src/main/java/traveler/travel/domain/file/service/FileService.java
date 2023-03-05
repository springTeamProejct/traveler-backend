package traveler.travel.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.repository.UserImgRepository;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.domain.post.entity.File;
import traveler.travel.global.exception.BadRequestException;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final UserImgRepository userImgRepository;

    private final UserRepository userRepository;

    @Value("${file.path}")
    private String FOLDER_PATH;

    //file 아이디 찾기
    public File findOne(Long fileId) {

        //파일이 존재하지 않는 경우
        File file = userImgRepository.findById(fileId).orElseThrow(() ->
                new BadRequestException("L00"));

        //이미 삭제된 파일일 경우 true, 삭제가 안된 경우 false
        boolean matches = file.isDeleted();

        if (matches == true) {
            throw new BadRequestException("J07");
        }

        return file;
    }

    //file의 originName으로 찾기
    public File findOneOriginName(String originName) {

        List<File> tableOriginName = userImgRepository.findByOriginName(originName);

        if (tableOriginName == null) {
            new BadRequestException("L00");
        }

        //이미 삭제된 파일일 경우 true, 삭제가 안된 경우 false
        //동적인 값을 넣은 이유는 어차피 사이즈가 1이상일 경우에는 예외처리를 해주기 때문에 상관없다고 판단.
        File file = tableOriginName.get(0);
        boolean matches = file.isDeleted();

        if (matches == true) {
            throw new BadRequestException("J07");
        }

        return file;

    }

    @Transactional
    public String uploadImageToFileSystem(MultipartFile file, User user) throws IOException {

        UUID uuid = UUID.randomUUID();

        //파일 고유의 원래 이름
        String originFileName = file.getOriginalFilename();

        String newFileName = createName();

        String profileExtension = file.getContentType();

        //변경된이름.확장자 형태로 변경이 필요.
        String storedFileName = uuid + "_" + newFileName;

        String filePath = FOLDER_PATH + storedFileName;

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

        File fileInfo = findOne(fileData.getId());
        user.setFile(fileInfo);
        userRepository.save(user);

        file.transferTo(new java.io.File(filePath));

        if (fileData != null) {
            return "Success";
        }
        return null;
    }

    public byte[] downloadImgFromFileSystem(Long fileId) throws IOException {

        File file = findOne(fileId);

        String filePath = file.getUploadDir();

        return Files.readAllBytes(new java.io.File(filePath).toPath());
    }

    //폴더, 파일 이름을 실행한 날짜 이름으로 바꾸는 기능
    private String createName() {
        //날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        //ex)저장할 폴더 이름을 20220210 이런 식으로 변환
        String str = sdf.format(date);

//        String newName = str.replace("-", java.io.File.separator);

        return str;
    }
}
