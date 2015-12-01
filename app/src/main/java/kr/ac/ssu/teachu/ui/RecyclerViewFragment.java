package kr.ac.ssu.teachu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.Board;


/**
 * Created by lk on 15. 11. 29..
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int position;

    private static final int ITEM_COUNT = 100;

    private List<Board> mContentItems = new ArrayList<>();

    public static RecyclerViewFragment newInstance(int position) {
        return new RecyclerViewFragment(position);
    }


    public RecyclerViewFragment(int position){
        this.position = position;
        Log.i("sadfa", position+"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview_carpaccio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerViewMaterialAdapter(new TestRecyclerViewAdapter(mContentItems, getActivity().getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);

        {
            for (int i = 0; i < ITEM_COUNT; ++i) {
                if (position == 0)
                    mContentItems.add(new Board("", "", "http://i.cdn.turner.com/cnn/2011/HEALTH/01/06/child.hospital.ep/t1larg.hospital.child.ts.jpg"));
                else if (position == 1)
                    mContentItems.add(new Board("bbb", "bbb", "http://cdn.kidspot.com.au/wp-content/uploads/2013/12/HospitalChild_happy-600x420.jpg"));
                else if (position == 2)
                    mContentItems.add(new Board("ccc", "ccc", "http://us.123rf.com/450wm/outsiderzone/outsiderzone1009/outsiderzone100900129/7815322-sick-child-in-hospital-fever-and-flu.jpg?ver=6"));
            }
            mAdapter.notifyDataSetChanged();

        }

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }
}