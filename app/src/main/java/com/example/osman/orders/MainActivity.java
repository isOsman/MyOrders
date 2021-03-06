package com.example.osman.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{

    Storage storage;
    OrderAdapter adapter;
    ArrayList<Order> orders;
    Button addButton;
    TextView topView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);

        storage = new Storage(this);
        try {
            storage.init();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        ListView ordersList;
        topView = (TextView) findViewById(R.id.topView);
        try {
            orders = storage.getList();
            if(orders == null)orders = new ArrayList<>();

            if (orders.size() == 0){
                topView.setText(getString(R.string.noOrders));
            }else{
                topView.setText(String.valueOf(getString(R.string.orderCount) + orders.size()));
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ordersList = (ListView) findViewById(R.id.orderList);
        adapter = new OrderAdapter(this,R.layout.order_item,orders,storage,topView);

        ordersList.setAdapter(adapter);

        addButton = (Button) findViewById(R.id.addBtn);
        addButton.setOnClickListener(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request.ORDER_EDIT) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Order order = new Order();
                order.setId(extras.getInt("orderId"));
                order.setCustomer(String.valueOf(extras.get("customer")));
                order.setCake(String.valueOf(extras.get("cake")));
                order.setOrderDate(extras.getString("orderDate"));
                order.setSendDate(extras.getString("sendDate"));
                order.setWeight(extras.getInt("weight"));
                order.setPrice(extras.getInt("price"));
                order.setMade(extras.getBoolean("made"));
                adapter.setList(adapter.currPos, order);
                Toast.makeText(this, R.string.changed, Toast.LENGTH_LONG).show();
            }

        }else if(requestCode == Request.ORDER_ADD){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Order order = new Order();
                try {
                    order.setId(storage.getAndIncID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                order.setCustomer(String.valueOf(extras.get("customer")));
                order.setCake(String.valueOf(extras.get("cake")));
                order.setOrderDate(extras.getString("orderDate"));
                order.setSendDate(extras.getString("sendDate"));
                order.setWeight(extras.getInt("weight"));
                order.setPrice(extras.getInt("price"));
                order.setMade(extras.getBoolean("made"));
                adapter.add(order);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.added, Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(this,OrderAdd.class);
            startActivityForResult(intent,Request.ORDER_ADD);
    }
}

