package ru.myitschool.jenyaiu90.diary;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LessonEditActivity extends AppCompatActivity
{
	String name;
	int start[], end[];
	int day;
	int id;
	EditText nameET;
	TextView startTimeTV, endTimeTV;
	LinearLayout lessonEditLL;
	private void setText()
	{
		startTimeTV.setText((start[0] < 10 ? "0" : "") + start[0] + ":" +
				(start[1] < 10 ? "0" : "") + start[1]);
		endTimeTV.setText((end[0] < 10 ? "0" : "") + end[0] + ":" +
				(end[1] < 10 ? "0" : "") + end[1]);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_edit);
		nameET = (EditText)findViewById(R.id.nameET);
		startTimeTV = (TextView)findViewById(R.id.startTimeTV);
		endTimeTV = (TextView)findViewById(R.id.endTimeTV);
		lessonEditLL = (LinearLayout)findViewById(R.id.lessonEditLL);
		start = new int[2];
		end = new int[2];
		day = getIntent().getIntExtra("day", 0);
		if (!getIntent().getBooleanExtra("new", true))
		{
			name = getIntent().getStringExtra("name");
			id = getIntent().getIntExtra("id", 0);
			start[0] = getIntent().getIntExtra("startH", 8);
			start[1] = getIntent().getIntExtra("startM", 0);
			end[0] = getIntent().getIntExtra("endH", 8);
			end[1] = getIntent().getIntExtra("endM", 0);
			nameET.setText(name);
			setText();
			Button deleteB = new Button(LessonEditActivity.this);
			lessonEditLL.addView(deleteB);
			deleteB.setText(R.string.delete);
			deleteB.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					delete(v);
				}
			});
		}
		else
		{
			start[0] = 0;
			start[1] = 0;
			end[0] = 0;
			end[1] = 0;
			id = -1;
		}
	}
	public void setStartTime(View view)
	{
		new TimePickerDialog(LessonEditActivity.this,
			new TimePickerDialog.OnTimeSetListener()
			{
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute)
				{
					start[0] = hourOfDay;
					start[1] = minute;
					setText();
				}
			}, start[0], start[1], true).show();
	}
	public void setEndTime(View view)
	{
		new TimePickerDialog(LessonEditActivity.this,
				new TimePickerDialog.OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute)
					{
						end[0] = hourOfDay;
						end[1] = minute;
						setText();
					}
				}, end[0], end[1], true).show();
	}
	public void cancel(View view)
	{
		this.finish();
	}
	public void save(View view)
	{
		if (nameET.getText().toString().isEmpty())
		{
			Toast.makeText(LessonEditActivity.this, R.string.nameNotSet, Toast.LENGTH_SHORT).show();
			return;
		}
		if (startTimeTV.getText().toString().equals(getResources().getString(R.string.none)))
		{
			Toast.makeText(LessonEditActivity.this, R.string.startTimeNotSet, Toast.LENGTH_SHORT).show();
			return;
		}
		if (endTimeTV.getText().toString().equals(getResources().getString(R.string.none)))
		{
			Toast.makeText(LessonEditActivity.this, R.string.endTimeNotSet, Toast.LENGTH_SHORT).show();
			return;
		}
		if (start[0] > end[0] || start[0] == end[0] && start[1] >= end[1])
		{
			Toast.makeText(LessonEditActivity.this, R.string.invalidTime, Toast.LENGTH_SHORT).show();
			return;
		}
		name = nameET.getText().toString();
		try
		{
			if (id != -1)
			{
				this.delete(null);
			}
			BufferedReader read = new BufferedReader(new InputStreamReader(openFileInput(
				"schedule\\" + day + ".txt")));
			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(openFileOutput(
				"schedule\\" + day + ".tmp", MODE_PRIVATE)));
			String str= "";
			int maxId = id;
			if (id == -1)
			{
				while ((str = read.readLine()) != null)
				{
					if (id < Integer.parseInt(str.split(";")[0]))
					{
						id = Integer.parseInt(str.split(";")[0]);
					}
				}
				read.close();
				read = new BufferedReader(new InputStreamReader(openFileInput(
					"schedule\\" + day + ".txt")));
			}
			while ((str = read.readLine()) != null)
			{
				if (start[0] <  Integer.parseInt(str.split(";")[2].split(":")[0]) ||
					start[0] == Integer.parseInt(str.split(";")[2].split(":")[0]) &&
					start[1] <  Integer.parseInt(str.split(";")[2].split(":")[1].split("—")[0]))
				{
					write.write(id + ";" + name + ";" + start[0] + ":" + start[1] + "—" +
						end[0] + ":" + end[1] + "\n");
				}
				write.write(str + "\n");
			}
			read.close();
			write.close();
			if (!new File("schedule\\" + day + ".txt").delete())
			{
				Toast.makeText(LessonEditActivity.this, R.string.error, Toast.LENGTH_LONG).show();
			}
			if (!new File("schedule\\" + day + ".tmp").renameTo(
				new File("schedule\\" + day + ".txt")))
			{
				Toast.makeText(LessonEditActivity.this, R.string.error, Toast.LENGTH_LONG).show();
			}
		}
		catch (FileNotFoundException e)
		{
			try
			{
				new File(getFilesDir(), "schedule\\" + day + ".txt").createNewFile();
				BufferedWriter write = new BufferedWriter(new OutputStreamWriter(openFileOutput(
					"schedule\\" + day + ".txt", MODE_PRIVATE)));
				write.write(0 + ";" + name + ";" + start[0] + ":" + start[1] + "—" +
						end[0] + ":" + end[1] + "\n");
				write.close();
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
		this.finish();
	}
	public void delete(@Nullable View view)
	{
		try
		{
			BufferedReader read = new BufferedReader(new InputStreamReader(openFileInput(
				"schedule\\" + day + ".txt")));
			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(openFileOutput(
				"schedule\\" + day + ".tmp", MODE_PRIVATE)));
			String str = "";
			while ((str = read.readLine()) != null)
			{
				if (!str.split(";")[0].equals(Integer.toString(id)))
				{
					write.write(str + "\n");
				}
			}
			read.close();
			write.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		File old = new File("schedule\\" + day + ".txt");
		File tmp = new File("schedule\\" + day + ".tmp");
		if (!old.delete())
		{
			Toast.makeText(LessonEditActivity.this, R.string.error, Toast.LENGTH_LONG).show();
		}
		if (!tmp.renameTo(old))
		{
			Toast.makeText(LessonEditActivity.this, R.string.error, Toast.LENGTH_LONG).show();
		}
		if (view != null)
		{
			this.finish();
		}
	}
}
