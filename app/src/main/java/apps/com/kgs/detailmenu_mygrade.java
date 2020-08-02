package apps.com.kgs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

public class detailmenu_mygrade extends AppCompatActivity {
    private EditText sailary;
    private EditText housecheck;
    private EditText carcheck;
    private EditText consume;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailmenu_mygrade);

        Log.d("detailmenu_mygrade", "detailmenu_mygrade");
        sailary = (EditText) findViewById(R.id.sailary);
        housecheck = (EditText) findViewById(R.id.housecheck);
        carcheck = (EditText) findViewById(R.id.carcheck);
        consume = (EditText) findViewById(R.id.consume);

        btnDone = (Button) findViewById(R.id.btn_q_signup);

        //회원가입버튼 클릭시 체크
        SharedPreferences location = getSharedPreferences("location", 0);
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        String uuid = location.getString("uuid", "");
        String cash = location.getString("cash", "");
        String point = location.getString("point", "");
        String age = location.getString("age", "");
        String sex = location.getString("sex", "");
        if(Integer.parseInt(point)<100) {
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(login.this, "닉네임확인"+nicname.getText().toString(), Toast.LENGTH_SHORT).show();
                    // 닉네임 입력 확인
                    if (sailary.getText().toString().length() == 0) {
                        Toast.makeText(detailmenu_mygrade.this, "당신의 연봉을 입려해주세요", Toast.LENGTH_SHORT).show();
                        sailary.requestFocus();
                        return;
                    }

                    // 인사말 입력 확인
                    if (housecheck.getText().toString().length() == 0) {
                        Toast.makeText(detailmenu_mygrade.this, "자가입력을 꼭 해주세요.", Toast.LENGTH_SHORT).show();
                        housecheck.requestFocus();
                        return;
                    }

                    if (carcheck.getText().toString().length() == 0) {
                        Toast.makeText(detailmenu_mygrade.this, "자동차 종류를 입력해주세요", Toast.LENGTH_SHORT).show();
                        carcheck.requestFocus();
                        return;
                    }

                    if (consume.getText().toString().length() == 0) {
                        Toast.makeText(detailmenu_mygrade.this, "한달 소비량을 넣어주세요", Toast.LENGTH_SHORT).show();
                        consume.requestFocus();
                        return;
                    }

                    getLogin();


                }

            });
        }else{
            TextView sailary = (TextView) findViewById(R.id.sailary);
            sailary.setText(cash);
            TextView housecheck = (TextView) findViewById(R.id.housecheck);
            housecheck.setText(age);
            TextView carcheck = (TextView) findViewById(R.id.carcheck);
            carcheck.setText(sex);
            TextView consume = (TextView) findViewById(R.id.consume);
            consume.setText(point);

            Button btn1 = (Button) findViewById(R.id.btn_q_signup); // 버튼이 btn1 이라면,
            btn1.setVisibility(View.INVISIBLE); // 화면에 보이게 한다.
            //btn1.settVisibility(View.INVISIBLE); // 화면에 안보이게 한다.

        }
    }

    ////////////////완료시 서버에 파라미터 전달////////////////////
    public void getLogin() {
        SharedPreferences location = getSharedPreferences("location", 0);
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        String uuid = location.getString("uuid", "");
        //Toast.makeText(this, "lonFrag :" + lon, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "latFrag :" + lat, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "latFrag :" + uuid, Toast.LENGTH_LONG).show();
        AQuery aq = new AQuery(this);
        // public JSONArray mItems = new JSONArray();
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/join.php?lat=" + lat + "&lon=" + lon + "&uuid=" + uuid +
                        "&cash=" + sailary.getText().toString() + "&age=" + housecheck.getText().toString() +
                        "&sex=" + carcheck.getText().toString() + "&point=" + consume.getText().toString(),
                JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        try {
                            //Toast.makeText(BoardWrite.this, "url :" + url, Toast.LENGTH_LONG).show();

                            //Log.e("url : ", url);

                            JSONObject oData = new JSONObject(object.toString());
                            //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            JSONArray arrData = new JSONArray(oData.getString("result"));


                            for (int i = 0; i < arrData.length(); i++) {
                                JSONObject getlist = arrData.getJSONObject(i);
                                String logincheck = getlist.getString("y");
                                Log.d("resultFor", logincheck);
                                if (logincheck.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "등록완료!!", Toast.LENGTH_LONG).show();

                                    ////가입 버튼을 누르면 마지막 이곳에서 이미지 업로드 실행 하는게 맞는것같음/////

                                    //Intent intent = new Intent(getApplicationContext(),MainActivity.class); // 다음 넘어갈 클래스 지정
                                    //startActivity(intent); // 다음 화면으로 넘어간다

                                    Toast.makeText(getApplicationContext(), getString(R.string.toast14), Toast.LENGTH_LONG).show();

                                    ////가입 버튼을 누르면 마지막 이곳에서 이미지 업로드 실행 하는게 맞는것같음/////

                                    Intent intent = new Intent(getApplicationContext(), detailmenu_mygrade2.class); // 다음 넘어갈 클래스 지정
                                    startActivity(intent); // 다음 화면으로 넘어간다

                                }
                            }

                        } catch (Exception e) {

                        }

                    }
                });
    }
////////////////완료시 서버에 파라미터 전달 끝////////////////////
}

