package traveler.travel.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import traveler.travel.controller.dto.ResponseDto;
import traveler.travel.entity.User;
import traveler.travel.enums.Authority;
import traveler.travel.service.UserService;

@RestController
public class UserApiController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user){
        System.out.println("save함수 호출");
        user.setAuthority(Authority.ROLE_USER);
        userService.회원가입(user);
        System.out.println("...");
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
