package kded.demo.fromplugin;

import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.plugin.AbstractFormPlugin;

import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2021/1/22
 */
public class App02TestPlugin extends AbstractFormPlugin {
    @Override
    public void registerListener(EventObject e) {
        this.addItemClickListeners("tbmain");
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
        if ("kded_baritemap".equalsIgnoreCase(evt.getItemKey())) {
            this.getView().showMessage("app02 version 1.0");
            this.getModel();
        }
    }
}
