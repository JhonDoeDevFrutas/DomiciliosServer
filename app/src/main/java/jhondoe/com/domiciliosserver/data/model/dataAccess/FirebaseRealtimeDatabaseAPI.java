package jhondoe.com.domiciliosserver.data.model.dataAccess;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRealtimeDatabaseAPI {
    private DatabaseReference mReference;

    private static FirebaseRealtimeDatabaseAPI INSTANCE = null;

    private FirebaseRealtimeDatabaseAPI() {
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseRealtimeDatabaseAPI getIntance(){
        if (INSTANCE == null){
            INSTANCE = new FirebaseRealtimeDatabaseAPI();
        }
        return INSTANCE;
    }

    // referencias
    public DatabaseReference getReference(){
        return mReference;
    }
}
