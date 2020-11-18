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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Provides UI for the view with Cards.
 */
public class CardContentFragment extends Fragment {
    public String mBbsName;
    private RecyclerViewListAdapter mRvListAdapter;
    public boolean mVisible = false;
    public boolean mLoading = false;
    public String mApiName = "";
    public String uuid;



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
        String uid = location.getString("uid", ""); //디비에서 고유 번호 불러오기
        uuid = location.getString("uuid", "");
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        //Toast.makeText(getContext(), "lon :" + lon, Toast.LENGTH_SHORT).show();
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
            final String muid;
            final String fuid;
            final String sex;
            final String sexflag;           ///null인 이미지 처리값을 넘기기위해 설정

            try {
                if (holder instanceof ListItemViewHolder) {
                    ListItemViewHolder item_holder = (ListItemViewHolder) holder;
                    //데이터가 없을때 표시해야 하는데...


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

                    if (mItems.getJSONObject(position).getString("nickname").equals("")) {
                        String nick = "not nickname";
                        item_holder.mSubject.setText(nick);

                    }else {
                        String nick = mItems.getJSONObject(position).getString("nickname");
                        item_holder.mSubject.setText(nick);
                    }

                    item_holder.mUserId.setText(mItems.getJSONObject(position).getString("message"));


                    Log.e("nickname", mItems.getJSONObject(position).getString("nickname"));
                    Log.e("message", mItems.getJSONObject(position).getString("message"));


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
            TextView mDate;
            TextView msex;      //프렌즈 성별
            ImageView mImg;   //이미지 표시를 위한 설정


            public ListItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                //mNo = (TextView) itemView.findViewById(R.id.);
                //mCate = (TextView) itemView.findViewById(R.id.textCate);
                mImg = (ImageView) itemView.findViewById(R.id.board_avatar);    //이미지 표시를 위한 설정
                mSubject = (TextView) itemView.findViewById(R.id.board_title);
                mUserId = (TextView) itemView.findViewById(R.id.board_desc);
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

