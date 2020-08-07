package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment_wedding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_wedding);

        Button button = (Button) findViewById(R.id.btn_tmwwedding);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_wedding.this, payment_wedding2.class);

                startActivity(myaccountActivity);

            }

        });
        Button button1 = (Button) findViewById(R.id.btn_sendflower);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_wedding.this, payment_wedding_flower.class);

                startActivity(myaccountActivity);

            }

        });

        Button button2 = (Button) findViewById(R.id.btn_sednweddingcard);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_wedding.this, payment_wedding_invite.class);

                startActivity(myaccountActivity);

            }

        });

    }
}