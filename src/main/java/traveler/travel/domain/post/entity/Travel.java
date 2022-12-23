package traveler.travel.domain.post.entity;

import lombok.Getter;
import traveler.travel.domain.account.enums.Gender;
import traveler.travel.domain.post.enums.TravelType;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
public class Travel extends Post {

    private String location;
    private boolean gatherYn;
    private LocalDate dateTime;
    private TravelType travelType; // 즉시참여, 대화후참여
    private Gender gender;
    private int minAge;
    private int maxAge;

}
