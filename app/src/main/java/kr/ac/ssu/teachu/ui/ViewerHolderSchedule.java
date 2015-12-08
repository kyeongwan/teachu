package kr.ac.ssu.teachu.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import kr.ac.ssu.teachu.R;

/**
 * Created by lk on 15. 12. 8..
 */
public class ViewerHolderSchedule extends RecyclerView.ViewHolder {

    public ViewHolder mRecycleHolder;
    public int type;

    class ViewHolder {
        public TextView mTitle = (TextView) itemView.findViewById(R.id.tv_scheduler_title);             // Recipe Name
        public ImageView mImage = (ImageView) itemView.findViewById(R.id.iv_scheduker_icon);     // Cooking Thumbnail
        public TextView mContext = (TextView) itemView.findViewById(R.id.tv_scheduler_context);
        public TextView mDate = (TextView) itemView.findViewById(R.id.tv_schedule_date);
    }

    public ViewerHolderSchedule(View itemView, int type) {
        super(itemView);
        setHolder(itemView);
        this.type = type;
        Log.i("type", type + "");
    }

    private void setHolder(View itemView) {
        if (type == 0) {
            mRecycleHolder = new ViewHolder();
            mRecycleHolder.mDate = (TextView) itemView.findViewById(R.id.tv_schedule_date);
        } else {
            mRecycleHolder = new ViewHolder();
            mRecycleHolder.mTitle = (TextView) itemView.findViewById(R.id.tv_scheduler_title);
            mRecycleHolder.mContext = (TextView) itemView.findViewById(R.id.tv_scheduler_context);
            mRecycleHolder.mImage = (ImageView) itemView.findViewById(R.id.iv_scheduker_icon);
        }
    }
}
