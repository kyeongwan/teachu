package kr.ac.ssu.teachu.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.UserInfo;
import kr.ac.ssu.teachu.util.DBManager;
import kr.ac.ssu.teachu.util.SockJSImpl;

public class ChattingService extends Service implements View.OnClickListener{

    /**
     * For ChatHead
     */
    private ImageView mImageView;
    private WindowManager.LayoutParams mParams;
    private WindowManager.LayoutParams mParams2;
    private WindowManager.LayoutParams mParams3;


    private WindowManager.LayoutParams mParamsbt1;
    private WindowManager.LayoutParams mParamsbt2;
    private WindowManager.LayoutParams mParamsbt3;
    private WindowManager.LayoutParams mParamsbt4;
    private WindowManager.LayoutParams mParamsbt5;

    private WindowManager mWindowManager;
    private ListView mChatList;
    private Button mColorButton;
    private EditText mEditText;
    private TextView mChatheadTitle;

    private ImageButton bt1;
    private ImageButton bt2;
    private ImageButton bt3;
    private ImageButton bt4;
    private ImageButton bt5;

    /**
     * For Floating Button
     */
    private float start_X, start_Y;
    private int PREV_X, PREV_Y;
    private int max_X = -1, max_Y = -1;
    private boolean hasLongPress;
    private LongPressClass LongPressFunction;
    private Handler mHandler = null;
    private final int DEFULT_START_X = 0;
    private final int DEFULT_START_Y = 0;

    Runnable n1;
    double time1;
    boolean showButton = false;
    private float dpiCorrection;


    private RelativeLayout chatheadView;
    private RelativeLayout chatheadLayout;
    private boolean showView = false;   // 챗해드 작은 버튼들 여부
    private short showchat = 0;         // 챗해드 버튼 상태. 0 = 챗해드만 있음. 1 = 보기모드. 2 = 수정모드. -1 = 아무것도 없음.

    private ArrayList<String> chatdata;
    private ChatListAdapter adapter;
    private SockJSImpl sockJS;
    private String channelId = "42f34b4143d7762b1d604c1f03036f7725d75798d44c461724d4a65e9cac8f79";
    private SharedPreferences pref;
    private int nickColor = -1;
    private boolean animationDone = true;
    int animationR;

    private String nickname = "닉넴";
    private String title;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ChatService", "Start Service");
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        chatheadView = (RelativeLayout) inflater.inflate(R.layout.chathead, null);
        pref = getSharedPreferences("chathead", MODE_PRIVATE);

        initView();
        initParams();


        mHandler = new Handler();

        mWindowManager.addView(mImageView, mParams);

