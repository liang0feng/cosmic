package kded.demo.fromplugin;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.result.IOperateInfo;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.FormShowParameter;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.PreOpenFormEventArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.EventObject;
import java.util.List;

/**
 * @author rd_feng_liang
 * @date 2021/1/22
 */
public class App01TestPlugin extends AbstractFormPlugin {
    @Override
    public void registerListener(EventObject e) {
        this.addItemClickListeners("tbmain");
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
        if ("kded_baritemap".equalsIgnoreCase(evt.getItemKey())) {
            this.getView().showMessage("app01 version 2.0");
        }
        EntryGrid entryGrid = null;
        this.getView().setEnable(true, "card_name");
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
    }

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        OperationResult operationResult = afterDoOperationEventArgs.getOperationResult();
        List<IOperateInfo> allErrorOrValidateInfo = operationResult.getAllErrorOrValidateInfo();
    }

    @Override
    public void preOpenForm(PreOpenFormEventArgs e) {
        super.preOpenForm(e);
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        System.out.println(formShowParameter);
    }
}
