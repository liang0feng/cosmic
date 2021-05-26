package kded.online.demo;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.IListView;
import kd.bos.list.ListGridView;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.service.PermissionServiceImpl;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.servicehelper.permission.PermissionServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.EventObject;
import java.util.List;

/**
 * 【子菜单项点击常用事件】——办公用品登记单 入库
 *  需求：
 *      点击入库时，判断当前用户 角色 是否是仓库管理员。
 *          1.不是则提示，仅限仓库管理员操作！
 *          2.是仓库管理员：将分录物品数量累加物品库存
 *
 * @author rd_feng_liang
 * @date 2021/4/15
 */
public class DyBillDemoPlugin03 extends AbstractFormPlugin {
    /**
     * @param e
     */
    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners("tbmain");
    }
    /**
     * 1、点击入库按钮前：校验用户角色是否有权限
     * @param evt
     */
    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
        if ("kded_warehousing".equals(evt.getItemKey())) {
            List<Long> usersByRoleNum = PermissionServiceHelper.getUsersByRoleNum("Role-000012");

            //非仓库管理员：取消事件，itemclick不执行
            if (!usersByRoleNum.contains(Long.valueOf(RequestContext.get().getUserId()))) {
                this.getView().showErrorNotification("入库失败，仅限仓库管理员操作！");
                evt.setCancel(true);
            }

        }
    }
    /**
     * 2、分录办公物品，入库业务逻辑：累加物品库存
     * @param evt
     */
    @Override
    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        if (!"kded_warehousing".equals(evt.getItemKey())) {
            return;
        }
        //入库业务：
        /**
         * 1、遍历分录
         * 2、从数据库查询分录行对应的物品，累加物品数量
         * 3、更新物品数量到物品库存
         */
        DynamicObjectCollection entryEntity = this.getModel().getEntryEntity("kded_entryentity");
        DynamicObject[] db_supplies = null;

        int i = 0;
        for (DynamicObject entry : entryEntity) {
            DynamicObject page_supplies = entry.getDynamicObject("kded_supplies");
            //查询库存记录
            db_supplies = BusinessDataServiceHelper.load("kded_supsinventorybill",
                    "kded_supplies,kded_inventoryqty",
                    new QFilter[]{new QFilter("kded_supplies", QCP.equals, page_supplies.getPkValue())});

            if (db_supplies == null || db_supplies.length <= 0) {
                this.getView().showErrorNotification("没有物品库存记录，请手动新增");
                return;
            }
            //计算数量并更新到数据库
            db_supplies[0].set("kded_inventoryqty", entry.getBigDecimal("kded_qty")
                    .add(db_supplies[0].getBigDecimal("kded_inventoryqty")));
            SaveServiceHelper.update(db_supplies[0]);
        }
        this.getView().showSuccessNotification("入库成功！");
    }
}
