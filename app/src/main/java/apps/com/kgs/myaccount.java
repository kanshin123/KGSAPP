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
                        Intent myaccountActivity = new Intent(myaccount.this, myaccount_charge.class);

                        startActivity(myaccountActivity);


                    }
                }
        );
    }


}
