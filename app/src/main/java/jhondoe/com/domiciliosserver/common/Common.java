package jhondoe.com.domiciliosserver.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import jhondoe.com.domiciliosserver.data.model.entities.Solicitud;
import jhondoe.com.domiciliosserver.remote.APIService;
import jhondoe.com.domiciliosserver.remote.IGeoCoordinates;
import jhondoe.com.domiciliosserver.remote.RetrofitClient;

public class Common {

    public static Solicitud currentRequest;

    public static final String PHONE = "3183608735";

    public static String NOTIFICATION;

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Pedido Realizado";
        else if (code.equals("1"))
            return "En camino";
        else
            return "Enviado";
    }
}
