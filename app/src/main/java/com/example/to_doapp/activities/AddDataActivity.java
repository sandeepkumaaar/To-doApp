package com.example.to_doapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.to_doapp.R;
import com.example.to_doapp.database.TodoDatabase;
import com.example.to_doapp.entities.Todo;

public class AddDataActivity extends AppCompatActivity {

    Toolbar toolbarSecond;
    EditText et_title, et_desc;
    Button btnCancel, btnDone;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        toolbarSecond = findViewById(R.id.toolbarSecond);
        setSupportActionBar(toolbarSecond);
        et_title = findViewById(R.id.et_title);
        et_desc = findViewById(R.id.et_desc);
        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);
        imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTodo();
            }
        });
    }

    private void saveTodo() {
        if (et_title.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Todo title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }else if (et_desc.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Todo Description can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Todo todo = new Todo();
        todo.setTitle(et_title.getText().toString());
        todo.setDescription(et_desc.getText().toString());
        todo.setCheckboxChecked(false);
        saveDataToDb(todo);

    }

    public void saveDataToDb(final Todo todo)
    {
        @SuppressLint("StaticFieldLeak")
          class SaveTodoTask extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                TodoDatabase.getTodoDatabase(getApplicationContext()).todoDao().insertTodo(todo);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                startActivity(new Intent(AddDataActivity.this, MainActivity.class));
            }
        }
        new SaveTodoTask().execute();
    }
}
