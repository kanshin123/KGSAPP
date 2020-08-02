package apps.com.kgs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class detailmenu_mygrade2 extends AppCompatActivity {
    private EditText sailary;
    private EditText housecheck;
    private EditText carcheck;
    private EditText consume;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailmenu_mygrade2);



        SharedPreferences location = getSharedPreferences("location", 0);
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        String uuid = location.getString("uuid", "");
        String cash = location.getString("cash", "");
        String point = location.getString("point", "");
        String age = location.getString("age", "");
        String sex = location.getString("sex", "");

        int allresult = Integer.parseInt(cash) - Integer.parseInt(point) + Integer.parseInt(age) + Integer.parseInt(sex);
        TextView tv_progress_message = (TextView) findViewById(R.id.set_mygrade_txt);
        if (allresult > 100) {
            tv_progress_message.setText("1등급");
        }else{

            tv_progress_message.setText("10등급");
        }

    }

}