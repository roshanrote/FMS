package com.acquiscent.myapplication.Adapter;

/**
 * Created by acquiscent on 19/11/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acquiscent.myapplication.Model.Data;
import com.acquiscent.myapplication.R;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Data> getDataAdapter;
    String from = "";
    View v;

    SharedPreferences sharedpreferencesContact;
    SharedPreferences.Editor editorContact;
    public static final String MyPREFERENCEContact = "preferenceContactType";


    public RecyclerViewAdapter(List<Data> getDataAdapter, Context context) {

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;


        sharedpreferencesContact = context.getSharedPreferences(MyPREFERENCEContact, Context.MODE_PRIVATE);
        editorContact = sharedpreferencesContact.edit();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Data listName = getDataAdapter.get(position);

        holder.setIsRecyclable(false);

        holder.NameTextView.setText(listName.getPassenger());


//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(getDataAdapter.get(position).getContact_type().equalsIgnoreCase("AOG")){
//
//                    Intent i=new Intent(context,AOGActivity.class);
//                    context.startActivity(i);
//                    ((Activity) context).finish();
//
//                }else {
//                    Intent i = new Intent(context, AllHomePageActivity.class);
//                    editorContact.putString("whichclick", getDataAdapter.get(position).getContact_type());
//                    editorContact.putString("searchData", "");
//                    editorContact.commit();
//                    i.putExtra("from", from);
//                    System.out.print("contact type:::::::::::::::::::::" + getDataAdapter.get(position).getContact_type());
//                    context.startActivity(i);
//                    ((Activity) context).finish();
//                }
//
//            }
//        });

    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public TextView NameTextView;


        public ViewHolder(View itemView) {

            super(itemView);

            NameTextView = (TextView) itemView.findViewById(R.id.bookdate);


        }


    }
}
