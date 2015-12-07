package kr.ac.ssu.teachu.ui;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.util.DrawLine;
import kr.ac.ssu.teachu.util.SockJSImpl;

/**
 * Created by nosubin on 2015-12-03.
 */
public class DrawActivity extends AppCompatActivity {

    private DrawLine drawLine;
    private SockJSImpl sockJS;
    private String channelId = "42f34b4143d7762b1d604c1f03036f7725d75798d44c461724d4a65e9cac8f79";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                setContentView(R.layout.activity_canvas);
        /*
        ListView list=(ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);

        */

        //db써서 반 list view로 출력
        connectSockJS();


    }

    public void onWindowFocusChanged(boolean hasFoucs){
        if(hasFoucs && drawLine == null){
            LinearLayout layout = (LinearLayout) findViewById(R.id.ll_canvas_canvas);
            if(layout != null){
                Rect rect = new Rect(0,0,layout.getMeasuredWidth(), layout.getMeasuredHeight());

                drawLine = new DrawLine(this, rect, sockJS);
                layout.addView(drawLine);

            }
            if(drawLine != null) drawLine.setLineColor(Color.parseColor("#888888"));
        }
        super.onWindowFocusChanged(hasFoucs);
    }


    private void connectSockJS() {
        try {
            sockJS = new SockJSImpl("http://133.130.113.101:7030/eventbus", channelId,"aaa","ttt") {
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
                        Log.i("type", type);
                        String address = json.getString("address");
//                        final JSONObject body = json.getJSONObject("body");
                        final JSONObject body = new JSONObject(json.getString("body"));
                        String bodyType = body.getString("type");
                        final String msg = body.getString("msg");
                        String nickname = body.getString("sender_nick");
                        Date myDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
                        String date = sdf.format(myDate);
                        final String data =  bodyType + "/&" +nickname + "/&" + msg + "/&" + date;
                        if (("to.channel."+ channelId).equals(address) && bodyType.equals("draw"))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] data = msg.split("/");
                                    drawLine.draw(Float.parseFloat(data[0]),Float.parseFloat(data[1]),Float.parseFloat(data[2]),Float.parseFloat(data[3]));
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
}
