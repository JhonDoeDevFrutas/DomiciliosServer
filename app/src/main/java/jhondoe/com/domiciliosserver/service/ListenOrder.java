package jhondoe.com.domiciliosserver.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.common.Common;
import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;
import jhondoe.com.domiciliosserver.provider.FirebaseReferences;
import jhondoe.com.domiciliosserver.ui.view.Home;

public class ListenOrder extends Service implements ChildEventListener{

    FirebaseDatabase mDatabase;
    //our database reference object
    DatabaseReference mDbReference;

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = FirebaseDatabase.getInstance();
        //getting the reference of node
        mDbReference = mDatabase.getReference(FirebaseReferences.ORDEN_COMPRA);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDbReference.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // Trigger here
        Solicitud solicitud = dataSnapshot.getValue(Solicitud.class);

        if (solicitud.getStatus().equals("0"))
            showNotification(dataSnapshot.getKey(), solicitud);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    private void showNotification(String key, Solicitud solicitud) {
        Common.NOTIFICATION = "request";

        Intent intent = new Intent(getBaseContext(), Home.class);
        intent.putExtra("userPhone", solicitud.getPhone());

        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("JHONDOE CLIENT")
                .setContentInfo("Info")
                .setContentText("Pedido #" + key+" por colocar " )
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if you want to many notification show, you need give unique Id for each Notification
        int randomInt = new Random().nextInt(9999-1)+1;
        notificationManager.notify(randomInt, builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
