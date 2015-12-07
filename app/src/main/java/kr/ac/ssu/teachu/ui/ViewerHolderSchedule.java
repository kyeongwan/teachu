package kr.ac.ssu.teachu.ui;

import android.support.v7.widget.RecyclerView;
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

    static class ViewHolder {

        public TextView mTitle;             // Recipe Name
        public ImageView mImage;     // Cooking Thumbnail
        public TextView mContext;
    }

    public ViewerHolderSchedule(View itemView) {
        super(itemView);
        setHolder(itemView);
    }

    private void setHolder(View itemView) {
        mRecycleHolder = new ViewHolder();
        mRecycleHolder.mTitle = (TextView) itemView.findViewById(R.id.tv_scheduler_title);
        mRecycleHolder.mContext = (TextView) itemView.findViewById(R.id.tv_scheduler_context);
        mRecycleHolder.mImage = (NetworkImageView) itemView.findViewById(R.id.iv_scheduker_icon);
    }
}
