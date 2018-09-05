package com.example.alton.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alton.myapplication.Models.Email;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alton on 2/18/2018.
 */

public class ComposeEmail extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextSubject, editTextMessage,editPhone;
    Button btnSend, btnAttachment;
    String email, subject, message, attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_email);
databaseReference=firebaseDatabase.getReference();


            editTextEmail = (EditText) findViewById(R.id.editTextTo);
            editTextSubject = (EditText) findViewById(R.id.editTextSubject);
            editTextMessage = (EditText) findViewById(R.id.editTextMessage);
            btnAttachment = (Button) findViewById(R.id.buttonAttachment);
            btnSend = (Button) findViewById(R.id.buttonSend);
editPhone=(EditText)findViewById(R.id.editTextPhone);
            editTextEmail.setText(getIntent().getStringExtra("email"));
            editPhone.setText(getIntent().getStringExtra("phone"));

            btnSend.setOnClickListener(this);
            btnAttachment.setOnClickListener(this);

        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
                /**
                 * Get Path
                 */
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);
                Log.e("Attachment Path:", attachmentFile);
                URI = Uri.parse("file://" + attachmentFile);
                cursor.close();
            }
        }

        public void addEmail(String email, String message, String date){
            Email email1=new Email();
            email1.setEmail(email);
            email1.setMessage(message);
            email1.setDate(date);

            databaseReference.child("email").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                   .child(getIntent().getStringExtra("book"))
                    .setValue(email1);

        }

        @Override
        public void onClick(View v) {

            if (v == btnAttachment) {
                openGallery();

            }
            if (v == btnSend) {
                try {
                    email = editTextEmail.getText().toString();
                    subject = editTextSubject.getText().toString();
                    message = editTextMessage.getText().toString();

                    final Intent emailIntent = new Intent(
                            android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                            new String[] { email });
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            subject);
                    if (URI != null) {
                        emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                    }
                    emailIntent
                            .putExtra(android.content.Intent.EXTRA_TEXT, message);
                    this.startActivity(Intent.createChooser(emailIntent,"Sending mail..."));

                    SmsManager.getDefault().sendTextMessage(editPhone.getText().toString(), null, "This is to remind you that " +
                           "you have been mailed a borrow request for your book on Book Barter.Kindly revert back " , null, null);

                    Date dateObject=new Date();
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
                    addEmail(email,message,date);

                } catch (Throwable t) {
                    Toast.makeText(this,
                            "Request failed try again: " + t.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }

        }

        public void openGallery() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    PICK_FROM_GALLERY);

        }
    }

