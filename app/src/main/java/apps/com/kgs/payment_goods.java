package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment_goods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_goods);

        Button button = (Button) findViewById(R.id.btn_paygoods);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_goods.this, payment_goods2.class);

                startActivity(myaccountActivity);

            }

        });
        Button button1 = (Button) findViewById(R.id.btn_goodsflower);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_goods.this, payment_goods_flower.class);

                startActivity(myaccountActivity);

            }

        });

        Button button2 = (Button) findViewById(R.id.btn_notigoods);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(payment_goods.this, patment_goods_noti.class);

                startActivity(myaccountActivity);

            }

        });

    }
}