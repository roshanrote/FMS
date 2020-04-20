package com.acquiscent.myapplication.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.acquiscent.myapplication.BuildConfig;
import com.acquiscent.myapplication.Controller.AppController;
import com.acquiscent.myapplication.R;
import com.acquiscent.myapplication.database.DbConstants;
import com.acquiscent.myapplication.database.DbOperation;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * Created by acquiscent on 10/1/17.
 */

public class JourneyDetails extends AppCompatActivity {
    SharedPreferences sharedpreferencesId;
    SharedPreferences.Editor editorId;
    public static final String MyPREFERENCEId = "preferenceId";

    EditText actual_total_km,actual_begin_km,begin_km, end_km, end_time, total_km,actual_end_km, comment, cancle_time, cancle_date, end_date;
    Spinner entered_by;
    Cryptography_Android cryptography;
    String beginkm, journeyid,BookedBy, TotalDistance, TotalEndDistance,totalBigin;
    String lat, longs, driverid,journeyid1;
    ProgressBar simpleProgressBar;
    Boolean isSynced = false;
    String begin_kmstr,mbegin_kmstr, end_kmstr,mend_kmstr, end_timestr,mend_timestr, end_datestr,mend_datestr, total_kmstr,mtotal_kmstr, commentstr,mcommentstr, cancle_timestr,mcancle_timestr, cancle_datestr,mcancle_datestr;
    String val = "", val1 = "0";
    Button submit, back;
    static String _cipherKey = "P@SSW@RD@09";
    String encriptUserpass = "";
    String actualBeginKm ;
    String output,mOutput;
    DbOperation dbOperation;
    //String url = "http://fms.terabhyte.com/FleetServices.asmx/After_Journey_Information";
    String url = "https://fms.terabhyte.com/FleetServices.asmx/After_Journey_Information";
    private int mYear, mMonth, mDay, mHour, mMinute, cYear, cMonth, cDay, cHour, cMinute;
    ConnectivityCheck mTestConnectivity;

