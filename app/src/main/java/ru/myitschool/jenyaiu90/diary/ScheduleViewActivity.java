package ru.myitschool.jenyaiu90.diary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleViewActivity extends AppCompatActivity
{
	private LinearLayout scheduleLL, lessonLLs[][];
	private Button currentB;
	private Date date, tmpDate;
	private Button daysBs[];
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
		daysBs = new Button[7];
		lessonLLs = new LinearLayout[7][];
		for (int i = 0; i < 7; i++)
		{
			String tmpDateS = (new SimpleDateFormat("dd.MM.yyyy")).format(tmpDate);
			daysBs[i] = new Button(ScheduleViewActivity.this);
			scheduleLL.addView(daysBs[i]);
			daysBs[i].setText(getResources().getStringArray(R.array.days)[i].toString() + " " + tmpDateS);
			daysBs[i].setOnClickListener(new View.OnClickListener()
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
						if (daysBs[i] == v)
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
				daysBs[i].setBackgroundColor(getResources().getColor(R.color.today));
			}
			tmpDate.setTime(tmpDate.getTime() + 86400000);
			try
			{
				BufferedReader schReader = new BufferedReader(new InputStreamReader(openFileInput(
					"schedule\\" + i + ".txt")));
				boolean f = true;
				String str = "";
				int j = 0;
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
					nameTV.setTextSize(30);
					int tmpInt = lessonLLs[i] == null ? 0 : lessonLLs.length;
					LinearLayout tmpLL[] = new LinearLayout[j + 1];
					for (int k = 0; k < j; k++)
					{
						tmpLL[k] = lessonLLs[i][k];
					}
					tmpLL[j] = lessonLL;
					lessonLLs[i] = tmpLL;
					lessonLL.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							int i = 0, j = 0;
							outer:
							for ( ; i < 7; i++)
							{
								if (lessonLLs[i] != null)
								{
									for ( ; j < lessonLLs[i].length; j++)
									{
										if (lessonLLs[i][j] == v)
										{
											break outer;
										}
									}
								}
							}
							SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
							Date tmp = new Date();
							tmp.setTime(date.getTime() + 86400000 * i);
							try
							{
								BufferedReader taskReader = new BufferedReader(new InputStreamReader(
									openFileInput("task\\" + sdf.format(tmp) + "\\" + j + ".txt")));
								String str = taskReader.readLine();
								taskReader.close();
								addTask(i, j, str);
							}
							catch (FileNotFoundException e)
							{
								addTask(i, j, "");
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
					nameTV.setText(str.split(";")[1]);
					timeTV.setText(" (" + str.split(";")[2] + ")");
					try
					{
						SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
						Date tmp = new Date();
						tmp.setTime(date.getTime() + 86400000 * i);
						BufferedReader taskReader = new BufferedReader(new InputStreamReader(
							openFileInput("task\\" + sdf.format(tmp) + "\\" + j + ".txt")));
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
					j++;
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
	@Override
	public void onResume()
	{
		super.onResume();
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
		draw();
	}
	private void addTask(int i, int j, String old)
	{
		Intent taskEditA = new Intent(ScheduleViewActivity.this, TaskEditActivity.class);
		taskEditA.putExtra("task", old);
		startActivityForResult(taskEditA, i + j * 10);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (resultCode == RESULT_OK)
		{
			int i = requestCode % 10;
			int j = requestCode / 10;
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date tmp = new Date();
			tmp.setTime(date.getTime() + 86400000 * i);
			String str = intent.getStringExtra("res");
			try
			{
				if (str == null)
				{
					throw new Exception("Something went wrong! (I was absolutely sure that str can`t be null)");
				}
				if (str.equals(""))
				{
					 new File(getFilesDir(), "task\\" + sdf.format(tmp) +
							"\\" + j + ".txt").delete();
				}
				else
				{
					BufferedWriter taskWriter = new BufferedWriter(new OutputStreamWriter(
						openFileOutput("task\\" + sdf.format(tmp) + "\\" + j + ".txt", MODE_PRIVATE)));
					taskWriter.write(str);
					taskWriter.close();
				}
			}
			catch (FileNotFoundException e)
			{
				try
				{
					new File(getFilesDir(), "task\\" + sdf.format(tmp) +
							"\\" + j + ".txt").createNewFile();
					BufferedWriter taskWriter = new BufferedWriter(new OutputStreamWriter(
							openFileOutput("task\\" + sdf.format(tmp) + "\\" +
									j + ".txt", MODE_PRIVATE)));
					taskWriter.write(str);
					taskWriter.close();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		draw();
	}
}
