package apps.KGSAPP.net.KGSAPP;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static apps.KGSAPP.net.KGSAPP.R.id.drawer;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle toggle;
    boolean isDrawerOpened;
    private String cash;
    private String point;
    private String uuid;

    ///////// 백버튼 설정 관련 ///////////////////////

    // private final long FINISH_INTERVAL_TIME = 2000;
    // private long   backPressedTime = 0;

    ///////// 백버튼 설정 끝 ////////////////////////


    FloatingActionButton fab;

    private static String ListContent_Fragment = "ListContent_Fragment";
    ListContentFragment listContentFragment = new ListContentFragment();
    private static String ListContent_Fragment0 = "ListContent_Fragment0";
    ListContentFragment0 listContentFragment0 = new ListContentFragment0();
    private static String ListContent_Fragment1 = "ListContent_Fragment1";
    ListContentFragment1 listContentFragment1 = new ListContentFragment1();

    private static String Board_Fragment = "Board_Fragment";
    BoardContentFragment boardContentFragment = new BoardContentFragment();
    private static String Board_Fragment1 = "Board_Fragment1";
    BoardContentFragment1 boardContentFragment1 = new BoardContentFragment1();
    private static String Board_Fragment2 = "Board_Fragment2";
    BoardContentFragment2 boardContentFragment2 = new BoardContentFragment2();
    private static String Board_Fragment3 = "Board_Fragment3";
    BoardContentFragment3 boardContentFragment3 = new BoardContentFragment3();

    private static String Nonobject = "Nonobject";
    Nonobject nonobject = new Nonobject();

    private static String Chat_Fragment = "Chat_Fragment";
    Chat_Fragment chat_Fragment = new Chat_Fragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        setContentView(R.layout.activity_main);


        ///push id획득////
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String token = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(getApplicationContext(),"push : "+token,Toast.LENGTH_LONG).show();
        //Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());

        if (token == null) {
            token = FirebaseInstanceId.getInstance().getToken();
            //Toast.makeText(getApplicationContext(),"token : "+token,Toast.LENGTH_LONG).show();
        }
        ////push id획득 끝////

        /*인텐트대신에 파일저장으로 바꿨음
        Intent intent = getIntent();
        if(intent.getStringExtra("lon") == null){
            lon = intent.getStringExtra("lon");
            lat = intent.getStringExtra("lat");
            starflag = intent.getIntExtra("starflag",0);
            Toast.makeText(getApplicationContext(),"starflag :" + starflag,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"lon :" + lon,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"lat :" + lat,Toast.LENGTH_LONG).show();
            if(starflag == 0 ){
                Toast.makeText(getApplicationContext(),"SplashActivity",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,SplashActivity.class));
            }
        }
        */
        //ListContentFragment listContentFragment = (ListContentFragment) getFragmentManager().;  //플래그먼트에서 불러오

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Setting ViewPager for each Tabs
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        //////////////////현재 어떤 페이지 인지 알려줌/////////////
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                if (position == 1) {
                    fab.setVisibility(View.VISIBLE);
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
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        // Create Navigation drawer and inlfate layout
        ///////// 마이메뉴의 헤더인 이미지 부분을 선언한것임!!!
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View hView =  navigationView.getHeaderView(0);
        mDrawerLayout = (DrawerLayout) findViewById(drawer);


        ////////////////////////////마이 페이지 해드 셋팅//////////////////////////
        //hView =  navigationView.inflateHeaderView(R.layout.navheader);
        //개인정보불러오기

        //((SplashActivity)SplashActivity.mContext).getLogin();

        SharedPreferences location = getBaseContext().getSharedPreferences("location", 0);
        String imagepath = location.getString("imagepath", "");
        String lon = location.getString("lon", "0");
        String lat = location.getString("lat", "0");
        String nickname = location.getString("nickname", "0");
        String sex = location.getString("sex", "");
        uuid = location.getString("uuid", "");
        if (sex.equals("0")) {
            sex = getString(R.string.choice1);
        } else {
            sex = getString(R.string.choice2);
        }
        String age = location.getString("age", "0");
        cash = location.getString("cash", "");
        point = location.getString("point", "");
        String mscount = location.getString("mscount", "0");  //메세지 확인했나 카운트보기
        ////처음설치하는 경우 이곳에서 에러가 난다 고쳐야함 어떻게 괼치 생각해보자구...
        double lon_d = Double.parseDouble(lon);
        double lat_d = Double.parseDouble(lat);

        ///////////////탭에 있는 뱃지 알람임///////////
        batgi();
        ////////////////
        //////////셋팅 페이지 클릭 이동///////
        ImageButton settingbtn = (ImageButton) findViewById(R.id.settting_btn);
        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Setting = new Intent(getApplicationContext(), Setting.class); // 다음 넘어갈 클래스 지정
                Setting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Setting); // 다음 화면으로 넘어간다

            }
        });

        //////////셋팅 페이지 이동끝//////


        ///////////////슬라이드 메뉴 설정/////////////////
        TextView navaddress;
        //navaddress = (TextView) hView.findViewById(R.id.navaddress);
        navaddress = (TextView) findViewById(R.id.navaddress);
        navaddress.setText(nickname + "/" + sex + age + getString(R.string.age1));
        ///////////////슬라이드 메뉴 설정/////////////////
        //MenuItem textone = (MenuItem) navigationView.findViewById(R.id.item_one);
        //textone.setTitle("adfsafdsa");
        ///////////////마이페이지 메뉴 해드 설정끝/////////////////

        ////위도 경도를 주소로 바꾸기/////
        getAddress(lat_d, lon_d);

        //Toast.makeText(getBaseContext(), "imagepath2 :" +imagepath, Toast.LENGTH_LONG).show();
        //ImageView imgvw = (ImageView) hView.findViewById(R.id.main_img);
        ImageView imgvw = (ImageView) findViewById(R.id.main_img);
        if (imagepath.equals("null")) {

            imgvw.setImageResource(R.drawable.ic_action_user);

        } else {
            Glide.with(getBaseContext()).load("http://kgs.yunstone.com/uploads/" + imagepath)
                    .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).into(imgvw);
        }
        ////텍스트 뷰일경우 사용한다

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
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        //마이메뉴 안쪽의 버튼을 눌렀을때 액션을 선언해주는곳이다.
        //마이메뉴의 header를 niclude로 분리히면서 따로 쓸수가없게되었음
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();

                        if (id == R.id.nav_share) {
                            //Intent intent = new Intent(getApplicationContext(),Notice.class); // 다음 넘어갈 클래스 지정
                            //startActivity(intent);
                            Intent intent = new Intent(getApplicationContext(), Information_Main.class); // 다음 넘어갈 클래스 지정
                            startActivity(intent);

                            //Toast.makeText(getBaseContext(), "click 공지사항 ", Toast.LENGTH_LONG).show();

                        } else if (id == R.id.nav_faq) {
                            //////바로가기가 안먹어서 일단 보류///////
                            //((Information_Main) mContext).fnqchange();


                            Intent intent = new Intent(getApplicationContext(), Information_Main.class); // 다음 넘어갈 클래스 지정
                            startActivity(intent);

                            // Toast.makeText(getBaseContext(), "click fnq ", Toast.LENGTH_LONG).show();

                        } else if (id == R.id.nav_private) {
                            //Toast.makeText(getBaseContext(), "click 이용약관 ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Information_Main.class); // 다음 넘어갈 클래스 지정
                            startActivity(intent);

                        } else if (id == R.id.nav_mail) {
                            //Toast.makeText(getBaseContext(), "click e-mail ", Toast.LENGTH_LONG).show();
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.setType("plain/text");
                            // email setting 배열로 해놔서 복수 발송 가능
                            String[] address = {"minatalkjpn@gmail.com"};
                            email.putExtra(Intent.EXTRA_EMAIL, address);
                            //email.putExtra(Intent.EXTRA_SUBJECT,"보내질 email 제목");
                            //email.putExtra(Intent.EXTRA_TEXT,"보낼 email 내용을 미리 적어 놓을 수 있습니다.\n");
                            startActivity(email);


                        }


                        // TODO: handle navigation 마이메뉴 부분의 리스트아이템 클릭시행동
                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // Adding Floating Action Button to bottom right of main view

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Hello Snackbar!", Snackbar.LENGTH_LONG).show();

            }
        });


        ///////////////admob 달기//////////////////////
        MobileAds.initialize(this, "ca-app-pub-1873683360216447/6956926610");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice("C55A3B974AF5DF3BE8881F564571FDDF")
        //        .addTestDevice("FF65F310BE9705B1A19DF133E45C1A4D")
        //        .build();
        mAdView.loadAd(adRequest);

        ///////////////admob 달기//////////////////////

        /*
        /////////////////게시판 글쓰기로이동 액티비티간 데이터 전달을 할수있////////
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //Log.d("board_log : ",bundle.getString("minatalk"));
            ///원하는 뷰페이지로 이동한다//////
            viewPager.setCurrentItem(1);
            ///페이지 뷰에서 원하는 프레그먼트로 이동하는건 안되고있음/////


        }
        /////////////////게시판 글쓰기로 이동끝//////
        */
        //////각 페이지별로 뒤로가기 설정////////////////
        Intent intent = getIntent();
        String pagename = intent.getStringExtra("pagename");
        if (pagename != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int pagenum = Integer.parseInt(pagename);

            //Toast.makeText(getBaseContext(), "pagename:" + pagenum, Toast.LENGTH_LONG).show();
            if (pagenum == 1) {
                viewPager.setCurrentItem(2);
                pagenum = 0;
            } else if (pagenum == 2) {
                viewPager.setCurrentItem(1);
                pagenum = 0;
            } else if (pagenum == 3) {
                viewPager.setCurrentItem(3);
                pagenum = 0;
            }
        }
        //////각페이지별 뒤로가기 설정 끝///////////////


    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        String mian_list1 = getString(R.string.mian_list1); //일본어 부분을 위해서 xml에서 가져오는 부분
        String mian_list2 = getString(R.string.mian_list2);
        String mian_list3 = getString(R.string.mian_list3);

        Adapter adapter = new Adapter(getSupportFragmentManager());
        //adapter.addFragment(new ListContentFragment(), mian_list1);
        adapter.addFragment(new ListAllFragment(), mian_list1);
        adapter.addFragment(new BoardAllFragment(), mian_list2);
        //adapter.addFragment(new Chat_Fragment(), mian_list3);
        adapter.addFragment(new ChatAllFragment(), mian_list3);
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
            Log.d("title", title);
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
            // Intent intent = new Intent(getApplicationContext(),Notice.class); // 다음 넘어갈 클래스 지정
            // startActivity(intent);

            return true;
        } else if (id == android.R.id.home) {
            ////////////맨위의 액션바를 누르면 메인 메뉴가 펼쳐진다 드로워를 좀 파봐야 겠다!!!/////
            //Glide.with(getBaseContext()).load("http://kgs.yunstone.com/uploads/1491808680424.jpg").bitmapTransform(new CropCircleTransformation(new MainActivity.CustomBitmapPool())).into(main_my_img);
            //Toast.makeText(getBaseContext(), "mymenu start ", Toast.LENGTH_LONG).show();
            mDrawerLayout.openDrawer(GravityCompat.START);
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

    ////////////////위도 경도를 주소로 치환 //////////////
    /*한국주소 구할때
    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        final Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        //final Geocoder geocoder = new Geocoder(mContext, Locale.JAPAN);
        List<android.location.Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소및 개인정보 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;
                    Log.d("may address", nowAddress);
                    TextView txtaddress =(TextView) findViewById(R.id.my_address);
                    txtaddress.setText(nowAddress);
                    TextView my_cash =(TextView) findViewById(R.id.my_cash);
                    my_cash.setText(cash);
                    TextView my_point =(TextView) findViewById(R.id.my_point);
                    my_point.setText(point);


                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return nowAddress;
    }


    ///일본 주소 구할때
    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        //final Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        //final Geocoder geocoder = new Geocoder(mContext, Locale.JAPAN);
        final Geocoder geocoder = new Geocoder(getBaseContext());
        String address_string = new String();
        List<android.location.Address> list_address;
        String[] address_arr = new String[0];
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                list_address = geocoder.getFromLocation(lat, lng, 1);
                if (!list_address.isEmpty()) {
                    // 주소및 개인정보 받아오기
                    Address address = list_address.get(0);
                    StringBuffer sb = new StringBuffer();

                    String s;
                    for (int i = 0; (s = address.getAddressLine(i)) != null; i++) {
                        sb.append(s + "\n");

                    }

                    address_string = sb.toString();
                    String adress = address_string;
                    address_arr = adress.split(" ");
                    Log.d("address_arr",address_arr[0]);
                    Log.d("address_arr_1",address_arr[1]);
                    Log.d("address_arr_2",address_arr[2]);
                }


                //TextView txtaddress = (TextView) findViewById(R.id.my_address);
                //txtaddress.setText(address_string);
                //TextView my_cash = (TextView) findViewById(R.id.my_cash);
                //my_cash.setText(cash);
                //TextView my_point = (TextView) findViewById(R.id.my_point);
                //my_point.setText(point);
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                // get menu from navigationView
                Menu menu = navigationView.getMenu();
                // find MenuItem you want to change
                MenuItem item_one = menu.findItem(R.id.item_one);
                MenuItem item_two = menu.findItem(R.id.item_two);
                MenuItem item_three = menu.findItem(R.id.item_three);
                // set new title to the MenuItem
                item_one.setTitle(address_string);
                item_two.setTitle(cash);
                item_three.setTitle(point);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address_string;
    }
    */
    public String getAddress(double lat, double lng) {
        String address = null;
        String addrstr = null;

        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //주소 목록을 담기 위한 List
        List<Address> list = null;

        try {
            //주소 목록을 가져온다. --> 위도, 경도, 조회 갯수
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list == null) {
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if (list.size() > 0) {
            Address addr = list.get(0);
            address = addr.getCountryName() + " "       // 나라
                    + addr.getAdminArea() + " "                 // 시
                    + addr.getLocality() + " "                  // 구
                    + addr.getThoroughfare() + " "              // 동
                    + addr.getFeatureName();                    // 지번

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            // get menu from navigationView
            Menu menu = navigationView.getMenu();
            // find MenuItem you want to change
            MenuItem item_one = menu.findItem(R.id.item_one);
            MenuItem item_two = menu.findItem(R.id.item_two);
            MenuItem item_three = menu.findItem(R.id.item_three);
            // set new title to the MenuItem

            //Log.d("address",addr.getCountryName()+"나라/"+addr.getAdminArea()+"시/"+addr.getLocality()+"구/"+addr.getThoroughfare()+"동/"+addr.getFeatureName()+"번지/");

            item_one.setTitle(addr.getLocality() + addr.getThoroughfare());
            item_two.setTitle(cash);
            item_three.setTitle(point);

        }
        return address;

    }

///////////////주소 치환끝///////////////////

    ///////채팅화면에서 삭제후 채팅으로 이동방법 ///
    /// ///FrameLayout이 되어있어야 실해잉되서 chat_fragment를 탭에 등록해준다 ///////////////
    public void chatFragment(int index) {
        chat_Fragment = new Chat_Fragment();

        FrameLayout ft = (FrameLayout) findViewById(R.id.chat_container2);
        ft.removeAllViews();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();


        if (index == 0) {

            fragmentTransaction.replace(R.id.chat_container2, chat_Fragment, Chat_Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

    }
    ////////채팅화면에서 삭제후 채팅으로 이동방법///////////////
    /////////////////리스트프레그먼트 통신//////////////
    public void onFragmentChanged(int index) {
        listContentFragment0 = new ListContentFragment0();
        listContentFragment1 = new ListContentFragment1();
        listContentFragment = new ListContentFragment();

        FrameLayout ft = (FrameLayout) findViewById(R.id.container2);
        ft.removeAllViews();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();

        if (index == 0) {

            fragmentTransaction.replace(R.id.container2, listContentFragment0, ListContent_Fragment0);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();



        } else if (index == 1) {

            fragmentTransaction.replace(R.id.container2, listContentFragment1, ListContent_Fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            //tf.call_fragment();
        } else if (index == 2) {

            fragmentTransaction.replace(R.id.container2, listContentFragment, ListContent_Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    ///////리스트 프레그먼트 통신 끝///////////////


    ///////보드 프레그먼트 통신 ///////////////
    public void baordonFragmentChanged(int index) {
        boardContentFragment = new BoardContentFragment();
        boardContentFragment1 = new BoardContentFragment1();
        boardContentFragment2 = new BoardContentFragment2();
        boardContentFragment3 = new BoardContentFragment3();

        FrameLayout ft = (FrameLayout) findViewById(R.id.board_container2);
        ft.removeAllViews();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();

        if (index == 0) {

            fragmentTransaction.replace(R.id.board_container2, boardContentFragment, Board_Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else if (index == 1) {

            fragmentTransaction.replace(R.id.board_container2, boardContentFragment1, Board_Fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            //tf.call_fragment();
        } else if (index == 2) {

            fragmentTransaction.replace(R.id.board_container2, boardContentFragment2, Board_Fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (index == 3) {

            fragmentTransaction.replace(R.id.board_container2, boardContentFragment3, Board_Fragment3);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (index == 4) {

            fragmentTransaction.replace(R.id.board_container2, boardContentFragment3, Board_Fragment3);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    ///////보드프레그먼트 통신 끝///////////////


    public void nonobject(int index) {
        nonobject = new Nonobject();

        FrameLayout ft = (FrameLayout) findViewById(R.id.board_container2);
        ft.removeAllViews();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();

        if (index == 0) {

            fragmentTransaction.replace(R.id.board_container2, nonobject, Nonobject);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

    }


    //////게시판에 글이 없을때 ////////////////




    //////// 백버튼 설정관련///////////////////

    @Override
    public void onBackPressed() {

        /*
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {

            Intent intent = new Intent();
            String strFlag = "exit";
            intent.putExtra("value", strFlag);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        */
        //Toast.makeText(getApplicationContext(), "종료합니다.", Toast.LENGTH_SHORT).show();
        MobileAds.initialize(this, "ca-app-pub-1873683360216447~6990939416");

        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1873683360216447/9944405819");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("asd", "The interstitial wasn't loaded yet.");
                }
            }
        });

        Intent intent = new Intent(MainActivity.this,MainActivity.class);


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        // 종료 다이얼로그 바디
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        // 다이얼로그 메세지
        //alertdialog.setMessage(message + "\n미나토크 종료");

        // 확인버튼
        alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);

                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                //ActivityCompat.finishAffinity((Activity) getBaseContext());////앱종료가 안됨
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
                //startActivity(intent);
            }
        });

        // 취소버튼
        alertdialog.setNegativeButton(getString(R.string.toast1), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(), getString(R.string.toast1) + getString(R.string.toast3), Toast.LENGTH_SHORT).show();
            }
        });
        // 메인 다이얼로그 생성
        AlertDialog alert = alertdialog.create();
        // 아이콘 설정
        alert.setIcon(R.drawable.ic_add_alert);
        // 타이틀
        alert.setTitle(getString(R.string.toast2));
        // 다이얼로그 보기
        alert.show();


    }

    //////// 백버튼 설정 끝///////////////////

    public void batgi() {
        AQuery aq = new AQuery(this);
        // public JSONArray mItems = new JSONArray();
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/batgi.php?uuid=" + uuid, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    Log.d("batgiurl",url);
                    JSONObject oData = new JSONObject(object.toString());
                    //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                    JSONArray arrData = new JSONArray(oData.getString("result"));

                    JSONObject getlist = arrData.getJSONObject(0);
                    String baginum = getlist.getString("batgicount");
                    Log.d("baginum", baginum);
                    /////////뱃지 달기//////////

                    if(baginum.equals("0")){

                    }else{
                        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
                        BadgeView batgi = new BadgeView(MainActivity.this, tabs);
                        batgi.setText(baginum);
                        batgi.show();

                        ///////뱃지기능이 안되서 기다림/////////
                       // setBadge(getApplicationContext(),0);
                    }
                    /////////뱃지 달기 끝///////

                    //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                } catch (Exception e) {

                }

            }
        });
    }


}



