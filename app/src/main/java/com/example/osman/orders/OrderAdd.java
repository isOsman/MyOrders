package com.example.osman.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderAdd extends Activity implements View.OnClickListener {

        private TextView orderIdView;
        private EditText customerView;
        private EditText cakeView;
        private DatePicker orderDateView;
        private DatePicker sendDateView;
        private EditText weightView;
        private EditText priceView;
        private CheckBox madeView;
        private Button okBtn;
        private Button cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit);

        orderIdView = (TextView) findViewById(R.id.orderIdEdit);
        orderIdView.setText(getString(R.string.orderAdd));

        customerView = (EditText) findViewById(R.id.customerEdit);
        cakeView = (EditText) findViewById(R.id.cakeEdit);
        orderDateView = (DatePicker) findViewById(R.id.orderDateEdit);
        sendDateView = (DatePicker) findViewById(R.id.sendDateEdit);
        weightView =  (EditText) findViewById(R.id.weightEdit);
        priceView = (EditText) findViewById(R.id.priceEdit);
        madeView = (CheckBox) findViewById(R.id.madeBoxEdit);
        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);


        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        madeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.madeBoxEdit){
            madeView.setText(madeView.isChecked() ? getString(R.string.madeTrue) : getString(R.string.madeFalse));
        }
        if(view.getId() == R.id.cancelBtn) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
        }else if (view.getId() == R.id.okBtn){
            if (!checkViews()) return;

            Intent intent = new Intent();
            intent.putExtra("customer",customerView.getText());
            intent.putExtra("cake",cakeView.getText());
            intent.putExtra("orderDate",getDate(orderDateView));
            intent.putExtra("sendDate",getDate(sendDateView));
            intent.putExtra("weight",Integer.parseInt(weightView.getText().toString()));
            intent.putExtra("price",Integer.parseInt(priceView.getText().toString()));
            intent.putExtra("made",madeView.isChecked());
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private boolean checkViews(){
        if(customerView.getText().toString().trim().length() == 0
                || cakeView.getText().toString().trim().length() == 0
                || (weightView.getText().toString().trim().length() == 0 || weightView.getText().toString().trim().length() > 5)
                || (priceView.getText().toString().trim().length() == 0 || priceView.getText().toString().trim().length() > 5))
        {
            Toast.makeText(this,R.string.checkView,Toast.LENGTH_SHORT).show();
            return false;
        }else if(!checkDate(orderDateView,sendDateView)) {
            Toast.makeText(this,R.string.checkDate,Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;


    }

    private boolean checkDate(DatePicker date1, DatePicker date2){
        try {
            Date dateLarge = new SimpleDateFormat("dd.MM.yyyy").parse(getDate(date1));
            Date dateLess = new SimpleDateFormat("dd.MM.yyyy").parse(getDate(date2));
            if(dateLarge.compareTo(dateLess) > 0){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getDate(DatePicker datePicker){
        return datePicker.getDayOfMonth() + "." + (datePicker.getMonth()+1) + "." + datePicker.getYear();
    }
}
