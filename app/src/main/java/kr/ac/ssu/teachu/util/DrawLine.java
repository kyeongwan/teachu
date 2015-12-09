package kr.ac.ssu.teachu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.ssu.teachu.ui.ViewerHolderSchedule;

/**
 * Created by lk on 15. 12. 8..
 */
public class DrawLine extends View {

    private Bitmap bitmap = null;
    private Path path;
    private Canvas canvas;
    private Paint paint;
    private int h;
    private int w;
    private float x;
    private float y;
    private SockJSImpl sockJS;
    private String channel_id = "42f34b4143d7762b1d604c1f03036f7725d75798d44c461724d4a65e9cac8f79";


    public DrawLine(Context context, Rect rect, SockJSImpl sockJS) {
        super(context);

        w = rect.width();
        h = rect.height();

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        path = new Path();
        this.sockJS = sockJS;
    }

    public void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float px = event.getX();
        float py = event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(px, py);

                x = px;
                y = py;

                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(px - x);
                float dy = Math.abs(py - y);

                if (dx >= 4 || dy <= 4) {
                    path.quadTo(x, y, px, py);
                    JSONObject obj = send((x / (float) w) + "/" + (y / (float) h) + "/" + (px / (float) w) + "/" + (py / (float) h));
                    x = px;
                    y = py;

                    sockJS.send(obj);
                    canvas.drawPath(path, paint);
                }
                invalidate();
                return true;

        }
        return false;
    }

    public void draw(float xp, float yp, float pxp, float pyp){
        float x = xp * w;
        float y = yp * h;
        float px = pxp * w;
        float py = pyp * h;
        float dx = Math.abs(px - x);
        float dy = Math.abs(py - y);
//        path.reset();
        setLineColor(Color.RED);
        path.moveTo(x, y);

        path.quadTo(x, y, px, py);
        canvas.drawPath(path, paint);
        invalidate();
        setLineColor(Color.GRAY);
    }

    public void setLineColor(int color) {
        paint = new Paint();
        paint.setColor(color);

        paint.setAlpha(255);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }


    public DrawLine(Context context) {
        super(context);
    }


    private JSONObject send(String msg) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "publish");
            obj.put("address", "to.server.channel");
            JSONObject body = new JSONObject();
            body.put("type", "draw");
            body.put("channel_id", channel_id);
            body.put("sender_id", "username");
            body.put("sender_nick", sockJS.nickname + "&&" + "#ffffff");
            body.put("app_id", "com.aaa.aaa");
            body.put("msg", msg);
            obj.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("onClick", e.toString());
        }
        return obj;
    }

    public void refresh() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();

        Log.i("refresh", "refresh");
    }
}
