package ru.myitschool.jenyaiu90.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
	TextView todayTV, noteTV;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		todayTV = (TextView)findViewById(R.id.todayTV);
		noteTV = (TextView)findViewById(R.id.noteTV);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String date = dateFormat.format(new Date());
		todayTV.setText(((new Formatter()).format(getResources().getText(R.string.today).toString(),
				date)).toString());
		noteTV.setText("");
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
					String buff;
					while ((buff = note.readLine()) != null)
					{
						noteTV.setText(noteTV.getText() + "\n" + buff);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (FileNotFoundException e)
		{
			noteTV.setText(R.string.noNotes);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
