package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        findViewById(R.id.btn_wedding).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick (View v){
                        Intent myaccountActivity = new Intent(payment.this, payment_wedding.class);

                        startActivity(myaccountActivity);
                    }

                }
        );
    }
}
