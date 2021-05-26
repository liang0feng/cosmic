package kded.demo.fromplugin.importdata;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.entity.datamodel.events.InitImportDataEventArgs;

import java.util.List;
import java.util.Map;

/**
 * @author rd_feng_liang
 * @date 2021/3/4
 */
public class ImportDataTestPlugin extends AbstractBillPlugIn {
    @Override
    public void initImportData(InitImportDataEventArgs e) {
        List<Map<String, Object>> sourceDataList = e.getSourceDataList();
        for (Map<String, Object> stringObjectMap : sourceDataList) {
            System.out.println(stringObjectMap.keySet());
        }
    }
}
