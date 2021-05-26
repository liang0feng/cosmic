package kded.demo.fromplugin;

import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.events.ListRowClickEvent;
import kd.bos.list.events.ListRowClickListener;

import java.util.Comparator;
import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2021/2/20
 */
public class UserF7SelectedSortPluginDemo extends AbstractFormPlugin implements ListRowClickListener {

    @Override
    public void registerListener(EventObject e) {
        BillList list = this.getControl("billlistap");
        list.addListRowClickListener(this);
    }

    @Override
    public void listRowClick(ListRowClickEvent evt) {
        ListSelectedRowCollection listSelectedRowCollection = evt.getListSelectedRowCollection();
        listSelectedRowCollection.sort(new Comparator<ListSelectedRow>() {
            @Override
            public int compare(ListSelectedRow o1, ListSelectedRow o2) {
                //按姓名排序升序
                return o1.getName().compareTo(o2.getName());
            }
        });
//        evt.setListSelectedRowCollection(listSelectedRowCollection);
//        F7SelectedList f7selectedlistap = this.getControl("f7selectedlistap");
    }
}
