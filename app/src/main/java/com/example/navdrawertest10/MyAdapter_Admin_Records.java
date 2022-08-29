package com.example.navdrawertest10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter_Admin_Records extends RecyclerView.Adapter<MyAdapter_Admin_Records.MyViewHolder> {

    private ArrayList<ModelSalesRecord> mList;
    private Context context;

    public MyAdapter_Admin_Records(Context context, ArrayList<ModelSalesRecord> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_admin_records, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelSalesRecord model = mList. get(position);
        holder.salesRecord.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView salesRecord;
        Button viewProceed;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            salesRecord = itemView.findViewById(R.id.recordDate);
            viewProceed = itemView.findViewById(R.id.buttonView);


            viewProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle dateBundle = new Bundle();
                    dateBundle.putString("date", salesRecord.getText().toString());
                    Fragment fragment = new AdminSalesRecordFragment();
                    fragment.setArguments(dateBundle);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            });

        }
    }

}
