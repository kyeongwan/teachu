package kr.ac.ssu.teachu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.StudentArray;

/**
 * Created by nosubin on 2015-12-09.
 */
public class StudentListAdapter extends BaseAdapter
{
    Context mcontext;
    LayoutInflater minflater;
    ArrayList<StudentArray> marrayLists;
    int mlayout;
    public StudentListAdapter(Context context,int layout, ArrayList<StudentArray> arrayLists){
        this.mcontext=context;
        this.minflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.marrayLists=arrayLists;
        this.mlayout=layout;
    }
    @Override
    public int getCount() {
        return marrayLists.size();
    }

    @Override
    public Object getItem(int position) {
        return marrayLists.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null)
        {
            convertView = minflater.inflate(mlayout,parent,false);
        }
        TextView name=(TextView)convertView.findViewById(R.id.student_name);
        name.setText(marrayLists.get(position).name);
        //ImageView imageView=(ImageView)convertView.findViewById(R.id.student_image);
        //imageView.setImageResource(R.drawable.person_image_empty);
        //imageView.setImageResource(arrayLists.get(position).imageView);
        return convertView;
    }
}
