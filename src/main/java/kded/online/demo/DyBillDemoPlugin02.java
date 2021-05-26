package kded.online.demo;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.workflow.MessageCenterServiceHelper;
import kd.bos.workflow.engine.msg.info.MessageInfo;

import java.util.EventObject;

/**
 * 【标签||按钮点击常用事件】——新增物品申请单
 * 需求 ：
 *      1、点击确定按钮时，查询物品表 检查物品是否已存在。
 *      2、若存在则提示：该物品已经存在
 *      3、不存在，则发送消息给管理员(消息内容：物品名称 + 申请理由)
 * @author rd_feng_liang
 * @date 2021/4/15
 */
public class DyBillDemoPlugin02 extends AbstractFormPlugin {
    /**
     * 注册 btnok 监听事件
     * @param e
     */
    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addClickListeners("btnok");
    }
    /**
     * 查询物品是否存在，存在则弹框提示，并取消事件。
     * @param evt
     */
    @Override
    public void beforeClick(BeforeClickEvent evt) {
        super.beforeClick(evt);
        //查询物品是否存在
        Object suppliername = this.getModel().getValue("kded_suppliername");
        DynamicObject[] supplies = BusinessDataServiceHelper.load("kded_supplies",
                "id,name",
                new QFilter[]{new QFilter("name", QCP.equals, suppliername)});

        if (supplies == null || supplies.length == 0 ) {
            //不存在，校验通过
            return;
        }

        //物品已存在：取消事件，不走click事件
        this.getView().showMessage(suppliername + "已存在！");
        evt.setCancel(true);
    }
    /**
     * 物品不存在，发送消息给管理员
     * @param evt
     */
    @Override
    public void click(EventObject evt) {
        super.click(evt);
        Object suppliername = this.getModel().getValue("kded_suppliername");
        Object applyreason = this.getModel().getValue("kded_applyreason");
        //发送消息给管理员
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setSenderId(Long.valueOf(RequestContext.get().getUserId()));
        messageInfo.setContent("[申请物品]——" + suppliername + "\n[申请理由]——" + applyreason);
        messageInfo.setId(Long.valueOf(RequestContext.get().getUserId()));

        MessageCenterServiceHelper.sendMessage(messageInfo);
        this.getView().showMessage("已发送申请给管理员！");
    }

}
