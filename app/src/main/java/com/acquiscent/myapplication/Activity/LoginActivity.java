package com.acquiscent.myapplication.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.acquiscent.myapplication.Controller.AppController;
import com.acquiscent.myapplication.Model.LoginResponse;
import com.acquiscent.myapplication.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.acquiscent.myapplication.Activity.Receiver.IS_NETWORK_AVAILABLE;

/**
 * Created by acquiscent on 9/1/17.
 */

public class LoginActivity extends AppCompatActivity {

    String login_url = "https://fms.terabhyte.com/FleetServices.asmx/IsLogin";
    //String login_url = "http://fms.terabhyte.com/FleetServices.asmx/IsLogin";
  //  String url = "http://brightkidsabcapi.acquiscent.com/SchoolServices.asmx/IsLogin";

    // String login_url ="http://fms.terabhyte.com/FleetServices.asmx/IsLogin";
    EditText username, password;
    Button login;
    String username_str, password_str;
    Cryptography_Android cryptography;
    static String _cipherKey = "P@SSW@RD@09";

    String encriptUserpass = "";
    String output, responseType = "", result = "";
    JSONObject obj;
    ProgressBar mprogressBar;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Boolean isNetworkAvailable;
    SharedPrefsHelper sp;


    ConnectivityCheck mTestConnectivity;
    public static final String MyPREFERENCE = "preference";
    private static String TAG = LoginActivity.class.getSimpleName();
    private String ot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MultiDex.install(this);
        init();

        sharedpreferences = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mTestConnectivity = new ConnectivityCheck(this);

        sp = new SharedPrefsHelper(getApplicationContext());
        if (sp.isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }

     /*   mprogressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
       // mprogressBar.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(15000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
         final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialogue);
      */

     final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
     login.setOnClickListener(new View.OnClickListener()
     {
            @Override
            public void onClick(View view)
            {
                username_str = username.getText().toString();
                password_str = password.getText().toString();
               // dialog.show();

                try
                {
                    encriptUserpass = username_str + "|||" + password_str;
                    System.out.print("cocatination of string--------" + encriptUserpass);
                    cryptography.Encrypt(encriptUserpass, _cipherKey);
                    System.out.print("encode data=================================" + cryptography.Encrypt(encriptUserpass, _cipherKey));
                    output = cryptography.Encrypt(encriptUserpass, _cipherKey);
                    System.out.print("outputLog========" + output);
                    ot = output.replaceAll("\n", "");
                    editor.putString("output", ot);
                    simpleProgressBar.setVisibility(View.VISIBLE);
                    editor.commit();

                } catch (Exception e) {

                    e.printStackTrace();
                }

                if (username_str.equals("") && password_str.equals(""))
                {
                //    mprogressBar.setVisibility(View.GONE);
                   // dialog.dismiss();
                    Snackbar.make(view, "Enter username and password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else
                {
                   if(!mTestConnectivity.isConnectedToInternet()) {

                       Toast.makeText(LoginActivity.this, "Please check network", Toast.LENGTH_SHORT).show();

                   }

                    login();
                }

            }

        });

        IntentFilter intentFilter = new IntentFilter(Receiver.NETWORK_AVAILABLE_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "Connected" : "Disconnected";
               // Toast.makeText(LoginActivity.this, "Network Status: " + networkStatus, Toast.LENGTH_SHORT).show();
                Log.i("Network",""+networkStatus);
                Snackbar.make(findViewById(R.id.activity_login), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.activity_login), "No Internet", Snackbar.LENGTH_LONG)
                        .setAction("Go Online", null).show();

            }

        }, intentFilter);

    }

    public void init()
    {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
    }

    private void login()
    {
        retrofit2.Retrofit mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://fms.terabhyte.com/FleetServices.asmx/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(initLog().build())
                .build();

        ServiceInterface mServiceApi = mRetrofit.create(ServiceInterface.class);
        retrofit2.Call<LoginResponse> mLoginCall = mServiceApi._login(ot);
        mLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginResponse> call, @NonNull retrofit2.Response<LoginResponse> response) {

                // String data = response.body();
               // Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                LoginResponse country1 = (LoginResponse) response.body();
                if (country1.getResponseType() != null) {

                 //   obj = new JSONObject(response.body());
                   responseType =  country1.getResponseType();
                  //  responseType = obj.getString("responseType");

                    System.out.print("type+++++++++++++++" + responseType);

                    //result = obj.getString("response");
                    result = country1.getResponse();
                    System.out.print("result+++++++++++++++" + result);

                    if (responseType.equalsIgnoreCase("Success")) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                        editor.putString("result", result);
                        editor.commit();

                        startActivity(i);
                        finish();
                        sp.createLoginSession(username_str,password_str);

                    } else {
                        //  mprogressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Enter correct username and password", Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {


            }

        });

        /*StringRequest postRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // response
             //   mprogressBar.setVisibility(View.GONE);

                Log.i("ResponseLog", response);
                try {
                    obj = new JSONObject(response);
                    responseType = obj.getString("responseType");

                    System.out.print("type+++++++++++++++" + responseType);

                    result = obj.getString("response");
                    System.out.print("result+++++++++++++++" + result);

                    if (responseType.equalsIgnoreCase("Success"))
                    {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                        editor.putString("result", result);

                        editor.commit();
                        startActivity(i);

                        finish();

                    } else {
                      //  mprogressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Enter correct username and password", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e)

                {
                    if (e.toString().equalsIgnoreCase("NoConnectionError"))
                        //GlobalUtils.show_ToastCenter("Please check network", context);
                    Toast.makeText(LoginActivity.this, "Please check network", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        // Log.d("Error.Response", error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stringLoginDetails", output);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest);*/

    }



    public static OkHttpClient.Builder initLog() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging).build();
        return httpClient;
    }

}
