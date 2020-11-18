package apps.KGSAPP.net.KGSAPP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
import static apps.KGSAPP.net.KGSAPP.BaseApplication.progressOFF;
import static apps.KGSAPP.net.KGSAPP.BaseApplication.progressON;
import static apps.KGSAPP.net.KGSAPP.R.id.user_image;


public class ChatPage_Fragment extends AppCompatActivity implements RewardedVideoAdListener{
    public String mBbsName;
    private RecyclerViewListAdapter mRvListAdapter;
    public boolean mVisible = false;
    public boolean mLoading = false;
    public String mApiName = "";
    public static final String MUID = "MUID";
    public static final String FUID = "FUID";
    public String muid;
    public String fuid;

    public static final String sex = "SEX";
    public static final String uuid = "UUID";
    public static final String sexflag = "SEXFLAG";
    public static final String Imgpass = "Imgpass";
    public static final String Imgtopass = "Imgtopass";
    public static final String Message = "Message";
    public String msex;
    public String message;
    public String muuid;
    public String msexflag;
    public String mimgpass;
    public String mimgtopass;
    public EditText messagebox;
    public static Context mContext;
    public String mcount;
    LinearLayout layout;
    ScrollView mImagescroll;

    public int lastVisibleItem;

    public String urlflag;
    private String dbuid; /////메세지 게시번호 uid///////
    private int arrlenth;
    private String replacemsg;


    ////////////사진관련///////////////////
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absoultePath;

    private String fileName;
    private String filePath;
    private String serverUrl;
    private String token;
    ////////////사진관련///////////////////

    final Context context = this;

    //////admob 관련//////////////////////////
    private RewardedVideoAd mRewardedVideoAd;
    private String adsID="ca-app-pub-1873683360216447/4325413850";
    //////admob 관련//////////////////////////

