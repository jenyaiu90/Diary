package ru.myitschool.jenyaiu90.diary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleViewActivity extends AppCompatActivity
{
	private LinearLayout scheduleLL;
	private Button currentB;
	private Date date, tmpDate;
	private Button crutchButtons[];
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
		tmpDate = new Date();
		tmpDate.setTime(date.getTime());
		String today = (new SimpleDateFormat("dd.MM.yyyy")).format(new Date());
		crutchButtons = new Button[7];
		for (int i = 0; i < 7; i++)
		{
			String tmpDateS = (new SimpleDateFormat("dd.MM.yyyy")).format(tmpDate);
			crutchButtons[i] = new Button(ScheduleViewActivity.this);
			scheduleLL.addView(crutchButtons[i]);
			crutchButtons[i].setText(getResources().getStringArray(R.array.days)[i].toString() + " " + tmpDateS);
			crutchButtons[i].setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent dayScheduleViewA = new Intent(ScheduleViewActivity.this,
							DayScheduleViewActivity.class);
					Date tmp = new Date();
					tmp.setTime(tmpDate.getTime());
					for (int i = 6; i >= 0; i--)
					{
						tmp.setTime(tmp.getTime() - 86400000);
						if (crutchButtons[i] == v)
						{
							break;
						}
					}
					dayScheduleViewA.putExtra("day", tmp);
					startActivity(dayScheduleViewA);
				}
			});
			if (tmpDateS.equals(today))
			{
				crutchButtons[i].setBackgroundColor(getResources().getColor(R.color.today));
			}
			tmpDate.setTime(tmpDate.getTime() + 86400000);
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
					nameLL.addView(nameTV);
					nameLL.addView(timeTV);
					lessonLL.addView(nameLL);
					lessonLL.addView(taskTV);
					scheduleLL.addView(lessonLL);
					nameTV.setTextColor(getResources().getColor(R.color.text));
					lessonLL.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							//ToDo: Call AddTaskActivity
						}
					});
					nameTV.setText(str.split(";")[1]);
					timeTV.setText(str.split(";")[2]);
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
				scheduleLL.addView(noSchTV);
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
		scheduleLL = (LinearLayout)findViewById(R.id.scheduleEditLL);
		currentB = (Button)findViewById(R.id.currentB);
		date = new Date();
		setDate((new Date()).getTime() / 86400000 * 86400000);
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
		Intent scheduleEditA = new Intent(ScheduleViewActivity.this, ScheduleEditActivity.class);
		startActivity(scheduleEditA);
	}
}
