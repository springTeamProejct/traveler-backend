package traveler.travel.global.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.post.entity.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserImageUpDto {
    private MultipartFile file;

    public File toEntity(String imageFileName,
                         String storedFileName,
                         String profileExtension,
                         Long profileSize,
                         String uploadFolder){
        return File.builder()
                .originName(imageFileName)
                .storedName(storedFileName)
                .extension(profileExtension)
                .size(profileSize)
                .uploadDir(uploadFolder)
                .build();
    }
}