        connectSockJS();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("화면회전", "=== onConfigurationChanged is called !!! ===");

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // 세로 전환시 발생
            Log.i("화면회전", "=== Configuration.ORIENTATION_PORTRAIT !!! ===");
            setMaxPosition();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 가로 전환시 발생
            Log.i("화면회전", "=== Configuration.ORIENTATION_LANDSCAPE !!! ===");
            setMaxPosition();
        }
    }


    private void removeChathead() {
        if (showchat > -1) {
            mWindowManager.removeView(mImageView);
            if (showView) {
                mWindowManager.removeView(bt1);
                mWindowManager.removeView(bt2);
                mWindowManager.removeView(bt3);
                mWindowManager.removeView(bt4);
                mWindowManager.removeView(bt5);
            }
            if (showchat > 0) {
                mWindowManager.removeView(chatheadView);
            }
        }
        showchat = -1;
    }

    private void connectSockJS() {
        try {
            Log.i("ConectJS", "Connect");
            chatdata.clear();
            adapter.notifyDataSetChanged();
            sockJS = new SockJSImpl("http://133.130.113.101:7030/eventbus", channelId, nickname, title) {
                //channel_
                @Override
                public void parseSockJS(String s) {
                    try {
                        //System.out.println(s);
                        s = s.replace("\\\"", "\"");
                        s = s.replace("\\\\", "\\");
//                        s = s.replace("\\\\\"", "\"");
                        s = s.substring(3, s.length() - 2); // a[" ~ "] 없애기
                        Log.i("Reci", s);

                        JSONObject json = new JSONObject(s);
                        String type = json.getString("type");
                        String address = json.getString("address");
//                        final JSONObject body = json.getJSONObject("body");
                        final JSONObject body = new JSONObject(json.getString("body"));
                        String bodyType = body.getString("type");
                        String msg = body.getString("msg");
                        String nickname = body.getString("sender_nick");

                        final String data = bodyType + "/&" + nickname + "/&" + msg;
                        if (("to.channel." + channelId).equals(address))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatdata.add(data);
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        System.out.println("body = " + body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            boolean b = sockJS.connectBlocking();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initParams() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);

        int dpi = matrix.densityDpi;
        int buttonSize;
        if (dpi > 350) {
            buttonSize = 140;
            dpiCorrection = 1;
        } else {
            buttonSize = 95;
            dpiCorrection = (float) 0.75;
        }
        mParamsbt1 = new WindowManager.LayoutParams(
                (int) (buttonSize * 0.9), (int) (buttonSize * 0.9),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParamsbt1.gravity = Gravity.TOP | Gravity.LEFT;

        mParamsbt2 = new WindowManager.LayoutParams(
                (int) (buttonSize * 0.9), (int) (buttonSize * 0.9),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParamsbt2.gravity = Gravity.TOP | Gravity.LEFT;

        mParamsbt3 = new WindowManager.LayoutParams(
                (int) (buttonSize * 0.9), (int) (buttonSize * 0.9),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParamsbt3.gravity = Gravity.TOP | Gravity.LEFT;

        mParams = new WindowManager.LayoutParams(
                buttonSize,
                buttonSize,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.TOP | Gravity.LEFT;

        mParamsbt4 = new WindowManager.LayoutParams(
                (int) (buttonSize * 0.9), (int) (buttonSize * 0.9),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParamsbt4.gravity = Gravity.TOP | Gravity.LEFT;

        mParamsbt5 = new WindowManager.LayoutParams(
                (int) (buttonSize * 0.9), (int) (buttonSize * 0.9),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParamsbt5.gravity = Gravity.TOP | Gravity.LEFT;

        mParams2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //mParams2.alpha = Integer.parseInt(pref.getString("chathead_alpha", "80"));
        Log.i("alpha", mParams2.alpha + "");
        mParams3 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSLUCENT);

        setMaxPosition();

        mParams.x = 34;
        mParams.y = 858;
    }

    private void initView() {
        mImageView = new ImageView(this);
        mImageView.setImageBitmap(getMaskedBitmap(R.drawable.chathead, 30));
        mImageView.setOnTouchListener(mViewTouchListener);
        mImageView.setMaxHeight(30);
        mImageView.setMaxWidth(30);

        mColorButton = (Button) chatheadView.findViewById(R.id.bt_chathead_color);
        mColorButton.setOnClickListener(this);


        bt1 = new ImageButton(this);
        bt1.setBackground(getResources().getDrawable(R.drawable.setting));
        bt1.setOnClickListener(this);

        bt2 = new ImageButton(this);
        bt2.setBackground(getResources().getDrawable(R.drawable.selecticon));
        bt2.setOnClickListener(this);

        bt3 = new ImageButton(this);
        bt3.setBackground(getResources().getDrawable(R.drawable.chaticon));
        bt3.setOnClickListener(this);

        bt4 = new ImageButton(this);
        bt4.setBackground(getResources().getDrawable(R.drawable.writeicon));
        bt4.setOnClickListener(this);

        bt5 = new ImageButton(this);
        bt5.setBackground(getResources().getDrawable(R.drawable.xicon));
        bt5.setOnClickListener(this);

        mChatList = (ListView) chatheadView.findViewById(R.id.lv_chathead_chatlist);

        chatdata = new ArrayList<>();
        adapter = new ChatListAdapter(getApplicationContext(), chatdata);
        mChatList.setAdapter(adapter);
        mChatList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        chatheadLayout = (RelativeLayout) chatheadView.findViewById(R.id.rl_chathead_layout);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mChatList.setSelection(adapter.getCount() - 1);
            }
        });
        adapter.notifyDataSetChanged();

        mEditText = (EditText) chatheadView.findViewById(R.id.et_chathead_chat);
//        mEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() != KeyEvent.ACTION_DOWN)
//                    return true;
//
//
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    Log.d("Send", "KeyEvent.KEYCODE_ENTER");
//                    JSONObject obj = send();
//                    if ("".equals(mEditText.getText().toString()))
//                        return true;
//                    sockJS.send(obj);
//                    Log.i("fff", "send event");
//                    mEditText.setText("");
//                    return true;
//                }
//
//                return false;
//            }
//        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("text", s + "");
                if (s.toString().contains("\n")) {
                    mEditText.setText(mEditText.getText().toString().replace("\n", ""));
                    JSONObject obj = send();
                    if ("".equals(mEditText.getText().toString()))
                        return;
                    Log.i("json", obj.toString());
                    try {
                        sockJS.send(obj);
                    } catch (WebsocketNotConnectedException e) {
                        chatdata.add("메시지 전송에 실패했습니다.");
                        adapter.notifyDataSetChanged();
                    }
                    mEditText.setText("");
                }
                Log.i("Enter ", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @NonNull
    private JSONObject send() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "publish");
            obj.put("address", "to.server.channel");
            JSONObject body = new JSONObject();
            body.put("type", "normal");
            body.put("channel_id", channelId);
            body.put("sender_id", UserInfo.getInstance().email);
            body.put("sender_nick", nickname + "&&" + nickColor);
            body.put("app_id", "com.aaa.aaa");
            body.put("msg", mEditText.getText().toString());
            obj.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("onClick", e.toString());
        }
        return obj;
    }

    private Bitmap getMaskedBitmap(int _srcResId, float _roundInPixel) {
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), _srcResId);
        srcBitmap = Bitmap.createScaledBitmap(srcBitmap, 180, 180, true);

        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), srcBitmap);

        roundedDrawable.setCornerRadius(_roundInPixel);
        roundedDrawable.setAntiAlias(true);

        return roundedDrawable.getBitmap();
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);

        max_X = matrix.widthPixels - mImageView.getWidth();
        max_Y = matrix.heightPixels - mImageView.getHeight();
    }

    private void optimizePosition() {
        if (mParams.x > max_X) mParams.x = max_X;
        if (mParams.y > max_Y) mParams.y = max_Y;
        if (mParams.x < 0) mParams.x = 0;
        if (mParams.y < 0) mParams.y = 0;
    }

    @Override
    public void onDestroy() {
        if (mWindowManager != null) {
            if (mImageView != null) mWindowManager.removeView(mImageView);
            if (bt1 != null) mWindowManager.removeView(bt1);
            if (bt2 != null) mWindowManager.removeView(bt2);
            if (bt3 != null) mWindowManager.removeView(bt3);
            if (bt4 != null) mWindowManager.removeView(bt4);
            if (bt5 != null) mWindowManager.removeView(bt5);
        }
        if (showchat != 0) {
            mWindowManager.removeView(chatheadView);
        }
        super.onDestroy();
    }


    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Log.i("touch Event", event.getAction() + "");

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (max_X == -1)
                        setMaxPosition();
                    start_X = event.getRawX();
                    start_Y = event.getRawY();
                    PREV_X = mParams.x;
                    PREV_Y = mParams.y;

                    hasLongPress = false;
                    CheckLongClick();
                    break;
                case MotionEvent.ACTION_MOVE:
                    buttonClick();
                    if (hasLongPress) {
                        int x = (int) (event.getRawX() - start_X);
                        int y = (int) (event.getRawY() - start_Y);

                        mParams.x = PREV_X + x;
                        mParams.y = PREV_Y + y;

                        optimizePosition();
                        mWindowManager.updateViewLayout(mImageView, mParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!hasLongPress) {
                        removeLongClickCallback();
                        shortClickEvent();
                    }
                    break;
            }
            return true;
        }
    };


    private void removeLongClickCallback() {
        if (LongPressFunction != null)
            mHandler.removeCallbacks(LongPressFunction);
    }

    private void CheckLongClick() {
        hasLongPress = false;
        if (LongPressFunction == null)
            LongPressFunction = new LongPressClass();

        mHandler.postDelayed(LongPressFunction, 100);
    }


    private void shortClickEvent() {

        Log.i("XY", mParams.x + "/" + mParams.y + "/" + showView);

        if (!showView) {
            animationDone = false;
            mParamsbt1.x = mParams.x;
            mParamsbt2.x = mParams.x;
            mParamsbt3.x = mParams.x;
            mParamsbt4.x = mParams.x;
            mParamsbt5.x = mParams.x;
            mParamsbt1.y = mParams.y;
            mParamsbt2.y = mParams.y;
            mParamsbt3.y = mParams.y;
            mParamsbt4.y = mParams.y;
            mParamsbt5.y = mParams.y;
            mWindowManager.addView(bt1, mParamsbt1);
            mWindowManager.addView(bt2, mParamsbt2);
            mWindowManager.addView(bt3, mParamsbt3);
            mWindowManager.addView(bt4, mParamsbt4);
            mWindowManager.addView(bt5, mParamsbt5);

            showView = true;
            showButton = true;

            n1 = new Runnable() {
                @Override
                public void run() {
                    int side;
                    if (mParams.x < max_X / 2)
                        side = 1;
                    else
                        side = -1;
                    animationR = (int) ((0.264 * Math.pow(time1, 4) - 7.277 * Math.pow(time1, 3) + 64.646 * Math.pow(time1, 2) - 167.18 * time1 + 116.33) * dpiCorrection);
                    mParamsbt1.x = (int) (mParams.x + (mImageView.getWidth() / 2) + side * animationR * Math.cos(Math.toRadians(40)));
                    mParamsbt1.y = (int) (mParams.y + animationR * Math.sin(Math.toRadians(40)));
                    mParamsbt2.x = (int) (mParams.x + (mImageView.getWidth() / 2) + side * animationR * Math.cos(Math.toRadians(0)));
                    mParamsbt2.y = (int) (mParams.y + animationR * Math.sin(Math.toRadians(0)));
                    mParamsbt3.x = (int) (mParams.x + (mImageView.getWidth() / 2) + side * animationR * Math.cos(Math.toRadians(-40)));
                    mParamsbt3.y = (int) (mParams.y + animationR * Math.sin(Math.toRadians(-40)));
                    mParamsbt4.x = (int) (mParams.x + (mImageView.getWidth() / 2) + side * animationR * Math.cos(Math.toRadians(-80)));
                    mParamsbt4.y = (int) (mParams.y + animationR * Math.sin(Math.toRadians(-80)));
                    mParamsbt5.x = (int) (mParams.x + (mImageView.getWidth() / 2) + side * animationR * Math.cos(Math.toRadians(80)));
                    mParamsbt5.y = (int) (mParams.y + animationR * Math.sin(Math.toRadians(80)));
                    time1 += 1;
                    try {
                        mWindowManager.updateViewLayout(bt1, mParamsbt1);
                        mWindowManager.updateViewLayout(bt2, mParamsbt2);
                        mWindowManager.updateViewLayout(bt3, mParamsbt3);
                        mWindowManager.updateViewLayout(bt4, mParamsbt4);
                        mWindowManager.updateViewLayout(bt5, mParamsbt5);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        time1 = 0;
                        return;
                    }
                    //Log.i("jj", mParamsbt1.x + "x1");
                    if (time1 < 11)
                        mHandler.postDelayed(n1, 30);
                    else {
                        time1 = 0;
                    }

                }
            };
//
            mHandler.postDelayed(n1, 30);

        } else {
            buttonClick();
        }


    }

    private void buttonClick() {
        if (showButton || showView) {
            mWindowManager.removeView(bt1);
            mWindowManager.removeView(bt2);
            mWindowManager.removeView(bt3);
            mWindowManager.removeView(bt4);
            mWindowManager.removeView(bt5);
        }
        showView = false;
        showButton = false;
    }


    @Override
    public void onClick(View v) {
        if (v.getBackground() == bt1.getBackground()) {

            buttonClick();
        } else if (v.getBackground() == bt2.getBackground()) {  // 선택 채팅방
            AlertDialog dialog = ChatSelectDialog();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        } else if (v.getBackground() == bt3.getBackground()) {  // 최소화 최대화
            if (showchat > 0) {
                mWindowManager.removeView(chatheadView);
                showchat = 0;
                showView = false;
                bt3.setBackground(getResources().getDrawable(R.drawable.openicon));
                buttonClick();
            } else {
                controlChatView();
                bt3.setBackground(getResources().getDrawable(R.drawable.closeicon));
            }
        } else if (v.getBackground() == bt4.getBackground()) {  // 수정모드 보기모드
            controlChatView();
        } else if (v.getBackground() == bt5.getBackground()) {  // 앱 종료
            sockJS.closeSession();
            stopSelf();
        } else if (v.getBackground() == mColorButton.getBackground()) {
            Log.i("color", "color");

        }
    }

    private void controlChatView() {
        buttonClick();
        SharedPreferences pref = getSharedPreferences("chathead", MODE_PRIVATE);
        if (showchat == 0) {
            //mParams2.alpha = Integer.parseInt(pref.getString("chathead_alpha", "80"));
            //chatheadLayout.setAlpha((float) (Integer.parseInt(pref.getString("chathead_alpha", "80"))*0.01));
            chatheadLayout.setBackgroundColor(Color.parseColor("#" + String.format("%02X", Integer.parseInt(pref.getString("chathead_alpha", "80")) & 0xFF) + "000000"));
            mChatList.setAlpha(1);
            mEditText.setHint("쓰기모드로 변경해보세요!");
            mWindowManager.addView(chatheadView, mParams2);
            mWindowManager.removeView(mImageView);
            bt4.setBackground(getResources().getDrawable(R.drawable.writeicon));
            mWindowManager.addView(mImageView, mParams);
            showchat++;
            mEditText.setVisibility(View.INVISIBLE);
            mColorButton.setVisibility(View.INVISIBLE);
        } else if (showchat == 1) {
            mEditText.setHint("메시지를 입력 후 엔터를 누르세요!");
            mParams2 = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                    PixelFormat.TRANSLUCENT);
            bt4.setBackground(getResources().getDrawable(R.drawable.seeicon));
            //mParams2.alpha = Integer.parseInt(pref.getString("chathead_alpha", "80"));
            //chatheadLayout.setAlpha((float) (Integer.parseInt(pref.getString("chathead_alpha", "80"))*0.01));
            chatheadLayout.setBackgroundColor(Color.parseColor("#" + String.format("%02X", Integer.parseInt(pref.getString("chathead_alpha", "80")) & 0xFF) + "000000"));
            mChatList.setAlpha(1);
            mWindowManager.updateViewLayout(chatheadView, mParams2);
            Log.i("alpha", mParams2.alpha + "");
            mWindowManager.removeView(mImageView);
            mWindowManager.addView(mImageView, mParams);
            mEditText.setVisibility(View.VISIBLE);
            mColorButton.setVisibility(View.VISIBLE);
            showchat++;
        } else if (showchat == 2) {
            mParams2 = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    PixelFormat.TRANSLUCENT);
            mEditText.setHint("쓰기모드로 변경해보세요!");
            //mParams2.alpha = Integer.parseInt(pref.getString("chathead_alpha", "80"));
            //chatheadLayout.setAlpha((float) (Integer.parseInt(pref.getString("chathead_alpha", "80"))*0.01));
            chatheadLayout.setBackgroundColor(Color.parseColor("#" + String.format("%02X", Integer.parseInt(pref.getString("chathead_alpha", "80")) & 0xFF) + "000000"));
            mChatList.setAlpha(1);
            bt4.setBackground(getResources().getDrawable(R.drawable.writeicon));
            mWindowManager.updateViewLayout(chatheadView, mParams2);
            mWindowManager.removeView(mImageView);
            mWindowManager.addView(mImageView, mParams);
            mEditText.setVisibility(View.INVISIBLE);
            mColorButton.setVisibility(View.INVISIBLE);
            showchat--;

        } else {

            mParams2 = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    PixelFormat.TRANSLUCENT);
            mWindowManager.updateViewLayout(chatheadView, mParams2);
            //mParams2.alpha = Integer.parseInt(pref.getString("chathead_alpha", "80"));
            //chatheadLayout.setAlpha((float) (Integer.parseInt(pref.getString("chathead_alpha", "80"))*0.01));
            chatheadLayout.setBackgroundColor(Color.parseColor("#" + String.format("%02X", Integer.parseInt(pref.getString("chathead_alpha", "80")) & 0xFF) + "000000"));
            mWindowManager.removeView(mImageView);
            mWindowManager.addView(mImageView, mParams);
            showchat = 1;
        }
    }



    public void ExitMessage() {
        JSONObject log = new JSONObject();

        try {
            log.put("type", "publish");
            log.put("address", "to.server.channel");
            JSONObject body = new JSONObject();
            body.put("type", "log");
            body.put("channel_id", channelId);
            body.put("sender_id", UserInfo.getInstance().email);
            body.put("sender_nick", nickname);
            body.put("msg", "님이 퇴장하셨습니다.");
            log.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ExitMessage", e.toString());
        }
        try {
            sockJS.send(log);
        } catch (WebsocketNotConnectedException e) {
            e.printStackTrace();
            Log.e("ExitMessage", "Fail");
        }
    }

    private class LongPressClass implements Runnable {
        @Override
        public void run() {
            if (LongClickEvent())
                hasLongPress = true;
        }

    }

    private boolean LongClickEvent() {
        return true;
    }

    private AlertDialog ChatSelectDialog() {
//
//        String items[] = new String[chatroomlist.size()];
//        for (int i = 0; i < chatroomlist.size(); i++) {
//            items[i] = chatroomlist.get(i).getChannel_name();
//        }
//        final int[] cnt = {0};
//        AlertDialog.Builder ab = new AlertDialog.Builder(getBaseContext());
//        ab.setTitle("입장할 채널 선택");
//        ab.setSingleChoiceItems(items, 0,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        cnt[0] = whichButton;
//                    }
//                }).setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
//                        sockJS.closeSession();
//                        try {
//                            ExitMessage();
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (showchat > 0) {
//                            showchat = -1;
//                        }
//                        controlChatView();
//                        //channelId = "96da751edc63634c4c5958ce90e6a889ee1cdda247d92a978f340336791d5fb3";
//                        channelId = chatroomlist.get(cnt[0]).getChannel_id();
//
//                    }
//                }).setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // Cancel 버튼 클릭시
//                    }
//                });
//        return ab.create();
        return null;
    }

}