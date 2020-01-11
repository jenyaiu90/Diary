package ru.myitschool.jenyaiu90.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayScheduleViewActivity extends AppCompatActivity
{
	Date date;
	int day;
	TextView titleTV;
	LinearLayout dayScheduleLL;
	private void draw()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		titleTV.setText(getResources().getStringArray(R.array.days)[day] + ", " +
				sdf.format(date));
		try
		{
			BufferedReader schReader = new BufferedReader(new InputStreamReader(openFileInput(
					"schedule\\" + day + ".txt")));
			boolean f = true;
			String str = "";
			while ((str = schReader.readLine()) != null)
			{
				f = false;
				LinearLayout lessonLL = new LinearLayout(DayScheduleViewActivity.this);
				lessonLL.setOrientation(LinearLayout.VERTICAL);
				LinearLayout nameLL = new LinearLayout(DayScheduleViewActivity.this);
				nameLL.setOrientation(LinearLayout.HORIZONTAL);
				TextView nameTV = new TextView(DayScheduleViewActivity.this);
				TextView timeTV = new TextView(DayScheduleViewActivity.this);
				TextView taskTV = new TextView(DayScheduleViewActivity.this);
				nameLL.addView(nameTV);
				nameLL.addView(timeTV);
				lessonLL.addView(nameLL);
				lessonLL.addView(taskTV);
				dayScheduleLL.addView(lessonLL);
				nameTV.setTextColor(getResources().getColor(R.color.text));
				lessonLL.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//ToDo: Call AddTaskActivity
					}
				});
				nameTV.setText(str.split(";")[0]);
				timeTV.setText(str.split(";")[1]);
				try
				{
					BufferedReader taskReader = new BufferedReader(new InputStreamReader(
							openFileInput("task\\" + sdf.format(day) + ".txt")));
					str = taskReader.readLine();
					taskTV.setText(str);
					taskReader.close();
				}
				catch (FileNotFoundException e)
				{
					taskTV.setText(R.string.noTask);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			if (f)
			{
				FileNotFoundException e = new FileNotFoundException();
				throw e;
			}
		}
		catch (FileNotFoundException e)
		{
			TextView noSchTV = new TextView(DayScheduleViewActivity.this);
			dayScheduleLL.addView(noSchTV);
			noSchTV.setText(R.string.noSchedule);
			noSchTV.setTextColor(getResources().getColor(R.color.text));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_schedule_view);
		date = new Date();
		date.setTime(((Date)getIntent().getSerializableExtra("day")).getTime());
		day = Integer.parseInt((new SimpleDateFormat("u")).format(date)) - 1;
		titleTV = (TextView)findViewById(R.id.titleTV);
		dayScheduleLL = (LinearLayout)findViewById(R.id.dayScheduleLL);
		draw();
	}
}
