package kded.demo.fromplugin.mobile.listmoban;

import java.util.EventObject;
import java.util.List;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.PackageDataEvent;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.entity.list.column.ColumnDesc;
import kd.bos.form.IFormView;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.MobileSearchInitEvent;
import kd.bos.form.control.events.MobileSearchTextChangeEvent;
import kd.bos.form.control.events.MobileSearchTextChangeListener;
import kd.bos.form.events.BeforeCreateListColumnsArgs;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.BillList;
import kd.bos.list.IListColumn;
import kd.bos.list.IListView;
import kd.bos.list.MobileSearch;
import kd.bos.list.events.ListRowClickListener;
import kd.bos.list.plugin.AbstractMobListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;

public class InternalmailMobListPlugin extends AbstractMobListPlugin implements ListRowClickListener {

	String group = "";

	@Override
	public void registerListener(EventObject e) {
		super.registerListener(e);
	}

	@Override
	public void beforeCreateListColumns(BeforeCreateListColumnsArgs args) {
		// 设计器预置的列集合
		List<IListColumn> columns = args.getListColumns();
		// 判断是否存在字段收件人，如果存在直接返回
		for (IListColumn col : columns) {
			if (col.getListFieldKey().equals("byyy_recipientstxt")) {
				// 将byyy_recipientstxt字段转为数据库tag字段
				col.setListFieldKey("byyy_recipientstxt_tag");
				col.setFieldName("byyy_recipientstxt_tag");
			}
		}
		super.beforeCreateListColumns(args);
	}

	@Override
	public void packageData(PackageDataEvent e) {
		if (e.getSource() instanceof ColumnDesc) {
			ColumnDesc columnDesc = (ColumnDesc) e.getSource();
			DynamicObject rowData = e.getRowData();
			if ("byyy_sendperson.name".equals(columnDesc.getKey())) {
				// logger.info(rowData.toString());
				e.setFormatValue(rowData.getString("byyy_sendperson.username") + "("
						+ rowData.getString("byyy_sendperson.name") + ")");
			} else if ("byyy_rrecipients.name".equals(columnDesc.getKey())) {
				// logger.info(rowData.toString());
				e.setFormatValue(rowData.getString("byyy_recipientstxt_tag"));
			}
		}
		super.packageData(e);
	}

	// 1、发件箱：发件人为当前操作用户，分组为发件箱，删除状态为 未删
	// 2、收件箱:（收件人或抄送人或密送人包含当前操作用户），且分组为发件箱， 删除状态为未删除
	// 3、垃圾箱：（（发件人为当前操作用户）或（收件人或抄送人或密送人包含当前操作用户）），且分组为垃圾箱，且状态为已删除
	// 4、草稿箱：发件人为当前操作用户，分组为草稿箱，且状态为未删除
	// 5、选择全部时：（收件人或抄送或密送或发件人为当前操作用户）且状态不为彻底删除
	public QFilter buildQfilter(String userId, String nodeNumber) {
		QFilter deleteStatus0 = new QFilter("byyy_deletestutas", QCP.equals, "0");
		QFilter deleteStatus1 = new QFilter("byyy_deletestutas", QCP.equals, "1");
		QFilter groupFilter = new QFilter("group.number", QCP.equals, nodeNumber);

		// 收件人
		QFilter recipFilterFid = new QFilter("byyy_rrecipients.id", QCP.equals, String.valueOf(userId));
		// 发件人
		QFilter sendPersonFilterFid = new QFilter("byyy_sendperson.id", QCP.equals, String.valueOf(userId));

		QFilter groupInFilter = new QFilter("group.number", QCP.equals, "inbox");
		// 放入页面缓存
		this.getPageCache().put("mobgroup", group);

		if ("inbox".equals(nodeNumber)) {
			// 收件箱:（收件人或抄送人或密送人包含当前操作用户），且分组为发件箱， 删除状态为未删除
			return deleteStatus0.and(groupFilter).and(recipFilterFid);
		} else if ("sentbox".equals(nodeNumber)) {
			return deleteStatus0.and(groupFilter).and(sendPersonFilterFid);
		} else if ("draftbox".equals(nodeNumber)) {
			return deleteStatus0.and(groupFilter).and(sendPersonFilterFid);
		} else if ("trashbox".equals(nodeNumber)) {
			return deleteStatus1.and(groupFilter)
					.and(recipFilterFid.or(sendPersonFilterFid.and(new QFilter("byyy_rid", QCP.equals, "0"))));
		} else {
			return deleteStatus0.and(groupInFilter).and(recipFilterFid);
		}
	}

	@Override
	public void setFilter(SetFilterEvent e) {
		// TODO Auto-generated method stub
		super.setFilter(e);
		MobileSearch control = this.getControl("mobilesearchap");
		String text = control.getText();

		String group = "";
		if (this.getView().getFormShowParameter().getFormId().equals("byyy_listdetails"))
			group = "inbox";
		else if (this.getView().getFormShowParameter().getFormId().equals("byyy_draftsbox"))
			group = "draftbox";
		else if (this.getView().getFormShowParameter().getFormId().equals("byyy_dustbinbox"))
			group = "trashbox";
		else if (this.getView().getFormShowParameter().getFormId().equals("byyy_sendbox"))
			group = "sentbox";

		String userId = RequestContext.get().getUserId();
		QFilter filter = buildQfilter(userId, group);

		for (int i = 0; i < e.getQFilters().size(); i++) {
			QFilter qFilter = e.getQFilters().get(i);
			if ("byyy_recipientstxt".equals(qFilter.getProperty())
					|| "byyy_crecipientstxt".equals(qFilter.getProperty())) {
				e.getQFilters().set(i, new QFilter(qFilter.getProperty() + "_tag", QCP.like, qFilter.getValue()));
			} else if ("1".equals(qFilter.getProperty())) {
				// 快速过滤自定义处理
				// String slike =
				// qFilter.getValue().toString().substring(qFilter.getValue().toString().indexOf("#")
				// + 1);
				String slike = text;
				if (!StringUtils.isBlank(text)) {
					String slikecol = qFilter.getValue().toString()
							.substring(0, qFilter.getValue().toString().indexOf("#"))
							.replace("byyy_recipientstxt", "byyy_recipientstxt_tag").replace("name,", "")
							.replace("number,", "");
					filter.and(QFilter.ftlike(slike.split("\b"), slikecol));
				}
			}
		}
		if (!"inbox".equals(group)) {
			for (int i = 0; i < e.getQFilters().size(); i++) {
				QFilter qFilter = e.getQFilters().get(i);
				if ("1".equals(qFilter.getProperty()))
					e.getQFilters().removeAll(e.getQFilters());
			}
		}
		e.getQFilters().add(filter);
	}

	@Override
	public void mobileSearchTextChange(MobileSearchTextChangeEvent args) {
		// TODO Auto-generated method stub
		super.mobileSearchTextChange(args);
	}

	@Override
	public void mobileSearchInit(MobileSearchInitEvent args) {
		// TODO Auto-generated method stub
		super.mobileSearchInit(args);
	}

	@Override
	public void mobileSearchFocus() {
		// TODO Auto-generated method stub
		super.mobileSearchFocus();
	}

}
