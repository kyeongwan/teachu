package kr.ac.ssu.teachu.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import kr.ac.ssu.teachu.R;


/**
 * Created by lk on 15. 11. 29..
 */
public class ViewerHolder extends RecyclerView.ViewHolder {

    public ViewHolder mRecycleHolder;

    static class ViewHolder {

        public TextView mTitle;             // Recipe Name
        public NetworkImageView mImage;     // Cooking Thumbnail
        public Button mlikeButton;          // Like Button
        public TextView mContext;
    }

    public ViewerHolder(View itemView) {
        super(itemView);
        setHolder(itemView);
    }

    private void setHolder(View itemView) {
        mRecycleHolder = new ViewHolder();
        mRecycleHolder.mTitle = (TextView) itemView.findViewById(R.id.tv_recycle_title);
        mRecycleHolder.mContext = (TextView) itemView.findViewById(R.id.tv_recycle_context);
        mRecycleHolder.mImage = (NetworkImageView) itemView.findViewById(R.id.iv_recycle_img);
        mRecycleHolder.mlikeButton = (Button) itemView.findViewById(R.id.bt_recycle_more);
    }
}
