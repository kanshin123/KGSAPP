package apps.KGSAPP.net.KGSAPP;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunseokchoi on 2017. 6. 21..
 */

public class Information_Main extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle toggle;
    boolean isDrawerOpened;
    private String cash;
    private String point;

    FloatingActionButton fab;


    //ListContentFragment0 listContentFragment0;
    //ListContentFragment listContentFragment;
    ListTitleFragment ListTitleFragment;

    private static String Fnq_Fragment = "Fnq_Fragment";
    Fnq_Fragment fnq_Fragment = new Fnq_Fragment();



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();

        setContentView(R.layout.informatino_main);




        //ListContentFragment listContentFragment = (ListContentFragment) getFragmentManager().;  //플래그먼트에서 불러오

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);

        //Setting ViewPager for each Tabs
        final ViewPager viewPager = (ViewPager) findViewById(R.id.info_viewpager);
        setupViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.info_fab);
        fab.setVisibility(View.INVISIBLE);


        //////////////////현재 어떤 페이지 인지 알려줌/////////////
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.info_fab);
                if (position == 1) {
                    fab.setVisibility(View.INVISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ////Todo
                            //Toast.makeText(getApplicationContext(), "스낵바액션바", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), BoardWrite.class); // 다음 넘어갈 클래스 지정
                            startActivity(intent);
                        }
                    });
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }
                //Toast.makeText(getApplicationContext(), "position" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //////////////////현재 어떤 페이지 인지 알려줌 끝/////////////


        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.info_tabs);
        tabs.setupWithViewPager(viewPager);
        // Create Navigation drawer and inlfate layout
        ///////// 마이메뉴의 헤더인 이미지 부분을 선언한것임!!!
        //NavigationView navigationView = (NavigationView) findViewById(R.id.info_nav_view);
        //View hView =  navigationView.getHeaderView(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.info_drawer);


        // 생성자에 인자로 들어간 문자열은 화면 출력 문자열이 아니라
        // 상태 표현을 위한 접근성 차원에서 제공하는 문자열
        // 마이메뉴가 옆으로 올려져있는지 아닌지 확인 할수있다
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            // 이벤트 추가, 필수는 아님
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Toast.makeText(getBaseContext(), "drawer start ", Toast.LENGTH_LONG).show();

                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Toast.makeText(getBaseContext(), "drawer closed ", Toast.LENGTH_LONG).show();
                isDrawerOpened = false;
            }
        };
        mDrawerLayout.addDrawerListener(toggle);   // 이벤트 핸들러인 toggle을 적용

        /////////드로우 메뉴 마이메뉴에 사진 바꾸기/////////
        //ImageView main_img = (ImageView) findViewById(R.id.main_img);
        //Glide.with(getBaseContext()).load("http://kgs.yunstone.com/uploads/1491808680424.jpg")
        // .bitmapTransform(new CropCircleTransformation(new MainActivity.CustomBitmapPool())).into(main_img);
        /////////드로우 메뉴 마이메뉴에 사진 바꾸기 끝/////////


        // Adding menu icon to Toolbar

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_home_wihte, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            //supportActionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_navigate_back));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        //마이메뉴 안쪽의 버튼을 눌렀을때 액션을 선언해주는곳이다.
        //마이메뉴의 header를 niclude로 분리히면서 따로 쓸수가없게되었음


        /////////////////게시판 글쓰기로이동 액티비티간 데이터 전달을 할수있////////
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //Log.d("board_log : ",bundle.getString("minatalk"));
            ///원하는 뷰페이지로 이동한다//////
            viewPager.setCurrentItem(1);
            ///페이지 뷰에서 원하는 프레그먼트로 이동하는건 안되고있음/////


        }
        /////////////////게시판 글쓰기로 이동끝//////

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
        //adapter.addFragment(new ListContentFragment(), mian_list1);
        adapter.addFragment(new Notice_Fragment(), getString(R.string.notice1));
        adapter.addFragment(new Fnq_Fragment(), getString(R.string.notice2));
        adapter.addFragment(new Policy_Fragment(), getString(R.string.notice3));
        //adapter.addFragment(new Notice_Fragment(), "notice"); ////뷰페이지를 추가하여 보이지 않게한다
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        //ArrayList<HashMap<String, String>> mFragmentList;
        //ArrayList<HashMap<String, String>> mFragmentTitleList;


        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);


        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            Log.d("title",title);
            //if(title.equals("notice")){

            //}else{
            mFragmentTitleList.add(title);
            //}

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    ////////////////////////펼치는 메뉴 정의////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_navigation, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ///오른쪽 윗쪽에 있는 .... 안에 있는 영역 클릭시/////////
        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_share) {
            Intent intent = new Intent(getApplicationContext(),Notice.class); // 다음 넘어갈 클래스 지정
            startActivity(intent);

            return true;
        } else if (id == android.R.id.home) {
            ////////////맨위의 액션바를 누르면 메인 메뉴가 펼쳐진다 드로워를 좀 파봐야 겠다!!!/////
            //Glide.with(getBaseContext()).load("http://kgs.yunstone.com/uploads/1491808680424.jpg").bitmapTransform(new CropCircleTransformation(new MainActivity.CustomBitmapPool())).into(main_my_img);
            //Toast.makeText(getBaseContext(), "mymenu start ", Toast.LENGTH_LONG).show();
            //mDrawerLayout.openDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    /////glied를 쓰기위한 선언
    public static class CustomBitmapPool implements BitmapPool {
        @Override
        public int getMaxSize() {
            return 0;
        }

        @Override
        public void setSizeMultiplier(float sizeMultiplier) {

        }

        @Override
        public boolean put(Bitmap bitmap) {
            return false;
        }

        @Override
        public Bitmap get(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public Bitmap getDirty(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public void clearMemory() {

        }

        @Override
        public void trimMemory(int level) {

        }
    }

    ///////보드 프레그먼트 통신 작동안해서 일단..보류..///////////////
    public void fnqchange() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.info_viewpager);
        setupViewPager(viewPager);

        viewPager.setCurrentItem(1);


        /*
        fnq_Fragment = new Fnq_Fragment();

        FrameLayout ft = (FrameLayout) findViewById(R.id.fnq_container1);
        ft.removeAllViews();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();

        if (index == 0) {

            fragmentTransaction.replace(R.id.fnq_container1, fnq_Fragment, Fnq_Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }
       */
    }
    ///////보드프레그먼트 통신 끝///////////////


}
