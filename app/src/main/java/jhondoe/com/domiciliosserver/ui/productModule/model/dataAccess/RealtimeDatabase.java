package jhondoe.com.domiciliosserver.ui.productModule.model.dataAccess;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.common.BasicErrorEventCallback;
import jhondoe.com.domiciliosserver.data.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import jhondoe.com.domiciliosserver.data.model.entities.Producto;
import jhondoe.com.domiciliosserver.ui.productModule.events.ProductEvent;
import jhondoe.com.domiciliosserver.ui.productModule.model.dataAccess.ProductsEventListener;

public class RealtimeDatabase {
    private static final String PATH_PRODUCTS = "products";

    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private ChildEventListener mProductsChildEventListener;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getIntance();
    }

    private DatabaseReference getProductsReference(){
        return mDatabaseAPI.getReference().child(PATH_PRODUCTS);
    }

    public void subscribeToProducts(final ProductsEventListener listener){
        if (mProductsChildEventListener == null){
            mProductsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(getProduct(dataSnapshot));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listener.onChildUpdated(getProduct(dataSnapshot));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(getProduct(dataSnapshot));
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.error_permission_denied);
                            break;

                        default:
                            listener.onError(R.string.error_server);
                    }

                }
            };
        }
        getProductsReference().addChildEventListener(mProductsChildEventListener);

    }

    private Producto getProduct(DataSnapshot dataSnapshot) {
        Producto producto = dataSnapshot.getValue(Producto.class);
        if (producto != null){
            producto.setId(dataSnapshot.getKey());
        }
        return producto;
    }

    public void unsubscribeToProducts(){
        if (mProductsChildEventListener != null){
            getProductsReference().removeEventListener(mProductsChildEventListener);
        }
    }

    public void removeProduct(Producto producto, final BasicErrorEventCallback callback){
        getProductsReference().child(producto.getId())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null){
                            callback.onSuccess();
                        }else {
                            switch (databaseError.getCode()){
                                case DatabaseError.PERMISSION_DENIED:
                                    callback.onError(ProductEvent.ERROR_TO_REMOVE, R.string.error_remove);
                                    break;
                                default:
                                    callback.onError(ProductEvent.ERROR_SERVER, R.string.error_server);
                            }
                        }

                    }
                });

    }
}
