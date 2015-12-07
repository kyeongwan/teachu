package kr.ac.ssu.teachu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import kr.ac.ssu.teachu.ui.ViewerHolderSchedule;

/**
 * Created by lk on 15. 12. 8..
 */
public class DrawLine extends View {

    private Bitmap bitmap = null;
    private Path path;
    private Canvas canvas;
    private Paint paint;
    private float x;
    private float y;


    public DrawLine(Context context, Rect rect) {
        super(context);

        bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        path = new Path();
    }

    public void onDraw(Canvas canvas){
        if(bitmap != null){
            canvas.drawBitmap(bitmap, 0,0, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        float px = event.getX();
        float py = event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(px, py);

                x = px;
                y = py;

                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(px - x);
                float dy = Math.abs(py - y);

                if(dx >= 4 || dy <= 4){
                    path.quadTo(x,y, px, py);

                    x = px;
                    y = py;

                    canvas.drawPath(path, paint);
                }
                invalidate();
                return true;

        }
        return false;
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
}
