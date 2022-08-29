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

public class MyAdapter_Admin_Accounts extends RecyclerView.Adapter<MyAdapter_Admin_Accounts.MyViewHolder> implements Filterable {
    private AdminAccountsFragment activity;
    private List<ModelAccounts> mList;
    private List<ModelAccounts> mListFull;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.Username_text);
        }
    }

    MyAdapter_Admin_Accounts(AdminAccountsFragment activity, List<ModelAccounts> mList){
        this.activity = activity;
        this.mList = mList;
        this.mListFull = new ArrayList<>(mList);
    }

    public void updateData(int position){
        ModelAccounts item =mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId", item.getId());
        bundle.putString("uUsername", item.getUsername());
        bundle.putString("uPassword", item.getPassword());
        Intent intent = new Intent(activity.getActivity(), AdminResetAccountsPasswordAct.class);
        intent.putExtras(bundle);
        activity.getActivity().startActivity(intent);
    }

    public void deleteData(int position){
        ModelAccounts item = mList.get(position);
        db.collection("accounts").document(item.getUsername()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getActivity(), "Data Deleted!!",Toast.LENGTH_SHORT).show();
                    activity.showData();
                }else{
                    Toast.makeText(activity.getActivity(), "Error !!" + task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_accounts, parent , false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(mList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelAccounts> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(mListFull);
            }else {
                String filterPatern = constraint.toString().toLowerCase().trim();

                for (ModelAccounts item : mListFull){
                    if(item.getUsername().toLowerCase().contains(filterPatern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
