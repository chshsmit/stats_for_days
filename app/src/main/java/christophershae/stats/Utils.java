package christophershae.stats;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by chrissmith on 11/16/17.
 */

public class Utils {


    private static FirebaseDatabase mDataBase;

    public static FirebaseDatabase getDatabase() {
        if (mDataBase == null) {
            mDataBase = FirebaseDatabase.getInstance();
            mDataBase.setPersistenceEnabled(true);
        }
        return mDataBase;
    }

}
