package ru.myitschool.jenyaiu90.diary;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
	private LinearLayout scheduleLL;
	private Button currentB;
	private Date date;
	private void setDate(long newDate)
	{
		date.setTime(newDate);
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
		scheduleLL.removeAllViews();
		Date tmpDate = new Date();
		tmpDate.setTime(date.getTime());
		for (int i = 0; i < 7; i++)
		{
			Button dayB = new Button(ScheduleViewActivity.this);
			scheduleLL.addView(dayB);
			dayB.setText(getResources().getStringArray(R.array.days)[i].toString() + " " +
				(new SimpleDateFormat("dd.MM.yyyy")).format(tmpDate));
			dayB.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//ToDo: Call DayScheduleViewActivity
				}
			});
			tmpDate.setTime(tmpDate.getTime() + 86400000);
			if (tmpDate.getTime() / 86400000 == (new Date()).getTime() / 86400000)
			{
				dayB.setBackgroundColor(getResources().getColor(R.color.today));
			}
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
					scheduleLL.addView(lessonLL);
					nameTV.setTextColor(getResources().getColor(R.color.text));
					lessonLL.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							//ToDo: Call LessonActivity
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
					scheduleLL.addView(noSchTV);
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
		scheduleLL = (LinearLayout)findViewById(R.id.scheduleLL);
		currentB = (Button)findViewById(R.id.currentB);
		date = new Date();
		setDate((new Date()).getTime());
		draw();
	}
	public void prevClick(View view)
	{
		setDate(date.getTime() - 86400000);
		draw();
	}
	public void currentClick(View view)
	{
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
		SimpleDateFormat sdfDay = new SimpleDateFormat("d");
		new DatePickerDialog(
			ScheduleViewActivity.this,
			new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
				{
					Date tmp = new Date(year - 1900, month, dayOfMonth);
					setDate(tmp.getTime());
					draw();
				}
			},
			Integer.parseInt(sdfYear.format(date)),
			Integer.parseInt(sdfMonth.format(date)) - 1,
			Integer.parseInt(sdfDay.format(date))).show();
	}
	public void nextClick(View view)
	{
		setDate(date.getTime() + 604800000);
		draw();
	}
	public void editClick(View view)
	{
		//ToDo: Call ScheduleEditActivity
	}
}
