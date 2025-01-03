package com.example.book_store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.book_store.customadapter.OrderAdapter;
import com.example.book_store.model.Order;
import com.example.book_store.sharedpreferences.Constants;
import com.example.book_store.sharedpreferences.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OrderFragment extends Fragment {
    Button btnBack;
    private RecyclerView orderRv;
    private ArrayList<Order> orderList;
    private OrderAdapter orderAdapter;
    private String phone;
    PreferenceManager preferenceManager;
    FragmentManager fragmentManager;
    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        orderRv = view.findViewById(R.id.order_recyclerview);
        preferenceManager = new PreferenceManager(getContext(), Constants.LOGIN_KEY_PREFERENCE_NAME);
        fragmentManager = getParentFragmentManager();
        loadOrders();
        btnBack = view.findViewById(R.id.order_btnBack);
        handleBackButton();
        return view;
    }

    private void loadOrders() {
        orderList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                phone = preferenceManager.getString(Constants.LOGIN_PHONE);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
                ref.orderByChild("orderBy").equalTo(phone).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for( DataSnapshot ds : snapshot.getChildren()){
                                Order order = ds.getValue(Order.class);
                                orderList.add(order);
                            }
                            orderAdapter = new OrderAdapter(getContext(),orderList,fragmentManager);
                            orderRv.setAdapter(orderAdapter);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void handleBackButton(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình trước đó trong ngăn xếp
                getParentFragmentManager().popBackStack();
            }
        });
    }
}