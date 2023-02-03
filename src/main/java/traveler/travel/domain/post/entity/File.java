package traveler.travel.domain.post.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.common.entity.BaseTimeEntity;

import javax.persistence.*;
import java.nio.file.Path;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String originName;  //원본 파일 이름
    private String storedName;  //변경된 파일 이름
    private Long size;
    private String uploadDir; // 저장 경로
    private String extension; // 확장자

    @Lob
    private byte[] file;

    @Builder
    public File(String originName
            , String storedName
            , Long size
            , String uploadDir
            , String extension
            , byte[] file){
        this.originName = originName;
        this.storedName = storedName;
        this.size = size;
        this.uploadDir = uploadDir;
        this.extension = extension;
        this.file = file;
    }

}
