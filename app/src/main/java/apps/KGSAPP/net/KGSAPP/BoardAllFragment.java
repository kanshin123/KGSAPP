package apps.KGSAPP.net.KGSAPP;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yunseokchoi on 2017. 5. 22..
 */

public class BoardAllFragment extends Fragment {
    private static View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ////아래와 같이 했더니 프레그먼트 뷰이동시 에러가 안남
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.board_all_fragment, container, false);

        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        return view;
    }

}


