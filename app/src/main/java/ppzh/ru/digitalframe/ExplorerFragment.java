package ppzh.ru.digitalframe;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.TransportClient;

public class ExplorerFragment extends ListFragment {
    ExplorerFragment fragment = this;
    OnFragmentInteractionListener mListener;
    TransportClient client;
    ItemsList list = new ItemsList();
    ItemsList imagesList = new ItemsList();

    ListView listView;
    TextView emptyListTextView;
    ExplorerAdapter adapter = null;

    public static ListItem currentFolderItem = null;

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
        emptyListTextView = (TextView)view.findViewById(android.R.id.empty);
        emptyListTextView.setText(R.string.login_message);

        if (mListener.getToken() != null) {

            try {
                client =  TransportClient.getInstance(getActivity(),
                                                      new Credentials("placeholder",
                                                                      mListener.getToken()));

                listView = (ListView) view.findViewById(android.R.id.list);
                this.registerForContextMenu(listView);

                ExplorerFragment c = this;

//               TODO change path to root folder
                openFolder("/");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ListItem item = (ListItem) adapter.getItem(position);
        if (item.isCollection()) {
            String path = item.getFullPath();
            openFolder(path);
        } else {
//            TODO: show image preview
        }
    }

    public void openFolder(String path) {
        new AsyncTask<String, Void, Void>(){
            @Override
            protected void onPreExecute() {
                list.clear();
                listView.setEnabled(false);
                listView.setBackgroundColor(getResources().getColor(R.color.colorNotEnabled));
            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                    client.getList(params[0], list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void s) {
                listView.setEnabled(true);
                listView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                currentFolderItem = list.getList().remove(0);

                Log.i("ExplorerFragment", currentFolderItem.getFullPath());

                fragment.getActivity().setTitle(currentFolderItem.getDisplayName());
                if (adapter == null) {
                    adapter = new ExplorerAdapter(getActivity(), list);
                    fragment.setListAdapter(adapter);
                } else {
                    emptyListTextView.setText(R.string.empty_list);
                    adapter.notifyDataSetChanged();
                }
            }

        }.execute(path);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        String getToken();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        ListItem item = (ListItem)adapter.getItem(info.position);
        if (item.isCollection()) {
            menu.setHeaderTitle(item.getDisplayName());
            menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Start slideshow");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        ListItem listItem = (ListItem)adapter.getItem(info.position);
        downloadImagesList(listItem.getFullPath());
        return true;
    }

    private void downloadImagesList(String fullPath) {
        new AsyncTask<String, Void, Void>(){
            @Override
            protected void onPreExecute() {
                imagesList.clear();
                listView.setEnabled(false);
                listView.setBackgroundColor(getResources().getColor(R.color.colorNotEnabled));
            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                    client.getList(params[0], imagesList);
                    imagesList.filterImages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void s) {
                if (imagesList.getList().size() == 0) {
                    listView.setEnabled(true);
                    listView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    Toast.makeText(getActivity(), R.string.no_images, Toast.LENGTH_LONG).show();
                } else {
                    startSlideshow();
                }

            }

        }.execute(fullPath);
    }

    private void startSlideshow() {

    }
}

