package kded.service;

import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.DispatchServiceHelper;

import java.util.EventObject;

public class ServiceDispatchTestPlugin extends AbstractFormPlugin {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addItemClickListeners("tbmain");
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
        if ("kded_servicetest".equals(evt.getItemKey())) {
            Object o = DispatchServiceHelper.invokeService("kd.kded_devtest.kded_testapp01",
                    "mservice02", "KdedService", "helloworld", null);
            this.getView().showSuccessNotification(o.toString());

        }
    }
}
