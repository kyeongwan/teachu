package kr.ac.ssu.teachu.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.QuickstartPreferences;
import kr.ac.ssu.teachu.model.UserInfo;
import kr.ac.ssu.teachu.util.AppSetting;
import kr.ac.ssu.teachu.util.DBManager;
import kr.ac.ssu.teachu.util.OnRequest;
import kr.ac.ssu.teachu.util.RegistrationIntentService;
import kr.ac.ssu.teachu.util.RequestUtil;

public class SplashActivity extends AppCompatActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String TAG = "ICELANCER";

    DBManager dbManager;

    GoogleCloudMessaging gcm;
    Context context;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    TelephonyManager tm;

    UserInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        registBroadcastReceiver();
        Init();
        getInstanceIdToken();

        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        String token = pref.getString("token", "");
        String email = pref.getString("email", "");
        String pw = pref.getString("password", "");



        //UserInfo.getInstance().deviceId = tm.getDeviceId();
        UserInfo.getInstance().email = email;
        UserInfo.getInstance().password = pw;

        Handler hd = new Handler();

        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
                String token = pref.getString("token", "");
                if("".equals(token)) {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    autoLogin();
                }
            }
        }, 1000);

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    UserInfo.getInstance().gcmKey = token;
                    Log.v("token", token);
                }
            }
        };
    }


    private void Init() {
        context = getApplicationContext();
        dbManager = new DBManager(context, "wraptalkdb.sqlite", null, 1);

        gcm = GoogleCloudMessaging.getInstance(this);
//        regid = getRegistrationId(context);

        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }




    private void autoLogin() {
        String url = AppSetting.REST_URL + "/user/login?" +
                "user_id=" + UserInfo.getInstance().email + "&user_pw=" + UserInfo.getInstance().password + "&device_id=" + UserInfo.getInstance().deviceId + "&gcm_id=" + UserInfo.getInstance().gcmKey;
        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int result_code = json.optInt("result_code", -1);
                    if (result_code == 0) {
                        String token = json.optString("token");
                        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", token);
                        editor.commit();
                        UserInfo.getInstance().token = token;
                    } else {
                        String result_msg = json.optString("result_msg", "fail");
                        Toast.makeText(getApplicationContext(), result_msg, Toast.LENGTH_SHORT).show();
                    }

                    String query = String.format("INSERT INTO user_info (token, device_id, user_id, gcm_key)" + "VALUES('%s', '%s', '%s', '%s')",
                            UserInfo.getInstance().token, UserInfo.getInstance().deviceId, UserInfo.getInstance().email, UserInfo.getInstance().gcmKey);
                    try {
                        DBManager.getInstance().write(query);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String url, String error) {
                if (error == null) {
                    Toast.makeText(getApplicationContext(), "로그인 세션이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }
}