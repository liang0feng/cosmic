package kded.demo.fromplugin;

import kd.bos.bill.BillShowParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.ShowType;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.logging.LogFactory;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2020/10/27
 */
public class ShowFormPlugin extends AbstractFormPlugin {


    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        BillShowParameter billShowParameter = new BillShowParameter();
        billShowParameter.getOpenStyle().setShowType(ShowType.InContainer);
        billShowParameter.getOpenStyle().setTargetKey("kded_flexpanelap");
        billShowParameter.setFormId("kded_bill001");

        DynamicObject[] load = BusinessDataServiceHelper.load("kded_bill001", "id", null);
        Object pkId = null;
        for (DynamicObject dynamicObject : load) {
            pkId = dynamicObject.getPkValue();
        }
        billShowParameter.setPkId(pkId);
        this.getView().showForm(billShowParameter);

    }
}
