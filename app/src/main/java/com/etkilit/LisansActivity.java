package com.etkilit;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;
import org.json.JSONException;

public class LisansActivity extends Activity {
    String lisansKalanGun,kod="",ekod="";

    private Button btnKaydol,btnSunucu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lisans);
     //   final TextView txtKey=(TextView)findViewById(R.id.txtKey);
        final TextView txtTelno=(TextView)(findViewById(R.id.txtTelno));
        final TextView txtOkulKodu=(TextView)(findViewById(R.id.txtOkulKodu));
        btnKaydol=(Button)findViewById(R.id.btnKaydol);
        btnSunucu=(Button)findViewById(R.id.btnSunucuAyarla);

 /*/*
        SharedPreferences settings=getPreferences(MODE_PRIVATE);
        String telNo=settings.getString("TELNO","");
        String okulKodu=settings.getString("OKUL_KODU","");
        if(telNo.length()!=0) {
            txtTelno.setText(telNo);
        }
        if(okulKodu.length()!=0){
            txtOkulKodu.setText(okulKodu);
        }

        //şifreleme
        char[] text = new char[20];
        int j;

        for(int i=0;i<imei.length();i++){
            text[i] = imei.charAt(i);
            j='9'-text[i];
            switch (j){
                case 0:text[i]='0';break;
                case 1:text[i]='Y';break;
                case 2:text[i]='2';break;
                case 3:text[i]='U';break;
                case 4:text[i]='4';break;
                case 5:text[i]='S';break;
                case 6:text[i]='6';break;
                case 7:text[i]='X';break;
                case 8:text[i]='8';break;
                case 9:text[i]='F';break;
            }
            kod=kod+text[i];
        }*/
      //  txtKey.setText(kod);
       // txtKey.setText(imei);
        btnKaydol.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LisansActivity.this);
                 String url=String.format("%s/karekilit/web/index.php?r=%s&imei=%s&tel_no=%s&okul_kodu=%s",settings.getString("SunucuIP",""),
                        "kullanici%2Fsave",LisansKontrol.getIMEI(LisansActivity.this),txtTelno.getText().toString(),txtOkulKodu.getText().toString());
                //String url2=String.format("http://192.168.63.223/karekilit/web/index.php?r=%s","kullanici%2Fsave");
                Toast.makeText(LisansActivity.this,settings.getString("SunucuIP",""),Toast.LENGTH_LONG);

                final StringRequest istek=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {//bağlantı için kullandığımız sınıf
                    @Override
                    public void onResponse(String response) {//geri dönen cevap json olarak

                        try {

                                JSONObject jsonObject= new JSONObject(response);
                                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                                String _imei = jsonObject.getString("imei");
                                String _tel_no = jsonObject.getString("tel_no");
                                String _okul_kodu=jsonObject.getString("okul_kodu");
                                String _cevap=jsonObject.getString("cevap");
                                if(_cevap.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Kayıt mevcut...", Toast.LENGTH_LONG).show();
                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LisansActivity.this);
                                    SharedPreferences.Editor editor = settings.edit();
                                    String _bitis_tarihi=jsonObject.getString("bitis_tarihi");
                                    if(!settings.getString("IMEI","").equals("")) {
                                        editor.putString("TEL_NO", _tel_no);
                                        editor.putString("OKUL_KODU", _okul_kodu);
                                        editor.putString("BITIS_TARIHI", _bitis_tarihi);
                                        editor.commit();
                                    }
                                }
                                else if(_cevap.equals("0")) {

                                    Toast.makeText(getApplicationContext(),"Kaydınız başarılı bir şekide oluşturuldu...",Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(),"Onay işleminden sonra program aktif olacaktır...",Toast.LENGTH_LONG).show();
                                }
                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {//herhangi bir hata varsa onaylanmadı şeklinde uyarı mesajı verdiriyoruz
                            @Override
                            public void onErrorResponse(VolleyError error) {//hata olursa geri dönen hata mesajı
                                Toast.makeText(getApplicationContext(),"istek onaylanmadı",Toast.LENGTH_SHORT).show();
                            }
                        }){
/*
                    @Override
                    protected Map<String, String> getParams()
                            throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("imei", imei);
                        params.put("okul_kodu",txtOkulKodu.getText().toString());
                        params.put("tel_no",txtTelno.getText().toString());
                        return params;
                    };
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> param = new HashMap<String, String>();

                        return param;
                    }
*/
                };
               // jsonForPostRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue kuyruk= Volley.newRequestQueue(getApplicationContext());//burada artık istekler kuyruğa aktarılıyor ve işlemler gerçekleştiriliyor.
                kuyruk.add(istek);

                /*
                if(txtTelno.length()==10) {
                    SharedPreferences settings=getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("TELNO", txtTelno.getText().toString());
                    editor.putString("OKUL_KODU",txtOkulKodu.getText().toString());
                    editor.commit();
                }


                // Create the text message with a string
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, kod);
                sendIntent.setType("text/plain");
                String title = getResources().getString(R.string.secici_basligi);
                Intent chooser = Intent.createChooser(sendIntent, title);
                // Verify that the intent will resolve to an activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                /*



                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-H:m:s:S");
                Date simdi=new Date();
                String strTarih=sdf.format(simdi);

                Date gelecek=new Date(simdi.getYear()+1,simdi.getMonth(),simdi.getDate(),0,0);
                long fark=gelecek.getTime()-simdi.getTime();
                long gunfark=fark/(1000*60*60*24);
                lisansKalanGun=Long.toString(gunfark);
                                lisansSuresi.setText(strTarih);

                Date simdi=new Date();
                long unxtime=simdi.getTime();
                lisansSuresi.setText(""+unxtime);

                            */

/*
              //şifre çözme
                for(int i=0;i<kod.length();i++){
                    text[i] = kod.charAt(i);
                    switch (text[i]){
                        case 0:text[i]='0';break;
                        case 'Y':text[i]='1';break;
                        case 2:text[i]='2';break;
                        case 'U':text[i]='3';break;
                        case 4:text[i]='4';break;
                        case 'S':text[i]='5';break;
                        case 6:text[i]='6';break;
                        case 'X':text[i]='7';break;
                        case 8:text[i]='8';break;
                        case 'F':text[i]='9';break;
                    }
                    j='9'-text[i];
                    ekod=ekod+j;
                }
                lisansSuresi.setText(ekod);
    */
    }
        });
        btnSunucu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sunucu=new Intent(LisansActivity.this,SunucuActivity.class);
                startActivity(sunucu);

            }
        });
    }



}
