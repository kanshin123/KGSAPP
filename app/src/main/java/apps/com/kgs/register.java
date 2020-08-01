package apps.com.kgs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

public class register extends AppCompatActivity {
    private EditText nickname;
    private EditText name;
    private EditText telnum;
    private EditText email;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Log.d("register","register");
        nickname = (EditText) findViewById(R.id.nickname);
        name = (EditText) findViewById(R.id.name);
        telnum = (EditText) findViewById(R.id.telnum);
        email = (EditText) findViewById(R.id.email);

        btnDone = (Button) findViewById(R.id.btn_signup);

        //회원가입버튼 클릭시 체크
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(login.this, "닉네임확인"+nicname.getText().toString(), Toast.LENGTH_SHORT).show();
                // 닉네임 입력 확인
                if (nickname.getText().toString().length() == 0) {
                    Toast.makeText(register.this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    nickname.requestFocus();
                    return;
                }

                // 인사말 입력 확인
                if (name.getText().toString().length() == 0) {
                    Toast.makeText(register.this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }

                if (email.getText().toString().length() == 0) {
                    Toast.makeText(register.this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }

                if (telnum.getText().toString().length() == 0) {
                    Toast.makeText(register.this, "전화번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    telnum.requestFocus();
                    return;
                }



               getLogin();

            }

        });

    }



    ////////////////완료시 서버에 파라미터 전달////////////////////
    public void getLogin() {
        SharedPreferences location = getSharedPreferences("location", 0);
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        final String uuid = location.getString("uuid", "");
        AQuery aq = new AQuery(this);
        // public JSONArray mItems = new JSONArray();
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/join.php?lat=" + lat + "&lon=" + lon + "&uuid=" + uuid +
                        "&nickname=" + nickname.getText().toString() + "&name=" + name.getText().toString() +
                        "&email=" + email.getText().toString() + "&telnum=" + telnum.getText().toString(),
                JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        try {
                            JSONObject oData = new JSONObject(object.toString());
                            //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            JSONArray arrData = new JSONArray(oData.getString("result"));

                            for (int i = 0; i < arrData.length(); i++) {
                                JSONObject getlist = arrData.getJSONObject(i);
                                String logincheck = getlist.getString("y");
                                final String uid = getlist.getString("uid");
                                Log.d("resultFor", logincheck);
                                Log.d("uidresult", uid);
                                if (logincheck.equals("0")) {
                                    SharedPreferences location1 = getSharedPreferences("location", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = location1.edit();
                                    editor.putString("cash", "1000"); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                    editor.putString("point", "10000"); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                    editor.putString("uid", uid); //디비의 고유 번호를 가져온다.
                                    editor.putString("sex", "0");
                                    editor.putString("age", "1");
                                    editor.putString("mscount", "0");
                                    editor.putString("nickname", nickname.getText().toString());
                                    editor.putString("name", name.getText().toString());
                                    editor.putString("uuid", uuid);
                                    editor.commit();


                                    Toast.makeText(getApplicationContext(), getString(R.string.toast14), Toast.LENGTH_LONG).show();

                                    ////가입 버튼을 누르면 마지막 이곳에서 이미지 업로드 실행 하는게 맞는것같음/////

                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class); // 다음 넘어갈 클래스 지정
                                    startActivity(intent); // 다음 화면으로 넘어간다

                                }
                            }

                        } catch (Exception e) {

                        }

                    }
                });
    }
////////////////완료시 서버에 파라미터 전달 끝////////////////////

    @Override
    public void onBackPressed() {
        // Intent intent = new Intent(getApplicationContext(), mainactivity.class); // 다음 넘어갈 클래스 지정
        // startActivity(intent);
        // super.onBackPressed();
        moveTaskToBack(true);
        Intent intent = new Intent(register.this,policy.class);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());


    }


}

