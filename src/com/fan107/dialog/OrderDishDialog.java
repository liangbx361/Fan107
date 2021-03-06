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
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderDishDialog extends Dialog implements ActivityTemplete,
		OnClickListener {
	OrderDish mOrderDish;

	private TextView orderNameTextView;
	private TextView oldPriceTextView;
	private TextView totalPriceView;
	private Button addButton;
	private Button subButton;
	private TextView numEditText;
	private LinearLayout okButton;
	private LinearLayout cancleButton;
	
	private boolean isConfirm = false;

	public OrderDishDialog(Context context, OrderDish mOrderDish) {
		super(context);
		this.mOrderDish = mOrderDish;
	}
	
	public OrderDishDialog(Context context, int theme, OrderDish mOrderDish) {
		super(context, theme);
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
		totalPriceView = (TextView) findViewById(R.id.order_dish_total_price);

		addButton = (Button) findViewById(R.id.order_dish_add_num);
		subButton = (Button) findViewById(R.id.order_dish_sub_num);
		okButton = (LinearLayout) findViewById(R.id.order_dish_ok);
		cancleButton = (LinearLayout) findViewById(R.id.order_dish_cancel);

		numEditText = (TextView) findViewById(R.id.order_dish_num);
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
			oldPriceTextView.setText(mOrderDish.getNewPrice() + "");
			totalPriceView.setText(mOrderDish.getNewPrice() * mOrderDish.getOrderNum() + "");

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
			if(mOrderDish.getOrderNum() > 1) 
				mOrderDish.setOrderNum(mOrderDish.getOrderNum() - 1);
			setWidgetAttribute();
			break;
			
		case R.id.order_dish_ok:
			isConfirm = true;
			dismiss();
			break;
			
		case R.id.order_dish_cancel:
			dismiss();
			break;
		}
	}
	
	public OrderDish getOrderDish() {
		return mOrderDish;
	}

	public boolean isConfirm() {
		return isConfirm;
	}
}
