package apps.KGSAPP.net.KGSAPP;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import org.json.JSONArray;
import org.json.JSONObject;


public class FnqActivity extends AppCompatActivity {
     public String mBbsName;
    private RecyclerViewListAdapter mRvListAdapter;
    public boolean mVisible = false;
    public boolean mLoading = false;
    public String mApiName = "";
    public String uuid;
    public String uid;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fnq_recycler);

        //맨위의 상태바 색깔 바꾸기
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.BLACK);
            }
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fnq_recycler_view);
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        setRecyclerView(recyclerView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.notice_toolbar);
        setSupportActionBar(toolbar);
        // Adding menu icon to Toolbar

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }



    }




    @Override
    public void onResume() {
        super.onResume();

        if (mRvListAdapter.mItems.length() == 0) {
            //로딩표시
            mRvListAdapter.mItems.put(null);
            mRvListAdapter.notifyItemInserted(mRvListAdapter.mItems.length() - 1);

            getListData(mApiName, "");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }


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
        }
        return super.onOptionsItemSelected(item);
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
                    int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    if (lastVisibleItem == totalItemCount - 1) {
                        if (!mLoading) {
                            mLoading = true;
                            try {
                                String sNo = mRvListAdapter.mItems.getJSONObject(mRvListAdapter.mItems.length() - 1).getString("no");

                                //로딩표시
                                mRvListAdapter.mItems.put(null);
                                mRvListAdapter.notifyItemInserted(mRvListAdapter.mItems.length() - 1);

                                getListData(mApiName, sNo);
                            } catch (Exception e) {

                            }
                            //showToast("스크롤 마지막!!" + mBbsName);
                        }
                    }

                }

            }
        });

        mRvListAdapter = new RecyclerViewListAdapter();
        //mRvListAdapter.mBbsName = mBbsName;
        recyclerView.setAdapter(mRvListAdapter);

    }

    public void getListData(String sListName, final String sLastNo) {
        ////메세지 박스에 표시하기 위한 테이터 불러오기
        SharedPreferences location = this.getSharedPreferences("location", 0);
        uid = location.getString("uid", ""); //디비에서 고유 번호 불러오기
        uuid = location.getString("uuid", "");
        //Toast.makeText(getContext(), "uid :" + uid, Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), "latFrag :" + lat, Toast.LENGTH_LONG).show();

        AQuery aq = new AQuery(this);
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/faq.php", JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (object != null) {
                    //로딩표시 지움.
                    mRvListAdapter.mItems.remove(mRvListAdapter.mItems.length() - 1);
                    mRvListAdapter.notifyItemRemoved(mRvListAdapter.mItems.length());

                    try {
                        JSONObject oData = new JSONObject(object.toString());

                        JSONArray arrData = new JSONArray(oData.getString("result"));
                        //JSONArray arrData = new JSONArray(oData.getString(""));
                        for (int i = 0; i < arrData.length(); i++) {
                            mRvListAdapter.mItems.put(arrData.getJSONObject(i));

                        }
                        //여기에서 받아온 값을 배열에 넣어서 카톡화면으로 넘겨야 한다고 생각함


                        mRvListAdapter.notifyDataSetChanged();
                        mLoading = false;

                        //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        //public ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        public JSONArray mItems = new JSONArray();
        public String mBbsName = "";



        @Override
        //public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //필수 인터패이스
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //필수 인터패이스
            if (viewType == VIEW_TYPE_ITEM) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fnq_list, viewGroup, false);
                return new ListItemViewHolder(itemView);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            return null;
        }

        @Override
        //public void onBindViewHolder(final ListItemViewHolder holder, int position) { //필수 인터패이스
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) { //필수 인터패이스

            String regsplit;
            String[] regarr;

            try {
                if (holder instanceof ListItemViewHolder) {
                    ListItemViewHolder item_holder = (ListItemViewHolder) holder;

                    item_holder.mSubject.setText(mItems.getJSONObject(position).getString("sub"));
                    item_holder.mUserId.setText(mItems.getJSONObject(position).getString("memo"));
                    regsplit=mItems.getJSONObject(position).getString("regdate");
                    regarr = regsplit.split(" ");
                    item_holder.mDate.setText(regarr[0]);


                    //Log.e("cnickname", mItems.getJSONObject(position).getString("nickname"));
                    //Log.e("cgreetings", mItems.getJSONObject(position).getString("greetings"));
                    //final String sNo = item_holder.mNo.getText().toString();
                    //리사이클뷰어 클릭시 카톡말풍선 화면으로 넘어가기
                    item_holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });


                } else if (holder instanceof LoadingViewHolder) {
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setIndeterminate(true);
                }
            } catch (Exception e) {

            }

        }

        @Override
        public int getItemCount() { //필수 인터패이스
            //return mItems.size();
            if (mItems == null)
                return 0;
            else
                return mItems.length();
        }

        @Override
        public int getItemViewType(int position) {
            try {
                return mItems.getJSONObject(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
            } catch (Exception e) {
                return VIEW_TYPE_LOADING;
            }
        }

        //필수 항목
        public final class ListItemViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView mSubject;
            TextView mUserId;
            TextView mDate;




            public ListItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                //mNo = (TextView) itemView.findViewById(R.id.);
                //mCate = (TextView) itemView.findViewById(R.id.textCate);

                mSubject = (TextView) itemView.findViewById(R.id.fnq_title);
                mUserId = (TextView) itemView.findViewById(R.id.fnq_desc);
                mDate = (TextView) itemView.findViewById(R.id.fnq_date);
                //msex = (TextView) itemView.findViewById(R.id.chat_sex);
                //mDate = (TextView) itemView.findViewById(R.id.textDate);
            }
        }

        public class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            }
        }

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

    }
}