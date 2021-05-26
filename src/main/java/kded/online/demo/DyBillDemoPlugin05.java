package kded.online.demo;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.ChangeData;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.FormShowParameter;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.IListView;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.Date;
import java.util.EventObject;

/**
 * 【其它界面交互常用事件】——办公物品领用单据
 * 需求 ：
 *      1、根据物品分类过滤物品可选范围
 *      2、先选择物品后，若物品对应分类为空，则自动带出物品分类基础资料
 *
 * @author rd_feng_liang
 * @date 2021/4/15
 */
public class DyBillDemoPlugin05 extends AbstractFormPlugin implements BeforeF7SelectListener {

    /**
     *
     * @param e
     */
    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        //添加f7Selecte事件监听
        BasedataEdit supplies = this.getControl("kded_supplies");
        supplies.addBeforeF7SelectListener(this);
    }
    /**
     * 1、根据物品分类过滤物品可选范围
     * @param beforeF7SelectEvent
     */
    @Override
    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {
        int row = beforeF7SelectEvent.getRow();
        DynamicObject suppliestype = (DynamicObject) this.getModel().getValue("kded_suppliestype", row);

        //物品分类为空处理
        if (suppliestype == null ) {
            return;
        }
        //根据物品分类，添加物品过滤器
        QFilter qfilter = new QFilter("group", QCP.equals, suppliestype.getPkValue());
        ListShowParameter showParameter = (ListShowParameter) beforeF7SelectEvent.getFormShowParameter();
        showParameter.getListFilterParameter().setFilter(qfilter);
    }


    /**
     * 值更新事件：
     *  2、先选择物品后，若物品对应分类为空，则自动带出物品分类基础资料
     * @param e
     */
    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        ChangeData[] changeSet = e.getChangeSet();

        String name = e.getProperty().getName();
        if ("kded_supplies".equals(name)) {
            Object newValue = changeSet[0].getNewValue();
            //新值为空：return;
            if (newValue == null) {
                return;
            }

            //物品分类不为空：return;
            int rowIndex = changeSet[0].getRowIndex();
            if (this.getModel().getValue("kded_suppliestype") != null) {
                return;
            }

            //物品分类为空：自动带出物品分类
            DynamicObject newValueDyObj = (DynamicObject) newValue;
            DynamicObject supplies = BusinessDataServiceHelper.loadSingle(newValueDyObj.getPkValue(),
                    "kded_supplies");
            this.getModel().setValue("kded_suppliestype", supplies.getDynamicObject("group"),
                    rowIndex);

        }
    }
}
