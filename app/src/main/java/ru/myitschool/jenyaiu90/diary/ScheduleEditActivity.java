package ru.myitschool.jenyaiu90.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

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
			try
			{
				BufferedReader schReader = new BufferedReader(new InputStreamReader(openFileInput(
					"schedule\\" + i + ".txt")));
				String str = "";
				for (int j = 0; (str = schReader.readLine()) != null; j++)
				{
					LinearLayout lessonLL = new LinearLayout(ScheduleEditActivity.this);
					lessonLL.setOrientation(LinearLayout.VERTICAL);
					LinearLayout nameLL = new LinearLayout(ScheduleEditActivity.this);
					nameLL.setOrientation(LinearLayout.HORIZONTAL);
					TextView nameTV = new TextView(ScheduleEditActivity.this);
					TextView timeTV = new TextView(ScheduleEditActivity.this);
					TextView taskTV = new TextView(ScheduleEditActivity.this);
					nameLL.addView(nameTV, 0);
					nameLL.addView(timeTV, 1);
					lessonLL.addView(nameLL, 0);
					lessonLL.addView(taskTV, 1);
					scheduleEditLL.addView(lessonLL);
					lessonLL.setBackgroundColor(getResources().getColor(R.color.lesson));
					nameTV.setTextColor(getResources().getColor(R.color.text));
					lessonLL.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							//ToDo: Call LessonEditActivity
						}
					});
					nameTV.setText(str.split(";")[0]);
					timeTV.setText(str.split(";")[1]);
					TextView emptyTV = new TextView(ScheduleEditActivity.this);
					emptyTV.setHeight(5);
					scheduleEditLL.addView(emptyTV);
				}
			}
			catch (FileNotFoundException e)
			{

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Button addLessonBT = new Button(ScheduleEditActivity.this);
			scheduleEditLL.addView(addLessonBT);
			addLessonBT.setText(R.string.add);
			addLessonBT.setTextColor(getResources().getColor(R.color.text));
			addLessonBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//ToDo: Call LessonEditActivity
				}
			});
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
