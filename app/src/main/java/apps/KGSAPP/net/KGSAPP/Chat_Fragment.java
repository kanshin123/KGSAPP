package apps.KGSAPP.net.KGSAPP;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static apps.KGSAPP.net.KGSAPP.BaseApplication.progressOFF;
import static apps.KGSAPP.net.KGSAPP.BaseApplication.progressON;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Chat_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Chat_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat_Fragment extends Fragment {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        SharedPreferences location = getContext().getSharedPreferences("location", 0);
        uid = location.getString("uid", "");
        uuid = location.getString("uuid", "0");
        */
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        setRecyclerView(rv);

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
        //mRvListAdapter.mBbsName = mBbsName;
        recyclerView.setAdapter(mRvListAdapter);

    }

    public void getListData(String sListName, final String sLastNo) {
        ////메세지 박스에 표시하기 위한 테이터 불러오기
        SharedPreferences location = getActivity().getSharedPreferences("location", 0);
        uid = location.getString("uid", ""); //디비에서 고유 번호 불러오기
        uuid = location.getString("uuid", "");
        //Toast.makeText(getContext(), "uid :" + uid, Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), "latFrag :" + lat, Toast.LENGTH_LONG).show();

        AQuery aq = new AQuery(this.getActivity());
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/messagebox.php?uid=" + uid, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (object != null) {
                    //로딩표시 지움.
                    Log.d("boxurl", url);
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
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list, viewGroup, false);
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
            final String muid;
            final String fuid;
            final String sex;
            final String sexflag;           ///null인 이미지 처리값을 넘기기위해 설정
            final String messcount;
            final String message;
            final String fnickname;

            try {
                if (holder instanceof ListItemViewHolder) {
                    //if (mItems.getJSONObject(position).getString("rowflag").equals("0")) {
                    ListItemViewHolder item_holder = (ListItemViewHolder) holder;
                    //데이터가 없을때 표시해야 하는데...


                        //item_holder.mImg.setImageResource(R.drawable.b_avator);   //사진박아놓은것 쓸때
                        imageurl = "http://kgs.yunstone.com/uploads/" + mItems.getJSONObject(position).getString("imagepath");
                        if (mItems.getJSONObject(position).getString("imagepath").equals("null")) {   //디비가 비어서 null이 아니라 문자null을 넣어놨음
                            if (mItems.getJSONObject(position).getString("sex").equals("0")) { // mItems.getJSONObject(position).getString("sex") 디비에서 불러오는 것임 디비필드를 써야
                                item_holder.mImg.setImageResource(R.drawable.icon_man);
                                sexflag = "0";
                            } else {
                                item_holder.mImg.setImageResource(R.drawable.icon_woman);
                                sexflag = "0";
                            }


                        } else {
                            Glide.with(item_holder.mImg.getContext()).load(imageurl).bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).into(item_holder.mImg);
                            sexflag = "1";
                        }

                        fnickname = mItems.getJSONObject(position).getString("nickname");
                        item_holder.mSubject.setText(fnickname);
                        messcount = mItems.getJSONObject(position).getString("count");

                        ///////디자인으로 배지숫자 설정함/////////////////////////////
                        if (messcount.equals("0")) {
                            item_holder.messtxt.setBackgroundColor(Color.TRANSPARENT);
                        } else {
                            item_holder.messtxt.setText(mItems.getJSONObject(position).getString("count"));
                        }
                        ///////디자인 배지숫자 설정 끝///////////////////////////////


                        if (mItems.getJSONObject(position).getString("muid").equals(uid)) {
                            muid = mItems.getJSONObject(position).getString("muid");
                            fuid = mItems.getJSONObject(position).getString("fuid");
                            item_holder.mUserId.setText("(" + getString(R.string.chatting1) + ")" + mItems.getJSONObject(position).getString("message"));
                            message = mItems.getJSONObject(position).getString("message");

                            //Toast.makeText(getContext(), "muid :" + muid, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getContext(), "fuid :" + fuid, Toast.LENGTH_LONG).show();
                        } else {
                            muid = mItems.getJSONObject(position).getString("fuid");
                            fuid = mItems.getJSONObject(position).getString("muid");
                            item_holder.mUserId.setText("(" + getString(R.string.chatting2) + ")" + mItems.getJSONObject(position).getString("message"));
                            message = mItems.getJSONObject(position).getString("message");
                            //Toast.makeText(getContext(), "muid :" + muid, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getContext(), "fuid :" + fuid, Toast.LENGTH_LONG).show();
                        }
                        sex = mItems.getJSONObject(position).getString("sex");
                        Log.e("muid", mItems.getJSONObject(position).getString("muid"));
                        Log.e("fuid", mItems.getJSONObject(position).getString("fuid"));

                        //Log.e("cnickname", mItems.getJSONObject(position).getString("nickname"));
                        //Log.e("cgreetings", mItems.getJSONObject(position).getString("greetings"));
                        //final String sNo = item_holder.mNo.getText().toString();
                        //리사이클뷰어 클릭시 카톡말풍선 화면으로 넘어가기
                        item_holder.mUserId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ChatPage_Fragment.class);  //messengetActiovity로 보내기위함
                                intent.putExtra("MUID", muid);
                                intent.putExtra("FUID", fuid);
                                intent.putExtra("SEX", sex);
                                intent.putExtra("SEXFLAG", sexflag);
                                intent.putExtra("Imgtopass", imageurl);
                                intent.putExtra("UUID", uuid);
                                intent.putExtra("FNICKNAME", fnickname);
                                //intent.putExtra("Message", message);
                                //context.startActivity(intent);
                                startActivity(intent);
                                //((getdata)getdata.mContext).regetdata();
                                Log.e("muid : ", muid);
                                Log.e("fuid : ", fuid);
                                Log.e("sex : ", sex);
                                Log.e("uuid : ", uuid);
                                Log.e("imageurl : ", imageurl);
                                Log.e("fnickname : ", fnickname);
                            }
                        });

                        item_holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //Context context = v.getContext();
                                //Intent intent = new Intent(getContext(), ChatPage_Fragment.class);
                                //Intent intent = new Intent(getContext(), MessengerActivity.class);  //messengetActiovity로 보내기위함
                                Intent intent = new Intent(getContext(), ChatPage_Fragment.class);  //messengetActiovity로 보내기위함
                                intent.putExtra("MUID", muid);
                                intent.putExtra("FUID", fuid);
                                intent.putExtra("SEX", sex);
                                intent.putExtra("SEXFLAG", sexflag);
                                intent.putExtra("Imgtopass", imageurl);
                                intent.putExtra("UUID", uuid);
                                intent.putExtra("FNICKNAME", fnickname);
                                //intent.putExtra("Message", message);
                                //context.startActivity(intent);
                                startActivity(intent);
                                //((getdata)getdata.mContext).regetdata();
                                Log.e("muid : ", muid);
                                Log.e("fuid : ", fuid);
                                Log.e("sex : ", sex);
                                Log.e("uuid : ", uuid);
                                Log.e("imageurl : ", imageurl);
                                Log.e("fnickname : ", fnickname);

                            }

                        });

                        item_holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                // 다이얼로그 바디
                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                                // 다이얼로그 메세지
                                alertdialog.setMessage("*" + getString(R.string.chatting4));

                                // 확인버튼
                                alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        AQuery aq = new AQuery(getContext());
                                        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
                                        aq.ajax("http://kgs.yunstone.com/ajax/messageboxdel.php?uid=" + muid + "&fuid=" + fuid, JSONObject.class, new AjaxCallback<JSONObject>() {
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
                                                                Toast.makeText(getContext(), getString(R.string.chatting5), Toast.LENGTH_LONG).show();

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
                                                progressON(getActivity(), "Loading...");

                                                /////로딩시작//////
                                                MainActivity activity = (MainActivity) getActivity();
                                                activity.chatFragment(0);
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

                        item_holder.mUserId.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                // 다이얼로그 바디
                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                                // 다이얼로그 메세지
                                alertdialog.setMessage("*" + getString(R.string.chatting4));

                                // 확인버튼
                                alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        AQuery aq = new AQuery(getContext());
                                        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
                                        aq.ajax("http://kgs.yunstone.com/ajax/messageboxdel.php?uid=" + muid + "&fuid=" + fuid, JSONObject.class, new AjaxCallback<JSONObject>() {
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
                                                                Toast.makeText(getContext(), getString(R.string.chatting5), Toast.LENGTH_LONG).show();

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
                                                //////로딩 시작/////
                                                progressON(getActivity(), "Loading...");

                                                /////로딩시작//////
                                                /////// 게시판 내글을  리플레쉬해서 삭제된 화면을 보여준다///////
                                                MainActivity activity = (MainActivity) getActivity();
                                                activity.chatFragment(0);
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
                    //}

                } else if (holder instanceof LoadingViewHolder) {
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setIndeterminate(true);
                }

            } catch (Exception e) {

            }

            /////로딩 끝/////
            progressOFF();
            /////로딩 끝////

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
            TextView mDate;
            TextView msex;      //프렌즈 성별
            ImageView mImg;   //이미지 표시를 위한 설정
            TextView messtxt;


            public ListItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                //mNo = (TextView) itemView.findViewById(R.id.);
                //mCate = (TextView) itemView.findViewById(R.id.textCate);
                mImg = (ImageView) itemView.findViewById(R.id.chat_avatar);    //이미지 표시를 위한 설정
                mSubject = (TextView) itemView.findViewById(R.id.chat_title);
                mUserId = (TextView) itemView.findViewById(R.id.chat_desc);
                messtxt = (TextView) itemView.findViewById(R.id.chat_list_txt);
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