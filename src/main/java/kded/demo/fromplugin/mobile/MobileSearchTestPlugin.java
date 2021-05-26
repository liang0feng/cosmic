package kded.demo.fromplugin.mobile;

import kd.bos.form.control.Control;
import kd.bos.form.control.events.MobileSearchTextChangeEvent;
import kd.bos.form.control.events.MobileSearchTextChangeListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.MobileSearch;

import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2021/3/10
 */
public class MobileSearchTestPlugin extends AbstractFormPlugin implements MobileSearchTextChangeListener {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        MobileSearch kded_mobilesearchap = this.getControl("kded_mobilesearch");
        kded_mobilesearchap.addMobileSearchTextChangeListener(this);
    }

    @Override
    public void click(MobileSearchTextChangeEvent mobileSearchTextChangeEvent) {

    }
}
