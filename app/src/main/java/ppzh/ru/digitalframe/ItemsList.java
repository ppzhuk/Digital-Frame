package ppzh.ru.digitalframe;

import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.ListParsingHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemsList extends ListParsingHandler {
    private List<ListItem> list = new ArrayList<ListItem>();

    // we are only interested in folders and images, so ignore other files.
    // TODO: add other image types
    @Override
    public boolean handleItem(ListItem item) {
        if (item.isCollection() || item.getContentType().equals("image/jpeg")) {
            list.add(item);
        }

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
