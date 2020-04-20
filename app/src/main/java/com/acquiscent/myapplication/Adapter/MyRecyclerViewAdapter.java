package com.acquiscent.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acquiscent.myapplication.Activity.MainActivity;
import com.acquiscent.myapplication.Activity.MapsActivity;
import com.acquiscent.myapplication.Activity.StartPageJourney;
import com.acquiscent.myapplication.Model.Data;
import com.acquiscent.myapplication.Model.DataObject;
import com.acquiscent.myapplication.Model.JourneyStatus;
import com.acquiscent.myapplication.R;
import com.acquiscent.myapplication.database.DbOperation;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by acquiscent on 9/1/17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    String lat;
    String logg;
    List<Data> getDataAdapter;
    DbOperation dbOperation;

    static String journey_id, userId;
    static String lat_id, lod_id;
    SharedPreferences sharedpreferencesId;
    static SharedPreferences.Editor editorId;
    private static final String MyPREFERENCEId = "preferenceId";
    Boolean status = false;

    public MyRecyclerViewAdapter(Context context1, List<Data> getDataAdapter) {
        this.getDataAdapter = getDataAdapter;
        this.context = context1;

        sharedpreferencesId = context.getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE);
        editorId = sharedpreferencesId.edit();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {

        this.myClickListener = myClickListener;

    }


    @Override
    public long getItemId(int position) {

        return super.getItemId(position);

    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);

        return new DataObjectHolder(view);

    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        final Data listName = getDataAdapter.get(position);

        // lat=getDataAdapter.get(position).getLati();

        //logg=getDataAdapter.get(position).getLongi();
        dbOperation = new DbOperation(context);
        lat = getDataAdapter.get(position).getLati();
        logg = getDataAdapter.get(position).getLongi();
        journey_id = String.valueOf(getDataAdapter.get(position).getJourney_id());
        holder.passenger.setText(getDataAdapter.get(position).getPassenger());
        holder.vehicletype.setText(getDataAdapter.get(position).getVehicleType());
        holder.bookdate.setText(getDataAdapter.get(position).getBookDate());
        holder.bookedby.setText(getDataAdapter.get(position).getBookedBy());

        holder.startdate.setText(getDataAdapter.get(position).getJourneyStartDate());
        holder.enddate.setText(getDataAdapter.get(position).getJourneyEndDate());
        holder.destinationcode.setText(getDataAdapter.get(position).getDestinationCode());
        holder.endtime.setText(getDataAdapter.get(position).getProposedEndTime());
        holder.bookRef.setText(getDataAdapter.get(position).getBookingRef());


        if (getDataAdapter.get(position).getIsPaused() != null) {
            if (getDataAdapter.get(position).getIsPaused().equals("null")) {
                holder.IsPaused.setText("No");
            } else if (getDataAdapter.get(position).getIsPaused().equals("true")) {
                holder.IsPaused.setText("Yes");
            }
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, StartPageJourney.class);

            /*editorId.putString("journeyid","2");
            System.out.print("journey id============="+journey_id);
            editorId.putString("driverid","2");
            System.out.print("driver id============="+userId);*/

           /* editorId.putString("Latitude",lat) ;
            System.out.print("Lat id============="+lat_id);driverid
            editorId.putString("Longitude", logg) ;
            System.out.print("Log id============="+lod_id);
            Toast.makeText(context,"user id"+userId+" "+"journey id"+journey_id,Toast.LENGTH_SHORT).show();
*/
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("Latitude", listName.getLati()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("Longitude", listName.getLongi()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("journeyid", "2").apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("journeyid2", listName.getJourney_id()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("BookedBy", listName.getBookedBy()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("deviceId", listName.getDeviceID()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("driverID", listName.getDriverID()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("BeginKm", listName.getBeginKm()).apply();
                context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("BookingRef", listName.getBookingRef()).apply();

                //context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("BeginKm", "100").apply();
                /*editorId.commit();*/


                dbOperation.OPEN();
                Boolean value = dbOperation.checkForTableExists();
                if (value == true) {

                    Boolean recordExist = dbOperation.CheckIsDataAlreadyInDBorNot(listName.getJourney_id());

                    if (recordExist == true) {


                        JourneyStatus journeyStatus = dbOperation.getJourneyStartedOrNew(listName.getJourney_id());
                        String status = journeyStatus.getStatus();
                        String ActualBeginKm = journeyStatus.getActualBeginKm();

                        if (status.equals("true")) {

                            Intent i1 = new Intent(context, MapsActivity.class);
                            i1.putExtra("beginkm",listName.getBeginKm());
                            i1.putExtra("Actualbeginkm",ActualBeginKm);
                            //context.getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("ActualBeginKm", ActualBeginKm).apply();

                            context.startActivity(i1);
                        }else {

                            context.startActivity(i);
                        }

                    } else {

                        context.startActivity(i);

                    }

                } else {

                    context.startActivity(i);
                  //  dbOperation.addJourneyStartedorNot(listName.getJourney_id(), "true");

                }

                ((Activity) context).finish();
                 dbOperation.CLOSE();
                //context.notifyAll();
            }

        });

    }

    class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView passenger, bookedby, bookdate, bookRef, IsPaused;
        TextView vehicletype;
        TextView startdate;
        TextView enddate;
        TextView destinationcode;
        TextView endtime;
        CardView mCardView;

        DataObjectHolder(View itemView) {
            super(itemView);

            passenger = (TextView) itemView.findViewById(R.id.passenger);
            bookedby = (TextView) itemView.findViewById(R.id.bookedby);
            bookdate = (TextView) itemView.findViewById(R.id.bookdate);
            vehicletype = (TextView) itemView.findViewById(R.id.vehicletype);
            startdate = (TextView) itemView.findViewById(R.id.startdate);
            enddate = (TextView) itemView.findViewById(R.id.enddate);
            destinationcode = (TextView) itemView.findViewById(R.id.destinationcode);
            endtime = (TextView) itemView.findViewById(R.id.endtime);
            bookRef = (TextView) itemView.findViewById(R.id.bookRef);
            IsPaused = (TextView) itemView.findViewById(R.id.status);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            Log.i(LOG_TAG, "Adding Listener");
        }

    }

    public void addItem(Data dataObj, int index) {
        getDataAdapter.add(index, dataObj);

        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        getDataAdapter.remove(index);

        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return getDataAdapter.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


    public void updateList(List<Data> list) {
        getDataAdapter = list;
        notifyDataSetChanged();
    }

}
