package kded.online.demo;

import kd.bos.context.RequestContext;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.ClientProperties;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.org.OrgUnitServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.*;

/**
 * 【数据加载相关事件demo案例】——登记单
 * 需求：
 *      1、自动带出登记人所在部门和公司
 *      2、打开单据时，不同单据状态显示不同颜色
 * @author rd_feng_liang
 * @date 2021/4/15
 */
public class DyBillDemoPlugin01 extends AbstractFormPlugin {
    /**
     * 【触发时机】：界面初始化或刷新，新建表单数据包成功，并给字段填写了默认值之后，触发此事件；
     * 【场景}】插件可以在此事件，重设字段的默认值。
     * @param e
     */
    @Override
    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        //设置登记单事由默认值
        Date toDate = new Date();
        int month = toDate.getMonth() + 1;
        String string = +month + "月份的物品登记单";
        this.getModel().setValue("kded_beizhu", string);

        //带出登记人所在部门和公司
        String userId = RequestContext.get().getUserId();
        Long deptId = UserServiceHelper.getUserMainOrgId(Long.valueOf(userId));
        this.getModel().setValue("kded_dept", deptId);
        Map<String, Object> companyByOrg = OrgUnitServiceHelper.getCompanyfromOrg(deptId);
        this.getModel().setValue("kded_company", companyByOrg.get("id"));
    }

    /**
     *	【事件触发时机】：界面数据包构建完毕，开始生成指令，刷新前端字段值、控件状态之前，触发此事件；
     *  【场景】：插件可以在此事件中，调整后台视图模型(IFormView)中的字段、控件属性，间接控制前端界面字段值、控件状态；
     * @param e
     */
    @Override
    public void afterBindData(EventObject e) {
        //打开单据界面时，不同单据状态 字段 显示 不同颜色
        String billstatus = (String) this.getModel().getValue("billstatus");
        Map<String, Object> params = new HashMap<String, Object>();
        if ("A".equals(billstatus)) {
            params.put(ClientProperties.ForeColor, "red");
        } else if ("B".equals(billstatus)) {
            params.put(ClientProperties.ForeColor, "blue");
        }

        this.getView().updateControlMetadata("billstatus", params);
    }

    /**
     * 【触发时机】界面数据包构建完毕，开始生成指令，刷新前端字段值、控件状态之前，触发此事件；
     * 【场景】插件可以在此事件中，调整后台视图模型(IFormView)中的字段、控件属性，间接控制前端界面字段值、控件状态；
     *
     *  本事件与afterCreateNewData事件的区别：
     1、本事件比afterCreateNewData事件晚触发；
     2、适合在afterCreateNewData事件中，修改数据模型中的字段值：
     3、在afterCreateNewData改变字段值，数据修改标志为false；退出时，不会提示数据被修改；而在此事件中修改字段值，数据修改标志为true，退出时系统可能会提示数据被修改。
     4、适合在beforeBindData中，调整视图模型中的控件属性；
     5、单据界面插件，afterCreateNewData不是必然会被触发（与afterLoadData互斥），而beforeBindData必然触发

     * 本事件与afterBindData事件的差别：
     1、本事件比afterBindData早触发：在本事件之后，系统会调用内置的字段值绑定过程，随后才会触发afterBindData事件；
     2、beforeBindData事件，适合设置字段、控件的属性，以间接的控制前端字段值、控件状态的刷新过程；
     3、在beforeBindData事件中设置控件状态会没有效果，因为系统随后会清空所有控件的状态；
     4、afterBindData事件，适合直接设置控件在前端表现的内容、状态； *
     * @param e
     */
    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
    }
}
