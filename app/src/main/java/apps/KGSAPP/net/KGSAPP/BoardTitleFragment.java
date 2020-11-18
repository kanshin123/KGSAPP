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

public class BoardTitleFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.board_title,container,false);


        view.findViewById(R.id.board_btn_all).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 여자
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.baordonFragmentChanged(0);

                    }
                }
        );

        view.findViewById(R.id.board_btn_today).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 남자
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.baordonFragmentChanged(1);

                    }
                }
        );

        view.findViewById(R.id.board_btn_weeks).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 전체
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.baordonFragmentChanged(2);

                    }
                }
        );

        view.findViewById(R.id.board_btn_my).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요 전체
                        //Toast.makeText(getContext(), "woman", Toast.LENGTH_LONG).show();
                        MainActivity activity = (MainActivity) getActivity();
                        activity.baordonFragmentChanged(3);

                    }
                }
        );

        return view;
    }



}
