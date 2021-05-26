package kded.demo.fromplugin.mobile;

import kd.bos.form.control.Control;
import kd.bos.form.control.ImageList;
import kd.bos.form.plugin.AbstractFormPlugin;

import java.util.EventObject;

public class ImageListDemo extends AbstractFormPlugin {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);

    }

    @Override
    public void afterCreateNewData(EventObject e) {
        ImageList kded_imagelistap = this.getControl("kded_imagelistap");
        String[] urls = {"htt://www.baidu.com/img/PCpad_012830ebaa7e4379ce9a9ed1b71f7507.png"};
        kded_imagelistap.setImageUrls(urls);
    }
}
