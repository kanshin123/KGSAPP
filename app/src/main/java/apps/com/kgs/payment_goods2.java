package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment_goods2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_goods2);
        findViewById(R.id.btn_paygoods1).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick (View v){
                        Intent myaccountActivity = new Intent(payment_goods2.this, payment_goods3.class);

                        startActivity(myaccountActivity);
                    }

                }
        );
    }
}