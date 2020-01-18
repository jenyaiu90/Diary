package ru.myitschool.jenyaiu90.diary;

import android.content.Intent;
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
	private LinearLayout scheduleEditLL;
	private Button addLessonBs[];
	private LinearLayout lessonLLs[][];
	private void draw()
	{
		scheduleEditLL.removeAllViews();
		addLessonBs = new Button[7];
		lessonLLs = new LinearLayout[7][];
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
				int j = 0;
				for (; schReader.readLine() != null; j++);
				lessonLLs[i] = new LinearLayout[j];
				schReader.close();
				schReader = new BufferedReader(new InputStreamReader(openFileInput(
					"schedule\\" + i + ".txt")));
				for (int k = 0; (str = schReader.readLine()) != null; k++)
				{
					LinearLayout lessonLL = new LinearLayout(ScheduleEditActivity.this);
					lessonLLs[i][k] = lessonLL;
					lessonLL.setOrientation(LinearLayout.VERTICAL);
					LinearLayout nameLL = new LinearLayout(ScheduleEditActivity.this);
					nameLL.setOrientation(LinearLayout.HORIZONTAL);
					TextView nameTV = new TextView(ScheduleEditActivity.this);
					TextView timeTV = new TextView(ScheduleEditActivity.this);
					nameLL.addView(nameTV);
					nameLL.addView(timeTV);
					lessonLL.addView(nameLL);
					scheduleEditLL.addView(lessonLL);
					lessonLL.setBackgroundColor(getResources().getColor(R.color.lesson));
					nameTV.setTextColor(getResources().getColor(R.color.text));
					nameTV.setTextSize(30);
					lessonLL.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Intent lessonEditA = new Intent(ScheduleEditActivity.this,
								LessonEditActivity.class);
							lessonEditA.putExtra("new", false);
							int i = 0, j = 0;
							outer:
							for (; i < 7; i++)
							{
								if (lessonLLs[i] != null)
								{
									for (j = 0; j < lessonLLs[i].length; j++)
									{
										if (lessonLLs[i][j] == v)
										{
											break outer;
										}
									}
								}
							}
							lessonEditA.putExtra("day", i);
							try
							{
								BufferedReader read = new BufferedReader(new InputStreamReader(
									openFileInput("schedule\\" + i + ".txt")));
								String str = "";
								for (int k = 0; k < j; k++)
								{
									read.readLine();
								}
								str = read.readLine();
								lessonEditA.putExtra("id", Integer.parseInt(str.split(";")[0]));
								lessonEditA.putExtra("name", str.split(";")[1]);
								lessonEditA.putExtra("startH", Integer.parseInt(str.split(";")[2].split(":")[0]));
								lessonEditA.putExtra("startM", Integer.parseInt(str.split(";")[2].split(":")[1].split("—")[0]));
								lessonEditA.putExtra("endH", Integer.parseInt(str.split("—")[1].split(":")[0]));
								lessonEditA.putExtra("endM", Integer.parseInt(str.split("—")[1].split(":")[1]));
								startActivity(lessonEditA);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
					nameTV.setText(str.split(";")[1]);
					timeTV.setText(" (" + str.split(";")[2] + ")");
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
			addLessonBs[i] = addLessonBT;
			scheduleEditLL.addView(addLessonBT);
			addLessonBT.setText(R.string.add);
			addLessonBT.setTextColor(getResources().getColor(R.color.text));
			addLessonBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent lessonEditA = new Intent(ScheduleEditActivity.this,
						LessonEditActivity.class);
					lessonEditA.putExtra("new", true);
					int i = 0;
					for (; i < 7; i++)
					{
						if (v == addLessonBs[i])
						{
							break;
						}
					}
					lessonEditA.putExtra("day", i);
					startActivity(lessonEditA);
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
	@Override
	public void onResume()
	{
		super.onResume();
		draw();
	}
}
