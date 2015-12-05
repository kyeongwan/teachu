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
import android.widget.TextView;

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

    private static final int ITEM_COUNT = 1;

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
                {
                    mContentItems.add(new Board("[밥상머리가 답이다Ⅱ .1] 프랑스의 밥상머리 교육", "경북도가 중점 추진 중인 밥상머리 교육을 소개하는 시리즈 ‘밥상머리가 답이다'...", "http://www.yeongnam.com/Photo/2015/11/30/L20151130.010190825110001i1.jpg"));
                    mContentItems.add(new Board("[학부모역량개발센터와 함께하는 멋진 부모되기] 우리 아이 집중력 높이기<4>", "◆지시는 명확하고 단호하게 하고, 행동의 경계를 분명히 알려 주자...", "http://www.yeongnam.com/Photo/2015/11/23/L20151123.010170758550001i1.jpg"));
                    mContentItems.add(new Board("새 교육과정 따라 큰 변화 예고…유형별 문제풀이에 집중","A형의 경우 변별력 있는 고난도 문항은 상용로그의 성질에 대한 이해를 묻는 30번 문항이었는데,... ", "http://www.yeongnam.com/Photo/2015/11/30/L20151130.010160756070001i1.jpg"));
                }
            }
            mAdapter.notifyDataSetChanged();

        }



        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }
}