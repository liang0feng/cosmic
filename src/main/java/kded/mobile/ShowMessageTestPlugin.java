package kded.mobile;

import kd.bos.form.ConfirmTypes;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.plugin.AbstractFormPlugin;

import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2021/5/24
 */
public class ShowMessageTestPlugin extends AbstractFormPlugin {

    @Override
    public void registerListener(EventObject e) {
        this.addClickListeners("bar_save");
    }

    @Override
    public void beforeClick(BeforeClickEvent evt) {
//        this.getView().showMessage("hellooo\nooooooooooooooo");
//        this.getView().showMessage("测试长度测试长度");
        this.getView().showConfirm("hello", "world", MessageBoxOptions.OKCancel, ConfirmTypes.Default, null);
    }
}
