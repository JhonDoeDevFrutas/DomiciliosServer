package jhondoe.com.domiciliosserver.data.model.dataAccess;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRealtimeDatabaseAPI {
    private static final String PATH_ORDER_STATUS   = "ordencompra";
    private static final String PATH_PRODUCTS       = "products";

    private DatabaseReference mReference;

    private static FirebaseRealtimeDatabaseAPI INSTANCE = null;

    private FirebaseRealtimeDatabaseAPI() {
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseRealtimeDatabaseAPI getInstance(){
        if (INSTANCE == null){
            INSTANCE = new FirebaseRealtimeDatabaseAPI();
        }
        return INSTANCE;
    }

    // referencias
    public DatabaseReference getReference(){
        return mReference;
    }

    public DatabaseReference getOrderStatusReference(){
        return getReference().child(PATH_ORDER_STATUS);
    }

    public DatabaseReference getProductsReference(){
        return getReference().child(PATH_PRODUCTS);
    }

}
