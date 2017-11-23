package inclassthree.passwordgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    private TextView txtViewThreadPwdCountValue;
    private TextView txtViewThreadPwdLengthValue;
    private TextView txtViewAsyncPwdCountValue;
    private TextView txtViewAsyncPwdLengthValue;
    private SeekBar skBarThreadPwdCount;
    private SeekBar skBarThreadPwdLength;
    private SeekBar skBarAsyncPwdCount;
    private SeekBar skBarAsyncPwdLength;
    private Button btnGenerate;
    private ProgressDialog progressDialog;

    private int pwdCountThread = 0;
    private int pwdLengthThread = 7;
    private int pwdCountAsync = 0;
    private int pwdLengthAsync = 7;

    ExecutorService threadpool;
    Handler handler;
    ArrayList<String> passarraythread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewThreadPwdCountValue = (TextView)findViewById(R.id.thread_pwd_count);
        txtViewThreadPwdLengthValue = (TextView)findViewById(R.id.thread_pwd_length);
        txtViewAsyncPwdCountValue = (TextView)findViewById(R.id.async_pwd_count);
        txtViewAsyncPwdLengthValue = (TextView)findViewById(R.id.async_pwd_length);

        skBarThreadPwdCount = (SeekBar)findViewById(R.id.thread_pwd_count_seek);
        skBarThreadPwdLength = (SeekBar)findViewById(R.id.thread_pwd_length_seek);
        skBarAsyncPwdCount = (SeekBar)findViewById(R.id.async_pwd_count_seek);
        skBarAsyncPwdLength = (SeekBar)findViewById(R.id.async_pwd_length_seek);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(1);
        progressDialog.setMessage("Generating Passwords");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        btnGenerate = (Button)findViewById(R.id.generateBtn);
        skBarThreadPwdCount.setMax(9);
        skBarThreadPwdCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwdCountThread = progress + 1;
                txtViewThreadPwdCountValue.setText(String.valueOf(pwdCountThread));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skBarThreadPwdLength.setMax(16);
        skBarThreadPwdLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwdLengthThread = progress + 7;
                txtViewThreadPwdLengthValue.setText(String.valueOf(pwdLengthThread));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skBarAsyncPwdCount.setMax(9);
        skBarAsyncPwdCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwdCountAsync = progress + 1;
                txtViewAsyncPwdCountValue.setText(String.valueOf(pwdCountAsync));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skBarAsyncPwdLength.setMax(16);
        skBarAsyncPwdLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwdLengthAsync = progress + 7;
                txtViewAsyncPwdLengthValue.setText(String.valueOf(pwdLengthAsync));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        passarraythread= new ArrayList();
        threadpool= Executors.newFixedThreadPool(2);

        btnGenerate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(pwdCountThread<1 || pwdCountThread>10 || pwdLengthThread<7 || pwdLengthThread > 23 || pwdCountAsync<1 || pwdCountAsync>10 || pwdLengthAsync<7 || pwdLengthAsync > 23){
                    Toast.makeText(getApplicationContext(), "Please set all values correctly!",Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(pwdCountAsync + pwdCountThread);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(getResources().getString(R.string.generating_pwds_dialog));
                    progressDialog.show();

                    Log.i("Async passwords: ", String.valueOf(new AsyncPassword().execute(pwdCountAsync, pwdLengthAsync)));
                    ThreadPassword(pwdCountThread, pwdLengthThread);
                }
            }
        });
    }

    public void ThreadPassword(int pwdCountThread, int pwdLengthThread) {
        Bundle b= new Bundle();
        b.putInt("Length",pwdLengthThread);

        for(int i=0;i<pwdCountThread;i++)
        {
            threadpool.execute(new threadwork());

            //          progressDialog.setProgress((int)((100/pwdCountThread)*(i+1)));
        }

        final CharSequence[] cs=passarraythread.toArray(new CharSequence[passarraythread.size()]);
    }

    public class AsyncPassword extends AsyncTask<Integer, Integer, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Integer... params)
        {

            ArrayList<String> passwords=new ArrayList<String>();

            for(int i=0;i<params[0];i++) {
                passwords.add(Util.getPassword(params[1]));

//                Log.i("Async pwds: ", String.valueOf(passwords));
                progressDialog.setProgress((int)((100/params[0])*(i+1)));
            }
            return passwords;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            //delegate.processFinish();
            Log.i("Async pwds: ", String.valueOf(s));

            Log.i("Thread pwds: ", String.valueOf(passarraythread));

            Intent intent = new Intent(MainActivity.this, GeneratedPasswordsActivity.class);
            intent.putExtra("Thread_Pwds",passarraythread);
            intent.putExtra("Async_Pwds",s);
            startActivity(intent);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);

        }
    }

    private class threadwork implements Runnable {
        @Override
        public void run() {

            String passtemp;
            passtemp=Util.getPassword(pwdLengthThread);
            passarraythread.add(passtemp);
            //           Log.i("Thread pwds: ", String.valueOf(passarraythread));
        }
    }


}



