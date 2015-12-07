package kr.ac.ssu.teachu.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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


    public ScheduleAdapter(List<Schedule> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewerHolderSchedule onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_scheduler, parent, false);
        return new ViewerHolderSchedule(view);
    }

    @Override
    public void onBindViewHolder(ViewerHolderSchedule holder, int position) {
        setItem(holder, position);
    }

    private void setItem(ViewerHolderSchedule holder, int position) {
        Schedule schedule = list.get(position);

        holder.mRecycleHolder.mTitle.setText(schedule.work);
        holder.mRecycleHolder.mContext.setText("ssss");
        holder.mRecycleHolder.mImage.setImageResource(R.drawable.ic_cast_dark);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
