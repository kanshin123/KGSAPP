package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        findViewById(R.id.btn_agreement).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick (View v){
                        Intent myaccountActivity = new Intent(info.this, info_agreementpic.class);

                        startActivity(myaccountActivity);
                    }

                }
        );
    }
}
