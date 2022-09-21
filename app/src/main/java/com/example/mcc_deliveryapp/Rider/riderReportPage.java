package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;

public class riderReportPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView header;
    EditText message;
    Spinner spinner;
    Button btn_submit, btn_upload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reportpage);

        // Making spinner functional
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.complain_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        message = findViewById(R.id.desc_content);
        btn_submit = findViewById(R.id.btn_reportSubmit);
        btn_upload = findViewById(R.id.btn_file_upload);
        header = findViewById(R.id.header_title);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

    private void sendMail() {
        String txtComplain = spinner.getSelectedItem().toString();
        String content = message.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"melhamexpress@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, txtComplain);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String complain = adapterView.getItemAtPosition(i).toString();
        if (complain.equals("Complain1")) {
            // for spinner options
        } else if (complain.equals("Complain2")) {
            // for spinner options
        } else {
            // for spinner options
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}
