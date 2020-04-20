package com.acquiscent.myapplication.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acquiscent.myapplication.Controller.AppController;
import com.acquiscent.myapplication.R;
import com.acquiscent.myapplication.database.DbOperation;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by acquiscent on 11/1/17.
 */

public class StartPageJourney extends AppCompatActivity {

    Button start, Back, cancel1;
    EditText begin_km, driver_id, journey_id;
    String beginkm, driverid, journeyid;
    DbOperation dbOperation;

    //String url = "http://fms.terabhyte.com/FleetServices.asmx/IsJourneystatus";
    String url = "https://fms.terabhyte.com/FleetServices.asmx/IsJourneystatus";
    String url1 = "http://fms.terabhyte.com/FleetServices.asmx/CancelJourney";

    SharedPreferences sharedpreferencesId;
    SharedPreferences.Editor editorId;
    public static final String MyPREFERENCEId = "preferenceId";

    String responseType = "", result = "";
    JSONObject obj;
    Cryptography_Android cryptography;
    static String _cipherKey = "P@SSW@RD@09";
    String encriptString = "", output;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        Intent i = new Intent(StartPageJourney.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        dbOperation = new DbOperation(StartPageJourney.this);
        begin_km = (EditText) findViewById(R.id.begin_km);

        sharedpreferencesId = getSharedPreferences(MyPREFERENCEId, Context.MODE_PRIVATE);
        editorId = sharedpreferencesId.edit();

        journeyid = sharedpreferencesId.getString("journeyid2", "");
        beginkm = sharedpreferencesId.getString("BeginKm", "");
        System.out.print("journey id-----------" + journeyid);
        try {

            encriptString = journeyid + "|||" + "true";
            System.out.print("cocatination of string--------" + encriptString);
            cryptography.Encrypt(encriptString, _cipherKey);
            System.out.print("encode data=================================" + cryptography.Encrypt(encriptString, _cipherKey));
            output = cryptography.Encrypt(encriptString, _cipherKey);
            System.out.print("outputJourny==================================" + output);
            getData();

        } catch (Exception e) {

            e.printStackTrace();
        }

        start = (Button) findViewById(R.id.start);
        Back = (Button) findViewById(R.id.back);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start.setTextColor(Color.GRAY);

                if (begin_km.getText().toString().isEmpty()) {
                    //Toast.makeText(StartPageJourney.this, "Enter Begin Km", Toast.LENGTH_SHORT).show();

                    Snackbar.make(view, "Enter Actual Begin Km", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    start.setTextColor(Color.WHITE);
                    //  Snackbar.make(view, "Enter username and password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    dbOperation.OPEN();
                    dbOperation.addJourneyStartedorNot(sharedpreferencesId.getString("journeyid2", ""), "true",begin_km.getText().toString());
                    Intent i = new Intent(StartPageJourney.this, MapsActivity.class);
                    i.putExtra("beginkm", beginkm);
                 /*   editorId.putString("ActualBeginKm", begin_km.getText().toString());
                    editorId.commit();*/
                    i.putExtra("Actualbeginkm", begin_km.getText().toString());
                    i.putExtra("journeyid2", sharedpreferencesId.getString("journeyid2", ""));
                    i.putExtra("BookedBy", sharedpreferencesId.getString("BookedBy", ""));
                    startActivity(i);
                    getData();
                    finish();
                    dbOperation.CLOSE();
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(StartPageJourney.this, MainActivity.class);
                startActivity(i);

            }
        });


     /*   cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog openDialog = new Dialog(StartPageJourney.this);
                openDialog.setContentView(R.layout.custom_cancel_jrny);
                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.text);
                Button YesButton = (Button) openDialog.findViewById(R.id.yes);
                Button NoButton = (Button) openDialog.findViewById(R.id.no);

                YesButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        getDataCancel();
                        //finish();
                        openDialog.dismiss();
                    }

                });

                NoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog.dismiss();
                    }
                });
                openDialog.show();

            }
        });*/

    }


    private void getData() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                //  Log.d("Error.Response", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stringBookingDetails", output);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void getDataCancel() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("ResponseCn", response);
                //Toast.makeText(StartPageJourney.this, "cancels data", Toast.LENGTH_SHORT).show();
                try {
                    obj = new JSONObject(response);
                    responseType = obj.getString("responseType");

                    System.out.print("type+++++++++++++++" + responseType);

                    result = obj.getString("response");
                    System.out.print("result+++++++++++++++" + result);

                    if (responseType.equalsIgnoreCase("Success")) {
                        finish();
                        Intent i = new Intent(StartPageJourney.this, MainActivity.class);
                        startActivity(i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("journeyid", journeyid);
                Log.i("jrnyid", "" + journeyid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }


}
