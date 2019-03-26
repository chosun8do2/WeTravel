package org.mrlee.wetravel;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {
    Button before, now, after;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        before = (Button) findViewById(R.id.before);
        now = (Button) findViewById(R.id.now);
        after = (Button) findViewById(R.id.after);

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectActivity.this, BeforeSearchActivity.class);
                startActivity(intent);
            }
        });

        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectActivity.this, nowSearchActivity.class);
                startActivity(intent);
            }
        });

        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectActivity.this, AfterActivity.class);
                startActivity(intent);
            }
        });
    }
}
