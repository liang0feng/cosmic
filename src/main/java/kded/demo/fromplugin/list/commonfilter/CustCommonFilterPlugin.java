package kded.demo.fromplugin.list.commonfilter;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.entity.filter.ControlFilters;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.filter.CommonFilterColumn;
import kd.bos.filter.FilterColumn;
import kd.bos.filter.FilterContainer;
import kd.bos.form.IFormView;
import kd.bos.form.control.Control;
import kd.bos.form.events.FilterContainerInitArgs;
import kd.bos.form.events.FilterContainerSearchClickArgs;
import kd.bos.form.field.ComboItem;
import kd.bos.form.field.events.BeforeFilterF7SelectEvent;
import kd.bos.list.IListView;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.report.filter.SearchListener;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.org.OrgUnitServiceHelper;
import kd.bos.servicehelper.org.OrgViewType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author rd_feng_liang
 * @date 2021/3/25
 */
public class CustCommonFilterPlugin extends AbstractListPlugin {

//    @Override
//    public void filterContainerInit(FilterContainerInitArgs args) {
//
//        List<FilterColumn> commonFilterColumns = args.getCommonFilterColumns();
//        CommonDateFilterColumn commonDateFilterColumn = (CommonDateFilterColumn) commonFilterColumns.get(0);
//        commonDateFilterColumn.setMustInput(true);
//    }


    @Override
    public void filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args) {
        super.filterContainerBeforeF7Select(args);
    }

    @Override
    public void filterContainerAfterSearchClick(FilterContainerSearchClickArgs args) {
        super.filterContainerAfterSearchClick(args);
        Map<String, List<Object>> currentCommonFilter = args.getCurrentCommonFilter();
    }

    @Override
    public void filterContainerSearchClick(FilterContainerSearchClickArgs args) {
        super.filterContainerSearchClick(args);
        Map<String, List<Object>> currentCommonFilter = args.getCurrentCommonFilter();
        IListView view = (IListView) this.getView();


        args.setCurrentCommonFilter(currentCommonFilter);
        FilterContainer filtercontainerap = this.getControl("filtercontainerap");
        ControlFilters controlFilters = filtercontainerap.getContext().getControlFilters();

    }

    @Override
    public void filterContainerInit(FilterContainerInitArgs args)

    {
        List<FilterColumn> listFilter = args.getFilterContainerInitEvent().getCommonFilterColumns();

        RequestContext billMap = RequestContext.get();
        long orgId = billMap.getOrgId();
//        List<Object> case01 = this.getControlFilters().getFilter("kded_basedatafield.name");
        DynamicObject[] case01Arr = BusinessDataServiceHelper.load("kded_bd_0422_001", "id,number,name", null);
        DynamicObject[] case02Arr = BusinessDataServiceHelper.load("kded_bd_0422_002", "id,number,name", null);
        
        List<Long> fromorgList = OrgUnitServiceHelper.getFromOrgs(OrgViewType.Inventory, orgId, OrgViewType.Purchase, false);
        for (FilterColumn filterColumn : args.getFilterContainerInitEvent().getCommonFilterColumns()) {
            CommonFilterColumn commonFilterColumn = (CommonFilterColumn) filterColumn;
            String fieldName = commonFilterColumn.getFieldName();
            if ("kded_basedatafield.name".equals(fieldName)) {
                List<ComboItem> caseComboItems = getCaseComboItems(case01Arr);
                commonFilterColumn.setComboItems(caseComboItems);
                commonFilterColumn.setDefaultValues(caseComboItems.get(0).getValue());
            }
            if ("kded_basedatafield1.name".equals(fieldName)) {
                List<ComboItem> caseComboItems = getCaseComboItems(case02Arr);
                commonFilterColumn.setComboItems(caseComboItems);
                commonFilterColumn.setDefaultValues(caseComboItems.get(2).getValue());
            }
        }
    }

    private List<ComboItem> getCaseComboItems(DynamicObject[] arr) {
        ArrayList<ComboItem> comboItems = new ArrayList<>();
        List<DynamicObject> dynamicObjects = Arrays.asList(arr);
        for (DynamicObject comboItem : dynamicObjects) {
            ComboItem orgItem = new ComboItem();
            if (comboItem.getString("name").endsWith("001")) {
                continue;
            }
            orgItem.setCaption(new LocaleString(comboItem.getString("name")));
            orgItem.setValue(comboItem.getString("id"));
            comboItems.add(orgItem);
        }
        return comboItems;
    }


    private List<ComboItem> getFromOrgList(List<Long> fromorgList) {
        ArrayList<ComboItem> orgItems = new ArrayList<>();
        DynamicObject[] orgs = BusinessDataServiceHelper.load("bos_org", "id,name", new QFilter[]{
                new QFilter("id", "in", fromorgList)
        });
        for (DynamicObject org : orgs) {
            ComboItem orgItem = new ComboItem();
            orgItem.setCaption(new LocaleString(org.getString("name")));
            orgItem.setValue(org.getString("id"));
            orgItems.add(orgItem);
        }
        return orgItems;
    }


}
