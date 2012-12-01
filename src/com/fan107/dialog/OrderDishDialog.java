package com.fan107.dialog;

import com.fan107.R;
import com.fan107.data.OrderDish;
import com.lbx.templete.ActivityTemplete;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OrderDishDialog extends Dialog implements ActivityTemplete,
		OnClickListener {
	OrderDish mOrderDish;

	private TextView orderNameTextView;
	private TextView oldPriceTextView;
	private TextView newPriceTextView;
	private Button addButton;
	private Button subButton;
	private EditText numEditText;
	private Button okButton;
	private Button cancleButton;

	public OrderDishDialog(Context context, OrderDish mOrderDish) {
		super(context);
		this.mOrderDish = mOrderDish;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_dish_layout);

		findWidget();
		setWidgetAttribute();
		setWidgetListenter();
	}

	public void findWidget() {
		orderNameTextView = (TextView) findViewById(R.id.order_dish_name);
		oldPriceTextView = (TextView) findViewById(R.id.order_dish_old_price);
		newPriceTextView = (TextView) findViewById(R.id.order_dish_new_price);

		addButton = (Button) findViewById(R.id.order_dish_add_num);
		subButton = (Button) findViewById(R.id.order_dish_sub_num);
		okButton = (Button) findViewById(R.id.order_dish_ok);
		cancleButton = (Button) findViewById(R.id.order_dish_cancel);

		numEditText = (EditText) findViewById(R.id.order_dish_num);
	}

	public void setWidgetListenter() {
		addButton.setOnClickListener(this);
		subButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);
	}

	public void setWidgetPosition() {

	}

	public void setWidgetAttribute() {
		if (mOrderDish != null) {
			orderNameTextView.setText(mOrderDish.getDishName());
			oldPriceTextView.setText(mOrderDish.getOldPrice()
					* mOrderDish.getOrderNum() + "");
			newPriceTextView.setText(mOrderDish.getNewPrice()
					* mOrderDish.getOrderNum() + "");

			numEditText.setText(mOrderDish.getOrderNum() + "");

		}
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.order_dish_add_num:
			mOrderDish.setOrderNum(mOrderDish.getOrderNum() + 1);
			setWidgetAttribute();
			break;
			
		case R.id.order_dish_sub_num:
			mOrderDish.setOrderNum(mOrderDish.getOrderNum() - 1);
			setWidgetAttribute();
			break;
			
		case R.id.order_dish_ok:
			dismiss();
			break;
			
		case R.id.order_dish_cancel:
			setOrderDishNull();
			dismiss();
			break;
		}
	}
	
	public OrderDish getOrderDish() {
		return mOrderDish;
	}
	
	public void setOrderDishNull() {
		mOrderDish = null;
	}

}
