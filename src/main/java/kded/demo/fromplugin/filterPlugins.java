package kded.demo.fromplugin;

import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;

import java.util.EventObject;

/**
 * and 优先级比 or 高 ，省略括号
 * @author rd_feng_liang
 * @date 2021/5/10
 */
public class filterPlugins extends AbstractFormPlugin {
    @Override
    public void afterBindData(EventObject e) {
        QFilter filter = new QFilter("1", QCP.equals, "1");
        QFilter filter1 = new QFilter("2", QCP.equals, "2");
        QFilter filter2 = new QFilter("3", QCP.equals, "3");
        QFilter filter3 = new QFilter("4", QCP.equals, "4");

        QFilter and = filter.or(filter1);
        QFilter and1 = filter2.or(filter3);

        QFilter or = and.and(and1);
    }
}
