package ru.myitschool.jenyaiu90.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleViewActivity extends AppCompatActivity
{
	private ScrollView scheduleSV;
	private Button currentB;
	private Date date;
	private void setDate(Date newDate)
	{
		date = newDate;
		SimpleDateFormat sdf = new SimpleDateFormat("u");
		while (!sdf.format(date).equals("1"))
		{
			date.setTime(date.getTime() - 86400000);
		}
	}
	private void draw()
	{
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
			Date tmp = new Date();
			tmp.setTime(date.getTime() + 518400000);
			currentB.setText(sdf.format(date) + "—" + sdf.format(tmp));
		}
		scheduleSV.removeAllViews();
		int index = 0;
		for (int i = 0; i < 7; i++)
		{
			Button dayB = new Button(ScheduleViewActivity.this);
			scheduleSV.addView(dayB, index++);
			dayB.setText(getResources().getStringArray(R.array.days)[i].toString() + " " +
				(new SimpleDateFormat("dd.MM.yyyy")).format(date));
			try
			{
				BufferedReader schReader = new BufferedReader(new InputStreamReader(openFileInput(
					"schedule\\" + i + ".txt")));
				boolean f = true;
				String str = "";
				while ((str = schReader.readLine()) != null)
				{
					f = false;
					LinearLayout lessonLL = new LinearLayout(ScheduleViewActivity.this);
					lessonLL.setOrientation(LinearLayout.VERTICAL);
					LinearLayout nameLL = new LinearLayout(ScheduleViewActivity.this);
					nameLL.setOrientation(LinearLayout.HORIZONTAL);
					TextView nameTV = new TextView(ScheduleViewActivity.this);
					TextView timeTV = new TextView(ScheduleViewActivity.this);
					TextView taskTV = new TextView(ScheduleViewActivity.this);
					nameLL.addView(nameTV, 0);
					nameLL.addView(timeTV, 1);
					lessonLL.addView(nameLL, 0);
					lessonLL.addView(taskTV, 1);
					scheduleSV.addView(lessonLL, index++);
					nameTV.setTextColor(getResources().getColor(R.color.text));
					lessonLL.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							//Call LessonActivity
						}
					});
					nameTV.setText(str.split(";")[0]);
					timeTV.setText(str.split(";")[1]);
					try
					{
						SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
						Date tmp = new Date();
						tmp.setTime(date.getTime() + 86400000 * i);
						BufferedReader taskReader = new BufferedReader(new InputStreamReader(
							openFileInput("task\\" + sdf.format(tmp) + ".txt")));
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
				TextView noSchTV = new TextView(ScheduleViewActivity.this);
				try
				{
					scheduleSV.addView(noSchTV, index++);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				noSchTV.setText(R.string.noSchedule);
				noSchTV.setTextColor(getResources().getColor(R.color.text));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_view);
		scheduleSV = (ScrollView)findViewById(R.id.scheduleSV);
		currentB = (Button)findViewById(R.id.currentB);
		date = new Date();
		draw();
	}
}
