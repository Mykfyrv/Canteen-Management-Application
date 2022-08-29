package com.example.navdrawertest10;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter_Admin_Inventory_Items extends RecyclerView.Adapter<MyAdapter_Admin_Inventory_Items.MyViewHolderInvent> implements Filterable {
    private AdmininventoryInvetoryFragment activity;
    private List<ModelItems> ImList;
    private List<ModelItems> ImListFull;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    class MyViewHolderInvent extends RecyclerView.ViewHolder{
        TextView itemname;
        TextView itemquantity;
        TextView date;

        public MyViewHolderInvent(@NonNull View itemView) {
            super(itemView);

            itemname = itemView.findViewById(R.id.Itemname_text);
            itemquantity = itemView.findViewById(R.id.Itemquantity_text);
            date = itemView.findViewById(R.id.Date_text);
        }
    }
    MyAdapter_Admin_Inventory_Items(AdmininventoryInvetoryFragment activity, List<ModelItems> ImList){
        this.activity = activity;
        this.ImList = ImList;
        this.ImListFull = new ArrayList<>(ImList);
    }

    public void updateData(int position){
        ModelItems item = ImList.get(position);
        Bundle inventbundle = new Bundle();
        inventbundle.putString("uId", item.getId());
        inventbundle.putString("uItemname", item.getItemname());
        inventbundle.putString("uItemquantity", item.getItemquantity());
        inventbundle.putString("uDate", item.getDate());
        Intent intent = new Intent(activity.getActivity(), AdmininventoryUpdateItemsAct.class);
        intent.putExtras(inventbundle);
        activity.getActivity().startActivity(intent);
    }

    public void deleteData(int position){
        ModelItems item = ImList.get(position);
        db.collection("inventory").document(item.getItemname()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getActivity(), "Data Deleted!!",Toast.LENGTH_SHORT).show();
                    activity.showInventorydata();
                }else{
                    Toast.makeText(activity.getActivity(), "Error !!" + task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @NonNull
    @Override
    public MyViewHolderInvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_canteen_inventory, parent , false);
        return  new MyViewHolderInvent(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderInvent holder, int position) {
        holder.itemname.setText(ImList.get(position).getItemname());
        holder.itemquantity.setText(ImList.get(position).getItemquantity());
        holder.date.setText(ImList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return ImList.size();
    }
    @Override
    public Filter getFilter() {
        return inventoryFilter;
    }
    private Filter inventoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelItems> inventfilteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                inventfilteredList.addAll(ImListFull);
            }else {
                String filterPatern = constraint.toString().toLowerCase().trim();

                for (ModelItems item : ImListFull){
                    if(item.getItemname().toLowerCase().contains(filterPatern)){
                        inventfilteredList.add(item);
                    }
                }
            }
            FilterResults inventresults = new FilterResults();
            inventresults.values = inventfilteredList;

            return inventresults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults inventresults) {
            ImList.clear();
            ImList.addAll((List) inventresults.values);
            notifyDataSetChanged();
        }
    };


}
