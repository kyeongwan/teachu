package kr.ac.ssu.teachu.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.Board;
import kr.ac.ssu.teachu.model.Schedule;

/**
 * Created by lk on 15. 12. 8..
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ViewerHolderSchedule> {

    List<Schedule> list;
    Context context;

    final int TYPE_HEADER = 0;
    final int TYPE_CELL = 1;


    public ScheduleAdapter(List<Schedule> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public ViewerHolderSchedule onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_scheduler_date, parent, false);
            return new ViewerHolderSchedule(view, TYPE_HEADER);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_scheduler, parent, false);
            return new ViewerHolderSchedule(view, TYPE_CELL);
        }
    }

    @Override
    public void onBindViewHolder(ViewerHolderSchedule holder, int position) {
        if (holder.type == TYPE_HEADER)
            setDateItem(holder, position);
        else
            setItem(holder, position);
    }

    private void setDateItem(ViewerHolderSchedule holder, int position) {
        holder.mRecycleHolder.mDate.setText("2015.1.1 (화)");
    }

    private void setItem(ViewerHolderSchedule holder, int position) {
        Schedule schedule = list.get(position);
        try {
            holder.mRecycleHolder.mTitle.setText(schedule.work);
            holder.mRecycleHolder.mContext.setText("ssss");
            holder.mRecycleHolder.mImage.setImageResource(R.drawable.ic_cast_dark);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
