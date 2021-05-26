package kded.demo.fromplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.EntryType;
import kd.bos.entity.property.EntryProp;
import kd.bos.form.control.events.EntryGridBindDataListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.QueryServiceHelper;

import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2021/2/20
 */
public class EntryGridDataBindTestPlugin01 extends AbstractFormPlugin implements EntryGridBindDataListener{


    @Override
    public void afterCreateNewData(EventObject e) {
        Object source = e.getSource();
        EntryProp entryProp = (EntryProp)this.getModel().getDataEntityType().getProperties().get("kded_treeentryentity");
        EntryType entryType = (EntryType) entryProp.getItemType();
        DynamicObject row = null;

        DynamicObjectCollection rows = this.getModel().getDataEntity(true).getDynamicObjectCollection("kded_treeentryentity");
        DynamicObjectCollection dbrows = null;
        dbrows = QueryServiceHelper.query(
                "kded_bill210218", "id,kded_treeentryentity.id,kded_treeentryentity.seq,kded_treeentryentity.pid,kded_treeentryentity.kded_integerfield,kded_treeentryentity.kded_textfield",
                null,
                "id,kded_treeentryentity.seq asc");
//        boolean secd = DB.query(DBRoute.of("secd"), "select * from tk_kded_treeentryentity ordyer fid,fseq desc", r -> {
//            return null;
//        });
            DynamicObjectCollection dynamicObjects = new DynamicObjectCollection();
        for (DynamicObject dbrow : dbrows) {
            row = new DynamicObject(entryType);
            row.set("id", dbrow.get("kded_treeentryentity.id"));
            row.set("seq", dbrow.get("kded_treeentryentity.seq"));
            row.set("pid", dbrow.get("kded_treeentryentity.pid"));
            row.set("kded_integerfield", dbrow.get("kded_treeentryentity.kded_integerfield"));
            row.set("kded_textfield", dbrow.get("kded_treeentryentity.kded_textfield"));
            dynamicObjects.add(row);
        }
        rows.addAll(dynamicObjects);

    }

}
