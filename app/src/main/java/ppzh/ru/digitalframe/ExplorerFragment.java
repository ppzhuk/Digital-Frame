package ppzh.ru.digitalframe;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.ListParsingHandler;
import com.yandex.disk.client.TransportClient;
import com.yandex.disk.client.exceptions.CancelledPropfindException;
import com.yandex.disk.client.exceptions.PreconditionFailedException;
import com.yandex.disk.client.exceptions.ServerWebdavException;
import com.yandex.disk.client.exceptions.UnknownServerWebdavException;
import com.yandex.disk.client.exceptions.WebdavClientInitException;
import com.yandex.disk.client.exceptions.WebdavFileNotFoundException;
import com.yandex.disk.client.exceptions.WebdavForbiddenException;
import com.yandex.disk.client.exceptions.WebdavInvalidUserException;
import com.yandex.disk.client.exceptions.WebdavNotAuthorizedException;
import com.yandex.disk.client.exceptions.WebdavUserNotInitialized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExplorerFragment extends ListFragment {

    OnFragmentInteractionListener mListener;
    TransportClient client;
    ItemsList list = new ItemsList();

    ListView listView;
    ExplorerAdapter adapter;

    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExplorerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);
        TextView emptyListTextView = (TextView)view.findViewById(android.R.id.empty);
        emptyListTextView.setText(R.string.login_message);

        if (mListener.getToken() != null) {

            try {
                client =  TransportClient.getInstance(getActivity(),
                                                      new Credentials("placeholder",
                                                                      mListener.getToken()));

                listView = (ListView) view.findViewById(android.R.id.list);
                final ExplorerFragment c = this;
                new AsyncTask<Void, Void, Void>(){
                    @Override
                    protected void onPreExecute() {
                        listView.setEnabled(false);
                        listView.setBackgroundColor(getResources().getColor(R.color.colorNotEnabled));
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            client.getList("/photo_test/", list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void s) {
                        listView.setEnabled(true);
                        listView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        adapter = new ExplorerAdapter(getActivity(), list);
                        c.setListAdapter(adapter);
                    }

                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        String getToken();
    }
}

