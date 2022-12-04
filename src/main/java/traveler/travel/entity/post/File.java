package traveler.travel.entity.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import traveler.travel.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
public class File extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String originName;
    private String storedName;
    private Long size;
    private String uploadDir; // 저장 경로
    private String extension; // 확장자
    private String contentType; // ContentType
}
