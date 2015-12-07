package kr.ac.ssu.teachu.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.Random;

import kr.ac.ssu.teachu.R;

/**
 * Created by lk on 15. 12. 1..
 */
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

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
    }
    public void news_onclick(View view)
    {
        TextView tv=(TextView)view;
        Intent obj1=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.yeongnam.com/mnews/newsview.do?mode=newsView&newskey=20151130.010190825110001"));
        Intent obj2=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.yeongnam.com/edu/newsview.do?mode=newsView&newskey=20151123.010170758550001"));
        Intent obj3=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.yeongnam.com/edu/newsview.do?mode=newsView&newskey=20151130.010160756070001"));
        Random rand=new Random();
        int num=rand.nextInt(3);
        if(num==0)
            startActivity(obj1);
        else if(num==1)
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
                                ContextCompat.getDrawable(getBaseContext(), R.drawable.schedule2));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorPrimary,
                                ContextCompat.getDrawable(getBaseContext(), R.drawable.noti3));
                    case 2:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorPrimary,
                                ContextCompat.getDrawable(getBaseContext(), R.drawable.news2));
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
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {

                switch (menuItem.getItemId()) {
                    case R.id.nav_board_manage: //게시판관리
                        Toast.makeText(getApplicationContext(),"게시판관리로 이동합니다",Toast.LENGTH_LONG).show();
                        Intent obj1 = new Intent(MainActivity.this,BoardManageUI.class);
                        startActivity(obj1);
                        break;

                    case R.id.nav_student_manage: //학생관리
                        Toast.makeText(getApplicationContext(),"학생관리로 이동합니다",Toast.LENGTH_SHORT).show();
                        Intent obj2=new Intent(MainActivity.this,DrawActivity.class);
                        startActivity(obj2);
                        //showSche(getResources().getText(R.string.all_schedule));
                        break;

                    case R.id.nav_logout:
                          Toast.makeText(getApplicationContext(),"로그아웃 해주세요",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_setting:
                        Toast.makeText(getApplicationContext(),"옵션 바꿔주세요",Toast.LENGTH_SHORT).show();
                        break;
                }
                menuItem.setChecked(true);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawers();
                return true;
            }
        });
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
