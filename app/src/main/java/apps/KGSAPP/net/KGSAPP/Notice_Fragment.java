package apps.KGSAPP.net.KGSAPP;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import static android.R.attr.type;
//import static apps.minatalk.net.minatalk.R.array.places;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Notice_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notice_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notice_Fragment extends Fragment {
    public String mBbsName;
    //private RecyclerViewListAdapter mRvListAdapter;
    public boolean mVisible = false;
    public boolean mLoading = false;
    public String mApiName = "";
    public String uuid;
    public String uid;
    public RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.notice_recycler_view,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.notice_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        getListData();

        return view;


    }

    public void getListData() {
        ////메세지 박스에 표시하기 위한 테이터 불러오기

        final List<ExpandableListAdapter.Item> data = new ArrayList<>();

        AQuery aq = new AQuery(this.getActivity());
        aq.ajax("http://kgs.yunstone.com/ajax/mainboard.php", JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                String[] regarr;
                if (object != null) {
                    //로딩표시 지움.
                    try {
                        JSONObject oData = new JSONObject(object.toString());

                        JSONArray arrData = new JSONArray(oData.getString("result"));
                        //JSONArray arrData = new JSONArray(oData.getString(""));
                        for (int i = 0; i < arrData.length(); i++) {
                        //data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, arrData.getJSONObject(i).getString("sub")));
                        //data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, arrData.getJSONObject(i).getString("memo")));

                            regarr = arrData.getJSONObject(i).getString("regdate").split(" ");

                            ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, arrData.getJSONObject(i).getString("sub"));
                            places.invisibleChildren = new ArrayList<>();
                            places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, regarr[0]));
                            places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, arrData.getJSONObject(i).getString("memo")));
                            data.add(places);

                        }
                        //여기에서 받아온 값을 배열에 넣어서 카톡화면으로 넘겨야 한다고 생각함

                        //showToast(Integer.toString(mRvListAdapter.mItems.length()));

                        //View view = inflater.inflate(R.layout.recycler_view, container, false);
                        //RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.my_recycler_view);
                        //recyclerView.setHasFixedSize(true);
                        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        //recyclerView.setLayoutManager(mLayoutManager);
                        //mRvListAdapter = new RecyclerViewListAdapter();
                        //mRvListAdapter.mBbsName = mBbsName;
                        //recyclerView.setAdapter(mRvListAdapter);

                        recyclerView.setAdapter(new ExpandableListAdapter(data));

                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    public static class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int HEADER = 0;
        public static final int CHILD = 1;

        private List<Item> data;

        public ExpandableListAdapter(List<Item> data) {
            this.data = data;
        }

        @Override

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) { //필수 인터패이스

            View view;
            Context context = parent.getContext();
            float dp = context.getResources().getDisplayMetrics().density;
            int subItemPaddingLeft = (int) (18 * dp);
            int subItemPaddingTopAndBottom = (int) (5 * dp);
            switch (type) {
                case HEADER:
                    LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.list_header, parent, false);
                    ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                    return header;
                case CHILD:
                    TextView itemTextView = new TextView(context);
                    itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                    itemTextView.setTextColor(0x88000000);
                    itemTextView.setLayoutParams(
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    return new RecyclerView.ViewHolder(itemTextView) {
                    };
            }
            return null;


        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final Item item = data.get(position);
            switch (item.type) {
                case HEADER:
                    final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                    itemController.refferalItem = item;
                    itemController.header_title.setText(item.text);
                    if (item.invisibleChildren == null) {
                        itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                    } else {
                        itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                    }
                    itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.invisibleChildren == null) {
                                item.invisibleChildren = new ArrayList<Item>();
                                int count = 0;
                                int pos = data.indexOf(itemController.refferalItem);
                                while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                    item.invisibleChildren.add(data.remove(pos + 1));
                                    count++;
                                }
                                notifyItemRangeRemoved(pos + 1, count);
                                itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                            } else {
                                int pos = data.indexOf(itemController.refferalItem);
                                int index = pos + 1;
                                for (Item i : item.invisibleChildren) {
                                    data.add(index, i);
                                    index++;
                                }
                                notifyItemRangeInserted(pos + 1, index - pos - 1);
                                itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                                item.invisibleChildren = null;
                            }
                        }
                    });
                    break;
                case CHILD:
                    TextView itemTextView = (TextView) holder.itemView;
                    itemTextView.setText(data.get(position).text);
                    break;
            }
        }


        @Override
        public int getItemViewType(int position) {
            return data.get(position).type;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class ListHeaderViewHolder extends RecyclerView.ViewHolder {
            public TextView header_title;
            public ImageView btn_expand_toggle;
            public Item refferalItem;

            public ListHeaderViewHolder(View itemView) {
                super(itemView);
                header_title = (TextView) itemView.findViewById(R.id.header_title);
                btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
            }
        }

        public static class Item {
            public int type;
            public String text;
            public List<Item> invisibleChildren;

            public Item() {
            }

            public Item(int type, String text) {
                this.type = type;
                this.text = text;
            }
        }
    }



}