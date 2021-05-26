package kded.online.demo;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.Audit;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.TabSelectListener;
import kd.bos.form.control.events.TreeMenuClickListener;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.operate.formop.DeleteEntry;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.IListView;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.EventObject;
import java.util.Iterator;

/**
 * 【操作代码执行(前后)事件】——办公物品领用单
 * 需求：
 *    删除分录操作时校验单据状态
 *      1、只能删除暂存
 *      2、删除成功给出提示
 * @author rd_feng_liang
 * @date 2021/4/15
 */
public class DyBillDemoPlugin04 extends AbstractFormPlugin {

    /**
     *
     * @param e
     */
    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners("toolbarap");
        this.addItemClickListeners("kded_advcontoolbarap");
        this.addItemClickListeners("toolbar");

    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
        System.out.println(evt.getItemKey());
    }

    /**
     * 删除分录前校验
     * @param args
     */
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        //如果是删除分录操作  && 单据状态为 暂存
        if ((args.getSource() instanceof DeleteEntry) && !"A".equals(this.getModel().getValue("billstatus"))) {
            this.getView().showErrorNotification("只能删除暂存状态的单据分录！");
            args.setCancel(true);
        }
    }
    /**
     * 删除成功给出提示
     * @param afterDoOperationEventArgs
     */
    @Override
    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        if (afterDoOperationEventArgs.getOperateKey().equals("deleteentry")) {
            this.getView().showSuccessNotification("分录行删除成功！");
        }
    }
}