    private int homebutton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chatmain);
        setContentView(R.layout.chat_recyler);

        //맨위의 상태바 색깔 바꾸기
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.BLACK);
            }
        }
        mContext = this;


        Intent intent = getIntent();
        muuid = intent.getStringExtra(uuid);
        muid = intent.getStringExtra(MUID);
        fuid = intent.getStringExtra(FUID);
        msex = intent.getStringExtra(sex);
        msexflag = intent.getStringExtra(sexflag);
        mimgpass = intent.getStringExtra(Imgpass);
        mimgtopass = intent.getStringExtra(Imgtopass);
        message = intent.getStringExtra(Message);

        //Toast.makeText(this, "muid :" + muid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "fuid :" + fuid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "mimgtopass :" + mimgtopass, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "msexflag :" + msexflag, Toast.LENGTH_SHORT).show();
        messagebox = (EditText) findViewById(R.id.chat_txt);

        ///////////////스크롤뷰 밑에 터치하기 laout에 하면 안되고 recyclerview 해야 함//////////
        RecyclerView layout = (RecyclerView) findViewById(R.id.chat_recycler_view);

        mImagescroll = (ScrollView) findViewById(R.id.chat_scroll);

        //////////////admob 동영성추가 //////////////////////
        MobileAds.initialize(this,adsID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
        //////////////admob 동영성추가 //////////////////////

        /*
        layout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    //////스크롤뷰 밑에를 터치하기위한 설정/////
                    mImagescroll.requestDisallowInterceptTouchEvent(true);

                    hideKeyboard(v);

                    //버튼 다운시 동작

                }
                return false;
            }
        });
        ///////////////스크롤뷰 밑에 터치하기 끝///////////////////////////////////////////
*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        setRecyclerView(recyclerView);

        /////////////카메라 기능 퍼미션///////////////////////////////////////
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(MessengerActivity.this, getString(R.string.toast6), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(ChatPage_Fragment.this, getString(R.string.toast7)+"\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getString(R.string.toast8)+"\n\n"+getString(R.string.toast9)+" [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
        /////////////카메라 기능 퍼미션///////////////////////////////////////

        ///메세지 전송버튼 눌었을때 디비에 입력및 푸쉬 전송
        findViewById(R.id.chat_send_bnt).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        if (messagebox.getText().toString().length() == 0) {
                            Toast.makeText(ChatPage_Fragment.this, getString(R.string.toast21), Toast.LENGTH_SHORT).show();
                            messagebox.requestFocus();
                            return;
                        }
                        //여기에 이벤트를 적어주세요
                        replacemsg = messagebox.getText().toString();

                        ///////디비에 줄바꿈이 들어가질 않아 치환해주고 웹서버에서 다시 \n으로 치환해준다///////////
                        replacemsg = replacemsg.replace(System.getProperty("line.separator"), "__");
                        String flagurl = "http://kgs.yunstone.com/ajax/message.php?uid=" + muid + "&messageid=" + fuid +
                                "&uuid=" + muuid + "&message=" + replacemsg;
                        //Toast.makeText(getBaseContext(), "flagurl :" + flagurl, Toast.LENGTH_LONG).show();
                        Log.d("flagurl : ", flagurl);
                        sendmessage(flagurl);


                    }
                }
        );

        iv_UserPhoto = (ImageView) this.findViewById(user_image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);

        setSupportActionBar(toolbar);
        // Adding menu icon to Toolbar

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)

        {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_home_wihte, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ////////////////////채팅창+ 플러스 마크 눌렀을때/////////////////////////////
        ImageButton chatpicbtn = (ImageButton) findViewById(R.id.chat_picture);
        chatpicbtn.findViewById(R.id.chat_picture).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요
                        showDialog();

                    }
                }
        );
        ////////////////////채팅창+ 플러스 마크 눌렀을때/////////////////////////////

        ////////////////////채팅입력 눌렀을때 스크롤 아래로 내리기/////////////////////
        EditText editText = (EditText) findViewById(R.id.chat_txt);

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scrollend();

            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // 포커스를 얻으면
            @Override
            public void onFocusChange(View v, boolean hasFocus) { // 포커스가 한뷰에서 다른뷰로 바뀔때
                if (hasFocus) {
                    scrollend();
                }
            }
        });
        ////////////////////채팅입력 눌렀을때 스크롤 아래로 내리기/////////////////////

        /*
        TextView myEditText = (TextView) findViewById(R.id.editText1);
        final ScrollView myScrollView = (ScrollView) findViewById(R.id.chat_scroll);
        myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus == true ){

                    myScrollView.postDelayed( new Runnable(){

                        @Override
                        public void run() {
                            ///스크롤 마지막으로 이동
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
                            //final ScrollView scrollView = (ScrollView) findViewById(R.id.chat_scroll);
                            recyclerView.getLayoutManager().scrollToPosition(mRvListAdapter.mItems.length()-1);
                            //Toast.makeText(getBaseContext(), "scroll up :  "+mRvListAdapter.mItems.length(), Toast.LENGTH_LONG).show();
                            myScrollView.smoothScrollTo(0, myScrollView.getBottom());


                        }

                    }, 100);

                }
            }
        });
        */
        //Toast.makeText(getBaseContext(), "refresh", Toast.LENGTH_LONG).show();
        //getListData(null,null);
        getListData(mApiName, "", 0);

    }


    //////////////툴바 설정임///////
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.chat_action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            ////////////맨위의 액션바를 누르면 메인 메뉴가 펼쳐진다 드로워를 좀 파봐야 겠다!!!/////
            //Glide.with(getBaseContext()).load("http://kgs.yunstone.com/uploads/1491808680424.jpg").bitmapTransform(new CropCircleTransformation(new MainActivity.CustomBitmapPool())).into(main_my_img);
            //Toast.makeText(getBaseContext(), "mymenu start ", Toast.LENGTH_LONG).show();
            //mDrawerLayout.openDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        //getListData(mApiName, "");

        if (mRvListAdapter.mItems.length() == 0) {
            //로딩표시
            //mRvListAdapter.mItems.put(null);
            //mRvListAdapter.notifyItemInserted(mRvListAdapter.mItems.length() - 1);
            //getListData(mApiName, "", 0);
        }
        /*
        else{
            getListData("", dbuid,0);
        }
        */
    }


    @Override
    public void onPause() {
        super.onPause();
        //homebutton = 1;
        //Toast.makeText(getBaseContext(), "onpause:"+ homebutton, Toast.LENGTH_LONG).show();
        //Save message
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
        //startActivity(intent);

    }

    private void setRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    //Toast.makeText(getBaseContext(), "lastVisibleItem : "+lastVisibleItem, Toast.LENGTH_SHORT).show();
                    if (lastVisibleItem == totalItemCount - 1) {

                        if (!mLoading) {
                            mLoading = true;
                            try {
                                String sNo = mRvListAdapter.mItems.getJSONObject(mRvListAdapter.mItems.length() - 1).getString("no");
                                //Toast.makeText(getBaseContext(), "sNo : "+sNo, Toast.LENGTH_SHORT).show();
                                //로딩표시
                                //mRvListAdapter.mItems.put(null);
                                //mRvListAdapter.notifyItemInserted(mRvListAdapter.mItems.length() - 1);

                                //getListData("1010", "sNo");
                                //getListData(mApiName, sNo);
                            } catch (Exception e) {

                            }
                            //showToast("스크롤 마지막!!" + mBbsName);
                        }

                        //Toast.makeText(getBaseContext(), "스크롤마직막 입니다", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        mRvListAdapter = new RecyclerViewListAdapter();
        //mRvListAdapter.mBbsName = mBbsName;
        recyclerView.setAdapter(mRvListAdapter);


    }


    public void getListData(String sListName, final String sLastNo, final int arrnum) {
        ////메세지 박스에 표시하기 위한 테이터 불러오기
        //SharedPreferences location = getSharedPreferences("location", 0);
        //Toast.makeText(getContext(), "lonFrag :" + lon, Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), "latFrag :" + lat, Toast.LENGTH_LONG).show();
        //Toast.makeText(getBaseContext(), "lonFrag muid :" + muid, Toast.LENGTH_LONG).show();
        //Toast.makeText(getBaseContext(), "lonFrag fuid :" + fuid, Toast.LENGTH_LONG).show();
        //Toast.makeText(getBaseContext(), "sLastNo :" + sLastNo, Toast.LENGTH_LONG).show();
        /////로딩시작////////////////////////////
        progressON(this, "Loading...");
        /////로딩 끝////////////////////////////


        if (sListName.equals("1010")) {
            /////////아이템 추가기능을 했으나 아이텀을 추가하지 못했음 하나씩 리사클러뷰를 넣는작업 하지 못함////////////////////////
            //urlflag = "http://kgs.yunstone.com/ajax/messagesend.php?uid=" + muid + "&fuid=" + fuid + "&dbuid=" + dbuid;
            Log.e("urlflag", urlflag);

            AQuery aq = new AQuery(this.getApplication());
            aq.ajax(urlflag, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    final int arrnumnum = arrnum;
                    //if (object != null) {
                    //로딩표시 지움.
                    //mRvListAdapter.mItems.remove(mRvListAdapter.mItems.length() - 1);
                    //mRvListAdapter.notifyItemRangeRemoved(0,mRvListAdapter.mItems.length());
                    //mLoading = false;
                    //Toast.makeText(getBaseContext(), "arrnum" + arrnumnum, Toast.LENGTH_SHORT).show();

                    try {
                        //Toast.makeText(getBaseContext(), "arrnum1" + arrnumnum, Toast.LENGTH_SHORT).show();
                        //mRvListAdapter.mItems = new JSONArray();
                        JSONObject oData = new JSONObject(object.toString());

                        JSONArray arrData = new JSONArray(oData.getString("result"));
                       // ArrayList<String> arrData = new JSONArray(oData.getString("result"));

                        //JSONArray arrData = new JSONArray(oData.getString(""));
                        //mRvListAdapter.mItems.put(""); //리스트뷰릴 전부 날리는 현상이 일어난다

                        //Toast.makeText(getBaseContext(), "arrnum2" + arrnumnum, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < arrData.length(); i++) {
                            arrData = new JSONArray(oData.getString("result"));
                            mRvListAdapter.mItems.put(arrData.getJSONObject(i));
                            //Toast.makeText(getBaseContext(), "i" + i, Toast.LENGTH_SHORT).show();
                            JSONObject getlist = arrData.getJSONObject(0);
                            //Toast.makeText(getBaseContext(), "message" + getlist.getString("message"), Toast.LENGTH_SHORT).show();

                        }

                        //JSONObject getlist = arrData.getJSONObject(0);
                        //Toast.makeText(getBaseContext(), "message" + getlist.getString("message"), Toast.LENGTH_SHORT).show();
                       // mRvListAdapter.mItems.put(arrData:"[{"uid":"6217","flag":"0","message":"ㅎㅎㅎ","regdate":"2018-03-26 17:54:36","fsend":"0","imgcon":"1520679129984.jpg","imagepath":"","nickname":"tablet","sex":"1","mcount":"0"}]");

                        mRvListAdapter.notifyDataSetChanged();

                        mLoading = false;

                        //////////////자판 내리기///////////////
                        messagebox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    hideKeyboard(v);
                                }
                            }
                        });
                        //////////////자판 내리기 끝///////////////


                        //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                    } catch (Exception e) {

                    }
                    //}
                    scrollend();//////스크롤 내기리
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            /////로딩////////////////////////////
                            progressOFF();
                            /////로딩 끝////////////////////////////
                        }
                    }, 1000);  // 2000은 2초를 의미합니다.
                    scrollend();//////스크롤 내기리
                }

            });


        } else if(sListName.equals("1012")){

            urlflag = "http://kgs.yunstone.com/ajax/messagefri.php?uid=" + muid + "&fuid=" + fuid + "&getflag=0";
            Log.e("urlflag", urlflag);

            AQuery aq = new AQuery(this.getApplication());
            aq.ajax(urlflag, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    try {
                        JSONObject oData = new JSONObject(object.toString());

                        JSONArray arrData = new JSONArray(oData.getString("result"));
                        //JSONArray arrData = new JSONArray(oData.getString(""));
                        //mRvListAdapter.mItems.put(null); 리스트뷰릴 전부 날리는 현상이 일어난다

                        //Toast.makeText(getBaseContext(), "arrData.length" + arrData.length(), Toast.LENGTH_SHORT).show();
                        arrlenth = arrData.length();////게시물 번호가져오기

                        for (int i = 0; i < arrData.length(); i++) {
                            mRvListAdapter.mItems.put(arrData.getJSONObject(i));
                            //Toast.makeText(getBaseContext(), "i :" + i, Toast.LENGTH_SHORT).show();
                            JSONObject getlist = arrData.getJSONObject(0);
                            mcount = getlist.getString("mcount");
                        }


                        mRvListAdapter.notifyDataSetChanged();

                        mLoading = false;

                        ////////////// admob리워드 광고///////////////////////
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                           // Toast.makeText(getBaseContext(), "mRewardedVideoAd", Toast.LENGTH_SHORT).show();
                        }
                        ////////////// admob리워드 광고///////////////////////

                        //////////////자판 내리기///////////////
                        messagebox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    hideKeyboard(v);
                                }
                            }
                        });
                        //////////////자판 내리기 끝///////////////


                        //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                    } catch (Exception e) {

                    }
                    //}
                    scrollend();//////스크롤 내기리
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            /////로딩////////////////////////////
                            progressOFF();
                            /////로딩 끝////////////////////////////
                        }
                    }, 1000);  // 2000은 2초를 의미합니다.
                    scrollend();//////스크롤 내기리
                }

            });

        }else {
            urlflag = "http://kgs.yunstone.com/ajax/messagefri.php?uid=" + muid + "&fuid=" + fuid + "&getflag=0";
            Log.e("urlflag", urlflag);

            AQuery aq = new AQuery(this.getApplication());
            aq.ajax(urlflag, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {

                    //if (object != null) {
                    //로딩표시 지움.
                    //mRvListAdapter.mItems.remove(mRvListAdapter.mItems.length() - 1);
                    //mRvListAdapter.notifyItemRangeRemoved(0,mRvListAdapter.mItems.length());
                    //mLoading = false;


                    try {
                        JSONObject oData = new JSONObject(object.toString());

                        JSONArray arrData = new JSONArray(oData.getString("result"));
                        //JSONArray arrData = new JSONArray(oData.getString(""));
                        //mRvListAdapter.mItems.put(null); 리스트뷰릴 전부 날리는 현상이 일어난다

                        //Toast.makeText(getBaseContext(), "arrData.length" + arrData.length(), Toast.LENGTH_SHORT).show();
                        arrlenth = arrData.length();////게시물 번호가져오기

                        for (int i = 0; i < arrData.length(); i++) {
                            mRvListAdapter.mItems.put(arrData.getJSONObject(i));
                            //Toast.makeText(getBaseContext(), "i :" + i, Toast.LENGTH_SHORT).show();
                            JSONObject getlist = arrData.getJSONObject(0);
                            mcount = getlist.getString("mcount");
                        }
                        /*
                        SharedPreferences location = getBaseContext().getSharedPreferences("location", 0);
                        int orimscount = parseInt(location.getString("mscount", "0"));
                        int nowmscount = Integer.parseInt(mcount);
                        mcount = String.valueOf(orimscount - nowmscount);
                        SharedPreferences location1 = getSharedPreferences("location", MODE_PRIVATE);
                        SharedPreferences.Editor editor = location1.edit();
                        editor.putString("mscount", mcount);
                        editor.commit(); //완료한다.
                        */
                        ///스크롤 마지막으로 이동 좀더 부드럽게 움직이고 싶지만 안됨 나중에 체크해야함
                        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
                        //recyclerView.getLayoutManager().scrollToPosition(mRvListAdapter.mItems.length() - 1);


                        //mRvListAdapter.notifyItemRangeRemoved(0 ,arrData.length());
                        mRvListAdapter.notifyDataSetChanged();

                        mLoading = false;

                        //////////////자판 내리기///////////////
                        messagebox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    hideKeyboard(v);
                                }
                            }
                        });
                        //////////////자판 내리기 끝///////////////

                        ////////////// admob전면 광고///////////////////////
                        getAD();
                        ////////////// admob전면 광고///////////////////////


                        //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                    } catch (Exception e) {

                    }
                    //}
                    scrollend();//////스크롤 내기리
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            /////로딩////////////////////////////
                            progressOFF();
                            /////로딩 끝////////////////////////////
                        }
                    }, 1000);  // 2000은 2초를 의미합니다.
                    scrollend();//////스크롤 내기리
                }

            });
        }


    }

    /*
        public void putListData() throws JSONException {

            String json ="{\"result\":[{\"message\":\"aaaaaaaa\"}]}";

            JSONObject oData = new JSONObject(json);

            JSONArray arrData = new JSONArray(oData.getString("result"));
            mRvListAdapter.mItems.put(arrData.getJSONObject(0));
            //mRvListAdapter.mItems.put(message,"aaaaaa");
            //item_holder.mMsgTo.setText(mItems.getJSONObject(position).getString("message"));
           // mItems.getJSONObject(position).getString("regdate");

            mRvListAdapter.notifyDataSetChanged();

        }

    */
    public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        //public ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private final int VIEW_TYPE_Right = 2;
        private final int VIEW_IMG_Left = 3;
        private final int VIEW_IMG_Right = 4;

        public JSONArray mItems = new JSONArray();
        public String mBbsName = "";
        //public String muid;
        //public String fuid;
        public int itemLayout;

       //final ArrayList<String> mItems = new ArrayList<>();

        @Override
        //public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //필수 인터패이스
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //필수 인터패이스
            //Toast.makeText(getBaseContext(), "viewType :" + viewType, Toast.LENGTH_SHORT).show();
            if (viewType == VIEW_TYPE_ITEM) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chatitem, viewGroup, false);
                return new ListItemViewHolder(itemView);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false);
                return new LoadingViewHolder(view);
            } else if (viewType == VIEW_TYPE_Right) {
                View itemRight = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_right, viewGroup, false);
                return new ListRightViewHolder(itemRight);
            } else if (viewType == VIEW_IMG_Left) {
                View imgLeft = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_img_left, viewGroup, false);
                return new ImageLeftViewHolder(imgLeft);
            } else if (viewType == VIEW_IMG_Right) {
                View imgRight = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_img_right, viewGroup, false);
                return new ImageRightViewHolder(imgRight);
            }

            return null;
        }

        @Override
        //public void onBindViewHolder(final ListItemViewHolder holder, int position) { //필수 인터패이스
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) { //필수 인터패이스
            final String imageurl;
            String regsplit;
            String[] regarr;

            final String imgurl = "http://kgs.yunstone.com/uploads/";

            try {
                dbuid = mItems.getJSONObject(position).getString("uid");

                //Log.d("dbuid", dbuid);
                if (holder instanceof ListItemViewHolder) {
                    ListItemViewHolder item_holder = (ListItemViewHolder) holder;
                    //데이터가 없을때 표시해야 하는데...
                    //Toast.makeText(getBaseContext(), "position :" + position, Toast.LENGTH_SHORT).show();
                    regsplit = mItems.getJSONObject(position).getString("regdate");
                    regarr = regsplit.split(" ");
                    final String messageuid = mItems.getJSONObject(position).getString("messageuid");


                    //item_holder.mMsgTo.setText(mItems.getJSONObject(position).getString("message"));

                    //imageurl = "http://kgs.yunstone.com/upload/" + mItems.getJSONObject(position).getString("imgcon") + ".jpg";

                    if (msexflag.equals("0")) { //chat_Fragment에서 이미지가 있는지 없는지 체크해서 없다면 맞는이미지를 보여주는것
                        //Toast.makeText(getBaseContext(), "mItems.sex :" + mItems.getJSONObject(position).getString("sex"), Toast.LENGTH_SHORT).show();
                        if (msex.equals("0")) { // mItems.getJSONObject(position).getString("sex") 디비에서 불러오는 것임 디비필드를 써야
                            item_holder.mImageTo.setImageResource(R.drawable.icon_man);
                        } else {
                            item_holder.mImageTo.setImageResource(R.drawable.icon_woman);
                        }


                    } else {
                        Glide.with(item_holder.mLayoutTo.getContext()).load(mimgtopass).bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).into(item_holder.mImageTo);

                    }
                    item_holder.mNicnameTo.setText(mItems.getJSONObject(position).getString("nickname"));

                    item_holder.mMsgTo.setText(mItems.getJSONObject(position).getString("message"));
                    item_holder.mMsgTimeTo.setText(regarr[1]);


                    //item_holder.mUserId.setText(mItems.getJSONObject(position).getString("message"));
                    //Log.e("flag", mItems.getJSONObject(position).getString("flag"));
                    //Log.e("message", mItems.getJSONObject(position).getString("message"));
                    //Log.e("mcount", mItems.getJSONObject(position).getString("mcount"));
                    //Log.e("regdate", mItems.getJSONObject(position).getString("regdate"));

                    /*
                    item_holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    */


                    item_holder.mMsgTo.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // 다이얼로그 바디
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(ChatPage_Fragment.this);
                            // 다이얼로그 메세지
                            alertdialog.setMessage("*" + getString(R.string.chatting4));

                            // 확인버튼
                            alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AQuery aq = new AQuery(ChatPage_Fragment.this);
                                    aq.ajax("http://kgs.yunstone.com/ajax/messagedel.php?messageuid=" + messageuid +"&muid="+fuid+"&fuid="+muid+"&position=1", JSONObject.class, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject object, AjaxStatus status) {
                                            if (object != null) {
                                                //로딩표시 지움.
                                                //Log.d("cahtdel",url);
                                                try {
                                                    JSONObject oData = new JSONObject(object.toString());

                                                    JSONArray arrData = new JSONArray(oData.getString("result"));


                                                    for (int i = 0; i < arrData.length(); i++) {
                                                        JSONObject getlist = arrData.getJSONObject(i);

                                                        String logincheck = getlist.getString("y");

                                                        if (logincheck.equals("0")) {
                                                            Toast.makeText(ChatPage_Fragment.this, getString(R.string.chatting5), Toast.LENGTH_LONG).show();

                                                        } else {
                                                            //Toast.makeText(getContext(), "다시 한번 시도해주세요!!", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                    // 3초간 딜레이를 줘서 토스트메세지를 보여준다
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            /////// 게시판 내글을  리플레쉬해서 삭제된 화면을 보여준다///////
                                            //////로딩 시작/////
                                            progressON(ChatPage_Fragment.this, "Loading...");

                                            /////로딩시작//////
                                            //MainActivity activity = (MainActivity) getActivity();
                                            //activity.chatFragment(0);
                                            //getListData("", "", 0);
                                            mRvListAdapter.mItems = new JSONArray();
                                            getListData("1012", dbuid, arrlenth);
                                        }
                                    }, 1000);  // 2000은 2초를 의미합니다.


                                }
                            });

                            // 취소버튼
                            alertdialog.setNegativeButton(getString(R.string.toast1), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Toast.makeText(getContext(), "'취소'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // 메인 다이얼로그 생성
                            AlertDialog alert = alertdialog.create();
                            // 아이콘 설정
                            alert.setIcon(R.drawable.ic_add_alert);
                            // 타이틀
                            alert.setTitle(getString(R.string.board12));
                            // 다이얼로그 보기
                            alert.show();
                            return false;
                        }
                    });

                } else if (holder instanceof LoadingViewHolder) {
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setIndeterminate(true);

                } else if (holder instanceof ListRightViewHolder) {  //////////새로운 홀더 생성한것
                    ListRightViewHolder item_rholder = (ListRightViewHolder) holder;

                    regsplit = mItems.getJSONObject(position).getString("regdate");
                    regarr = regsplit.split(" ");
                    final String messageuid = mItems.getJSONObject(position).getString("messageuid");
                    //item_rholder.mMsgMe.setText(mItems.getJSONObject(position).getString("message"));
                    item_rholder.mMsgMe.setText(mItems.getJSONObject(position).getString("message"));
                    item_rholder.mMsgTimeMe.setText(regarr[1]);

                    item_rholder.mMsgMe.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // 다이얼로그 바디
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(ChatPage_Fragment.this);
                            // 다이얼로그 메세지
                            alertdialog.setMessage("*" + getString(R.string.chatting4));

                            // 확인버튼
                            alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AQuery aq = new AQuery(ChatPage_Fragment.this);
                                    aq.ajax("http://kgs.yunstone.com/ajax/messagedel.php?messageuid=" + messageuid +"&muid="+muid+"&fuid="+fuid+"&position=0", JSONObject.class, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject object, AjaxStatus status) {
                                            if (object != null) {
                                                //로딩표시 지움.
                                                Log.d("boarddel",url);
                                                try {
                                                    JSONObject oData = new JSONObject(object.toString());

                                                    JSONArray arrData = new JSONArray(oData.getString("result"));


                                                    for (int i = 0; i < arrData.length(); i++) {
                                                        JSONObject getlist = arrData.getJSONObject(i);

                                                        String logincheck = getlist.getString("y");

                                                        if (logincheck.equals("0")) {
                                                            Toast.makeText(ChatPage_Fragment.this, getString(R.string.chatting5), Toast.LENGTH_LONG).show();

                                                        } else {
                                                            //Toast.makeText(getContext(), "다시 한번 시도해주세요!!", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                    // 3초간 딜레이를 줘서 토스트메세지를 보여준다
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            /////// 게시판 내글을  리플레쉬해서 삭제된 화면을 보여준다///////
                                            //////로딩 시작/////
                                            progressON(ChatPage_Fragment.this, "Loading...");

                                            /////로딩시작//////
                                            //MainActivity activity = (MainActivity) getActivity();
                                            //activity.chatFragment(0);
                                            //getListData("", "", 0);
                                            mRvListAdapter.mItems = new JSONArray();
                                            getListData("1012", dbuid, arrlenth);
                                        }
                                    }, 1000);  // 2000은 2초를 의미합니다.


                                }
                            });

                            // 취소버튼
                            alertdialog.setNegativeButton(getString(R.string.toast1), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Toast.makeText(getContext(), "'취소'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // 메인 다이얼로그 생성
                            AlertDialog alert = alertdialog.create();
                            // 아이콘 설정
                            alert.setIcon(R.drawable.ic_add_alert);
                            // 타이틀
                            alert.setTitle(getString(R.string.board12));
                            // 다이얼로그 보기
                            alert.show();
                            return false;
                        }
                    });

                } else if (holder instanceof ImageLeftViewHolder) {  //////////새로운 홀더 생성한것
                    regsplit = mItems.getJSONObject(position).getString("regdate");
                    regarr = regsplit.split(" ");
                    ImageLeftViewHolder img_Left_holder = (ImageLeftViewHolder) holder;
                    final String imgpathurl = imgurl + mItems.getJSONObject(position).getString("imagepath");
                    final String messageuid = mItems.getJSONObject(position).getString("messageuid");

                    if (msexflag.equals("0")) { //chat_Fragment에서 이미지가 있는지 없는지 체크해서 없다면 맞는이미지를 보여주는것
                        //Toast.makeText(getBaseContext(), "mItems.sex :" + mItems.getJSONObject(position).getString("sex"), Toast.LENGTH_SHORT).show();
                        if (msex.equals("0")) { // mItems.getJSONObject(position).getString("sex") 디비에서 불러오는 것임 디비필드를 써야
                            img_Left_holder.mimgProfileimgTo.setImageResource(R.drawable.icon_man);
                        } else {
                            img_Left_holder.mimgProfileimgTo.setImageResource(R.drawable.icon_woman);
                        }


                    } else {
                        Glide.with(img_Left_holder.mimgProfileimgTo.getContext()).load(mimgtopass).bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).into(img_Left_holder.mimgProfileimgTo);

                    }

                    img_Left_holder.mimgNicknameTo.setText(mItems.getJSONObject(position).getString("nickname"));
                    img_Left_holder.mimgMsgTimeTo.setText(regarr[1]);
                    Glide.with(img_Left_holder.mimgMsgTo.getContext()).load(imgpathurl).into(img_Left_holder.mimgMsgTo);


                    img_Left_holder.mimgMsgTo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ////사진 팝업//////////////
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.customdialog);
                            dialog.setTitle("Custom Dialog");
                            ImageView iv = (ImageView) dialog.findViewById(R.id.image);
                            //iv.setImageResource(R.drawable.a);

                            Glide.with(ChatPage_Fragment.this).load(imgpathurl).into(iv);

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                }
                            });
                            dialog.show();

                        }
                    });


                    img_Left_holder.mimgMsgTo.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // 다이얼로그 바디
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(ChatPage_Fragment.this);
                            // 다이얼로그 메세지
                            alertdialog.setMessage("*" + getString(R.string.chatting4));

                            // 확인버튼
                            alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AQuery aq = new AQuery(ChatPage_Fragment.this);
                                    aq.ajax("http://kgs.yunstone.com/ajax/messagedel.php?messageuid=" + messageuid, JSONObject.class, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject object, AjaxStatus status) {
                                            if (object != null) {
                                                //로딩표시 지움.
                                                //Log.d("boarddel",url);
                                                try {
                                                    JSONObject oData = new JSONObject(object.toString());

                                                    JSONArray arrData = new JSONArray(oData.getString("result"));


                                                    for (int i = 0; i < arrData.length(); i++) {
                                                        JSONObject getlist = arrData.getJSONObject(i);

                                                        String logincheck = getlist.getString("y");

                                                        if (logincheck.equals("0")) {
                                                            Toast.makeText(ChatPage_Fragment.this, getString(R.string.chatting5), Toast.LENGTH_LONG).show();

                                                        } else {
                                                            //Toast.makeText(getContext(), "다시 한번 시도해주세요!!", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                    // 3초간 딜레이를 줘서 토스트메세지를 보여준다
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            /////// 게시판 내글을  리플레쉬해서 삭제된 화면을 보여준다///////
                                            //////로딩 시작/////
                                            progressON(ChatPage_Fragment.this, "Loading...");

                                            /////로딩시작//////
                                            //MainActivity activity = (MainActivity) getActivity();
                                            //activity.chatFragment(0);
                                            //getListData("", "", 0);
                                            mRvListAdapter.mItems = new JSONArray();
                                            getListData("1012", dbuid, arrlenth);
                                        }
                                    }, 1000);  // 2000은 2초를 의미합니다.


                                }
                            });

                            // 취소버튼
                            alertdialog.setNegativeButton(getString(R.string.toast1), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Toast.makeText(getContext(), "'취소'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // 메인 다이얼로그 생성
                            AlertDialog alert = alertdialog.create();
                            // 아이콘 설정
                            alert.setIcon(R.drawable.ic_add_alert);
                            // 타이틀
                            alert.setTitle(getString(R.string.board12));
                            // 다이얼로그 보기
                            alert.show();
                            return false;
                        }
                    });


                } else if (holder instanceof ImageRightViewHolder) {  //////////새로운 홀더 생성한것
                    regsplit = mItems.getJSONObject(position).getString("regdate");
                    regarr = regsplit.split(" ");
                    ImageRightViewHolder img_Right_holder = (ImageRightViewHolder) holder;
                    final String imgpathurl = imgurl + mItems.getJSONObject(position).getString("imagepath");
                    final String messageuid = mItems.getJSONObject(position).getString("messageuid");

                    img_Right_holder.mimgMsgTimeMe.setText(regarr[1]);
                    Glide.with(img_Right_holder.mimgMsgMe.getContext()).load(imgpathurl).into(img_Right_holder.mimgMsgMe);

                    img_Right_holder.mimgMsgMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ////사진 팝업//////////////
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.customdialog);
                            dialog.setTitle("Custom Dialog");
                            ImageView iv = (ImageView) dialog.findViewById(R.id.image);
                            //iv.setImageResource(R.drawable.a);

                            Glide.with(ChatPage_Fragment.this).load(imgpathurl).into(iv);

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                }
                            });
                            dialog.show();

                        }
                    });

                    img_Right_holder.mimgMsgMe.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // 다이얼로그 바디
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(ChatPage_Fragment.this);
                            // 다이얼로그 메세지
                            alertdialog.setMessage("*" + getString(R.string.chatting4));

                            // 확인버튼
                            alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AQuery aq = new AQuery(ChatPage_Fragment.this);
                                    aq.ajax("http://kgs.yunstone.com/ajax/messagedel.php?messageuid=" + messageuid, JSONObject.class, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject object, AjaxStatus status) {
                                            if (object != null) {
                                                //로딩표시 지움.
                                                //Log.d("boarddel",url);
                                                try {
                                                    JSONObject oData = new JSONObject(object.toString());

                                                    JSONArray arrData = new JSONArray(oData.getString("result"));


                                                    for (int i = 0; i < arrData.length(); i++) {
                                                        JSONObject getlist = arrData.getJSONObject(i);

                                                        String logincheck = getlist.getString("y");

                                                        if (logincheck.equals("0")) {
                                                            Toast.makeText(ChatPage_Fragment.this, getString(R.string.chatting5), Toast.LENGTH_LONG).show();

                                                        } else {
                                                            //Toast.makeText(getContext(), "다시 한번 시도해주세요!!", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                    // 3초간 딜레이를 줘서 토스트메세지를 보여준다
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            /////// 게시판 내글을  리플레쉬해서 삭제된 화면을 보여준다///////
                                            //////로딩 시작/////
                                            progressON(ChatPage_Fragment.this, "Loading...");

                                            /////로딩시작//////
                                            //MainActivity activity = (MainActivity) getActivity();
                                            //activity.chatFragment(0);
                                            //getListData("", "", 0);
                                            mRvListAdapter.mItems = new JSONArray();
                                            getListData("1012", dbuid, arrlenth);
                                        }
                                    }, 1000);  // 2000은 2초를 의미합니다.


                                }
                            });

                            // 취소버튼
                            alertdialog.setNegativeButton(getString(R.string.toast1), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Toast.makeText(getContext(), "'취소'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // 메인 다이얼로그 생성
                            AlertDialog alert = alertdialog.create();
                            // 아이콘 설정
                            alert.setIcon(R.drawable.ic_add_alert);
                            // 타이틀
                            alert.setTitle(getString(R.string.board12));
                            // 다이얼로그 보기
                            alert.show();
                            return false;
                        }
                    });





                }


            } catch (Exception e) {

            }

        }

        @Override
        public int getItemCount() { //필수 인터패이스
            ///return mItems.size();
            if (mItems == null)
                return 0;
            else
                //Toast.makeText(getBaseContext(), "mItems.length :" + mItems.length(), Toast.LENGTH_SHORT).show();
                return mItems.length();
        }

        @Override
        public int getItemViewType(int position) {

            try {

                ////////////if문 안에 리폰을 넣으면 동작을 안한다 왜그런지 모르겠음//////////////////////
                if (!mItems.getJSONObject(position).getString("message").isEmpty()) {
                    if (mItems.getJSONObject(position).getString("flag").equals("1")) {
                        return VIEW_TYPE_ITEM;
                    } else {
                        return VIEW_TYPE_Right;
                    }
                } else {
                    if (mItems.getJSONObject(position).getString("flag").equals("1")) {
                        return VIEW_IMG_Left;
                    } else {
                        return VIEW_IMG_Right;
                    }
                }

                //Log.d("position" , Integer.toString(position));
                //Log.d("position2" , mItems.getJSONObject(Integer.toString(position)));
                //Log.d("position3" , Integer.toString(VIEW_TYPE_LOADING));
                //Log.d("position4" , Integer.toString(VIEW_TYPE_ITEM));
                //Log.d("position5" , Integer.toString(VIEW_TYPE_LOADING));
                //Log.d("position6" , Integer.toString(VIEW_TYPE_ITEM));
                //Toast.makeText(getBaseContext(), "position2" + mItems.getJSONObject(position) , Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), "position3" + VIEW_TYPE_LOADING, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), "position4" + VIEW_TYPE_ITEM, Toast.LENGTH_SHORT).show();
                //return mItems.getJSONObject(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
                //return mItems.getJSONObject(position).getString("flag").equals("1") ? VIEW_TYPE_ITEM : VIEW_TYPE_Right;
                //return mItems.getJSONObject(position).getString("flag").equals("1") ? VIEW_IMG_Left : VIEW_IMG_Right;
            } catch (Exception e) {
                return VIEW_TYPE_LOADING;
            }
        }

        //필수 항목
        public final class ListItemViewHolder extends RecyclerView.ViewHolder {
            View mView;
            //LinearLayout mLayoutTo;
            RelativeLayout mLayoutTo;
            ImageView mImageTo;
            TextView mNicnameTo;
            TextView mMsgTo;
            TextView mMsgTimeTo;


            public ListItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                //mLayoutTo = (LinearLayout) itemView.findViewById(R.id.layoutTo);
                mLayoutTo = (RelativeLayout) itemView.findViewById(R.id.layoutTo);
                mImageTo = (ImageView) itemView.findViewById(R.id.imgProfileTo);
                mNicnameTo = (TextView) itemView.findViewById(R.id.textViewNicnameTo);
                mMsgTo = (TextView) itemView.findViewById(R.id.textViewMsgTo);
                mMsgTimeTo = (TextView) itemView.findViewById(R.id.textViewMsgTimeTo);

            }
        }

        public class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            }
        }

        public class ListRightViewHolder extends RecyclerView.ViewHolder {
            View itemRight;

            LinearLayout mLayoutMe;
            TextView mMsgMe;
            TextView mMsgTimeMe;

            public ListRightViewHolder(View itemView) {
                super(itemView);
                itemRight = itemView;

                mLayoutMe = (LinearLayout) itemView.findViewById(R.id.layoutMe);
                mMsgMe = (TextView) itemView.findViewById(R.id.textViewMsgMe);
                mMsgTimeMe = (TextView) itemView.findViewById(R.id.textViewMsgTimeMe);
            }
        }

        public class ImageLeftViewHolder extends RecyclerView.ViewHolder {
            View imgLeft;

            RelativeLayout mlayoutimgTo;
            ImageView mimgProfileimgTo;
            TextView mimgNicknameTo;
            TextView mimgMsgTimeTo;
            ImageView mimgMsgTo;

            public ImageLeftViewHolder(View itemView) {
                super(itemView);
                imgLeft = itemView;

                mlayoutimgTo = (RelativeLayout) itemView.findViewById(R.id.layoutimgTo);
                mimgProfileimgTo = (ImageView) itemView.findViewById(R.id.imgProfileimgTo);
                mimgNicknameTo = (TextView) itemView.findViewById(R.id.imgNicknameTo);
                mimgMsgTimeTo = (TextView) itemView.findViewById(R.id.imgMsgTimeTo);
                mimgMsgTo = (ImageView) itemView.findViewById(R.id.imgMsgTo);
            }
        }

        public class ImageRightViewHolder extends RecyclerView.ViewHolder {
            View imgRight;

            LinearLayout mlayoutimgMe;
            TextView mimgMsgTimeMe;
            ImageView mimgMsgMe;

            public ImageRightViewHolder(View itemView) {
                super(itemView);
                imgRight = itemView;

                mlayoutimgMe = (LinearLayout) itemView.findViewById(R.id.layoutimgMe);
                mimgMsgTimeMe = (TextView) itemView.findViewById(R.id.imgMsgTimeMe);
                mimgMsgMe = (ImageView) itemView.findViewById(R.id.imgMsgMe);
            }
        }


    }

    ////////////////완료시 서버에 파라미터 전달////////////////////
    public void sendmessage(String Url) {
        AQuery aq = new AQuery(this);
        aq.ajax(Url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    JSONObject oData = new JSONObject(object.toString());
                    //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                    JSONArray arrData = new JSONArray(oData.getString("result"));

                    for (int i = 0; i < arrData.length(); i++) {
                        JSONObject getlist = arrData.getJSONObject(i);

                        String logincheck = getlist.getString("y");
                        Log.d("messageresult", logincheck);

                        if (logincheck.equals("0")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast22), Toast.LENGTH_LONG).show();

                            //Toast.makeText(getApplicationContext(), "uid"+dbuid, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "arrlenth"+arrlenth, Toast.LENGTH_LONG).show();
                            //////액티비티 리로딩을 할 필요가 없어 지웠음/////
                            //Intent intent = new Intent(ChatPage_Fragment.this, ChatPage_Fragment.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            //startActivity(intent);
                            //////액티비티 리로딩 끝/////
                            //mRvListAdapter.notifyItemRangeRemoved(0 ,8);
                            //mRvListAdapter.notifyItemRemoved(1);
                            //mRvListAdapter.notifyDataSetChanged();
                            mRvListAdapter.mItems = new JSONArray();
                            //mRvListAdapter.notifyDataSetChanged();
                            getListData("1012", dbuid, arrlenth);
                            //getListData("1010", dbuid, arrlenth);

                            //putListData();
                            EditText editText = (EditText) findViewById(R.id.chat_txt);
                            editText.setText(null);

                        }
                    }

                } catch (Exception e) {

                }

            }
        });
    }

