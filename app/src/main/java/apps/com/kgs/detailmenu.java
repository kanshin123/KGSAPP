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

        findViewById(R.id.btn_mygrade).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent myaccountActivity = new Intent(detailmenu.this, mygrade.class);

                        startActivity(myaccountActivity);


                    }
                }
        );

    }

}


