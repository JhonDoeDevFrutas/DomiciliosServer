package jhondoe.com.domiciliosserver.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;

public class SessionPrefs {

    public static final String PREFS_NAME = "SEVER_PREFS";

    public static final String PHONE = "PHONE";
    public static final String PWD_KEY = "PASSWORD";

    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    private SessionPrefs(Context context){
        mPrefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PHONE, null));
    }

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }


    public void saveUser(String phone, String password){
        SharedPreferences.Editor editor = mPrefs.edit();
        /*editor.putString(USER_KEY, user);
        */

        editor.putString(PHONE, phone);
        editor.putString(PWD_KEY, password);
        editor.apply();

        mIsLoggedIn = true;
    }

    public void logOut(){
        mIsLoggedIn = false;

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(PHONE);
        editor.remove(PWD_KEY);
        editor.clear();
        editor.apply();
    }

    public String getPhone(){
        return mPrefs.getString(PHONE, null);
    }
}
