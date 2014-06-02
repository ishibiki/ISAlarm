package com.ishibiki.isalarm;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends Activity {
	private EditText name = null;
	private EditText tel = null;
	private DatabaseHelper dbhelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		name = (EditText) this.findViewById(R.id.edit1);
		tel = (EditText) this.findViewById(R.id.edit2);
		dbhelper = new DatabaseHelper(this);


    Button startButton=(Button)findViewById(R.id.bt_start);
    startButton.setOnClickListener(new StartButtonClickListener());
 }
 class StartButtonClickListener implements OnClickListener {
    public void onClick(View v) {
       Intent intent = new Intent(AlarmActivity.this,StartServiceSampleService.class);

      EditText stopcount = (EditText)findViewById(R.id.edit2);
      intent.putExtra("STOPTIME", stopcount.getText().toString());

      startService(intent);
   }
}
 // オプションメニュー生成
/*     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // オプションメニューを設定
       getMenuInflater().inflate(R.menu.start_service_sample, menu);
       return true;
   }*/


	public void doSave(View view) {
		Editable s_name = name.getText();
		Editable s_tel = tel.getText();
		SQLiteDatabase db= dbhelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.NAME, s_name.toString());
		values.put(DatabaseHelper.TEL, s_tel.toString());
		db.insert(DatabaseHelper.TABLE_NAME,null,values);
		name.setText("");
		tel.setText("");
		Toast toast = Toast.makeText(this, "saved",
			Toast.LENGTH_SHORT);
		toast.show();
	}

	public void doDel(View view) {
		Editable s_name = name.getText();
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		String findset = DatabaseHelper.NAME + " =?";
		String[] params = {s_name.toString()};
		db.delete(DatabaseHelper.TABLE_NAME,findset,params);
		name.setText("");
		tel.setText("");
		Toast toast = Toast.makeText(this, "delete", Toast.LENGTH_SHORT);
		toast.show();
	}

	public void doFind(View view) {
		Editable s_name = name.getText();
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String findset = DatabaseHelper.NAME + "=?";
		String[] params = {s_name.toString()};
		Cursor c = db.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_ARR,
				findset, params, null,null,null,null);
		if (c.moveToFirst()) {
			name.setText(c.getString(1));
			tel.setText(c.getString(2));
		}
		else {
			Toast toast = Toast.makeText(this, "not find '" + s_name + "'.",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void doShow(View view) {
		TableLayout tablelayout =(TableLayout)findViewById(R.id.tl_list);
		//tablelayout.removeAllViews();

		SQLiteDatabase db = dbhelper.getReadableDatabase();
		//String columns[] = {DatabaseHelper.NAME, DatabaseHelper.TEL};
		Cursor c = db.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_ARR,
				null, null,null,null,DatabaseHelper.ID);
		tablelayout.setStretchAllColumns(true);

		TableRow headrow = new TableRow(AlarmActivity.this);
		TextView headtxt1 = new TextView(AlarmActivity.this);
		headtxt1.setText("なまえ");
		headtxt1.setGravity(Gravity.CENTER_HORIZONTAL);
		headtxt1.setWidth(120);
		TextView headtxt2 = new TextView(AlarmActivity.this);
		headtxt2.setText("時間(分)");
		headtxt2.setGravity(Gravity.CENTER_HORIZONTAL);
		headtxt2.setWidth(30);
		headrow.addView(headtxt1);
		headrow.addView(headtxt2);
		tablelayout.addView(headrow);

		while(c.moveToNext()) {
			TableRow row = new TableRow(AlarmActivity.this);
			TextView nametxt = new TextView(AlarmActivity.this);
			nametxt.setGravity(Gravity.CENTER_HORIZONTAL);
			nametxt.setText(c.getString(1));
			TextView teltxt = new TextView(AlarmActivity.this);
			teltxt.setGravity(Gravity.CENTER_HORIZONTAL);
			teltxt.setText(c.getString(2));
			row.addView(nametxt);
			row.addView(teltxt);
			tablelayout.addView(row);

		}



	}

}
