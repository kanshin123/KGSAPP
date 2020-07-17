package apps.com.kgs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText nickname;
    private EditText greetings;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nickname = (EditText) findViewById(R.id.nickname);
        greetings = (EditText) findViewById(R.id.greetings);
        btnDone = (Button) findViewById(R.id.btn_signup);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(login.this, "닉네임확인"+nicname.getText().toString(), Toast.LENGTH_SHORT).show();
                // 닉네임 입력 확인
                if (nickname.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "닉네임을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    nickname.requestFocus();
                    return;
                }

                // 인사말 입력 확인
                if (greetings.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "인사말을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    greetings.requestFocus();
                    return;
                }



                getLogin();

            }

        });

    }

    public void getLogin() {
        SharedPreferences location = getSharedPreferences("location", 0);
        String lon = location.getString("lon", "0");
        String lat = location.getString("lat", "0");
        String uuid = location.getString("uuid", "1111");
        //Log.d("filename",fileName);
        AQuery aq = new AQuery(MainActivity.this);
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://KGS.yunstone.com/ajax/join.php?nickname=" + nickname.getText().toString() + "&Greetings=" + greetings.getText().toString(),
                JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        try {
                            JSONObject oData = new JSONObject(object.toString());
                            //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            //JSONArray arrData = new JSONArray[];
                            JSONArray arrData = new JSONArray();
                            arrData.put(oData.getString("result"));
                            //JSONArray arrData = new JSONArray(oData.getString("result"));

                            arrData = new JSONArray(oData.getString("result"));
                            Log.d("arrData", String.valueOf(arrData.length()));

                            //for (int i = 0; i < 1; i++) {
                            JSONObject getlist = arrData.getJSONObject(0);
                            String logincheck = getlist.getString("y");
                            Log.d("resultFor", logincheck);
                            if (logincheck.equals("0")) {
                                SharedPreferences location1 = getSharedPreferences("location", MODE_PRIVATE);
                                SharedPreferences.Editor editor = location1.edit();
                                editor.putString("nickname", nickname.getText().toString());
                                editor.putString("greetings", greetings.getText().toString());
                                editor.commit();

                                Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_LONG).show();

                                ////가입 버튼을 누르면 마지막 이곳에서 이미지 업로드 실행 하는게 맞는것같음/////
                                //SystemClock.sleep(30);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
                                startActivity(intent); // 다음 화면으로 넘어간다

                            }
                            //}

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
                            startActivity(intent); // 다음 화면으로 넘어간다
                        } catch (Exception e) {

                        }

                    }
                });
    }
////////////////완료시 서버에 파라미터 전달 끝////////////////////


}