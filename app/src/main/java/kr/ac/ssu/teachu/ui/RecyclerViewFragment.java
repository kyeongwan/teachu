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
                mContentItems.add(new Board("[밥상머리가 답이다Ⅱ .1] 프랑스의 밥상머리 교육", " \n" +
                        "경북도가 중점 추진 중인 밥상머리 교육을 소개하는 시리즈 ‘밥상머리가 답이다’ 후속편으로 ‘동서양 밥상머리 교육’을 5회에 걸쳐 싣는다. 이번 시리즈에는 프랑스·독일·미국의 밥상머리 교육실태를 살펴보고 최근 인기를 끌고 있는 ‘스칸디 교육법’도 간략하게 소개한다. 또 한국과는 비슷하면서도 다른 일본, 중국과의 비교를 통해 우리나라가 나아가야 할 바람직한 밥상머리 교육의 모습에 대해 고민해 보기로 한다. 이번 시리즈 기사는 대구권 대학에 재직 중인 외국인 교수들의 인터뷰를 바탕으로 재정리했다. 나라별로 3명의 교수를 만나 자신들의 어린 시절 경험을 들은 뒤 그 나라 보편적인 가정의 밥상머리 교육을 청취했다. \n" +
                        "\n" +
                        "\n" +
                        "전후 프랑스의 밥상머리 교육은 1960~70년대를 기점으로 극명하게 나뉜다. 전후부터 1970년대까지는 매우 엄격하게 밥상머리 교육이 이뤄졌으나 이후에는 자율성이 매우 강조되는 등 양 극단의 모습을 보이고 있다. 프랑스의 밥상머리 교육은 1968년 5월에 일어난 학생혁명이 변화의 전환점이다. 프랑스는 5월 학생혁명을 기점으로 개인의 자유가 신장되고 국가·사회적으로는 평등사회 개념이 확산되게 된다. 또 1971년 이후 페미니즘 운동이 일어나면서 밥상머리 교육은 전통사회와는 완전히 다른 모습으로 진행된다. 그전까지 매우 엄격하고 권위적인 모습을 보였던 밥상머리 교육은 1970년대부터는 방종에 가까울 정도로 자율성을 지나치게 강조하게 된다. 지금의 프랑스 밥상머리 교육은 지나친 자율성이 가져 오는 문제점을 극복하기 위해 전통적인 밥상머리 교육과의 적절한 접점을 찾아가는 과정 중에 있다. \n" +
                        "\n" +
                        "◆ 전후 엄격한 교육\n" +
                        "\n" +
                        "현재 프랑스의 일부 귀족층과 보수적인 가정에서는 과거의 엄격한 밥상머리 교육 패턴을 유지하고 있다. 가톨릭 국가의 엄격함이 남아 있는 전후 프랑스 가정에서 식사, 즉 만찬은 매우 엄숙하게 진행된다. \n" +
                        "\n" +
                        "식사 자리를 하나님의 축복으로 생각하는 프랑스는 바로 식사(만찬)에서부터 모든 것이 출발한다고 해도 과언이 아니다. 교육도 예외가 아니다. 만찬은 정해진 절차에 따라 진행되는 데 어릴 때부터 엄격한 식사 예절을 배우게 된다. 어린이는 단정히 옷을 차려입은 뒤 식사 시간에 맞춰 식탁에 앉아야 하고 기본적으로 스스로 식사를 해야 된다. 부모가 질문하기 전에는 말을 하면 안된다. 부모가 말을 시키지 않는 이상 자녀는 침묵 속에서 식사를 해야 한다. 대화도 부모가 질문한 내용에 대해서만 이야기해야 한다. 만찬(식사) 중에 나누는 대화 그 자체가 하나의 교육과정으로 이해된다. \n" +
                        "\n" +
                        "\n" +
                        "일부 어릴적 책읽는 습관 들여\n" +
                        "식사시간 때 다양한 지식 공유\n" +
                        "\n" +
                        "대부분 가정선 자율성만 강조\n" +
                        "맞벌이 부부의 증가와 맞물려\n" +
                        "대화 줄고 탈선 아이도 늘어나\n" +
                        "엄격했던 옛방식과 접점 찾아\n" +
                        "\n" +
                        "\n" +
                        "프랑스 가정에서는 식사 중에 주로 특정 주제를 갖고 대화를 하는 경우가 많다. 저녁 식사의 경우 미리 주제를 정해 놓고 부모와 자식 간에 대화 형식으로 진행하는데 독서 등을 통해 관련 지식을 미리 이해하고 있어야 한다고 한다. 주말에 손님을 초대하는 경우 2~4시간까지도 만찬이 진행되는데 자녀가 중·고교생일 경우 부모와 초대받은 손님과 함께 철학이나 문학 등 다양한 주제를 갖고 상당히 심도있는 대화를 주고받는다고 한다. 따라서 프랑스에서는 긴 시간 만찬을 하면서 나눌 대화를 위해서 어릴 적부터 책 읽는 습관을 들여 다양한 주제와 관심사에 대해 지식을 축적해 나가고 있다고 한다.\n" +
                        "\n" +
                        "프랑스의 이같은 밥상머리 교육은 ‘엄격하지 않으면 본질을 모른다’‘이성적으로 사유하지 않으면 대화는 불가능하다’ 등과 같은 전통적인 교육관에 그 뿌리를 두고 있다. 이러한 엄격한 밥상머리 교육을 통해 세련된 매너, 품위있는 식습관, 다양한 지적 능력을 겸비한 전형적인 프랑스 스타일의 인간이 탄생된다는 것이다. 당시에는 아이 교육이 매우 엄격해 체벌도 다반사로 이뤄졌다고 한다. 말을 듣지 않을 경우 화장실에 몇시간을 가둬두거나 방안에서 나오지 못하게 하는 등 매우 엄한 처벌을 내려 두 번 다시 같은 실수를 반복하지 않도록 한다. \n" +
                        "\n" +
                        "◆ 1980년대 이후 지나친 자율 교육\n" +
                        "\n" +
                        "서두에 언급한 것처럼 지금의 보편적인 가정은 지나치다 싶을 정도로 자유방임형 가정교육을 하고 있다. 어린 시절부터 예의범절을 가르치고 올바른 식사 습관과 이웃에게 인사하기 등 기본적인 예절 교육이 밥상머리에서 이뤄지기는 하지만 예전 같지는 않다. 1968년 5월 학생혁명 이후 자유·자율 개념이 강조되면서 전통적인 장유(長幼)관계가 무너졌다. 부모의 말에 절대적으로 따라야 했던 과거에 비해 부모와 자식 간에 평등한 가족관계가 형성된 것이다. 당연히 밥상머리 교육에도 변화가 닥쳤다. 1970년대는 페미니즘 운동이 일면서 프랑스 밥상머리 교육은 1980년대 들어 급격한 변화를 맞는다. 남녀노소 간 위계나 차별이 없어지고 자율과 평등 개념이 확산되면서 밥상머리 교육도 아이의 자율성이 강조됐다.\n" +
                        "\n" +
                        "여기에다 ‘프랑스인의 대모(代母)’ 프랑수아즈 돌토가 1980년대 연구 논문을 통해 ‘아기도 인격체’라고 주장하면서 밥상머리 교육은 근본적인 변화를 맞게 된다. 프랑스에서는 이 시점부터 부모들이 아이들의 말에 귀를 기울이기 시작했다고 한다. 그동안 부모의 일방적 강요나 지시적 스타일에서 벗어나 아이의 이야기를 충분히 듣고 가족 생활에도 참여 의식을 높였다고 한다. 심지어 부모가 이혼하거나 이사를 해야 할 경우 5~6세 된 아이에게 이유를 설명하고 동의를 구하는 과정을 거칠 정도로 많이 달라졌다. \n" +
                        "\n" +
                        "◆ 반성, 중도의 길을 찾아라\n" +
                        "\n" +
                        "최근 10년간 프랑스도 우리나라처럼 경제난에다 맞벌이 부부 증가로 자녀와 함께할 시간이 절대적으로 부족해지고 있다. 요즘은 아침에 서로 바빠 부모나 자식 모두 시간되는 대로 간단히 식사를 하고 주말이 돼야 저녁 식탁에서 마주할 정도로 바삐 돌아간다. 자유화 바람으로 상대적으로 밥상머리 교육이 소홀해지면서 탈선하는 아이들도 많아 사회 문제화되기도 한다. 육체적인 징벌이 사라지면서 TV시청 금지, 외출 금지, 청소하기, 용돈 줄이기 등의 페널티가 주어지지만 밥상머리 교육 여건이 예전보다 많이 나빠졌다고 한다. 지나친 자율성과 방종에 가까운 가정교육에 대한 반성으로 엄격함과 자율성 사이에서 중도적인 밥상머리 교육을 찾아야 한다는 공감대가 확산되고 있다고 한다. 프랑스 가정교육의 또하나 큰 문제는 높은 이혼율이다. 국가 평균 이혼율이 30%이고 파리는 무려 50%나 돼 밥상머리 교육 이전에 가정 환경이 좋지 않은 것이 문제라고 한다. \n" +
                        "\n" +
                        "부모가 이혼하면 자녀는 통상 엄마를 따라가는데, 새 아버지를 아버지로 받아들이지 않고, 새 아버지도 아이를 자녀로 생각하지 않으려는 경향이 많아 올바른 가정교육 기반을 마련하기 어렵다고 한다. 가정 이혼이 보편화되면서 한국처럼 이혼 가정에 대한 편견은 없지만 편모슬하에 자라거나 부모 사이를 왔다갔다하며 자란 어린이들이 사춘기에 접어들면서 반항 등으로 사회문제를 일으키기도 한다고 한다.\n" +
                        "\n" +
                        "박종문기자 kpjm@yeongnam.com\n" +
                        "▨ 도움말 = △나탈리 고예 교수(경북대 인문대학 불어불문학과) △프랑크 레이노 교수(영남대 문과대학 불어불문학과) △베로니크 엘리아스 교수(대구대 인문대학 불어불문학과)\n", "http://www.yeongnam.com/Photo/2015/11/30/L20151130.010190825110001i1.jpg"));
                mContentItems.add(new Board("[학부모역량개발센터와 함께하는 멋진 부모되기] 우리 아이 집중력 높이기<4>", "아이의 집중력 향상을 위해서는 완급을 조절하는 능력도 필요하다. 자녀에게 명확한 지침을 내려 조금은 강압적인 자세를 취하는, 이른바 ‘조여줄 때’가 있어야 하는 반면, 활동을 장려함으로써 ‘풀어주는 시기’도 반드시 있어야 한다.\n" +
                        "\n" +
                        "\n" +
                        "남을 때리거나 위험한 행동땐\n" +
                        "“하면 안된다” 고 단호히 제재\n" +
                        "\n" +
                        "설거지·TV시청중 지시 말고\n" +
                        "아이 눈보며 엄격하게 말해야\n" +
                        "\n" +
                        "산만하고 충동적인 아이들은\n" +
                        "좋아하는 운동시키면 효과적\n" +
                        "\n" +
                        "◆지시는 명확하고 단호하게 하고, 행동의 경계를 분명히 알려 주자\n" +
                        "\n" +
                        "아이들에게 경계를 인식하게 하는 건 매우 중요하다. 특히 산만하고 충동적인 아이들에게는 경계가 더욱 중요하다. ‘기다려 주라’는 말을 ‘내버려 두라’는 말로 잘못 이해하는 부모들은 아이가 무슨 행동을 하든 허용해야 한다고 생각하고 경계를 정해 주지 않기도 하지만, 경계가 없는 아이들은 불안을 더 많이 느끼게 된다. 어느 선까지는 되고, 어느 선부터는 안 되는지를 분명히 알려주면서 아이의 성장과 함께 경계의 범위와 정도를 조절하여야 한다. 반드시 필요한 경계 항목은 다음과 같다.\n" +
                        "\n" +
                        "▲기본 생활 습관: 정해진 시간보다 늦게 잔다거나 군것질만 많이 하고 밥을 안 먹으려고 하는 행위, 씻기를 귀찮아 해서 남에게 불쾌감을 줄 정도의 행동은 제재해야 한다.\n" +
                        "\n" +
                        "▲숙제를 포함한 계획된 학습: 매일 조금씩 공부하는 습관을 들이는 것은 꼭 필요하다. 당연히 숙제를 빼먹지 않고 하는 것도 반드시 필요한 습관이다. 아이와 함께 정한 학습 분량은 미루지 않고 할 수 있게 해야 한다. 단, 양이 너무 많거나 시간에 쫓기는 경우는 분량이나 스케줄을 조정해야 한다.\n" +
                        "\n" +
                        "▲자신이나 타인을 위험하게 하는 행동: 높은 곳에서 뛰어내리거나 칼을 가지고 논다거나 다른 사람을 때리는 등의 행동은 좋은 말로 타이르는 것이 아니라 엄격하게 제재해야 한다.\n" +
                        "\n" +
                        "이러한 행동들은 아이와 대화를 통해 타협하기보다는 분명하게 지시하고 엄격하게 실천해야 한다. “숙제 안 할거니? 제발 숙제부터 좀 해, 응?”이라는 식으로 부탁하듯 말하는 것이 아니라 “숙제할 시간이야. 지금 바로 일어나” 하고 엄격하게 말해야 한다. “제발 좀 그만하고 자” 애원하듯 말하는 것도 안 된다. “잘 시간이 지났어. 이제 침대로 들어가” 하고 불을 꺼야 한다.\n" +
                        "\n" +
                        "설거지 하거나 TV를 보면서 지시하는 것도 좋지 않다. 가까이서 눈을 보고 분명하게 말해야 한다. 모든 대화를 이렇게 할 수는 없고 이렇게 해서도 안 되지만 꼭 해야 하는 행동을 지시할 때는 이렇게 하는 것이 좋다.\n" +
                        "\n" +
                        "◆운동을 통한 에너지 발산\n" +
                        "\n" +
                        "정도의 차이가 있긴 하지만 아이들은 대체로 신체 에너지 수준이 높다. 그래서 많이 움직인다. 어른들 눈에는 그 움직임이 쓸데없어 보이기까지 한다. 얌전히 입 다물고 있으면 좋겠는데 이것저것 건드리며 질문을 하고, 천천히 걸어가면 좋겠는데 저만치 앞서 뛰어갔다가 다시 돌아와서는 빨리 좀 가자고 한다. 이런 쓸데없어 보이는 행동들은 사실 아이들에게 꼭 필요한 것들이다. 아이들은 이런 과정을 통해 신체 운동 에너지를 조절하는 법을 배우기 때문이다.\n" +
                        "\n" +
                        "하지만 안타깝게도 지금의 아이들은 과거 부모 세대에 비해 마음껏 신체 에너지를 발산할 기회가 적다. 어쩌면 이것이 산만하고 충동적인 아이들이 늘어만 가는 이유일 수도 있다. 집 밖은 추워서, 황사가 심해서, 차에 치일까봐, 유괴당할 수 있어서, 옆집에 방해 되니까 아이들은 나가지 마라, 뛰지 마라, 조용히 해라는 소리를 들으며 넘쳐나는 신체 에너지를 묶어 두고 있다.\n" +
                        "\n" +
                        "아이들에게 몸을 자유롭게 움직일 수 있는 시간을 만들어 주자. 특히 산만하고 충동적인 아이들에게는 꼭 필요하다. 규칙에 따라 움직여야 하는 운동보다는 원하는 대로 움직여도 되는 놀이가 더 좋다. 우리가 어릴 때 했던 그림자밟기 놀이나 얼음 땡 놀이가 좋은 예이다. \n" +
                        "\n" +
                        "운동을 할 수 있는 학원을 보내는 것도 좋다. 대신 운동 학원을 몇 달째 다녔는데 아직 윗 단계로 못 올라갔다거나 폼이 너무 엉성하다거나 하는 것을 지적하지 말고, 오로지 자신의 신체 움직임을 즐길 수 있게 해 주어야 한다. \n" +
                        "\n" +
                        "운동의 종류는 상관없다. 자녀가 좋아하는 것이면 무엇이든 좋다. 부모처럼 교사 역시 아이가 자기 몸 움직이는 기쁨을 느낄 수 있게 서두르지 않고 기다려 줄 수 있다면 아이는 자신의 신체 에너지를 발산하고 조절하는 방법을 익힐 수 있게 된다.\n" +
                        "\n" +
                        "주말에는 집 밖으로 데리고 나가서 편안하고 자유롭게 뛰어 놀 수 있게 해 주는 것이 필요하다. 주말 내내 집에서 TV를 보고 컴퓨터 게임만 한 아이들은 월요일에 학교에서 특히 더 산만하게 움직인다. 집에서 못 움직였던 것까지 더해서 밖에서 과잉으로 움직이는 것이다.\n" +
                        "\n" +
                        "신체 에너지를 조절하는 힘은 감정을 조절하고 생각을 조절하는 힘의 기초가 된다. 체육 시간에도 영어 공부를 시키면 공부를 더 잘할 것 같지만 사실은 그렇지 않다. 서구의 우등생들이 운동을 소홀히 하지 않는 것도 같은 이유다.\n", "http://www.yeongnam.com/Photo/2015/11/23/L20151123.010170758550001i1.jpg"));
                mContentItems.add(new Board("새 교육과정 따라 큰 변화 예고…유형별 문제풀이에 집중", "A형의 경우 변별력 있는 고난도 문항은 상용로그의 성질에 대한 이해를 묻는 30번 문항이었는데, 기존의 유형과 다른 신유형 문항으로 출제되어 상위권 학생들도 문항 해결에 어려움을 겪었을 것으로 보인다. B형은 중간 난도 문항 중에서 다소 까다롭게 출제된 문항이 있어 중위권 학생들의 체감 난이도가 다소 높았다.\n" +
                        "\n" +
                        "\n" +
                        "◆주요 고난도 문항과 학습대책\n" +
                        "\n" +
                        "<1>A형 30번= 상용로그의 지표와 가수에 대한 이해를 묻는 문항이었다. 최근 A형의 경우 고난도 문항이 지수함수와 로그함수 단원에서 많이 출제되고 있는데 특히 지난해 수능에 이어 올해 수능에서도 상용로그의 지표와 가수를 활용한 문항이 A형 30번 문항으로 출제되었다. 이러한 문제는 로그의 진수 범위를 나누어 접근하는 것이 중요하다.\n" +
                        "\n" +
                        "<2>B형 30번= 함수가 연속일 때 적분과 미분의 관계에 대한 이해를 묻는 문항. 이차함수, 무리함수, 미분법, 적분법 단원에 대한 전반적인 이해가 필요하다. 미분법과 적분법 단원에 대한 깊은 이해가 요구되는데, 최근 B형의 경우 고난도 문항이 미분법과 적분법 단원에서 출제되고 있기 때문이다. 기본적으로 미분과 적분을 자유자재로 할 수 있어야 하며, 미분법과 적분법 단원이 다른 단원과 통합되어 최고난도 문항으로 출제되는 경우가 많으므로 상위권 학생일수록 고난도 문항에 충분히 대비해야 한다.\n" +
                        "\n" +
                        "◆학습전략\n" +
                        "\n" +
                        "<1>교과서의 기본 개념을 충실히 익히자\n" +
                        "\n" +
                        "수학은 다른 영역에 비해 과목 간 위계가 확실하기 때문에 단계적 학습이 매우 중요하다. 또 기본적인 수학 능력을 갖추고 있지 않으면 문제를 해결하는 능력이 떨어져 풀 수 있는 문항이 거의 없게 된다. 따라서 주어진 문제의 상황을 이해하고 알맞은 공식을 적절히 이용하기 위해서는 각 단원의 기본 성질이나 관련 공식을 정확히 알고 있어야 한다.\n" +
                        "\n" +
                        "평소 자주 틀리는 유형의 문항과 관련된 단원은 그 내용과 관련 공식을 따로 정리해 두고 주기적으로 공부하여 익히도록 하자. 부족하다고 생각되는 단원이나 내용이 있다면 그 부분부터 해결한 후 진도를 나가는 것이 가장 좋은 학습 방법이다.\n" +
                        "\n" +
                        "<2>계산력 위주의 학습은 피하고, 사고력을 기를 수 있게 학습하자\n" +
                        "\n" +
                        "2009 개정 수학과 교육 과정에서는 계산 과정이 많은 방정식과 부등식 단원, 복잡한 계산을 다루고 있는 유리식과 무리식, 계차수열, 다항식의 약수와 배수 단원이 삭제되었다. 또한 한국교육과정평가원에서도 계산 능력보다는 사고력 위주의 측정을 중시하고 있으므로, 내년도 수능은 이 부분에 초점을 맞추어 학습 방향을 설정하여야 할 것이다.\n" +
                        "\n" +
                        "특히 단원 간의 연계성을 다루는 문항들이 주로 사고력을 많이 필요로 하기 때문에, 단원별 학습에 그치는 것이 아니라 여러 단원이 통합되어 출제된 문항을 어떤 방식으로 해결할 것인지에 대한 고민이 필요하다. 한 문제를 풀더라도 스스로 생각하고 고민한 뒤 해결해야 완전히 본인의 것으로 만들 수 있다. 해설을 먼저 보지 않고 스스로 계속 생각하는 훈련을 하도록 하자.\n" +
                        "\n" +
                        "<3>기출문제가 답이다\n" +
                        "\n" +
                        "2017학년도 수능은 새로운 교육과정이 적용되는 첫째 시험이다. 교육과정이 달라짐에 따라 지금까지의 수능과 비교하였을 때 내용 영역에는 상당한 변화가 있겠지만, 문항의 유형은 최근 수능의 경향과 크게 다르지 않을 것으로 예상된다.\n" +
                        "\n" +
                        "따라서 교과서나 기본개념서를 통해 개념 공부를 마쳤다면 과목별 또는 유형별로 정리된 기출문제집을 이용하여 기출 문제를 분석하고 기출 문제와 유사한 문항을 충분히 다루어보는 시간을 갖도록 하자. 이 과정에서 기출 문제의 유형을 익힐 수 있을 뿐 아니라, 비슷한 유형의 문항이 출제되었을 때 빠르고 정확하게 풀 수 있는 힘이 생길 것이다. 더불어 알고 있는 개념을 활용하여 문제를 푸는 연습을 할 수 있고 다시 한번 개념정리를 할 수 있는 기회이니 일석이조다. 우선 지난 3~5년간 기출문제를 공부하면 충분할 것이다.\n" +
                        "\n" +
                        "<4>EBS 수능 교재를 꼼꼼히 풀어보자\n" +
                        "\n" +
                        "올해 수능 역시 EBS 수능 교재에서 70% 정도의 문제가 연계 출제됐다. 2017학년도 수능 또한 연계율이 이와 비슷할 것으로 예상된다. EBS 수능 교재에서 연계된 문항들은 보통 숫자나 형태를 바꾸어 출제되므로, 이를 풀어본 학생이라면 연계된 문항을 해결하는 아이디어를 쉽게 찾을 수 있어 답을 구하는 데 상당한 시간이 단축될 것이다.\n" +
                        "\n" +
                        "또한 EBS 수능 교재를 학습하는 과정에서 추론 능력·사고력 등이 길러지므로 상위권 변별을 위해 출제된 고난도 문항에 대한 대비도 할 수 있다. 단, EBS 수능 교재를 풀기 전에 교과서의 개념들에 대한 이해가 선행되어야 하며 상위권 학생들은 EBS 수능 교재 외에 다른 교재도 함께 풀어보는 노력이 필요하겠다. \n", "http://www.yeongnam.com/Photo/2015/11/30/L20151130.010160756070001i1.jpg"));
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