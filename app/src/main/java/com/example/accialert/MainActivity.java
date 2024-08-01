package com.example.accialert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.example.accialert.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    int id = 0, oldid = 0;
    int ii = 0;
    String msg = "";
    int chNO =0;
    boolean start;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        timer = new Timer();
        start  = true;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        oldid = id;
                        FirebaseDatabase.getInstance().getReference().child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                id = snapshot.getValue(Integer.class);
                                binding.tv.setText("ID :: " + id + "");


                                FirebaseDatabase.getInstance().getReference()
                                        .child("location").child("d" + id + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    msg = snapshot.getValue(String.class);
                                                }
                                                nn();
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
                });
            }
        }, 0, 1000);


        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataChange.class);
                startActivity(intent);
            }
        });

        binding.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(1);
            }
        });

        binding.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(2);
            }
        });

        binding.b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(3);
            }
        });

        binding.b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(4);
            }
        });

        binding.b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(5);
            }
        });

        binding.b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun(6);
            }
        });
    }

    public void fun(int myID) {

        FirebaseDatabase.getInstance().getReference().child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id = snapshot.getValue(Integer.class);
                zz(myID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void nn() {

        if(start)oldid=0;
        if (oldid != id) ii = 0;

//        Toast.makeText(MainActivity.this, "ol: "+oldid+".. id: "+id+".. ii:"+ii, Toast.LENGTH_SHORT).show();



        if (oldid != id && ii < 5) {
            if(id == 0)chNO=2;
            else chNO=1;
            createNotificationChannel();
            NotificationCompat.Builder builder;
            if (id == 0) {
                builder = new NotificationCompat.Builder(getApplicationContext(), "ch2")
                        .setSmallIcon(R.drawable.alertlogo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Problem has been Ramped Down !")
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Problem has been Ramped Down by on the STREET light which have started it..."));
                binding.status.setText("No Problem at all...");
                binding.status2.setText("    ");
                start = false;
            } else {
                builder = new NotificationCompat.Builder(getApplicationContext(), "ch1")
                        .setSmallIcon(R.drawable.alertlogo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("AcciAlert in STREET LIGHT id :: " + id)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(Html.fromHtml("AcciAlert occurred at the location of :: <b>" + msg + "</b> and STREET LIGHT id :: " + id)));
                binding.status.setText(Html.fromHtml("AcciAlert occurred at the location of :: <b>" + msg + "</b> and STREET LIGHT id :: " + id));
                binding.status2.setText(msg);
                start =false;
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());
            ii++;

        }
    }

    public void zz(int myID) {
        int value = 0;
        boolean cc = false;

        if (myID < id) {
            value = myID;
            cc = true;
        } else if (id == 0) {
            value = myID;
            cc = true;
        } else if (id == myID) {
            value = 0;
            cc = true;
        }

        if (cc) {
            FirebaseDatabase.getInstance().getReference().child("id").setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
                }
            });

            FirebaseDatabase.getInstance().getReference().child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    binding.tv.setText("ID :: " + snapshot.getValue(Integer.class) + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri soundUri1 = Uri.parse(
                    "android.resource://" +
                            getApplicationContext().getPackageName() +
                            "/" +
                            R.raw.alerton);

            Uri soundUri2 = Uri.parse(
                    "android.resource://" +
                            getApplicationContext().getPackageName() +
                            "/" +
                            R.raw.alertoff);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if(chNO==1){
                NotificationChannel channel = new NotificationChannel("ch1", "ch1", NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(soundUri1, audioAttributes);
                channel.setDescription("ch1");
                notificationManager.createNotificationChannel(channel);
            }
            else if (chNO==2){
                NotificationChannel channel2 = new NotificationChannel("ch2", "ch2", NotificationManager.IMPORTANCE_HIGH);
                channel2.setSound(soundUri2, audioAttributes);
                channel2.setDescription("ch2");
                notificationManager.createNotificationChannel(channel2);
            }

        }
    }


}