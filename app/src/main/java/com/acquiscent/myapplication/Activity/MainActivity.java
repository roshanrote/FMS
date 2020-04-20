package com.acquiscent.myapplication.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acquiscent.myapplication.Adapter.MyRecyclerViewAdapter;
import com.acquiscent.myapplication.Controller.AppController;
import com.acquiscent.myapplication.Model.Data;
import com.acquiscent.myapplication.Model.DataObject;
import com.acquiscent.myapplication.Model.JourneyModel;
import com.acquiscent.myapplication.Model.LatDataModel;
import com.acquiscent.myapplication.Model.LoginResponse;
import com.acquiscent.myapplication.R;
import com.acquiscent.myapplication.database.DbConstants;
import com.acquiscent.myapplication.database.DbOperation;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String login_url = "https://fms.terabhyte.com/FleetServices.asmx/IsLogin";
    String  responseType = "", output = "";
    JSONObject obj;

    ConnectivityCheck mTestConnectivity;
    private RecyclerView mRecyclerView;
    private Button logou,menu;
    private MyRecyclerViewAdapter mAdapter;
    EditText editext;
    private static String LOG_TAG = "CardViewActivity";
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPrefsHelper sp;
    DbOperation dbOperation;

    Button button,logout;
    TextView sync;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCE = "preference";
    String result = "",trackEnd = "track",track = "0";
    JSONObject object;
    String userId;
    List<Data> list;
    private String data;
    ArrayList<JourneyModel> arrJourneyDetailModel = new ArrayList<>();
    ArrayList<JourneyModel> arrJourneyDetailModelList = new ArrayList<>();
    ArrayList<LatDataModel> arrLatLongDetailModel = new ArrayList<>();
    private List<Data> listNotNet;
    private List<Data> listPaused;
    private List<Data> listUnPaused;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        Log.i("called","->");
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
       // updateMenuTitles(menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pauseed) {
            paused();
            return true;
        }

        if (id == R.id.unpaused) {
            unpaused();
            return true;
        }

        if (id == R.id.none) {
            none();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void none() {
        mAdapter = new MyRecyclerViewAdapter(MainActivity.this, list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        notifi();
    }

    private void unpaused() {

        listUnPaused = new ArrayList<>();
        listUnPaused.addAll(list);

        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getIsPaused().equals("true"))
            {
                listUnPaused.remove(list.get(i));

            }
        }

        mAdapter = new MyRecyclerViewAdapter(MainActivity.this, listUnPaused);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        notifi();
    }

    private void paused() {

        listPaused = new ArrayList<>();
        listPaused.addAll(list);

        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getIsPaused().equals("null"))
            {
                listPaused.remove(list.get(i));

            }
        }
        mAdapter = new MyRecyclerViewAdapter(MainActivity.this, listPaused);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        notifi();
    }

   /* private void updateMenuTitles(Menu menu) {
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setTitle(res.getString(R.string.action_settings));
        MenuItem exit = menu.findItem(R.id.action_exit);
        exit.setTitle(res.getString(R.string.action_exit));

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logout=(Button) toolbar.findViewById(R.id.logout);
        sync =(TextView) toolbar.findViewById(R.id.sync);

       // menu =(Button) toolbar.findViewById(R.id.menu);

        sp = new SharedPrefsHelper(getApplicationContext());
        sp.checkLogin();
        sharedpreferences = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mTestConnectivity = new ConnectivityCheck(this);
        result = sharedpreferences.getString("result", "");
        output = sharedpreferences.getString("output", "");
        trackEnd = sharedpreferences.getString("trackEnd", "");

        System.out.print("result-----------" + result);
        dbOperation = new DbOperation(MainActivity.this);

                init();

            if(trackEnd.equals("trackEnd")) {
                init();
            }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog openDialog = new Dialog(MainActivity.this);
                openDialog.setContentView(R.layout.custom_logout);
                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.text);
                Button YesButton = (Button) openDialog.findViewById(R.id.yes);
                Button NoButton = (Button) openDialog.findViewById(R.id.no);

                YesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        sharedpreferences.edit().clear().commit();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sp.setBoolianValues("isLogedIn", false);
                        sp.logoutUser();
                        startActivity(intent);
                        finish();
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
        });


        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mTestConnectivity.isConnectedToInternet()) {

                    arrJourneyDetailModel = dbOperation.getNotUploadedJourneyDetails();
                    if (arrJourneyDetailModel.size() > 0) {
                        uploadJourneyOfflineDataFirst();
                    }

                    arrLatLongDetailModel = dbOperation.getNotUploadedLatLongDetails();
                    if (arrLatLongDetailModel.size() > 0) {
                        uploadLatLongOfflineData();
                    }

                    init();
                }
            }
        });

        editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

    }

    private void uploadLatLongOfflineData() {

        final LatDataModel logDetailModel = arrLatLongDetailModel.get(0);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://fms.terabhyte.com/_trackingService.asmx/SaveNewVechileLocation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                try {
                    JSONObject obj = new JSONObject(response);

                    if (responseType.equalsIgnoreCase("Success")) {

                        LatDataModel model = arrLatLongDetailModel.get(0);
                        model.setSendToserverLatLong("1");
                        String id = model.getId();
                        dbOperation.updateLatLong(model ,id);
                        arrLatLongDetailModel.remove(0);
                        if (arrLatLongDetailModel.size() > 0) {

                            uploadLatLongOfflineData();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Lat Long Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("locationString", logDetailModel.getLatLongData());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest);

    }

    void filter(String text){

        List<Data> temp = new ArrayList();
        if(list !=null) {
            for (Data d : list) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getJourney_id().contains(text)) {
                    temp.add(d);
                }
            }
            //update recyclerview
            mAdapter.updateList(temp);
        }
    }

    private void uploadJourneyOfflineDataFirst() {

        final JourneyModel logDetailModel = arrJourneyDetailModel.get(0);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://fms.terabhyte.com/FleetServices.asmx/After_Journey_Information", new Response.Listener<String>() {
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

                        JourneyModel model = arrJourneyDetailModel.get(0);
                        model.setSendToserverJD("1");
                        dbOperation.updateLog(model);
                        arrJourneyDetailModel.remove(0);
                        if (arrJourneyDetailModel.size() > 0) {

                            uploadJourneyOfflineDataFirst();

                        } else {

                            Toast.makeText(MainActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stringDetailsKey", logDetailModel.getOffLineJDData());
                System.out.print("stringDetailsKey========" + logDetailModel.getOffLineJDData());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(postRequest);
    }


    public void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        logou = (Button) findViewById(R.id.logou);
        editext = (EditText) findViewById(R.id.editext);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        retrofit2.Retrofit mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://fms.terabhyte.com/FleetServices.asmx/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(initLog().build())
                .build();

        ServiceInterface mServiceApi = mRetrofit.create(ServiceInterface.class);
        retrofit2.Call<LoginResponse> mLoginCall = mServiceApi._login(output);
        mLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginResponse> call, @NonNull retrofit2.Response<LoginResponse> response) {
                list = new ArrayList<>();
                Data listname = new Data();
                // String data = response.body();
            //    Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

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
                        long result1 = 0;
                        dbOperation.OPEN();
                        result1 = dbOperation.addLoginData(result);
                        Log.d("result1",String.valueOf(result1));
                        Cursor cursor = dbOperation.getLoginData();

                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {

                                data = cursor.getString(cursor.getColumnIndex(DbConstants.dataLogin));
                                Log.d("dataLoginDetails",data);

                            }
                            while (cursor.moveToNext()) ;
                        }

                        dbOperation.CLOSE();
                        editor.putString("result", result);
                        editor.commit();
                        try {

                            object = new JSONObject(result);
                            System.out.print("result------------------" + userId);
                            JSONArray array = object.getJSONArray("BookingList");

                        /*JSONObject userObject = object.getJSONObject("loginDetails");
                          userId =journeyid String.valueOf(userObject.getInt("userId"));
                          System.out.print("user info-------------------" + userId);*/

                        for (int i = 0; i < array.length(); i++) {

                            listname = new Data();
                            JSONObject objectjson = (JSONObject) array.get(i);
                            listname.setJourney_id(objectjson.getString("ID"));
                            listname.setVehicleType(objectjson.getString("VehicleType"));
                            listname.setBookDate(objectjson.getString("BookDate"));
                            listname.setBookedBy(objectjson.getString("BookedBy"));
                            listname.setPassenger(objectjson.getString("Passenger"));
                            Log.d("latandlonginfo", (objectjson.getString("Latitude") + objectjson.getString("Longitude")));
                            listname.setJourneyStartDate(objectjson.getString("JourneyStartDate"));
                            listname.setJourneyEndDate(objectjson.getString("JourneyEndDate"));
                            listname.setDestinationCode(objectjson.getString("DestinationCode"));
                            listname.setProposedEndTime(objectjson.getString("ProposedEndTime"));
                            listname.setLati(objectjson.getString("Latitude"));
                            listname.setLongi(objectjson.getString("Longitude"));
                            listname.setDeviceID(objectjson.getString("DeviceID"));
                            listname.setDriverID(objectjson.getString("DriverID"));
                            listname.setBookingRef(objectjson.getString("BookingRef"));
                            listname.setBeginKm(objectjson.getString("BeginKm"));
                            listname.setIsPaused(objectjson.getString("IsPaused"));
                            list.add(listname);
                        }

                        mAdapter = new MyRecyclerViewAdapter(MainActivity.this, list);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        notifi();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        Toast.makeText(MainActivity.this, "Enter correct username and password", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

            }

        });

        if(!mTestConnectivity.isConnectedToInternet()) {

            Data listname = new Data();
            list = new ArrayList<>();
            listNotNet = new ArrayList<>();
            long result1 = 0;

            dbOperation.OPEN();
            result1 = dbOperation.addLoginData(result);
            Log.d("result1", String.valueOf(result1));
            Cursor cursor = dbOperation.getLoginData();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                 do {

                    data = cursor.getString(cursor.getColumnIndex(DbConstants.dataLogin));
                    Log.d("dataLoginDetails", data);

                } while (cursor.moveToNext());

            }

            dbOperation.CLOSE();
            editor.putString("result", result);
            editor.commit();
            try {
                    object = new JSONObject(data);
                    System.out.print("result------------------" + userId);
                    JSONArray array = object.getJSONArray("BookingList");
            /*JSONObject userObject = object.getJSONObject("loginDetails");
            userId =journeyid String.valueOf(userObject.getInt("userId"));
            System.out.print("user info-------------------" + userId);*/
            for (int i = 0; i < array.length(); i++) {

                    listname = new Data();
                    JSONObject objectjson = (JSONObject) array.get(i);
                    listname.setJourney_id(objectjson.getString("ID"));
                    listname.setVehicleType(objectjson.getString("VehicleType"));
                    listname.setBookDate(objectjson.getString("BookDate"));
                    listname.setBookedBy(objectjson.getString("BookedBy"));
                    listname.setPassenger(objectjson.getString("Passenger"));
                    Log.d("latandlonginfo", (objectjson.getString("Latitude") + objectjson.getString("Longitude")));
                    listname.setJourneyStartDate(objectjson.getString("JourneyStartDate"));
                    listname.setJourneyEndDate(objectjson.getString("JourneyEndDate"));
                    listname.setDestinationCode(objectjson.getString("DestinationCode"));
                    listname.setProposedEndTime(objectjson.getString("ProposedEndTime"));
                    listname.setLati(objectjson.getString("Latitude"));
                    listname.setLongi(objectjson.getString("Longitude"));
                    listname.setDeviceID(objectjson.getString("DeviceID"));
                    listname.setDriverID(objectjson.getString("DriverID"));
                    listname.setBookingRef(objectjson.getString("BookingRef"));
                    listname.setBeginKm(objectjson.getString("BeginKm"));
                    list.add(listname);
                }

                arrJourneyDetailModelList = dbOperation.getNotUploadedJourneyDetails();
                if (arrJourneyDetailModelList.size() > 0) {
                    listNotNet.addAll(list);
                    refreshList();
                    mAdapter = new MyRecyclerViewAdapter(MainActivity.this, listNotNet);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    notifi();
                }
                else {

                    mAdapter = new MyRecyclerViewAdapter(MainActivity.this, list);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    notifi();
                    Toast.makeText(MainActivity.this, "Please check network", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }


    private void refreshList() {

        final JourneyModel logDetailModel = arrJourneyDetailModelList.get(0);
        for (int i = 0; i < list.size(); i++)
        {
            if(logDetailModel.getJournyIdJD().contains(list.get(i).getJourney_id()))
            {
                listNotNet.remove(list.get(i));

            }
        }
        arrJourneyDetailModelList.remove(0);

        if (arrJourneyDetailModelList.size() > 0) {
            refreshList();
        }
    }

    public  void notifi() {
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<DataObject> getDataSet() {

        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("User Name" + index,
                    "Date " + index, "Donate to....." + index);
            results.add(index, obj);
        }
        return results;

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
