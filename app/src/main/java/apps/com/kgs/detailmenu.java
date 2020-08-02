package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class detailmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailmenu);

        Button button = (Button) findViewById(R.id.btn_reveneu);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(detailmenu.this, myaccount_charge.class);

                startActivity(myaccountActivity);

            }

        });
        Button button1 = (Button) findViewById(R.id.btn_mygrade);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(detailmenu.this, detailmenu_mygrade.class);

                startActivity(myaccountActivity);

            }

        });
        Button button2 = (Button) findViewById(R.id.friendgrade);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(detailmenu.this, myaccount_charge.class);

                startActivity(myaccountActivity);

            }

        });
        Button button3 = (Button) findViewById(R.id.schedule);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent mychargeActivity = new Intent(detailmenu.this, detailmenu_schedule.class);

                startActivity(mychargeActivity);

            }

        });

    }
}