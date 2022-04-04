package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessPayment extends AppCompatActivity {

    private TextView tableNo, total;
    private RecyclerView orderItems;
    private Button payByCash, payByCard;
    private FirebaseFirestore db;
    private String docId, tableNumber, orderTotal, staffMember, role;
    private Intent data;
    private List<MenuItem> items;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private final String TAG = "TAG";
    private currentOrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_payment);
        getSupportActionBar().setTitle("Payment Portal");

        db = FirebaseFirestore.getInstance();
        data = getIntent();
        docId = data.getStringExtra("docId");
        tableNumber = data.getStringExtra("tableNo");
        orderTotal = data.getStringExtra("total");
        staffMember = data.getStringExtra("staffMember");
        role = data.getStringExtra("role");
        items = (ArrayList) data.getParcelableArrayListExtra("items");
        for(MenuItem i : items){
            Log.d(TAG, i.getName());

        }

        tableNo = findViewById(R.id.processPaymentTableNo);
        tableNo.setText(tableNumber);

        total = findViewById(R.id.processPaymentTotal);
        total.setText(orderTotal);

        payByCard = findViewById(R.id.processCardPayment);
        payByCash = findViewById(R.id.processCashPayment);

        orderItems = findViewById(R.id.processPaymentRecycler);
        orderItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new currentOrderAdapter(items);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        orderItems.setLayoutManager(staggeredGridLayoutManager);
        orderItems.setAdapter(adapter);

        payByCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paymentMethod = "Cash";
                inputDataIntoSales(items, orderTotal, staffMember, paymentMethod);
                removeOrder(docId);
                Intent i;
                if(role.equalsIgnoreCase("server")){
                    i = new Intent(ProcessPayment.this, ServerMain.class);
                } else {
                    i = new Intent(ProcessPayment.this, ManagerMain.class);
                }
                i.putExtra("staffMember", staffMember);
                startActivity(i);
            }
        });


        payByCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProcessPayment.this, CardPayment.class);
                i.putExtra("total", orderTotal);
                i.putExtra("staffMember", staffMember);
                i.putParcelableArrayListExtra("items", (ArrayList) items);
                i.putExtra("role", role);
                i.putExtra("docId", docId);
                startActivity(i);
            }
        });



    }

    private void removeOrder(String docId) {
        DocumentReference docRef = db.collection("Orders").document(docId);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Order deleted from Orders Database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Order failed to be deleted from Orders Database");
            }
        });
    }

    private void inputDataIntoSales(List<MenuItem> items, String total, String staffMember, String paymentMethod) {
        DocumentReference docRef = db.collection("Sales").document();
        Map<String, Object> sale = new HashMap<>();
        sale.put("items", items);
        sale.put("total", total);
        sale.put("staffMember", staffMember);
        sale.put("paymentMethod", paymentMethod);
        docRef.set(sale).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Sale added to the Database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Sale failed to be added to Database");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_staff_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.backToStaffMain){
            Intent i;
            if(role.equalsIgnoreCase("server")){
                i = new Intent(ProcessPayment.this, ServerMain.class);
            } else {
                i = new Intent(ProcessPayment.this, ManagerMain.class);
            }
            i.putExtra("staffMember", staffMember);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}