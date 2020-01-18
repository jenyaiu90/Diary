package ru.myitschool.jenyaiu90.diary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class TaskEditActivity extends AppCompatActivity
{
	private EditText taskET;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_edit);
		taskET = (EditText)findViewById(R.id.taskET);
		taskET.setText(getIntent().getStringExtra("task"));
	}
	public void saveClick(View view)
	{
		Intent res = new Intent();
		res.putExtra("res", taskET.getText().toString());
		setResult(RESULT_OK, res);
		finish();
	}
	public void cancelClick(View view)
	{
		Intent res = new Intent();
		res.putExtra("res", "");
		setResult(RESULT_CANCELED, res);
		finish();
	}
}
