package ppzh.ru.digitalframe;

import android.util.Log;

import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.ListParsingHandler;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Pavel on 10.01.2016.
 */
public class ItemsList extends ListParsingHandler {
    private List<ListItem> list = new ArrayList<ListItem>();

    @Override
    public boolean handleItem(ListItem item) {
        if (item.isCollection() || item.getContentType().equals("image/jpeg")) {
            list.add(item);
        }

//      what to return??
        return true;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void clear() {
        list.clear();
    }

    public List<ListItem> filterImages() {
        int i = 0;
        while (i < list.size()) {
            if (list.get(i).isCollection()) {
                list.remove(i);
            } else {
                ++i;
            }
        }
        return list;
    }
}
