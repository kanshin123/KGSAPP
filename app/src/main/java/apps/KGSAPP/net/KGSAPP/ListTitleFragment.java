package apps.KGSAPP.net.KGSAPP;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by yunseokchoi on 2017. 5. 22..
 */

public class ListTitleFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_title,container,false);


        view.findViewById(R.id.btn_woman).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 여자
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.onFragmentChanged(1);

                    }
                }
        );

        view.findViewById(R.id.btn_man).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 남자
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.onFragmentChanged(0);

                    }
                }
        );

        view.findViewById(R.id.btn_all).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 전체
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.onFragmentChanged(2);

                    }
                }
        );

        return view;
    }



}
