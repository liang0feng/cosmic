package kded.report;

import kd.bos.algo.*;
import kd.bos.entity.report.AbstractReportListDataPlugin;
import kd.bos.entity.report.ReportQueryParam;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;


public class QueryReportTestPlugin extends AbstractReportListDataPlugin {

    @Override
    public DataSet query(ReportQueryParam reportQueryParam, Object o) throws Throwable {
        System.out.println("hllo");
        return null;

    }

    @Override
    public ReportQueryParam getQueryParam() {
        return super.getQueryParam();
    }
}
