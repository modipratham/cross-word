package com.example.crossword;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Popup();
    }
    public void Popup(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SecondActivity.this);
        builder2.setMessage("DO YOU WANT TO RETRY");
        builder2.setCancelable(false);
        builder2.setPositiveButton(
                "yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                        startActivity(intent);

                    }
                }
        );

        builder2.setIcon(android.R.drawable.ic_dialog_alert);
        builder2.setNegativeButton(
                "EXIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert11 = builder2.create();
        alert11.show();
    }

}

