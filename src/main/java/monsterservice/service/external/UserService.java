package monsterservice.service.external;

import monsterservice.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    public User getUser() {
        return restTemplate.getForEntity(
                "https://randomuser.me/api/?nat=US&inc=name,gender,nat&noinfo",
                User.class).getBody();
    }
}
