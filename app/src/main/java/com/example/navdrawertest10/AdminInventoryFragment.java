package com.example.navdrawertest10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminInventoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private MyAdapter_Admin_Inventory adapter;
    private List<ModelItems> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_inventory_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showInventorydata();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_menu_admin_inventory, menu);
        MenuItem searchItem = menu.findItem(R.id.invent_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showInventorydata(){

        db.collection("inventory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                for (DocumentSnapshot snapshot : task.getResult()){

                    ModelItems model = new ModelItems(snapshot.getString("id"),snapshot.getString("itemname"), snapshot.getString("itemquantity"), snapshot.getString("date"));
                    list.add(model);
                }
                recyclerView = (RecyclerView) getView().findViewById(R.id.admin_invent_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                adapter = new MyAdapter_Admin_Inventory(list);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminInventoryFragment.this.getActivity(), "Oops ... something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
