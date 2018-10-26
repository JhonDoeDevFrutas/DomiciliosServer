package jhondoe.com.domiciliosserver.common;

public interface BasicErrorEventCallback {
    void onSuccess();
    void onError(int typeEvent, int resMsg);
}
