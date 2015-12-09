package kr.ac.ssu.teachu.ui;

import android.database.Cursor;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.adapter.Notice_adapter;
import kr.ac.ssu.teachu.adapter.ScheduleAdapter;
import kr.ac.ssu.teachu.adapter.TestRecyclerViewAdapter;
import kr.ac.ssu.teachu.model.Board;
import kr.ac.ssu.teachu.model.Schedule;
import kr.ac.ssu.teachu.util.DBManager;


/**
 * Created by lk on 15. 11. 29..
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    private int position;

    private static final int ITEM_COUNT = 1;

    private List<Board> mContentItems = new ArrayList<>();
    private ArrayList<Schedule> mScheduleItems = new ArrayList<>();
    private ArrayList<ArrayList<Schedule>> mScheduleList = new ArrayList<>();
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");


    public static RecyclerViewFragment newInstance(int position) {
        return new RecyclerViewFragment(position);
    }


    public RecyclerViewFragment(int position) {
        this.position = position;
        Log.i("sadfa", position + "");
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

        {

            if (position == 0) {
                DBManager.getInstance().select("SELECT * FROM schedule", new DBManager.OnSelect() {
                    @Override
                    public void onSelect(Cursor cursor) {

                        String title = cursor.getString(1);
                        String time = cursor.getString(2);
                        String date = cursor.getString(3);
                        String context = cursor.getString(4);

                        Date to = null;
                        try {
                            to = transFormat.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i("schedule", title +"/"+ time +"/"+ date +"/"+ context);
                        Schedule schedule = new Schedule(title, context, to);
                        mScheduleItems.add(schedule);
                    }

                    @Override
                    public void onComplete(int cnt) {
                        ArrayList<Schedule> list = new ArrayList<>();
                        Collections.sort(mScheduleItems);
                        Date date = mScheduleItems.get(0).date;
                        String to = transFormat.format(date);
                        Log.i("to", to);
                        Schedule sT = new Schedule(to);
                        list.add(sT);
                        for (int i = 0; i < cnt; i++) {
                            Schedule s = mScheduleItems.get(i);
                            if (0 == date.compareTo(s.date)) {
                                s.setType(1);
                                list.add(s);
                            } else {
                                to = transFormat.format(s.date);
                                sT = new Schedule(to);
                                Log.i("changee", to);
                                list.add(sT);
                                date = s.date;
                                mScheduleItems.get(i).setType(1);
                                list.add(mScheduleItems.get(i));
                            }
                        }
                        mScheduleItems = list;
                        mAdapter.notifyDataSetChanged();
//                        mScheduleList.add(list);
                    }

                    @Override
                    public void onErrorHandler(Exception e) {

                    }
                });

            } else if (position == 1) {
                mContentItems.add(new Board("Yap Chee en 선생님의 마지막 인사", "Thank you everyone","http://cse.ssu.ac.kr/images/01_sub/prof_img_31.gif"));
                mContentItems.add(new Board("장훈 선생님의 '즐거운 인생' 강좌 개설", "숭실대학교 교수로 계시는 장훈 교수님의 인생을 즐겁게 사는 법에 대한 강좌가 개설됩니다. 신청할 학생들은 담당 선생님에게 개인 메시지를 보내주세요","http://cse.ssu.ac.kr/images/01_sub/prof_img_23.gif"));
                mContentItems.add(new Board("전문석 선생님의 Wifi를 통한 남북통일강좌 개설", "숭실대학교 교수로 계시는 전문석 교수님의 남북통일강좌가 개설됩니다. 신청할 학생들은 담당 선생님에게 개인 메시지를 보내주세요","http://cse.ssu.ac.kr/images/01_sub/prof_img_24.gif"));

            }


            else if (position == 2) {
                mContentItems.add(new Board("[밥상머리가 답이다Ⅱ .1] 프랑스의 밥상머리 교육", "경북도가 중점 추진 중인 밥상머리 교육을 소개하는 시리즈 ‘밥상머리가 답이다'...", "http://www.yeongnam.com/Photo/2015/11/30/L20151130.010190825110001i1.jpg"));
                mContentItems.add(new Board("[학부모역량개발센터와 함께하는 멋진 부모되기] 우리 아이 집중력 높이기<4>", "◆지시는 명확하고 단호하게 하고, 행동의 경계를 분명히 알려 주자...", "http://www.yeongnam.com/Photo/2015/11/23/L20151123.010170758550001i1.jpg"));
                mContentItems.add(new Board("새 교육과정 따라 큰 변화 예고…유형별 문제풀이에 집중", "A형의 경우 변별력 있는 고난도 문항은 상용로그의 성질에 대한 이해를 묻는 30번 문항이었는데,... ", "http://www.yeongnam.com/Photo/2015/11/30/L20151130.010160756070001i1.jpg"));
            }

        }
        if (position == 0) {
            mAdapter = new RecyclerViewMaterialAdapter(new ScheduleAdapter(mScheduleItems, getActivity()));
            mRecyclerView.setAdapter(mAdapter);
        } else if (position == 1) {
            mAdapter = new RecyclerViewMaterialAdapter(new Notice_adapter(mContentItems, getActivity()));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new RecyclerViewMaterialAdapter(new TestRecyclerViewAdapter(mContentItems, getActivity().getApplicationContext()));
            mRecyclerView.setAdapter(mAdapter);
        }


        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }
}