package kded.demo.fromplugin;

import kd.bos.context.RequestContext;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.list.SummaryResult;
import kd.bos.form.IFormView;
import kd.bos.form.IPageCache;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.list.BillList;
import kd.bos.list.F7SelectedList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.mvc.list.ListView;
import kd.bos.servicehelper.user.UserConfigServiceHelper;

import java.util.*;

/**
 * @author rd_feng_liang
 * @date 2020/12/11
 */
public class ListTestPlugin extends AbstractListPlugin {

    private final static Log logger = LogFactory.getLog("name");

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        this.addClickListeners("tblrefresh");
        this.addItemClickListeners("toolbarap");
        logger.info("++++++++++++++++++++++++++++");
        logger.error("+++++++++++++++++++++++++++");
    }

    private void clearUserConfig () {
        long userid = Long.parseLong(RequestContext.get().getUserId());
        String settingKey = this.getView().getFormShowParameter().getSettingKey();
        UserConfigServiceHelper.clearSetting(userid, settingKey);
    }

    @Override
    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);

        if ("kded_clearuserconfig".equalsIgnoreCase(evt.getItemKey())) {

            this.clearUserConfig();
            IFormView view = this.getView();
            F7SelectedList f7selectedlistap = this.getControl("f7selectedlistap");
//            f7selectedlistap.sortItem(sortItems);
            view.updateView("f7selectedlistap");
        }
        if ("kded_baritemap".equalsIgnoreCase(evt.getItemKey())) {

//            view.getClientProxy().invokeControlMethod("kded_integerfield", "");
            ArrayList<Map<String, String>> maps = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            map.put("kded_integerfield", "1000");
            map.put("fseq", "加权平均");
            maps.add(map);
            HashMap<String, Object> data = new HashMap<>();
            data.put("data", maps);
            List<SummaryResult> summaryResults = ((BillList) this.getControl("billlistap")).querySummaryResults("kded_integerfield");
//            List<SummaryResult> summaryResults1 = ((ListView) this.getView()).getListModel().getSummaryResults();

//            ((ListView) this.getView()).getClientProxy().addAction("u", data);
//            ((ListView)this.getView()).getClientProxy().invokeControlMethod("billlistap", "u",data);
            IFormView view = this.getView();
        }



        //        ListView view = (ListView) this.getView();
//        String billFormId = view.getBillFormId();
//        DataSet dataSet = QueryServiceHelper.queryDataSet(this.getClass().getName(), "kded_demo1023", "id,kded_textfield,auditdate", null, null);
////        DataSet dataSet1 = dataSet.executeSql("select id,nullif(0,kded_textfield)");
////        DataSet select = dataSet.select("id,isnull(0,kded_textfield)");
//        JoinHint joinHint = new JoinHint();
//        joinHint.setNullAsZero(false);
//        DataSet finish = dataSet.leftJoin(dataSet).on("id", "id").hint(joinHint).select(new String[]{"id", "kded_textfield","auditdate"}).finish();
//        for (Row row : finish) {
//            System.out.println(row.get("id"));
//            System.out.println(row.get("kded_textfield"));
//            System.out.println(row.get("auditdliate"));
//        }
//        System.out.println("jjjj");


    }

    @Override
    public void beforeClick(BeforeClickEvent evt) {
        Button source = (Button) evt.getSource();
        if ("tblrefresh".equalsIgnoreCase(source.getKey())) {
            Control gridview = this.getControl("gridview");
        }
    }

    @Override
    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent) {
        super.closedCallBack(closedCallBackEvent);
        this.clearUserConfig();
    }

    @Override
    public void destory() {
        super.destory();
        this.clearUserConfig();
    }

    @Override
    public void packageData(PackageDataEvent e) {
        Object formatValue = e.getFormatValue();
    }
}
