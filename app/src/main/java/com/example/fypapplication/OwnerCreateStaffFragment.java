package com.example.fypapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OwnerCreateStaffFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ownerCreateStaffView = inflater.inflate(R.layout.fragment_owner_create_staff, container, false);
        mAuth = FirebaseAuth.getInstance();
        String[] types = new String[4];
        types[0] = "server";
        types[1] = "host";
        types[2] = "manager";
        types[3] = "chef";

        ArrayAdapter<String> adapter;

        AutoCompleteTextView roleInput = ownerCreateStaffView.findViewById(R.id.newStaffRole);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.itemtype_dropdown_item, types);
        roleInput.setAdapter(adapter);

        roleInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(view.getContext(), "Type Selected " + item, Toast.LENGTH_LONG).show();
            }
        });

        EditText usernameInput = ownerCreateStaffView.findViewById(R.id.newStaffUsername);
        EditText passwordInput = ownerCreateStaffView.findViewById(R.id.newStaffPassword);
        Button createStaffAccount = ownerCreateStaffView.findViewById(R.id.createNewStaffAccount);

        createStaffAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roleString = roleInput.getText().toString();
                String usernameString = usernameInput.getText().toString();
                String passwordString = passwordInput.getText().toString();

                if(roleString.isEmpty() || usernameString.isEmpty() || passwordString.isEmpty()){
                    Toast.makeText(view.getContext(), "All Fields are required", Toast.LENGTH_LONG).show();
                } else if(passwordString.length() < 6){
                    passwordInput.setError("Please enter at least 6 characters");
                } else {
                    mAuth.createUserWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String wage = "";
                                if(roleString.equalsIgnoreCase("server")){
                                    wage = "14.50";
                                }
                                if(roleString.equalsIgnoreCase("host")){
                                    wage = "11.00";
                                }
                                if(roleString.equalsIgnoreCase("chef")){
                                    wage = "24.00";
                                }
                                if(roleString.equalsIgnoreCase("manager")){
                                    wage = "20.00";
                                }
                                inputDataIntoStaff(usernameString, roleString, passwordString, wage);
                                Toast.makeText(view.getContext(), "New staff member created", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(view.getContext(), "An error occurred, New staff member was not created", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


        return ownerCreateStaffView;
    }

    private void inputDataIntoStaff(String username, String role, String password, String wage) {
        String TAG = "TAG";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Staff").document();
        Map<String, Object> newStaff = new HashMap<>();
        newStaff.put("role", role);
        newStaff.put("username", username);
        newStaff.put("password", password);
        newStaff.put("wage", wage);

        docRef.set(newStaff).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success, new staff member added to the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed, new staff member was not added to the database");
            }
        });
    }
}