package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment_babybirth2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_babybirth2);

        Button button = (Button) findViewById(R.id.btn_babybirth1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_babybirth2.this, payment_babybirth3.class);

                startActivity(myaccountActivity);

            }

        });
        Button button1 = (Button) findViewById(R.id.btn_babybirth2);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_babybirth2.this, payment_babybirth3.class);

                startActivity(myaccountActivity);

            }

        });


    }
}