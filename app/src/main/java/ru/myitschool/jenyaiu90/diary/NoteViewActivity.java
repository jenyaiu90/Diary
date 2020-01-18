package ru.myitschool.jenyaiu90.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteViewActivity extends AppCompatActivity
{
	private TextView nameTV, noteViewTV;
	private Date date;
	private String name;
	private void draw()
	{
		try
		{
			BufferedReader note;
			if (date == null)
			{
				note = new BufferedReader(new InputStreamReader(openFileInput("notes\\" + name + ".txt")));
			}
			else
			{
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				note = new BufferedReader(new InputStreamReader(openFileInput("notes\\" + sdf.format(date) + "\\" + name + ".txt")));
			}
			String buff, t = "";
			int j = 0;
			outer:
			while ((buff = note.readLine()) != null)
			{
				t += buff;
			}
			noteViewTV.setText(t);
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
		setContentView(R.layout.activity_note_view);
		nameTV = (TextView)findViewById(R.id.nameTV);
		noteViewTV = (TextView)findViewById(R.id.noteViewTV);
		date = (Date)getIntent().getSerializableExtra("date");
		name = getIntent().getStringExtra("name");
		nameTV.setText(name);
		draw();
		setResult(1);
	}
	public void edit(View view)
	{
		Intent noteEditA = new Intent(NoteViewActivity.this, NoteEditActivity.class);
		noteEditA.putExtra("new", false);
		noteEditA.putExtra("name", name);
		noteEditA.putExtra("date", date);
		startActivityForResult(noteEditA, 1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		draw();
	}
}
