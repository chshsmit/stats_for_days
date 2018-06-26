package christophershae.stats;

/**
 * Created by chrissmith on 7/14/17.
 */

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SecureRandomString {
    private SecureRandom random = new SecureRandom();

    public String nextString() {
        return new BigInteger(130, random).toString(32);
    }

}