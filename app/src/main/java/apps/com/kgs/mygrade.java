package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class mygrade extends AppCompatActivity {
    private EditText sailary;
    private EditText housecheck;
    private EditText carcheck;
    private EditText consume;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);

        Log.d("question", "question");
        sailary = (EditText) findViewById(R.id.sailary);
        housecheck = (EditText) findViewById(R.id.housecheck);
        carcheck = (EditText) findViewById(R.id.carcheck);
        consume = (EditText) findViewById(R.id.consume);

        btnDone = (Button) findViewById(R.id.btn_q_signup);

        //회원가입버튼 클릭시 체크
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(login.this, "닉네임확인"+nicname.getText().toString(), Toast.LENGTH_SHORT).show();
                // 닉네임 입력 확인
                if (sailary.getText().toString().length() == 0) {
                    Toast.makeText(mygrade.this, "당신의 연봉을 입려해주세요", Toast.LENGTH_SHORT).show();
                    sailary.requestFocus();
                    return;
                }

                // 인사말 입력 확인
                if (housecheck.getText().toString().length() == 0) {
                    Toast.makeText(mygrade.this, "자가입력을 꼭 해주세요.", Toast.LENGTH_SHORT).show();
                    housecheck.requestFocus();
                    return;
                }

                if (carcheck.getText().toString().length() == 0) {
                    Toast.makeText(mygrade.this, "자동차 종류를 입력해주세요", Toast.LENGTH_SHORT).show();
                    carcheck.requestFocus();
                    return;
                }

                if (consume.getText().toString().length() == 0) {
                    Toast.makeText(mygrade.this, "한달 소비량을 넣어주세요", Toast.LENGTH_SHORT).show();
                    consume.requestFocus();
                    return;
                }


            }

        });

    }
}

