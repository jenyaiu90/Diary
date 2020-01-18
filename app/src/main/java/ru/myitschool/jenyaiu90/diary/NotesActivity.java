package ru.myitschool.jenyaiu90.diary;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesActivity extends AppCompatActivity
{
	private LinearLayout notesListLL;
	private Button noteBTs[];
	private String names[];
	private void draw()
	{
		notesListLL.removeAllViews();
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(
				"notes\\notes.txt")));
			noteBTs = null;
			int l = 0;
			for (String i; (i = br.readLine()) != null; l++)
			{
				try
				{
					BufferedReader note = new BufferedReader(new InputStreamReader(openFileInput(
							"notes\\" + i + ".txt")));
					Button noteBT = new Button(NotesActivity.this);
					notesListLL.addView(noteBT);
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
					if (noteBTs == null)
					{
						noteBTs = new Button[1];
						names = new String[1];
						noteBTs[0] = noteBT;
						names[0] = i;
					}
					else
					{
						Button tmpB[] = new Button[l + 1];
						String tmpS[] = new String[l + 1];
						for (int m = 0; m < l; m++)
						{
							tmpB[m] = noteBTs[m];
							tmpS[m] = names[m];
						}
						tmpB[l] = noteBT;
						tmpS[l] = i;
						noteBTs = tmpB;
						names = tmpS;
					}
					noteBT.setText(t);
					noteBT.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							int i = 0;
							for ( ; i < noteBTs.length; i++)
							{
								if (noteBTs[i] == v)
								{
									break;
								}
							}
							Intent noteViewA = new Intent(NotesActivity.this, NoteViewActivity.class);
							Date date = null;
							noteViewA.putExtra("date", date);
							noteViewA.putExtra("name", names[i]);
							startActivityForResult(noteViewA, 1);
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
			try
			{
				TextView noNoteTV = new TextView(NotesActivity.this);
				notesListLL.addView(noNoteTV, 0);
				noNoteTV.setText(R.string.noNotes);
				noNoteTV.setTextColor(getResources().getColor(R.color.text));
				noNoteTV.setTextSize(20);
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
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		notesListLL = (LinearLayout)findViewById(R.id.notesListLL);
		draw();
		setResult(1);
	}
	public void notesDate(View view)
	{
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
		SimpleDateFormat sdfDay = new SimpleDateFormat("d");
		new DatePickerDialog(
				NotesActivity.this,
				new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
					{
						Date tmp = new Date(year - 1900, month, dayOfMonth);
						callNotesDateA(tmp);
					}
				},
				Integer.parseInt(sdfYear.format(new Date())),
				Integer.parseInt(sdfMonth.format(new Date())) - 1,
				Integer.parseInt(sdfDay.format(new Date()))).show();
	}
	public void callNotesDateA(Date date)
	{
		Intent notesDateA = new Intent(NotesActivity.this, NotesDateActivity.class);
		notesDateA.putExtra("date", date);
		startActivityForResult(notesDateA, 1);
	}
	public void addNote(View view)
	{
		Intent noteEditA = new Intent(NotesActivity.this, NoteEditActivity.class);
		noteEditA.putExtra("new", true);
		startActivityForResult(noteEditA, 1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		draw();
	}
}
