/*
package kded.demo.fromplugin.css;

import kd.bos.form.ClientProperties;
import kd.bos.form.FormShowParameter;
import kd.bos.form.control.Control;
import kd.bos.form.control.IFrame;
import kd.bos.form.events.CustomEventArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import sun.plugin2.main.server.AbstractPlugin;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * @author rd_feng_liang
 * @date 2021/4/20
 *//*

public class CSSTestPlugin extends AbstractListPlugin {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
    }

*/
/*    @Override
    public void afterBindData(EventObject e) {
        //设置iframe 宽高
//        this.setIframeCSS();

        //设置字段值域 颜色
//        this.setFieldValueBCColor();
    }*//*



    @Override
    public void setFilter(SetFilterEvent e) {
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        Map<String, Object> customParams = formShowParameter.getCustomParams();
        for (String s : customParams.keySet()) {
            System.out.println(s);
        }
    }

    @Override
    public void afterCreateNewData(EventObject e) {
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        Map<String, Object> customParams = formShowParameter.getCustomParams();
        for (String s : customParams.keySet()) {
            System.out.println(s);
        }
    }

    @Override
    public void customEvent(CustomEventArgs e) {
        String eventArgs = e.getEventArgs();
        System.out.println(eventArgs);
        BillList billlistap = this.getControl("billlistap");
        billlistap.setFilter(new QFilter("billno", QCP.equals, "ff"));
        this.getView().updateView("billlistap");
        this.getView().showTipNotification(eventArgs);
    }

    */
/**
     * 设置字段控件 值域背景颜色
     *//*

    private void setFieldValueBCColor() {
        Map<String, Object> feildCSSParams = new HashMap<String, Object>();
        Map<String, Object> feildValueCSSParams = new HashMap<String, Object>();
        feildValueCSSParams.put(ClientProperties.BackColor, "#fb2323");
        feildCSSParams.put("item", feildValueCSSParams);
        this.getView().updateControlMetadata("billstatus", feildCSSParams);
    }

    private void setIframeCSS() {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> height = new HashMap<String, Object>();
        height.put("zh_CN", "800px");
        Map<String, Object> width = new HashMap<String, Object>();
        width.put("zh_CN", "300px");
        params.put(ClientProperties.Height, height);
        params.put(ClientProperties.Width, width);
        this.getView().updateControlMetadata("kded_iframeap", params);

        IFrame kded_iframeap = this.getView().getControl("kded_iframeap");
        kded_iframeap.setSrc("http://baidu.com/");
    }


}
*/
