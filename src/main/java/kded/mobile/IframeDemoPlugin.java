package kded.mobile;

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.ClientProperties;
import kd.bos.form.control.Control;
import kd.bos.form.control.IFrame;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.form.field.ComboEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.metadata.entity.commonfield.ComboField;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rd_feng_liang
 * @date 2021/4/19
 */
public class IframeDemoPlugin extends AbstractFormPlugin implements BeforeF7SelectListener {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners("tbmain");
    }

    @Override
    public void loadCustomControlMetas(LoadCustomControlMetasArgs e) {
        super.loadCustomControlMetas(e);
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {

    }

    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
        IFrame kded_iframeap = this.getView().getControl("kded_iframeap");
        kded_iframeap.setSrc("http://baidu.com/");
        Map<String,Object> metaData_iframe = new HashMap<>();
        metaData_iframe.put(ClientProperties.Height,"500px");
        this.getView().updateControlMetadata("kded_iframeap",metaData_iframe);

        ComboEdit combofield = this.getView().getControl("kded_combofield");
//        combofield.setMustInput(true);
    }

    @Override
    public void beforeF7Select(BeforeF7SelectEvent beforeF7SelectEvent) {

    }

    @Override
    public void propertyChanged(PropertyChangedArgs e) {

    }

    @Override
    public void afterCreateNewData(EventObject e) {

    }
}
