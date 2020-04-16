package com.etkilit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yusuf on 31.10.2018.
 */

public class LisansKontrol {
    private boolean kayitlimi;

    @SuppressLint("MissingPermission")
    public static String getIMEI(Activity act){
        TelephonyManager telephonyManager = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 26) {

            return telephonyManager.getImei();
        }
        else
        {
            return telephonyManager.getDeviceId();
        }

    }
    public boolean LisansSuresiGecerlimi(final Context context){

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String bitisTarihiStr=settings.getString("BITIS_TARIHI","");
        String _kayitTarihi=settings.getString("KAYIT_TARIHI","");
        if(bitisTarihiStr.equals("null") || _kayitTarihi.equals("null")) {
            Toast.makeText(context, "Geçersiz lisans süresi...", Toast.LENGTH_SHORT).show();
           // return false;
        } else {
           // Log.e("DATA",bitisTarihiStr);
            Date simdi = Calendar.getInstance().getTime();
            //Log.e("DATA Current time", simdi.toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date bitisTarihi = format.parse(bitisTarihiStr);
                //Log.e("DATA d",bitisTarihi.toString());
                if(simdi.after(bitisTarihi)) {
                    return false;
                } //else Toast.makeText(context, "Geçerli lisans süresi..", Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        return true;
    }
    public boolean ImeiKontrol(Activity act,String imei){
        if(LisansKontrol.getIMEI(act).equals(imei)) return true;
        else return false;
    }
    public boolean ImeiKayitlimi(final Context context, String imei)
    {
        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(context);
        //  String url="http://10.0.2.2/et/et.php";
        String url=String.format("%s/karekilit/web/index.php?r=%s&imei=%s",settings.getString("SunucuIP",""),
                "kullanici%2Flisanskontrol",imei);

        StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                   // Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    String _cevap=jsonObject.getString("cevap");
                    if(_cevap.equals("1")) {
                        String _bitis_tarihi=jsonObject.getString("bitis_tarihi");

                        if(!_bitis_tarihi.equals("null")) {

                            String _imei = jsonObject.getString("imei");
                            String _tel_no = jsonObject.getString("tel_no");
                            String _okul_kodu = jsonObject.getString("okul_kodu");
                            String _kayit_tarihi = jsonObject.getString("kayit_tarihi");

                       /* Log.e("DATA",_cevap);
                        Log.e("DATA",_imei);
                        Log.e("DATA",_tel_no);
                        Log.e("DATA",_okul_kodu);
                        Log.e("DATA",_kayit_tarihi);
                        Log.e("DATA",_bitis_tarihi);
                        */
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("IMEI", _imei);
                            editor.putString("TEL_NO", _tel_no);
                            editor.putString("OKUL_KODU", _okul_kodu);
                            editor.putString("KAYIT_TARIHI", _kayit_tarihi);
                            editor.putString("BITIS_TARIHI", _bitis_tarihi);
                            editor.commit();
                            kayitlimi = true;
                        }
                    }
                    else if(_cevap.equals("0")) {
                        Toast.makeText(context,"Kayit mevcut değil ya da onaysız...",Toast.LENGTH_LONG).show();
                /*
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor=settings.edit();
                        editor.putString("IMEI",null);
                        editor.putString("TEL_NO",null);
                        editor.putString("OKUL_KODU",null);
                        editor.putString("KAYIT_TARIHI",null);
                        editor.putString("BITIS_TARIHI",null);*/
                        kayitlimi=false;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                 Toast.makeText(context, "Sunucuya ulaşılamadı!!", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
        return kayitlimi;
    }
}
