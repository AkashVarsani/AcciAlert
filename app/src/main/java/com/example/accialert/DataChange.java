package com.example.accialert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.accialert.databinding.ActivityDataChangeBinding;
import com.example.accialert.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataChange extends AppCompatActivity {

    ActivityDataChangeBinding binding;
    StringModel stringModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        stringModel = new StringModel();

        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stringModel.d1 = binding.e1.getText().toString();
                stringModel.d2 = binding.e2.getText().toString();
                stringModel.d3 = binding.e3.getText().toString();
                stringModel.d4 = binding.e4.getText().toString();
                stringModel.d5 = binding.e5.getText().toString();
                stringModel.d6 = binding.e6.getText().toString();

                FirebaseDatabase.getInstance().getReference()
                                .child("location").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                StringModel model = snapshot.getValue(StringModel.class);

                                if(!stringModel.d1.equals(""))model.d1 = stringModel.d1;
                                if(!stringModel.d2.equals(""))model.d2 = stringModel.d2;
                                if(!stringModel.d3.equals(""))model.d3 = stringModel.d3;
                                if(!stringModel.d4.equals(""))model.d4 = stringModel.d4;
                                if(!stringModel.d5.equals(""))model.d5 = stringModel.d5;
                                if(!stringModel.d6.equals(""))model.d6 = stringModel.d6;

                                FirebaseDatabase.getInstance().getReference()
                                        .child("location").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(DataChange.this, "Location Data saved Successfully...", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataChange.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}