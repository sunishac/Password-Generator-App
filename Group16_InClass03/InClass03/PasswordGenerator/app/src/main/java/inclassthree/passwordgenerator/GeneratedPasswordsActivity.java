package inclassthree.passwordgenerator;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GeneratedPasswordsActivity extends AppCompatActivity {
    private ListView listView;
    private ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        ArrayList<String> Thread_Pwds = getIntent().getStringArrayListExtra("Thread_Pwds");
        ArrayList<String> Async_Pwds = getIntent().getStringArrayListExtra("Async_Pwds");
        Log.i("Thread: ", String.valueOf(Thread_Pwds));
        Log.i("Async: ", String.valueOf(Async_Pwds));
        listView = (ListView)findViewById(R.id.listview1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Thread_Pwds);
        listView.setAdapter(adapter);
        listView1 = (ListView)findViewById(R.id.listview2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Async_Pwds);
        listView1.setAdapter(adapter1);
    }

    public void goBack(View view) {
        Intent intent = new Intent(GeneratedPasswordsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
