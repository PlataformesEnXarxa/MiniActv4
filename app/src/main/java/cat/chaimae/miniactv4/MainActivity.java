package cat.chaimae.miniactv4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            new AsyncClass().execute(networkInfo);
        }
    }

    public class AsyncClass extends AsyncTask<NetworkInfo, Void, List<String>> {

        @Override
        protected List<String> doInBackground(NetworkInfo... networkInfos) {
            NetworkInfo networkInfo = networkInfos[0];
            List<String> ret = new ArrayList<>();

            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                //Toast.makeText(getApplicationContext(), R.string.wifi_connected, Toast.LENGTH_SHORT).show();
                ret.add(networkInfo.toString());
                ret.add( getString(R.string.wifi_connected));
            } else if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(getApplicationContext(), R.string.mobile_connected, Toast.LENGTH_SHORT).show();
                ret.add(networkInfo.toString());
                ret.add(getString(R.string.mobile_connected));
            } else {
                //Toast.makeText(getApplicationContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                ret.add(getString(R.string.lost_connection));
                ret.add(getString(R.string.lost_connection));
            }

            return ret;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
            TextView yellow = (TextView) findViewById(R.id.yellow);
            TextView green = (TextView) findViewById(R.id.green);
            green.setText(s.get(0));
            yellow.setText(String.format("%s\n%s", yellow.getText().toString(), s.get(1)));
            Toast.makeText(MainActivity.this, s.get(1), Toast.LENGTH_SHORT).show();
        }
    }
}
