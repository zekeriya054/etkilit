package com.etkilit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Yusuf on 18.02.2018.
 */
public class QrScan extends Activity implements OnClickListener {
    private Button btnOturumuAc,btnOturumuKilitle,btnOturumuKapat,btnBilKapat;
   // private TextView formatTxt, contentTxt;
   // ClientVT client;
    TextView infoip, qrIp,txtIP;
    String serverIP,IP="";
    static final int serverPort = 5461;
    private Socket socket;
    String mesaj="",imei;
    HashMap<String,String> tel;


    //TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scan);
        btnOturumuAc = (Button)findViewById(R.id.btnOturumAc);
        btnOturumuKilitle=(Button) findViewById(R.id.btnOturumuKilitle);
        btnOturumuKapat=(Button)findViewById(R.id.btnOturumuKapat);
        btnBilKapat=(Button)findViewById(R.id.btnBilKapat);
       // formatTxt = (TextView)findViewById(R.id.scan_format);
       // contentTxt = (TextView)findViewById(R.id.scan_content);
      //  infoip = (TextView) findViewById(R.id.scan_format);
      //  qrIp = (TextView) findViewById(R.id.scan_content);
     //   client=new ClientVT(this);
      //  infoip.setText(server.getIpAddress() + ":" + server.getPort());
        btnOturumuAc.setOnClickListener(this);
        btnOturumuKilitle.setOnClickListener(this);
        btnOturumuKapat.setOnClickListener(this);
        btnBilKapat.setOnClickListener(this);


    }


    public void onResume() {
        super.onResume();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alert=new AlertDialog.Builder(QrScan.this);
            alert.setTitle("Çıkmak istediğinizden emin misiniz?");
            alert.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},225);
            return;
        }
/*
        if(IP.length()==0) {
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //SharedPreferences settings = getPreferences(MODE_PRIVATE);
            IP = settings.getString("IP", "");
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        LisansKontrol lisansKontrol=new LisansKontrol();
        String imeiString = settings.getString("IMEI","");
       // Toast.makeText(QrScan.this, settings.getString("IMEI",""), Toast.LENGTH_SHORT).show();
        if(imeiString.equals("")) {
            imei = lisansKontrol.getIMEI(QrScan.this);
            boolean cevap = lisansKontrol.ImeiKayitlimi(QrScan.this, imei);
            if(cevap==false) {
                //   Toast.makeText(QrScan.this, settings.getString("IMEI",""), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LisansActivity.class);
                startActivity(intent);
            } else Toast.makeText(QrScan.this,"Imei kayıtlı ve onaylı",Toast.LENGTH_LONG);


        } else {
            if(lisansKontrol.ImeiKontrol(QrScan.this,imeiString)!=true){
                Toast.makeText(QrScan.this,"Tanımsız telefon...",Toast.LENGTH_LONG);
                System.exit(0);
            }
            if(lisansKontrol.LisansSuresiGecerlimi(QrScan.this)==false) {
                Toast.makeText(QrScan.this, "Lisans süreniz dolmuş..", Toast.LENGTH_SHORT).show();
                Intent lisans=new Intent(this,LisansActivity.class);
                startActivity(lisans);

            }
        }

*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //SharedPreferences settings=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("IP",IP);
        editor.commit();
    }


    public void onClick(View v){
        if(v.getId()==R.id.btnOturumAc){
            mesaj="login";
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();


        } else if(v.getId()==R.id.btnOturumuKilitle){
            mesaj="kilitle";
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Oturum kilitleniyor...", Toast.LENGTH_SHORT);
            toast.show();
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //SharedPreferences settings=getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("IP","");
            editor.commit();
            Thread ClientThread = new Thread(new QrScan.ClientThread());
            ClientThread.start();

        } else if(v.getId()==R.id.btnOturumuKapat){
            mesaj="oturumuKapat";
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Oturum kapatılıyor...", Toast.LENGTH_SHORT);
            toast.show();
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
           // SharedPreferences settings=getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("IP","");
            editor.commit();
            Thread ClientThread = new Thread(new QrScan.ClientThread());
            ClientThread.start();
        } else if(v.getId()==R.id.btnBilKapat){

            mesaj="Kapat";
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Bilgisayar kapatılıyor...", Toast.LENGTH_SHORT);
            toast.show();
            //SharedPreferences settings=getPreferences(MODE_PRIVATE);
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("IP","");
            editor.commit();
            Thread ClientThread = new Thread(new QrScan.ClientThread());
            ClientThread.start();

        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                char c;
                char[] text = new char[20];
                serverIP = scanningResult.getContents();
                IP = "";
                for (int i = 0; i < serverIP.length(); i++) {
                    text[i] = serverIP.charAt(i);
                    if (text[i] != 'Z') {
                        switch (text[i]) {
                            case 'y':
                                text[i] = 57;
                                break;
                            case 'U':
                                text[i] = 56;
                                break;
                            case 's':
                                text[i] = 55;
                                break;
                            case 'u':
                                text[i] = 54;
                                break;
                            case 'F':
                                text[i] = 53;
                                break;
                            case 'b':
                                text[i] = 52;
                                break;
                            case 'A':
                                text[i] = 51;
                                break;
                            case 'd':
                                text[i] = 50;
                                break;
                            case 'E':
                                text[i] = 49;
                                break;
                            case 'k':
                                text[i] = 48;
                                break;
                        }
                    } else text[i] = '.';
                    IP = IP + text[i];
                }

                String scanFormat = scanningResult.getFormatName();
                //  qrIp.setText(serverIP);
                Thread ClientThread2 = new Thread(new QrScan.ClientThread2());
                ClientThread2.start();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Oturum açılıyor...", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "QrCode okunamadı...!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "QrCode okunamadı...!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Oturum kapatma,kilitleme,bigisayarı kapatma
    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(IP);
                socket = new Socket(serverAddr, 5461);
                try {

                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(mesaj);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }
    // Okunan QrCode gönderiliyor
    class ClientThread2 implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(IP);
                socket = new Socket(serverAddr, 5444);
                try {

                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(mesaj);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ayar, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_anahtar:
                Intent lisans=new Intent(this,LisansActivity.class);
                startActivity(lisans);
                /*
                Toast.makeText(this, "Lisans seçildi", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.lisans);*/
                return true;
            case R.id.m_sunucu:
            Intent sunucu=new Intent(this,SunucuActivity.class);
            startActivity(sunucu);
        }

        return super.onOptionsItemSelected(item);
    }
}
