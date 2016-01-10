package ppzh.ru.digitalframe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.net.URI;
import java.util.UUID;

public class ExplorerActivity extends AppCompatActivity {
    private static final String TAG = "ExplorerActivity";
    private static final String DEVICE_ID = "ru.ppzh.digitalfrane.DEVICE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        Intent i = getIntent();
        Uri url = i.getData();
        if (url != null)
            Log.i(TAG, url.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                yandexDiskLogin();
                return true;
            // TODO: add menu actions
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void yandexDiskLogin() {
        String authURL = generateAuthURL();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(authURL));
        startActivity(i);
    }

    private String generateAuthURL() {
        StringBuilder url =
                new StringBuilder("https://oauth.yandex.ru/authorize?response_type=token");
        url.append("&client_id=ec10556277544ff495a5e04c7dd44e0b");

        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        String device_id = sp.getString(DEVICE_ID, null);
        if (device_id == null) {
            device_id = UUID.randomUUID().toString();
            sp.edit().putString(DEVICE_ID, device_id).apply();
        }

        Log.i(TAG, "device_id - " + device_id);

        url.append("&device_id=");
        url.append(device_id);
        url.append("&device_name=");
        url.append(android.os.Build.MODEL);

        return url.toString();
    }
}
