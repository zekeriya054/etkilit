package com.etkilit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SunucuActivity extends Activity implements View.OnClickListener {
    private Button btnKaydet;
    private TextView txtSunucuIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunucu);
        btnKaydet=(Button) findViewById(R.id.btnSunucuKaydet);
        txtSunucuIP=(TextView)findViewById(R.id.txtsSunucuIP);
        btnKaydet.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(SunucuActivity.this);
        String sunucuIP=settings.getString("SunucuIP","");
        txtSunucuIP.setText(sunucuIP);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnSunucuKaydet){
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(SunucuActivity.this);
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("SunucuIP",txtSunucuIP.getText().toString());
            editor.commit();
            Toast.makeText(getApplicationContext(),"Sunucu başarıyla kaydedildi...",Toast.LENGTH_SHORT).show();
        }
    }
}
