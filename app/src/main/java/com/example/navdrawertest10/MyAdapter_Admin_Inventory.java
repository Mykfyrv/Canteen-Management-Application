package com.example.navdrawertest10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter_Admin_Inventory extends RecyclerView.Adapter<MyAdapter_Admin_Inventory.AdminInventViewHolder> implements Filterable {


    private List<ModelItems> ImList;
    private List<ModelItems> ImListFull;



    class AdminInventViewHolder extends RecyclerView.ViewHolder{
        TextView itemname;
        TextView itemquantity;
        TextView date;

        public AdminInventViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.Itemname_text);
            itemquantity = itemView.findViewById(R.id.Itemquantity_text);
            date = itemView.findViewById(R.id.Date_text);
        }
    }
    MyAdapter_Admin_Inventory(List<ModelItems> ImList) {
        this.ImList = ImList;
        this.ImListFull = new ArrayList<>(ImList);
    }


    @NonNull
    @Override
    public AdminInventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_canteen_inventory, parent , false);
        return  new AdminInventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminInventViewHolder holder, int position) {
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
