package ppzh.ru.digitalframe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.disk.client.ListItem;

/**
 * Created by Pavel on 10.01.2016.
 */
// TODO: manage disk content
public class ExplorerAdapter extends BaseAdapter {
    private Context context;
    private ItemsList items;
    private LayoutInflater lInflater;

    public ExplorerAdapter(Context c, ItemsList list) {
        this.context = c;
        this.items = list;
        lInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return items.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.explorer_list_item, parent, false);
        }

        ListItem item = (ListItem) getItem(position);

        ImageView icon = (ImageView)view.findViewById(R.id.item_image);
        TextView name = (TextView)view.findViewById(R.id.item_name);

        // TODO: filter only folders and jpegs
        if (item.isCollection()) {
            icon.setImageResource(R.drawable.ic_folder_black_24dp);
        } else {
            icon.setImageResource(R.drawable.ic_image_black_24dp);
        }

        name.setText(item.getDisplayName());

        return view;
    }
}
