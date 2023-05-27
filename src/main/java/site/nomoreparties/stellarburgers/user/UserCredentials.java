package site.nomoreparties.stellarburgers.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {

    private String email;
    private String password;

    public static UserCredentials from(User user){
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

}