    private static String TAG = JourneyDetails.class.getSimpleName();
    private String actual_end_kmstr,actual_total_kmstr;
    private String actualendkm ;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        Intent i = new Intent(JourneyDetails.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectNonSdkApiUsage()
                    .penaltyLog()
                    .build());
        }

        dbOperation = new DbOperation(JourneyDetails.this);
        begin_km = (EditText) findViewById(R.id.begin_km);
        simpleProgressBar = (ProgressBar)findViewById(R.id.simpleProgressBar);

        sharedpreferencesId = getSharedPreferences(MyPREFERENCEId, Context.MODE_PRIVATE);
        editorId = sharedpreferencesId.edit();
        journeyid = sharedpreferencesId.getString("journeyid", "").toString();
        journeyid1 = sharedpreferencesId.getString("journeyid2","");
        BookedBy = sharedpreferencesId.getString("BookedBy", "").toString();

        TotalDistance  =  getIntent().getExtras().getString("totalDistance");
        totalBigin = getIntent().getExtras().getString("beginkm");
        begin_km.setText("" + getIntent().getExtras().getString("beginkm"));

        double value = Double.valueOf(begin_km.getText().toString());
        double value2 = Double.valueOf(TotalDistance);
        double TotalValue = value + value2;

        end_km = (EditText) findViewById(R.id.end_km);
        TotalEndDistance = String.valueOf(new DecimalFormat("##.##").format(TotalValue));

        end_km.setText(TotalEndDistance);
        end_km.setEnabled(false);
        end_time = (EditText) findViewById(R.id.end_time);
        end_date = (EditText) findViewById(R.id.end_date);
        actual_end_km = (EditText) findViewById(R.id.actual_end_km);
        actual_begin_km = (EditText) findViewById(R.id.actual_begin_km);
        total_km = (EditText) findViewById(R.id.total_km);
        actual_total_km = (EditText) findViewById(R.id.actual_total_km);
        total_km.setText(String.valueOf(new DecimalFormat("##.##").format(value2)));
        total_km.setEnabled(false);
        comment = (EditText) findViewById(R.id.comment);
        cancle_time = (EditText) findViewById(R.id.cancle_time);
        cancle_date = (EditText) findViewById(R.id.cancle_date);
        //actualBeginKm = actual_begin_km.getText().toString();
        actual_begin_km.setText("" + getIntent().getExtras().getString("Actualbeginkm"));
        begin_kmstr = getIntent().getExtras().getString("Actualbeginkm");

        submit = (Button) findViewById(R.id.submit);
        back = (Button) findViewById(R.id.backk);

        val = getIntent().getExtras().getString("Actualbeginkm");
       // val = begin_km.getText().toString();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        val1 = TotalEndDistance;
       // val1 = end_km.getText().toString();
        mTestConnectivity = new ConnectivityCheck(this);

        //calulate();

       /* end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(JourneyDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                end_date.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year);
                                end_datestr = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        final Calendar c1 = Calendar.getInstance();
        mHour = c1.get(Calendar.HOUR_OF_DAY);
        mMinute = c1.get(Calendar.MINUTE);


        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(JourneyDetails.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                end_time.setText(hourOfDay + ":" + minute);
                                end_timestr = hourOfDay + ":" + minute;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
*/
        final Calendar c1 = Calendar.getInstance();
        mHour = c1.get(Calendar.HOUR_OF_DAY);
        mMinute = c1.get(Calendar.MINUTE);

        DateFormat df = new SimpleDateFormat("HH:mm"); //format time
        end_timestr = df.format(Calendar.getInstance().getTime());

        DateFormat df1=new SimpleDateFormat("MM/dd/yyyy");//foramt date
        end_datestr =df1.format(Calendar.getInstance().getTime());

        end_date.setText(end_datestr);
        end_time.setText(end_timestr);
        end_date.setEnabled(false);
        end_time.setEnabled(false);

        final Calendar c2 = Calendar.getInstance();
        cYear = c2.get(Calendar.YEAR);
        cMonth = c2.get(Calendar.MONTH);
        cDay = c2.get(Calendar.DAY_OF_MONTH);

        cancle_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(JourneyDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                cancle_date.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year);

                            }

                        }, cYear, cMonth, cDay);

                datePickerDialog.show();
            }
        });


        final Calendar c3 = Calendar.getInstance();
        cHour = c3.get(Calendar.HOUR_OF_DAY);
        cMinute = c3.get(Calendar.MINUTE);

        cancle_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(JourneyDetails.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                cancle_time.setText(hourOfDay + ":" + minute);

                            }

                        }, cHour, cMinute, false);
                timePickerDialog.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   begin_kmstr = begin_km.getText().toString();
                //end_kmstr = end_km.getText().toString();
                end_kmstr = end_km.getText().toString();
                total_kmstr = total_km.getText().toString();
                commentstr = comment.getText().toString();
                cancle_timestr = cancle_time.getText().toString();
                cancle_datestr = cancle_date.getText().toString();
                actual_end_kmstr = actual_end_km.getText().toString();
                actual_total_kmstr = actual_total_km.getText().toString();

                try {
                   // encriptUserpass = journeyid1 + "|||" + begin_kmstr  + "|||" + TotalEndDistance + "|||" + end_kmstr + "|||" + end_timestr + "|||" + end_datestr + "|||" + cancle_timestr + "|||" + cancle_datestr + "|||" + commentstr + "|||" + BookedBy + "|||" + actual_end_kmstr + "|||" + actual_total_kmstr + "|||" + actual_begin_km.getText().toString();
                    encriptUserpass = journeyid1 + "|||" + totalBigin  + "|||" + TotalEndDistance + "|||" + TotalEndDistance + "|||" + end_timestr + "|||" + end_datestr + "|||" + cancle_timestr + "|||" + cancle_datestr + "|||" + commentstr + "|||" + BookedBy + "|||" + actual_end_kmstr + "|||" + actual_total_kmstr + "|||" + actual_begin_km.getText().toString();
                    System.out.print("cocatination of Journy string--------" + encriptUserpass);
                    cryptography.Encrypt(encriptUserpass, _cipherKey);
                    System.out.print("Jencode data=================================" + cryptography.Encrypt(encriptUserpass, _cipherKey));
                    Log.i("jrnycoded data", "" + cryptography.Encrypt(encriptUserpass, _cipherKey));
                    output = cryptography.Encrypt(encriptUserpass, _cipherKey);
                    mOutput = output.replaceAll("\n","");
                    System.out.print("output journy========" + mOutput);
                    Log.i("jrnycoded data", "" + mOutput);

                } catch (Exception e) {

                    e.printStackTrace();

                }

                if (begin_kmstr.equals("")) {

                    Snackbar.make(view, "Enter Begin km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if (actual_begin_km.getText().toString().equals("")) {

                    Snackbar.make(view, "Enter actual Begin km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else if (end_kmstr.equals("")) {
                    Snackbar.make(view, "Enter End km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else if (actual_end_kmstr.equals("")) {
                    Snackbar.make(view, "Enter Actual End km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else if (Integer.valueOf(actual_end_kmstr) < Integer.valueOf(actual_begin_km.getText().toString())) {
                    Snackbar.make(view, "Actual End km Should be greater than actual Begin km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }  else if (total_kmstr.equals("")) {
                    Snackbar.make(view, "Enter total km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (actual_total_kmstr.equals("")) {
                    Snackbar.make(view, "Enter Actual total km..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (commentstr.equals("")) {
                    Snackbar.make(view, "Write the comment..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
               /* else if (cancle_timestr.equals(""))
                {
                    Snackbar.make(view, "Insert The Time", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if (cancle_datestr.equals(""))
                {
                    Snackbar.make(view, "Insert The date..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }*/
                else {

                    simpleProgressBar.setVisibility(View.VISIBLE);
                    if(mTestConnectivity.isConnectedToInternet()) {

                        getData();

                    }
                    else {

                        long resultNew = 0;
                        dbOperation.OPEN();
                        resultNew = dbOperation.addJourneyDetailsOfflineData(journeyid1,mOutput,"0");
                        dbOperation.addJourneyDetailsOfflineDataforList(journeyid1,mOutput,"0");

                        if (resultNew > 0)
                        {
                            Intent i = new Intent(JourneyDetails.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                        dbOperation.CLOSE();
                    }

                    //sqlliteDataSave();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JourneyDetails.this, MapsActivity.class);
                startActivity(i);
                finish();

            }
        });

        end_km.addTextChangedListener(passwordWatcher);
        actual_end_km.addTextChangedListener(actualEndKmWatcher);
       // actual_begin_km.addTextChangedListener(actualEndKmWatcher);

    }


    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
               // textView.setVisibility(View.GONE);
            } else{
                val1 = end_km.getText().toString();
                int valuee = Integer.parseInt(val1) - Integer.parseInt(val);
                String vall1 = valueOf(valuee);
                total_km.setText(vall1);
                total_km.setEnabled(false);
                //textView.setText("You have entered : " + passwordEditText.getText());
            }
        }
    };

    private final TextWatcher actualEndKmWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (actual_begin_km.getText().toString().trim().length()== 0)
            {
                Toast.makeText(JourneyDetails.this, "Enter Actual begin km First",Toast.LENGTH_LONG).show();
            }
        }

        public void afterTextChanged(Editable s) {
            if (actual_begin_km.getText().toString().trim().length()!= 0 && actual_end_km.getText().toString().trim().length() > 0)
            {
                int a= Integer.parseInt(actual_begin_km.getText().toString().trim());
                int b= Integer.parseInt(actual_end_km.getText().toString().trim());
                actual_total_km.setText(valueOf(b-a));
            }
        }

    };


    private void totalKm() {

        val1 = end_km.getText().toString();
        int valuee = Integer.parseInt(val1) - Integer.parseInt(val);
        String vall1 = valueOf(valuee);
        total_km.setText(vall1);
    }

    private void calulate() {

        int result = 0;

         //result = Integer.parseInt(val1) - Integer.parseInt(val);
       // String vall1 = valueOf(valuee);
      //  total_km.setText(vall1);

        // If statements to determine highest value
        if(!val.equals("")) {
            if (Integer.parseInt(val) <= Integer.parseInt(val1)) {
                result = (Integer.parseInt(val1) - Integer.parseInt(val));
            }
            if (Integer.parseInt(val) >= Integer.parseInt(val1)) {
                result = (Integer.parseInt(val) - Integer.parseInt(val1));
            }
            String vall1 = valueOf(result);
            total_km.setText(vall1);
            // Print our result to console
            System.out.println(result);
        }
    }

    private void getData() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.i("ResponseJrny", response);
                System.out.print("ResponseJrny" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String responseType = obj.getString("responseType");

                    System.out.print("type+++++++++++++++" + responseType);

                    String result1 = obj.getString("response");
                    System.out.print("result+++++++++++++++" + result1);

                    if (responseType.equalsIgnoreCase("Success")) {

                        Intent i = new Intent(JourneyDetails.this, MainActivity.class);
                        simpleProgressBar.setVisibility(View.GONE);
                        /*editor.putString("result", result);
                        editor.commit();*/
                        startActivity(i);
                        // Toast.makeText(JourneyDetails.this, "Journey Details Added Succesfully", Toast.LENGTH_SHORT).show();
                        finish();
                        dbOperation.updateNewOrStartesJourney("false",journeyid1);
                        deleteItems();
                        dbOperation.createTable();
                        dbOperation.CLOSE();

                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                         Log.d("Error.ResponseJ", error.getMessage());

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("stringDetailsKey", mOutput);
                System.out.print("stringDetailsKey========" + mOutput);
                return params;

              }

        };

        AppController.getInstance().addToRequestQueue(postRequest);

    }


    private void deleteItems() {
        dbOperation.OPEN();
        dbOperation.clearDatabase();
    }

}