////////////////완료시 서버에 파라미터 전달 끝////////////////////

    @Override
    public void onBackPressed() {
        // Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
        // startActivity(intent);
        // super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("pagename", "1");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(intent);


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void scrollend() {
        ////////////////스크롤 마지막으로 내리기///////////////////////////////////////////////////
        mImagescroll.postDelayed(new Runnable() {
            final RecyclerView.State mState = new RecyclerView.State();

            @Override
            public void run() {
                ///스크롤 마지막으로 이동
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
                //final ScrollView scrollView = (ScrollView) findViewById(R.id.chat_scroll);
                /////스크롤 마지막으로 바로 가기//////
                recyclerView.getLayoutManager().scrollToPosition(mRvListAdapter.mItems.length() - 1);
                ////스크롤 다운을 보면서 마지막으로가기//////////
                //recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, mState, mRvListAdapter.mItems.length() - 1);
                //Toast.makeText(getBaseContext(), "scroll up :  "+mRvListAdapter.mItems.length(), Toast.LENGTH_LONG).show();
                mImagescroll.smoothScrollTo(0, mImagescroll.getBottom());

            }

        }, 100);
        ////////////////스크롤 마지막으로 내리기///////////////////////////////////////////////////
    }

    ////////////////////사잔관련 처리///////////////////////////////////////
    /////////////////사진 관련 부분시작/////////

    /**
     * 카메라에서 사진 촬영
     */

    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
    {
        Intent result = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            mImageCaptureUri = FileProvider.getUriForFile(ChatPage_Fragment.this, BuildConfig.APPLICATION_ID + ".provider", new File(Environment.getExternalStorageDirectory(), url));
            Log.d("URI!!", String.valueOf(mImageCaptureUri));

        } else {
            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        }
        result.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(result, PICK_FROM_CAMERA);
        //Crop.pickImage(Setting.this);
    }

    private void beginCrop(Uri source) {

        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
        Uri destination = Uri.fromFile(new File(getCacheDir(), url));

        Crop.of(source, destination).asSquare().start(this);


    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
            Uri destination = Uri.fromFile(new File(getCacheDir(), url));
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/minatalk/" + url;
            fileName = url;
            serverUrl = "http://kgs.yunstone.com/jsonp/upload.php";
            Bitmap photo = null;
            try {

                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
                //iv_UserPhoto.setImageBitmap(photo);
                storeCropImage(photo, filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
            //Toast.makeText(Setting.this, "album on!!", Toast.LENGTH_SHORT).show();
        }else if(requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK){
            beginCrop(mImageCaptureUri);
        }else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }
    /*
    * Bitmap을 저장하는 부분
    */

    private void storeCropImage(Bitmap bitmap, String filePath) {
        // minatalk 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/minatalk/";
        File directory_minatalk = new File(dirPath);

        if (!directory_minatalk.exists()) // minatalk 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_minatalk.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            //여기에 이벤트를 적어주세요
            String flagurl = "http://kgs.yunstone.com/ajax/message.php?uid=" + muid + "&messageid=" + fuid +
                    "&uuid=" + muuid + "&imagepath=" + fileName;
            //Toast.makeText(getBaseContext(), "flagurl :" + flagurl, Toast.LENGTH_LONG).show();
            //Log.d("flagurl : ", flagurl);
            sendmessage(flagurl);

            //Glide.with(this).load(filePath).bitmapTransform(new CropCircleTransformation(new login.CustomBitmapPool())).into(iv_UserPhoto);

            ///////////사진 파일 올리는 스레드로 넘길때 파라미터도 같이 넘기는 방법///////
            uploadFile thread = new uploadFile(filePath, fileName, serverUrl);
            thread.start();
            ///////////////////////////////////////////////////////////////

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////glied를 쓰기위한 선언
    public class CustomBitmapPool implements BitmapPool {
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
    ////////////////////사진관련 처리 끝//////////////////////////////////////

    ///////////////////사진 버튼 클릭시 처리////////////////////////
    private void showDialog() {
        final String[] items = {
                getString(R.string.login3),
                getString(R.string.login4)
        };

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login1))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position) {
                            case 0:
                                //openGallery();
                                //doTakeAlbumAction();
                                Crop.pickImage(ChatPage_Fragment.this);
                                break;
                            case 1:
                                //mChatView.getMessageView().removeAll();
                                doTakePhotoAction();
                                break;
                        }
                    }
                })
                .show();
    }

    ///////////////////////admob reward/////////////////////////
    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd("ca-app-pub-1873683360216447/4325413850", new AdRequest.Builder().build());
            //Toast.makeText(getBaseContext(), "loadRewardedVideoAd", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewarded(RewardItem reward) {
        //Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        // Toast.makeText(this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        //homebutton = 3;
        //Toast.makeText(getBaseContext(), "onhomekey:" + homebutton, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad"+errorCode, Toast.LENGTH_SHORT).show();
        Log.d("Adserrorcode", String.valueOf(errorCode));
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        // Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        // Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }
    ///////////////////////admob reward/////////////////////////

    ///////////////////adbmob 전면광고////////////////////////////////
    private void getAD() {
        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        //ad.setAdUnitId("ca-app-pub-1873683360216447/5211814617");
        mInterstitialAd.setAdUnitId("ca-app-pub-1873683360216447/5211814617");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                //homebutton=3;
                //Toast.makeText(ChatPage_Fragment.this, "adclosed:"+homebutton, Toast.LENGTH_LONG).show();
            }
        });
    }
    //////////////////전면광고/////////////////////////////////

    @Override
    protected void onUserLeaveHint() {
        /*/이벤트 홈버튼 눌랐을때 이동할곳 선언해주기////////
        super.onUserLeaveHint();
        //homebutton = 0;
        if(homebutton == 3) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent);
            Toast.makeText(getBaseContext(), "onhomekey:" + homebutton, Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(this, "Homebutton", Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
        //startActivity(intent);
        */
    }

}
