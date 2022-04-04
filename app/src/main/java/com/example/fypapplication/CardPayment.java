package com.example.fypapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CardPayment extends AppCompatActivity {

    private final String TAG = "TAG";
    private FirebaseFirestore db;
    private Intent data;
    private Button pay;
    private List<MenuItem> items;
    private String total, staffMember, role, docId;
    private static final String BACKEND_URL = "http://10.0.2.2:3000/";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        getSupportActionBar().setTitle("Card Payment Portal");

        db = FirebaseFirestore.getInstance();
        data = getIntent();

        items = data.getParcelableArrayListExtra("items");
        total = data.getStringExtra("total");
        staffMember = data.getStringExtra("staffMember");
        role = data.getStringExtra("role");
        docId = data.getStringExtra("docId");

        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull(
                        "pk_test_51Kdy62HRxthLoqkfFXgn7zgIictcNcZk83oi56h0o6I3esQ2q8f5DrtvLCJ5lXvJtFtAyd9yxbQqZgw6BFVRdQWU00zKCzYnJY"));

        String[] split = total.split("€");
        String totalToBeSentToCheckout = split[1];
        Log.d(TAG, totalToBeSentToCheckout);

        startCheckout(totalToBeSentToCheckout);
    }

    private void startCheckout(String total) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\"eur\","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                +"}";
        double amount = Double.parseDouble(total);
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency", "eur");
        itemMap.put("id", "photo_subscription");
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new PayCallback(this));

        pay = findViewById(R.id.payButton);
        pay.setOnClickListener((View view) -> {
                CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                if(params != null){
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    stripe.confirmPayment(this, confirmParams);
                }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private final class PayCallback implements Callback {
        @NonNull private final WeakReference<CardPayment> activityRef;

        PayCallback(@NonNull CardPayment activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CardPayment activity = activityRef.get();
            if(activity == null){
                return;
            }

            activity.runOnUiThread(() ->
                    Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show()

            );
            Log.d(TAG, e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            final CardPayment activity = activityRef.get();
            if(activity == null){
                return;
            }

            if(!response.isSuccessful()){
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Error: " + response.toString(), Toast.LENGTH_LONG).show());
            } else {
                activity.onPaymentSuccess(response);
            }
            Log.d(TAG, response.toString());
        }
    }


    private void onPaymentSuccess(@NonNull Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult>{

        @NonNull private final WeakReference<CardPayment> activityRef;

        PaymentResultCallback(@NonNull CardPayment activity){
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CardPayment activity = activityRef.get();
            if(activity == null){
                return;
            }
            activity.displayAlert("Error", e.toString());
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult paymentIntentResult) {
            final CardPayment activity = activityRef.get();
            if(activity == null){
                return;
            }

            PaymentIntent paymentIntent = paymentIntentResult.getIntent();
            StripeIntent.Status status = paymentIntent.getStatus();
            if(status == StripeIntent.Status.Succeeded){
                //Payment Successful
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //String receiptTotal = gson.toJson(paymentIntent.getAmount());

//                activity.displayAlert(
//                        "Payment Completed",
//                        gson.toJson(paymentIntent.getAmount())
//                );

                activity.successAlert(
                        "Payment Completed",
                        "Total Paid: €"
                );


            } else if(status == StripeIntent.Status.RequiresPaymentMethod){
                //Payment Failed
                activity.displayAlert(
                  "Payment failed",
                  Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
    }

    private void displayAlert(@NonNull String title, @Nullable String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    private void successAlert(@NonNull String title, @Nullable String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message + total);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String paymentMethod = "Card";
                inputIntoSales(items, total, staffMember, paymentMethod);
                removeOrder(docId);
                Intent intent;
                if(role.equalsIgnoreCase("server")){
                    intent = new Intent(CardPayment.this, ServerMain.class);
                    intent.putExtra("staffMember", staffMember);
                    intent.putExtra("role", role);
                } else {
                    intent = new Intent(CardPayment.this, ManagerMain.class);
                }
                startActivity(intent);

            }
        });
        builder.create().show();
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

    private void inputIntoSales(List<MenuItem> items, String total, String staffMember, String paymentMethod) {
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
                i = new Intent(CardPayment.this, ServerMain.class);
                i.putExtra("staffMember", staffMember);
            } else {
                i = new Intent(CardPayment.this, ManagerMain.class);
            }
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}