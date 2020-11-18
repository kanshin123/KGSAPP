package apps.KGSAPP.net.KGSAPP;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Provides UI for the view with List.
 */

public class ListContentFragment0 extends Fragment {

    public String mBbsName;
    private RecyclerViewListAdapter mRvListAdapter;
    public boolean mVisible = false;
    public boolean mLoading = false;
    public String mApiName = "";
    public String muid;
    public String uuid;
    public String sex;
    public String sexflag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        mBbsName = getArguments() != null ? getArguments().getString("bbs_name") : "";

        if(mBbsName.contains("숙소정보"))
            mApiName = "getHouseList";
        else if(mBbsName.contains("구인구직"))
            mApiName = "getJobList";
        else if(mBbsName.contains("벼룩시장"))
            mApiName = "getMarketList";
         */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // String sortinid = getArguments().getString("sortinid"); // 전달한 key 값
       // Toast.makeText(getContext(), "getArguments :" + sortinid, Toast.LENGTH_LONG).show();
        /*
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
       */

            /*프레그먼트 통신으로  메인 액티비티에서 값을 가져오는건 안되므로 저장으로 바꾼다.
            String lon = getArguments().getString("lon"); // 전달한 key 값
            String lat = getArguments().getString("lat"); // 전달한 key 값

            Toast.makeText(getContext(), "lonFrag :" + lon, Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), "latFrag :" + lat, Toast.LENGTH_LONG).show();
            */
        //inflater.inflate(R.layout.list_new_all,container,false);
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view,container,false);

        //View  drawer = inflater.inflate(R.layout.recycler_view, container, false);
        //RecyclerView rv = (RecyclerView) drawer.findViewById(R.id.my_recycler_view);
        setRecyclerView(rv);

        /*//메세지 전송버튼 눌었을때 디비에 입력및 푸쉬 전송
        rv.findViewById(R.id.post_send).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요
                        Toast.makeText(getContext(), "position :"+position, Toast.LENGTH_LONG).show();
                    }
                }
        );

        if (getArguments().getString("sortinid") == null){
            getListData("", "");
        }else {
            String sortinid = getArguments().getString("sortinid"); // 전달한 key 값
            Toast.makeText(getContext(), "getArguments :" + sortinid, Toast.LENGTH_LONG).show();
            getListData(sortinid, "");
        }
        */
        return rv;
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
        mRvListAdapter.mBbsName = mBbsName;
        recyclerView.setAdapter(mRvListAdapter);

    }

    public void getListData(String urlsex, final String sLastNo) {
        SharedPreferences location = getActivity().getSharedPreferences("location", 0);
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        muid = location.getString("uid", "");
        uuid = location.getString("uuid", "");
        //Toast.makeText(getContext(), "lonFrag :" + lon, Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), "latFrag :" + lat, Toast.LENGTH_LONG).show();

        AQuery aq = new AQuery(this.getActivity());
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/friend_an.php?lat=" + lat + "&lon=" + lon + "&start=0&uuid="+uuid+"&sex=0", JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                Log.e("url : ", url);
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
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
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
            final String imageurl;
            final String fuid;
            final String sex;
            final String sexflag;


            try {
                if (holder instanceof ListItemViewHolder) {
                    ListItemViewHolder item_holder = (ListItemViewHolder) holder;
                    //item_holder.mImg.setImageResource(R.drawable.b_avator);   //사진박아놓은것 쓸때
                    imageurl = "http://kgs.yunstone.com/uploads/" + mItems.getJSONObject(position).getString("imagepath");
                    if (mItems.getJSONObject(position).getString("imagepath").equals("null")) {   //디비가 비어서 null이 아니라 문자null을 넣어놨음
                        if (mItems.getJSONObject(position).getString("sex").equals("0")) { // mItems.getJSONObject(position).getString("sex") 디비에서 불러오는 것임 디비필드를 써야
                            item_holder.mImg.setImageResource(R.drawable.icon_man);
                            sexflag="0";
                        } else {
                            item_holder.mImg.setImageResource(R.drawable.icon_woman);
                            sexflag="0";
                        }


                    } else {
                        Glide.with(item_holder.mImg.getContext()).load(imageurl).bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).into(item_holder.mImg);
                        sexflag="1";
                    }

                    item_holder.mSubject.setText(mItems.getJSONObject(position).getString("nickname"));
                    item_holder.mUserId.setText(mItems.getJSONObject(position).getString("greetings"));
                    int distance = (int) Math.round(Double.parseDouble(mItems.getJSONObject(position).getString("d")));
                    String sdistace = String.valueOf(distance);
                    //Toast.makeText(getContext(), "distance :" + distance, Toast.LENGTH_LONG).show();
                    if(sdistace.equals("0")){
                        sdistace = "1km";
                    }else{
                        sdistace = sdistace+"km";
                    }
                    item_holder.mDistance.setText(sdistace);

                    sex = mItems.getJSONObject(position).getString("sex");
                    fuid = mItems.getJSONObject(position).getString("fuid");

                    item_holder.mImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ////사진 팝업//////////////
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.customdialog);
                            dialog.setTitle("Custom Dialog");
                            ImageView iv = (ImageView) dialog.findViewById(R.id.image);
                            //iv.setImageResource(R.drawable.a);

                            Glide.with(getActivity()).load(imageurl).into(iv);

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                }
                            });
                            dialog.show();
                        }

                    });


                    item_holder.mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Context context = v.getContext();
                            //Intent intent = new Intent(getContext(), ChatPage_Fragment.class);
                            Intent intent = new Intent(getContext(), ChatPage_Fragment.class);
                            intent.putExtra("MUID", muid);
                            intent.putExtra("FUID", fuid);
                            intent.putExtra("SEX", sex);
                            intent.putExtra("SEXFLAG", sexflag);
                            intent.putExtra("Imgpass", imageurl);
                            intent.putExtra("UUID", uuid);
                            //context.startActivity(intent);
                            startActivity(intent);

                            Log.e("muid : ", muid + " / " + String.valueOf(position));
                            Log.e("fuid : ", fuid + " / " + String.valueOf(position));
                            //Log.e("sex : ", sex);
                            Log.e("uuid : ", uuid);
                            Log.e("imageurl : ", imageurl);
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
            TextView mNo;
            TextView mCate;
            TextView mSubject;
            TextView mUserId;
            TextView mDistance;
            TextView mDate;
            ImageView mImg;   //이미지 표시를 위한 설정
            ImageButton mButton;

            public ListItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                //mNo = (TextView) itemView.findViewById(R.id.);
                //mCate = (TextView) itemView.findViewById(R.id.textCate);
                mImg = (ImageView) itemView.findViewById(R.id.list_avatar);    //이미지 표시를 위한 설정
                mSubject = (TextView) itemView.findViewById(R.id.list_title);
                mUserId = (TextView) itemView.findViewById(R.id.list_desc);
                mDistance = (TextView) itemView.findViewById(R.id.list_distance);
                mButton = (ImageButton) itemView.findViewById(R.id.post_send);
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
    public void call_fragment(int index){


        Toast.makeText(getContext(), "call_fragment :", Toast.LENGTH_LONG).show();
        ////0을 넣어주면 남자만 소팅 1을 넣어주면 여자만 소팅됨
        //getListData("0","");

    }
}
