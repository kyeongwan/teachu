package kr.ac.ssu.teachu.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.Board;
import kr.ac.ssu.teachu.model.Schedule;
import kr.ac.ssu.teachu.ui.MoreDialogFragment;
import kr.ac.ssu.teachu.ui.ViewerHolder;
import kr.ac.ssu.teachu.util.AppController;


/**
 * Created by lk on 15. 11. 29..
 */
public class Notice_adapter extends RecyclerView.Adapter<ViewerHolder> {

    private ImageLoader mImageLoader;
    List<Board> list;
    List<Schedule> slist;
    Context context;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public Notice_adapter(List<Board> contents, Context context)
    {
        mImageLoader = AppController.getInstance().getImageLoader();
        this.list = contents;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_notice, parent, false);
        return new ViewerHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewerHolder holder, int position) {
        setItem(holder, position);
    }

    private void setItem(final ViewerHolder holder, int position) {

        final Board board = list.get(position);
        holder.mRecycleHolder.mTitle.setText(board.title);
        holder.mRecycleHolder.mImage.setImageUrl(board.imageUrl, mImageLoader);
        holder.mRecycleHolder.mContext.setText(board.context);
        holder.mRecycleHolder.mlikeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MoreDialogFragment dialog = new MoreDialogFragment(board);
                FragmentManager fm = ((Activity) context).getFragmentManager();
                dialog.show(fm, "fm");
            }
        });


    }
}