package ppzh.ru.digitalframe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.disk.client.ListItem;

public class ExplorerAdapter extends BaseAdapter {
    private Context context;
    private ItemsList items;
    private LayoutInflater lInflater;

    public ExplorerAdapter(Context context, ItemsList items) {
        this.context = context;
        this.items = items;
        lInflater = (LayoutInflater) context
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

        ImageView icon = (ImageView) view.findViewById(R.id.item_image);
        TextView name = (TextView) view.findViewById(R.id.item_name);


        if (item.isCollection()) {
            icon.setImageResource(R.drawable.ic_folder_black_24dp);
        } else {
            icon.setImageResource(R.drawable.ic_image_black_24dp);
        }

        name.setText(item.getDisplayName());

        return view;
    }
}
