package ru.myitschool.jenyaiu90.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class MainActivity extends AppCompatActivity
{
	private TextView todayTV;
	private LinearLayout notesLL;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		todayTV = (TextView)findViewById(R.id.todayTV);
		notesLL = (LinearLayout)findViewById(R.id.notesLL);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String date = dateFormat.format(new Date());
		todayTV.setText(((new Formatter()).format(getResources().getText(R.string.todayNotes).toString(),
				date)).toString());
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(
				"notes\\" + date + "\\notes.txt")));
			for (String i; (i = br.readLine()) != null; )
			{
				try
				{
					BufferedReader note = new BufferedReader(new InputStreamReader(openFileInput(
						"notes\\" + date + "\\" + i + ".txt")));
					Button noteBT = new Button(MainActivity.this);
					notesLL.addView(noteBT);
					noteBT.setTextSize(15);
					String buff, t = "";
					int j = 0;
					outer:
					while ((buff = note.readLine()) != null)
					{
						for (int k = 0; k < buff.length(); k++, j++)
						{
							if (j >= 100)
							{
								t += "...";
								break outer;
							}
							else
							{
								t += buff.charAt(k);
							}
						}
					}
					noteBT.setText(t);
					noteBT.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							//Call NoteViewActivity
						}
					});
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (FileNotFoundException e)
		{
			TextView noNoteTV = new TextView(MainActivity.this);
			notesLL.addView(noNoteTV, 0);
			noNoteTV.setText(R.string.noNotes);
			noNoteTV.setTextColor(getResources().getColor(R.color.text));
			noNoteTV.setTextSize(20);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void scheduleClick(View view)
	{
		Intent scheduleViewA = new Intent(MainActivity.this, ScheduleViewActivity.class);
		startActivity(scheduleViewA);
	}
	public void notesClick(View view)
	{
		//Call NotesActivity
	}
}
