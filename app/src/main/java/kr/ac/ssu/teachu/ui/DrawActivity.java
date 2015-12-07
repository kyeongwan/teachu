package kr.ac.ssu.teachu.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.util.DrawLine;

/**
 * Created by nosubin on 2015-12-03.
 */
public class DrawActivity extends AppCompatActivity {

    private DrawLine drawLine;

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


    }

    public void onWindowFocusChanged(boolean hasFoucs){
        if(hasFoucs && drawLine == null){
            LinearLayout layout = (LinearLayout) findViewById(R.id.ll_canvas_canvas);
            if(layout != null){
                Rect rect = new Rect(0,0,layout.getMeasuredWidth(), layout.getMeasuredHeight());

                drawLine = new DrawLine(this, rect);
                layout.addView(drawLine);

            }
            if(drawLine != null) drawLine.setLineColor(Color.parseColor("#888888"));
        }
        super.onWindowFocusChanged(hasFoucs);
    }
}
