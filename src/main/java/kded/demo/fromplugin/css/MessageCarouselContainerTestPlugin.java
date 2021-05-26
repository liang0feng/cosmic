package kded.demo.fromplugin.css;

import kd.bos.ext.form.container.MessageCarouselContainer;
import kd.bos.form.control.Control;
import kd.bos.form.plugin.AbstractFormPlugin;

import java.util.EventObject;

/**
 * @author rd_feng_liang
 * @date 2021/4/20
 */
public class MessageCarouselContainerTestPlugin extends AbstractFormPlugin {

    @Override
    public void afterCreateNewData(EventObject e) {
        MessageCarouselContainer messagecarouselcontainerap = this.getControl("kded_messagecarouselcontainerap");
    }
}
