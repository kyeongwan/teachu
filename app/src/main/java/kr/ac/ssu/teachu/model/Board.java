package kr.ac.ssu.teachu.model;

import android.app.Activity;
import android.widget.TextView;

import kr.ac.ssu.teachu.R;

/**
 * Created by lk on 15. 11. 29..
 */
public class Board //extends Activity
{
    public String title;
    public String imageUrl;
    public String context;
    //TextView tv=(TextView)findViewById(R.id.tv_recycle_context);
    public Board(String title, String context, String imgurl)
    {
        this.title = title;
        this.context = context;
        this.imageUrl = imgurl;
    }
}
