package ppzh.ru.digitalframe;

import android.app.ListActivity;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExplorerActivity extends AppCompatActivity
        implements ExplorerFragment.OnFragmentInteractionListener {
    public static final String APP_ID = "ec10556277544ff495a5e04c7dd44e0b";
    public static final String APP_KEY = "cd1912116ffd4ecd9cd5bd34af79c723";
    private String token = null;

    private static final String TAG = "ExplorerActivity";
    private static final String DEVICE_ID = "ru.ppzh.digitalfrane.DEVICE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri url = getIntent().getData();
        if (url != null) {
            String fragment = url.getFragment();
            if (fragment != null && fragment.startsWith("access_token=")) {
                token = extractToken(fragment);

                Log.i(TAG, fragment);
                Log.i(TAG, token);
            }
        }

        setContentView(R.layout.activity_explorer);

    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (token == null) {
            inflater.inflate(R.menu.login_options_menu, menu);
        } else {
            inflater.inflate(R.menu.explorer_options_menu, menu);
        }

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

    public String extractToken(String fragment) {
        int start = "access_token=".length();
        int end = fragment.indexOf('&');
        return fragment.substring(start, end);
    }
}
