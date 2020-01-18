package ru.myitschool.jenyaiu90.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class NotesDateActivity extends AppCompatActivity
{
	private TextView dateTV;
	private LinearLayout notesDateLL;
	private Date date;
	private Button noteBTs[];
	private String names[];
	private void draw()
	{
		String dateStr = new SimpleDateFormat("dd.MM.yyyy").format(date);
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(
					"notes\\" + dateStr + "\\notes.txt")));
			noteBTs = null;
			int l = 0;
			for (String i; (i = br.readLine()) != null; l++)
			{
				try
				{
					BufferedReader note = new BufferedReader(new InputStreamReader(openFileInput(
							"notes\\" + dateStr + "\\" + i + ".txt")));
					Button noteBT = new Button(NotesDateActivity.this);
					notesDateLL.addView(noteBT);
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
							Intent noteViewA = new Intent(NotesDateActivity.this, NoteViewActivity.class);
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
			TextView noNoteTV = new TextView(NotesDateActivity.this);
			notesDateLL.addView(noNoteTV, 0);
			noNoteTV.setText(R.string.noNotes);
			noNoteTV.setTextColor(getResources().getColor(R.color.text));
			noNoteTV.setTextSize(20);
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
		setContentView(R.layout.activity_notes_date);
		date = (Date)getIntent().getSerializableExtra("date");
		dateTV = (TextView)findViewById(R.id.dateTV);
		dateTV.setText(((new Formatter()).format(getResources().getText(
			R.string.todayNotes).toString(), new SimpleDateFormat("dd.MM.yyyy").format(
			date))).toString());
		notesDateLL = (LinearLayout)findViewById(R.id.notesDateLL);
		draw();
		setResult(1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		draw();
	}
}
