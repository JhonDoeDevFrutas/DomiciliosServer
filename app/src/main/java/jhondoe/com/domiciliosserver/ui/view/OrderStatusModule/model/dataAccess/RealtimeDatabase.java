package jhondoe.com.domiciliosserver.ui.view.OrderStatusModule.model.dataAccess;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;

public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private ChildEventListener mOrderStatusChildEventListener;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void subscribeToOrderStatus(final OrderStatusEventListener listener){
        if (mOrderStatusChildEventListener == null){
            mOrderStatusChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listener.onChildUpdated(getOrderStatus(dataSnapshot));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

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

        mDatabaseAPI.getOrderStatusReference().addChildEventListener(mOrderStatusChildEventListener);
    }

    private Solicitud getOrderStatus(DataSnapshot dataSnapshot) {
        Solicitud orderStatus = dataSnapshot.getValue(Solicitud.class);
        if (orderStatus != null){
            orderStatus.setId(dataSnapshot.getKey());
        }
        return orderStatus;
    }

    public void unsubscribeToOrderStatus(){
        if (mOrderStatusChildEventListener != null){
            mDatabaseAPI.getOrderStatusReference().removeEventListener(mOrderStatusChildEventListener);
        }
    }

    public void updateOrderStatus(Solicitud solicitud){
        mDatabaseAPI.getOrderStatusReference().child(solicitud.getId()).setValue(solicitud)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}
