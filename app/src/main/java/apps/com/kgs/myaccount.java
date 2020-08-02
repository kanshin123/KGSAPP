package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class myaccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount);

        Button button = (Button) findViewById(R.id.Cconnection);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent myaccountActivity = new Intent(myaccount.this, myaccount_charge.class);

                startActivity(myaccountActivity);

            }

        });
        Button button1 = (Button) findViewById(R.id.btn_charge);
        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 레이아웃의 내용을 객체화 시키는 과정
                Intent mychargeActivity = new Intent(myaccount.this, myaccount_bankaccount.class);

                startActivity(mychargeActivity);

            }

        });
        /*
        findViewById(R.id.Cconnection).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent myaccountActivity = new Intent(myaccount.this, myaccount_bankaccount.class);

                        startActivity(myaccountActivity);


                    }
                }
        );

        findViewById(R.id.btn_account).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent mychargeActivity = new Intent(myaccount.this, myaccount_charge.class);

                        startActivity(mychargeActivity);


                    }
                }
        );
         */
    }


}
