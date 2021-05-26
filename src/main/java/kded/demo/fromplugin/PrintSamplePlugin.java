package kded.demo.fromplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.plugin.AbstractPrintServicePlugin;
import kd.bos.entity.plugin.args.CustomPrintDataEntitiesArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author rd_feng_liang
 * @date 2021/1/20
 */
public class PrintSamplePlugin extends AbstractPrintServicePlugin {
    private static final String CHANGE_FIELD = "textfield";
    // 单据标识
    private static final String BILL_HEAD_KEY = "printsampleplugin";
    // 单据体标识
    private static final String ENTRY_KEY = "entryentity";
    @Override
    public void customPrintDataEntities(CustomPrintDataEntitiesArgs e) {
        // 新的数据包
        List<DynamicObject> newDataEntities = new ArrayList<>();
        // 数据源的标识
        String dataSourceName = e.getDataSourceName();
        // 自定义字段的集合
        Set<String> customFields = e.getCustomFields();
        // 査询的数据包
        List<DynamicObject> dataEntities = e.getDataEntities();
        // 查询当前单据pkid
        Object billId = e.getPKId();
        // 单据头处理
        if (BILL_HEAD_KEY.equals(dataSourceName)) {
            // Do something
        }
        // 单据体处理
        else if (ENTRY_KEY.equals(dataSourceName)) {
            // Do something
        }
        e.setDataEntities(newDataEntities);
    }
}