package com.etkilit;

/**
 * Created by Yusuf on 26.02.2018.
 */


import android.app.Activity;
import android.os.Bundle;




public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.qr_scan);

}

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}