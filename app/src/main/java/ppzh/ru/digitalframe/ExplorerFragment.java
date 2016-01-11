package ppzh.ru.digitalframe;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.TransportClient;

import java.util.ArrayList;
import java.util.List;

public class ExplorerFragment extends ListFragment {
    public static final String IMAGE_PATHS = "ru.ppzh.digitalframe.image_paths";
    public static final String AUTH_TOKEN = "ru.ppzh.digitalframe.token";

    private ExplorerFragment fragment = this;
    private OnFragmentInteractionListener mListener;
    private TransportClient client;
    private ItemsList list = new ItemsList();
    private ItemsList imagesList = new ItemsList();

    private ListView listView;
    private TextView emptyListTextView;
    private ExplorerAdapter adapter = null;

    public static ListItem currentFolderItem = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);
        emptyListTextView = (TextView) view.findViewById(android.R.id.empty);
        emptyListTextView.setText(R.string.login_message);

        if (mListener.getToken() != null) {
            // TODO: access user login and replace PLACEHOLDER
            try {
                client = TransportClient.getInstance(getActivity(),
                        new Credentials("placeholder",
                                mListener.getToken()));

                listView = getListView();
                this.registerForContextMenu(listView);

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
        // TODO: show image preview if click on image
        }
    }

    public void openFolder(String path) {
        new AsyncTask<String, Void, Void>() {
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
                // Except others, client.getList() returns current folder.
                // We don't need it in list.
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

    // Interface for communicating with host activity.
    // Thought it would contain more methods.
    public interface OnFragmentInteractionListener {
        String getToken();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListItem item = (ListItem) adapter.getItem(info.position);
        if (item.isCollection()) {
            menu.setHeaderTitle(item.getDisplayName());
            menu.add(Menu.NONE, Menu.NONE, Menu.NONE, R.string.context_menu_start_slideshow);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListItem listItem = (ListItem) adapter.getItem(info.position);
        downloadImagesList(listItem.getFullPath());
        return true;
    }

    private void downloadImagesList(String fullPath) {
        new AsyncTask<String, Void, Void>() {
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
                listView.setEnabled(true);
                listView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                if (imagesList.getList().size() == 0) {
                    Toast.makeText(getActivity(), R.string.no_images, Toast.LENGTH_LONG).show();
                } else {
                    startSlideshowActivity();
                }
            }

        }.execute(fullPath);
    }

    private void startSlideshowActivity() {
        ArrayList<String> imagesPaths = getImagesPaths(imagesList);
        Intent i = new Intent(getActivity(), SlideshowActivity.class);

        i.putStringArrayListExtra(IMAGE_PATHS, imagesPaths);
        i.putExtra(AUTH_TOKEN, mListener.getToken());

        this.startActivity(i);
    }

    private ArrayList<String> getImagesPaths(ItemsList imagesList) {
        ArrayList<String> paths = new ArrayList<String>();
        List<ListItem> list = imagesList.getList();

        for (ListItem i : list) {
            paths.add(i.getFullPath());
        }

        return paths;
    }
}

