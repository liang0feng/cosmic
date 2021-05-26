package kded.demo.fromplugin;

import java.sql.ResultSet;
import java.util.*;

import kd.bos.algo.DataSet;
import kd.bos.algo.Row;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.db.DB;
import kd.bos.db.DBRoute;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.*;
import kd.bos.form.events.BeforeFieldPostBackEvent;
import kd.bos.form.field.AttachmentEdit;
import kd.bos.form.field.TextEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.workflow.WorkflowServiceHelper;

public class TestPlugin1 extends AbstractFormPlugin {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners("tbmain");
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        String name = e.getProperty().getName();
        if (name.equals("kded_checkboxfield")) {
            this.getModel().deleteEntryData("kded_entryentity");
        }
    }



    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
//        QFilter qFilter = QFilter.of("createtime=?", "{ts '2021-05-06 09:50:59'}");
        QFilter qFilter = QFilter.sqlExpress("createtime", "=", "{ts '2021-03-31 14:48:42'}");
        DynamicObject[] load = BusinessDataServiceHelper.load("bos_user", "id,number,name,createtime", new QFilter[]{qFilter});
        for (DynamicObject dynamicObject : load) {
            dynamicObject.getString("name");
        }
    }

    @Override
    public void beforeFieldPostBack(BeforeFieldPostBackEvent e) {
        Object source = e.getSource();
        if (source instanceof TextEdit) {
            DynamicObjectCollection kded_entryentity = this.getModel().getEntryEntity("kded_entryentity");
            if (kded_entryentity == null || kded_entryentity.isEmpty()) {
                e.setCancel(true);
            }
        }
    }
}
