package traveler.travel.entity.post;

import lombok.Getter;
import traveler.travel.enums.Gender;
import traveler.travel.enums.TravelType;

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
