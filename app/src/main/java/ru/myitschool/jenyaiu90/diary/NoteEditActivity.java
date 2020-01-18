package ru.myitschool.jenyaiu90.diary;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEditActivity extends AppCompatActivity
{
	private EditText titleET, noteEditET;
	private LinearLayout headerLL;
	private Button dateBT;
	private Date date, newDate;
	private String name;
	private boolean isNew;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_edit);
		titleET = (EditText)findViewById(R.id.titleET);
		noteEditET = (EditText)findViewById(R.id.noteEditET);
		headerLL = (LinearLayout)findViewById(R.id.headerLL);
		dateBT = (Button)findViewById(R.id.dateBT);
		isNew = getIntent().getBooleanExtra("new", true);
		if (!isNew)
		{
			Button deleteBT = new Button(NoteEditActivity.this);
			headerLL.addView(deleteBT);
			deleteBT.setText(R.string.deleteNote);
			deleteBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					delete(v);
				}
			});
			titleET.setText(name = getIntent().getStringExtra("name"));
			date = (Date)getIntent().getSerializableExtra("date");
			newDate = date;
			try
			{
				BufferedReader note;
				if (date == null)
				{
					dateBT.setText(R.string.setDate);
					note = new BufferedReader(new InputStreamReader(openFileInput("notes\\" + name + ".txt")));
				}
				else
				{
					dateBT.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
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
				noteEditET.setText(t);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			dateBT.setText(R.string.setDate);
		}
		setResult(1);
	}
	public void dateClick(View view)
	{
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
		SimpleDateFormat sdfDay = new SimpleDateFormat("d");
		new DatePickerDialog(
			NoteEditActivity.this,
			new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
				{
					newDate = new Date(year - 1900, month, dayOfMonth);
					dateBT.setText(new SimpleDateFormat("dd.MM.yyyy").format(newDate));
				}
			},
			Integer.parseInt(sdfYear.format(new Date())),
			Integer.parseInt(sdfMonth.format(new Date())) - 1,
			Integer.parseInt(sdfDay.format(new Date()))).show();
	}
	public void clearDate(View view)
	{
		newDate = null;
		dateBT.setText(R.string.setDate);
	}
	public void save(View view)
	{
		if (titleET.getText().toString().equals(""))
		{
			Toast.makeText(NoteEditActivity.this, R.string.noName, Toast.LENGTH_LONG).show();
			return;
		}
		if (noteEditET.getText().toString().equals(""))
		{
			Toast.makeText(NoteEditActivity.this, R.string.empty, Toast.LENGTH_LONG).show();
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		try
		{
			String tmpName = titleET.getText().toString();
			String noteS;
			String listS;
			String notesList = "";
			if (newDate == null)
			{
				noteS = "notes\\" + tmpName + ".txt";
				listS = "notes\\notes.txt";
			}
			else
			{
				noteS = "notes\\" + sdf.format(newDate) + "\\" + tmpName + ".txt";
				listS = "notes\\" + sdf.format(newDate) + "\\notes.txt";
			}
			try
			{
				BufferedReader listRead = new BufferedReader(new InputStreamReader(openFileInput(listS)));
				if (!tmpName.equals(name))
				{
					if ("notes".equals(name))
					{
						Toast.makeText(NoteEditActivity.this, R.string.nameNotNotes, Toast.LENGTH_LONG).show();
						return;
					}
					String buff;
					while ((buff = listRead.readLine()) != null)
					{
						if (buff.equals(tmpName))
						{
							Toast.makeText(NoteEditActivity.this, R.string.nameExists, Toast.LENGTH_LONG).show();
							listRead.close();
							return;
						}
						notesList += buff + "\n";
					}
					listRead.close();
				}
			}
			catch (FileNotFoundException e)
			{
				new File(getFilesDir(), listS).createNewFile();
			}
			if (!isNew)
			{
				remove();
			}
			new File(getFilesDir(), noteS).createNewFile();
			BufferedWriter note = new BufferedWriter(new OutputStreamWriter(openFileOutput(
				noteS, MODE_PRIVATE)));
			BufferedWriter list = new BufferedWriter(new OutputStreamWriter(openFileOutput(
				listS, MODE_PRIVATE)));
			list.write(notesList + tmpName);
			list.close();
			note.write(noteEditET.getText().toString());
			note.close();
			finish();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void cancel(View view)
	{
		finish();
	}
	private void remove()
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			new File(getFilesDir(), "temp.txt").createNewFile();
			BufferedReader read;
			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(openFileOutput(
				"temp.txt", MODE_PRIVATE)));
			if (date == null)
			{
				new File(getFilesDir(), "notes\\" +  name + ".txt").delete();
				read = new BufferedReader(new InputStreamReader(openFileInput(
					"notes\\notes.txt")));
			}
			else
			{
				new File(getFilesDir(), "notes\\" + sdf.format(date) + "\\" + name + ".txt").delete();
				read = new BufferedReader(new InputStreamReader(openFileInput(
					"notes\\" + sdf.format(date) + "\\notes.txt")));
			}
			String buff;
			while ((buff = read.readLine()) != null)
			{
				if (!buff.equals(name))
				{
					write.write(buff + "\n");
				}
			}
			read.close();
			write.close();
			if (date == null)
			{
				new File(getFilesDir(), "notes\\notes.txt").delete();
				new File(getFilesDir(), "temp.txt").renameTo(new File(getFilesDir(), "notes\\notes.txt"));
			}
			else
			{
				new File(getFilesDir(), "notes\\" + sdf.format(date) + "\\notes.txt").delete();
				new File(getFilesDir(), "temp.txt").renameTo(new File(getFilesDir(), "notes\\" + sdf.format(date) + "\\notes.txt"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void delete(View view)
	{
		remove();
		finish();
	}
}
