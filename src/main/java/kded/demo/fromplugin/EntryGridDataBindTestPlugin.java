package kded.demo.fromplugin;

import kd.bos.entity.datamodel.RowDataEntity;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.EntryGridBindDataEvent;
import kd.bos.form.control.events.EntryGridBindDataListener;
import kd.bos.form.plugin.AbstractFormPlugin;

import java.util.EventObject;
import java.util.List;
import java.util.Map;

/**
 * @author rd_feng_liang
 * @date 2021/2/19
 */
public class EntryGridDataBindTestPlugin extends AbstractFormPlugin implements EntryGridBindDataListener {

    @Override
    public void registerListener(EventObject e) {

        EntryGrid control = this.getControl("kded_treeentryentity");
//        control.addDataBindListener(this);
        EntryGrid entryGrid = this.getControl("kded_entryentity");
    }



//    @Override
//    public void entryGridBindData(EntryGridBindDataEvent e) {
//        Map<String, Object> data = e.getData();
//        List<RowDataEntity> rows = e.getRows();
//        int startIndex = e.getStartIndex();
//        System.out.println(data);
//    }
//
//    @Override
//    public void beforeBindData(EventObject e) {
//
//        Object source = e.getSource();
//        EntryGrid control = this.getControl("kded_treeentryentity");
//        control.addDataBindListener(this);
//    }
}
