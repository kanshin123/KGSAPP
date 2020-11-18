/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package apps.KGSAPP.net.KGSAPP;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import static apps.KGSAPP.net.KGSAPP.ChatPage_Fragment.sex;

/**
 * Provides UI for the view with Cards.
 */
public class BoardContentFragment extends Fragment {
    public String mBbsName;
    private RecyclerViewListAdapter mRvListAdapter;
    public boolean mVisible = false;
    public boolean mLoading = false;
    public String mApiName = "";

    public String uuid;
    public String muid;

    private static String Nonobject = "Nonobject";
    Nonobject nonobject = new Nonobject();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.board_view, container, false);
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
        muid = location.getString("uid", ""); //디비에서 고유 번호 불러오기
        uuid = location.getString("uuid", "");
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");

        //Toast.makeText(getContext(), "boarduuid :" + uuid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "boardmuid :" + muid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "lat :" + lat, Toast.LENGTH_SHORT).show();

        AQuery aq = new AQuery(this.getActivity());
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/board.php?lat=" +lat+"&lon="+lon+"&start=0&uuid="+uuid, JSONObject.class, new AjaxCallback<JSONObject>() {
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

                        if(arrData.length()==0){
                            MainActivity activity = (MainActivity) getActivity();
                            activity.nonobject(0);
                        }
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
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_list, viewGroup, false);
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
            //final String muid;
            final String fuid;
            final String sexflag;           ///null인 이미지 처리값을 넘기기위해 설정

            try {
                if (holder instanceof ListItemViewHolder) {
                    ListItemViewHolder item_holder = (ListItemViewHolder) holder;
                    //데이터가 없을때 표시해야 하는데...


                    //item_holder.mImg.setImageResource(R.drawable.b_avator);   //사진박아놓은것 쓸때
                    imageurl = "http://kgs.yunstone.com/uploads/" + mItems.getJSONObject(position).getString("imageboard");
                    //Log.d("imageurl",imageurl);
                    if (mItems.getJSONObject(position).getString("imageboard").equals("")) {   //디비가 비어서 null이 아니라 문자null을 넣어놨음

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

                    if (mItems.getJSONObject(position).getString("nickname").equals("")) {
                        String nick = "not nickname";
                        item_holder.mSubject.setText(nick);

                    }else {
                        String nick = mItems.getJSONObject(position).getString("nickname");
                        item_holder.mSubject.setText(nick);
                    }

                    int distance = (int) Math.round(Double.parseDouble(mItems.getJSONObject(position).getString("d")));
                    String sdistace = String.valueOf(distance);
                    //Toast.makeText(getContext(), "distance :" + distance, Toast.LENGTH_LONG).show();
                    if(sdistace.equals("0")){
                        sdistace = "1km";
                    }else{
                        sdistace = sdistace+"km";
                    }
                    item_holder.mDistance.setText(sdistace);

                    item_holder.mUserId.setText(mItems.getJSONObject(position).getString("message"));

                    final String message = mItems.getJSONObject(position).getString("message");

                    fuid = mItems.getJSONObject(position).getString("fuid");

                    Log.e("fuid", mItems.getJSONObject(position).getString("fuid"));
                    Log.e("message", mItems.getJSONObject(position).getString("message"));



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
                            if(muid.equals(fuid)){

                                Toast.makeText(getContext(), getString(R.string.toast5), Toast.LENGTH_LONG).show();

                            }else {
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
                                Log.e("sex : ", sex);
                                Log.e("uuid : ", uuid);
                                Log.e("imageurl : ", imageurl);
                            }
                        }
                    });

                    item_holder.msien.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(muid.equals(fuid)){

                                Toast.makeText(getContext(), getString(R.string.toast23), Toast.LENGTH_LONG).show();

                            }else{
                                /*
                                // 다이얼로그 바디
                                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                                // 메세지
                                alert_confirm.setMessage(message+"\n*허위신고시 이용에 불이익을 받을수 있습니다.");
                                // 확인 버튼 리스너
                                alert_confirm.setPositiveButton("확인", null);
                                // 다이얼로그 생성
                                AlertDialog alert = alert_confirm.create();

                                // 아이콘
                                alert.setIcon(R.drawable.ic_add_alert);
                                // 다이얼로그 타이틀
                                alert.setTitle("신고하기");
                                // 다이얼로그 보기
                                alert.show();
                                */

                                // 다이얼로그 바디
                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                                // 다이얼로그 메세지
                                alertdialog.setMessage(message+"\n*"+getString(R.string.toast24));

                                // 확인버튼
                                alertdialog.setPositiveButton(getString(R.string.toast4), new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        AQuery aq = new AQuery(getContext());
                                        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
                                        aq.ajax("http://kgs.yunstone.com.com/ajax/policesend.php?muid=" +muid+"&fuid="+fuid+"&section=2&uuid="+uuid+"&message="+message, JSONObject.class, new AjaxCallback<JSONObject>() {
                                            @Override
                                            public void callback(String url, JSONObject object, AjaxStatus status) {
                                                if (object != null) {
                                                    //로딩표시 지움.

                                                    try {
                                                        JSONObject oData = new JSONObject(object.toString());

                                                        JSONArray arrData = new JSONArray(oData.getString("result"));


                                                        for (int i = 0; i < arrData.length(); i++) {
                                                            JSONObject getlist = arrData.getJSONObject(i);

                                                            String logincheck = getlist.getString("y");

                                                            if (logincheck.equals("0")) {
                                                                Toast.makeText(getContext(), getString(R.string.toast22), Toast.LENGTH_LONG).show();

                                                            }else{
                                                               // Toast.makeText(getContext(), "다시 한번 시도해주세요!!", Toast.LENGTH_LONG).show();

                                                            }
                                                       }
                                                    } catch (Exception e) {

                                                    }
                                                }
                                            }
                                        });



                                        Toast.makeText(getContext(), getString(R.string.toast25), Toast.LENGTH_SHORT).show();
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
                                alert.setTitle(getString(R.string.toast26));
                                // 다이얼로그 보기
                                alert.show();


                            }

                            //Toast.makeText(getContext(), "sien :" + muid + "/ fuid :"+fuid, Toast.LENGTH_LONG).show();



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
            TextView msex;      //프렌즈 성별
            ImageView mImg;   //이미지 표시를 위한 설정
            ImageButton mButton;
            ImageButton msien;


            public ListItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                //mNo = (TextView) itemView.findViewById(R.id.);
                //mCate = (TextView) itemView.findViewById(R.id.textCate);
                mImg = (ImageView) itemView.findViewById(R.id.board_avatar);    //이미지 표시를 위한 설정
                mSubject = (TextView) itemView.findViewById(R.id.board_title);
                mUserId = (TextView) itemView.findViewById(R.id.board_desc);
                mDistance = (TextView) itemView.findViewById(R.id.board_distance);
                mButton = (ImageButton) itemView.findViewById(R.id.board_post_send);
                msien = (ImageButton) itemView.findViewById(R.id.board_post_siren);
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

