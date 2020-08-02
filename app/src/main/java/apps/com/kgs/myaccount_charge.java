package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class myaccount_charge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_charge);

    findViewById(R.id.btn_accountchange).setOnClickListener(
                new Button.OnClickListener() {
        public void onClick (View v){
            Intent myaccountActivity = new Intent(myaccount_charge.this, myaccount_charge2.class);

            startActivity(myaccountActivity);
        }

    }
    );
    }
}