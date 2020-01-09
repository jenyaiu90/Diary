package ru.myitschool.jenyaiu90.diary;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScheduleEditActivity extends AppCompatActivity
{
	LinearLayout scheduleEditLL;
	private void draw()
	{
		scheduleEditLL.removeAllViews();
		for (int i = 0; i < 7; i++)
		{
			TextView dayTV = new TextView(ScheduleEditActivity.this);
			scheduleEditLL.addView(dayTV);
			dayTV.setText(getResources().getStringArray(R.array.days)[i]);
			dayTV.setTextColor(getResources().getColor(R.color.text));
			dayTV.setTextSize(40);


		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_edit);
		scheduleEditLL = (LinearLayout)findViewById(R.id.scheduleEditLL);
		draw();
	}
}
