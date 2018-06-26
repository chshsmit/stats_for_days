package christophershae.stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chrissmith on 10/19/17.
 */

public class User implements Serializable {
    public String email;

    public Map<String, List<BasketballPlayer>> userRosters = new HashMap<String, List<BasketballPlayer>>();


    public User(){

    }

    public User(String email){
        this.email = email;
    }

}
