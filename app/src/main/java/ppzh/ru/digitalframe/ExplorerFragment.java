package ppzh.ru.digitalframe;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExplorerFragment extends ListFragment {

    OnFragmentInteractionListener mListener;

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
//      Mock data
        List<String> list = new ArrayList<>();
        if (mListener.getToken() != null) {
            list.add("111");
            list.add("222");
            list.add("333");
            list.add("111");
            list.add("222");
            list.add("333");
            list.add("111");
            list.add("222");
            list.add("333");
            list.add("111");
            list.add("222");
            list.add("333");
            list.add("111");
            list.add("222");
            list.add("333");
            list.add("111");
            list.add("222");
            list.add("333");
            list.add("111");
            list.add("222");
            list.add("333");
        }
//      ---------------

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this.getActivity(), R.layout.explorer_list_item, R.id.item_name);
        adapter.addAll(list);
        this.setListAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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

