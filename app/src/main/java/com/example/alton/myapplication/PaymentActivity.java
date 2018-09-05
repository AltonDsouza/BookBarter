package com.example.alton.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by alton on 2/11/2018.
 */

public class PaymentActivity extends AppCompatActivity{

    CardForm cardForm;
    TextView txt;
    Button btn;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cardForm=(CardForm)findViewById(R.id.cardForm);
        txt=(TextView)findViewById(R.id.payment_amount);
        btn=(Button)findViewById(R.id.btn_pay);
    progressDialog=new ProgressDialog(this);
        databaseReference=firebaseDatabase.getReference("books");

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("price", 0);
        txt.setText(String.valueOf(intValue));


               btn.setText(String.format("Payer %s ",txt.getText()));
        cardForm.setPayBtnClickListner(
                new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                AlertDialog.Builder builder =new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle("Details");
                builder.setMessage(card.getName()+"\n"+card.getNumber()+"\n"+ card.getAddressCity());
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
                        Toast.makeText(PaymentActivity.this, "Transaction successfull", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(),HomePageActivity.class));

                    }
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

    }
}
