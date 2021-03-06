package kr.ac.ssu.teachu.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.util.DBManager;

/**
 * Created by lk on 15. 12. 1..
 */
public class MainActivity extends AppCompatActivity implements ehlMainFragment.OnFragmentInteractionListener {

    //private static final String TAG = makeLogTag("MainActivity");

    private MaterialViewPager mViewPager;
    private Toolbar mToolbar;
    private ImageView mAvatarImage;
    private TextView mNameText;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initMaterialViewPager();
        initToolbar();
        initNavigationView();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 8);
        String query = "INSERT INTO schedule (title, time, date, context)" + "VALUES('" + "과학 수업 (11:30 ~ 13:00)" + "', '숙제 없음', '" + dateFormat.format(cal.getTime()) + "', '숙제 없음')";
        try {
            DBManager.getInstance().write(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        query = "INSERT INTO schedule (title, time, date, context)" + "VALUES('" + "과학 보강 (13:00 ~ 15:00)" + "', 'ㅠㅠㅠㅠㅠ', '" + dateFormat.format(cal.getTime()) + "', 'ㅠㅠㅠㅠ')";
        try {
            DBManager.getInstance().write(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 12);
         query = "INSERT INTO schedule (title, time, date, context)" + "VALUES('" + "과학 수업 (11:30 ~ 13:00)" + "', 'p130~p140', '" + dateFormat.format(cal.getTime()) + "', 'p130~p140')";
        try {
            DBManager.getInstance().write(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 15);
         query = "INSERT INTO schedule (title, time, date, context)" + "VALUES('" + "과학 수업 (11:30 ~ 13:00)" + "', 'ㅎㅎ..', '" + dateFormat.format(cal.getTime()) + "', 'ㅎㅎ..')";
        try {
            DBManager.getInstance().write(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        query = "INSERT INTO schedule (title, time, date, context)" + "VALUES('" + "과학 보강 (13:00 ~ 15:00)" + "', 'ㅠㅠㅠㅠㅠ', '" + dateFormat.format(cal.getTime()) + "', 'ㅠㅠㅠㅠㅠ')";
        try {
            DBManager.getInstance().write(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 17);
            query = "INSERT INTO schedule (title, time, date, context)" + "VALUES('" + "과학 수업 (11:30 ~ 13:00)" + "', '', '" + dateFormat.format(cal.getTime()) + "', '오늘도 힘든 하루를...')";
        try {
            DBManager.getInstance().write(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setBackgroundColor(Color.RED);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleaddFragment dialog = new ScheduleaddFragment(MainActivity.this);
                android.app.FragmentManager fm = getFragmentManager();
                dialog.show(fm, "fm");
            }
        });



    }

    public void news_onclick(View view) {
        TextView tv = (TextView) view;
        Intent obj1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yeongnam.com/mnews/newsview.do?mode=newsView&newskey=20151130.010190825110001"));
        Intent obj2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yeongnam.com/edu/newsview.do?mode=newsView&newskey=20151123.010170758550001"));
        Intent obj3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yeongnam.com/edu/newsview.do?mode=newsView&newskey=20151130.010160756070001"));
        Random rand = new Random();
        int num = rand.nextInt(3);
        if (num == 0)
            startActivity(obj1);
        else if (num == 1)
            startActivity(obj2);
        else
            startActivity(obj3);
    }

    private void initToolbar() {
        setTitle("");
        mToolbar = mViewPager.getToolbar();
        setSupportActionBar(mToolbar);
    }

    private void initMaterialViewPager() {
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return RecyclerViewFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "스케줄러";
                    case 1:
                        return "공지사항";
                    case 2:
                        return "뉴스";
                }
                return null;
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorPrimary,
                                ContextCompat.getDrawable(getBaseContext(), R.drawable.schedule4));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorPrimary,
                                ContextCompat.getDrawable(getBaseContext(), R.drawable.noti3));
                    case 2:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorPrimary,
                                ContextCompat.getDrawable(getBaseContext(), R.drawable.news4));
                }
                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    private void initNavigationView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        mAvatarImage = (ImageView) headerView.findViewById(R.id.profile_image);
        mNameText = (TextView) headerView.findViewById(R.id.profile_name_text);

        //setUserInfo();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_board_manage: //게시판관리
                        Toast.makeText(getApplicationContext(), "게시판관리로 이동합니다", Toast.LENGTH_LONG).show();
                        Intent obj1 = new Intent(MainActivity.this, BoardManageUI.class);
                        startActivity(obj1);
                        break;

                    case R.id.nav_student_manage: //학생관리
                        Toast.makeText(getApplicationContext(), "학생관리로 이동합니다", Toast.LENGTH_SHORT).show();
                        Intent obj2 = new Intent(MainActivity.this, DrawActivity.class);
                        startActivity(obj2);
                        //showSche(getResources().getText(R.string.all_schedule));
                        break;

                    case R.id.nav_logout:
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        break;

                    case R.id.nav_setting: {
                        Toast.makeText(getApplicationContext(), "옵션", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,Pref_UI.class);
                        startActivityForResult(intent,1);
                        break;
                    }
                }
                menuItem.setChecked(true);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawers();
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int result, Intent data)
    {
        super.onActivityResult(requestCode, result, data);
        if(requestCode==1)
        {
            display();
        }
    }
    public void display()
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String s="";
    }
    public void showHome() {
        /**
         * Todo 아래의 메소드가 호출되면 MainActivity위로 있는 모든 Fragment가 소멸됨
         */
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

//    private void showSche(CharSequence title) {
//        Intent intent = new Intent(getBaseContext(), ScheActivity.class);
//        intent.putExtra("title", title);
//        startActivity(intent);
//    }
//
//    private void showFindFriends() {
//
//    }
//
//    private void showLocation() {
//        startActivity(new Intent(getBaseContext(), LocationActivity.class));
//    }

//    private void showAccount() {
//        startActivityForResult(new Intent(getBaseContext(), AccoutActivity.class), AccoutActivity.ACCOUNT_REQUEST);
//    }

//    private void showSetting() {
//        startActivity(new Intent(getBaseContext(), SettingActivity.class));
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        AppEventsLogger.activateApp(this);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        AppEventsLogger.deactivateApp(this);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            showHome();
        }
    }

}
