package apps.com.kgs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nickname;
    private EditText greetings;
    private Button btnDone;
    private Button btn_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        nickname = (EditText) findViewById(R.id.nickname);
        greetings = (EditText) findViewById(R.id.greetings);
        btnDone = (Button) findViewById(R.id.btn_signup);


        findViewById(R.id.btn_account).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent myaccountActivity = new Intent(MainActivity.this, myaccount.class);

                        startActivity(myaccountActivity);


                    }
                }
        );
        findViewById(R.id.btn_detail).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent myaccountActivity = new Intent(MainActivity.this, detailmenu.class);

                        startActivity(myaccountActivity);


                    }
                }
        );



    }
}