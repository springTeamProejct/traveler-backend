package traveler.travel.domain.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.enums.TravelType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Travel{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;
    private boolean gatherYn;
    private int maxCnt; // 모집 인원
    private int nowCnt; // 현재 참여 인원
    private TravelType travelType; // 즉시참여, 대화후참여
    private Gender gender;
    private int minAge;
    private int maxAge;
    private double xPos;
    private double yPos;
    private String location;
    private LocalDateTime dateTime;

    @Builder
    public Travel(int maxCnt, TravelType travelType, Gender gender, int maxAge, int minAge
            , double xPos, double yPos, String location, LocalDateTime dateTime) {
        this.maxCnt = maxCnt;
        this.travelType = travelType;
        this.gender = gender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.xPos = xPos;
        this.yPos = yPos;
        this.location = location;
        this.dateTime = dateTime;
        nowCnt = 1;
        gatherYn = true;
    }

    public void changeGatherYn(boolean tf) {
        gatherYn = tf;
    }

    public void update(Integer maxCnt, TravelType travelType, Gender gender, Integer maxAge, Integer minAge
            , Double xPos, Double yPos, String location, LocalDateTime dateTime, Boolean gatherYn) {
        if (maxCnt != null) this.maxCnt = maxCnt;
        if (travelType != null) this.travelType = travelType;
        if (gender != null) this.gender = gender;
        if (minAge != null) this.minAge = minAge;
        if (maxAge != null) this.maxAge = maxAge;
        if (xPos != null) this.xPos = xPos;
        if (yPos != null) this.yPos = yPos;
        if (location != null) this.location = location;
        if (dateTime != null) this.dateTime = dateTime;
        if (gatherYn != null) changeGatherYn(gatherYn);
    }
}
