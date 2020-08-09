package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment_wedding_invite  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_wedding_invite);
        findViewById(R.id.btn_send1).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick (View v){
                        Intent myaccountActivity = new Intent(payment_wedding_invite.this, payment_wedding_invite2.class);

                        startActivity(myaccountActivity);
                    }

                }
        );
    }
}