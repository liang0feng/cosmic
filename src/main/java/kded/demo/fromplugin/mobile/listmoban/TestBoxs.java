package kded.demo.fromplugin.mobile.listmoban;

import kd.bos.form.control.Control;
import kd.bos.form.control.events.MobileSearchTextChangeEvent;
import kd.bos.form.control.events.MobileSearchTextChangeListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.MobileSearch;
import java.util.*;
import kd.bos.bill.MobileBillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

public class TestBoxs extends AbstractFormPlugin implements MobileSearchTextChangeListener {

	private String[] buttons = new String[] { "byyy_sjx", "byyy_fjx", "byyy_cgx", "byyy_ljx", "byyy_xyj",
			"byyy_newmail" };
	private String[] items = new String[] { "byyy_writeemail", "byyy_delete", "byyy_deletecompletely",
			"byyy_markasread", "byyy_refresh", "byyy_restore", "byyy_unread" };
	private final static String KEY_SEARCH = "mobilesearchap"; // 搜索控件标识

	String group = "";

	@Override
	public void registerListener(EventObject e) {
		super.registerListener(e);
		this.addClickListeners(buttons);
		this.addClickListeners(items);
		this.addClickListeners("byyy_cardflexpanelap2", "byyy_byyy_cardflexpanelap", "byyy_cardflexpanelap");
		MobileSearch mobileSearch = this.getView().getControl(KEY_SEARCH);// 监听搜索框
		mobileSearch.addMobileSearchTextChangeListener(this);
	}

	@Override
	public void click(MobileSearchTextChangeEvent arg0) {
		// 监听输入的内容
		MobileSearch search = (MobileSearch) arg0.getSource();
		String searchText = search.getText();

		BillList billList = this.getControl("billlistap");
		// 为列表添加filter
		FilterParameter filterParameter = new FilterParameter();
		QFilter f = new QFilter("byyy_recipientstxt_tag", QCP.like, "%" + searchText + "%");
		QFilter f2 = new QFilter("byyy_title", QCP.like, "%" + searchText + "%");
		QFilter f3 = new QFilter("byyy_sendperson.name", QCP.like, "%" + searchText + "%");
		QFilter filters = f2;

		group = this.getPageCache().get("mobgroup");
		if (group.equals("inbox"))
			filters = f3.or(filters);
		else if (group.equals("trashbox"))
			filters = f.or(f3).or(filters);
		else
			filters = f.or(filters);

		String userId = RequestContext.get().getUserId();
		QFilter filter = buildQfilter(userId, group);

		filterParameter.setFilter(filter.and(filters));
		billList.setFilterParameter(filterParameter);
		billList.refresh();// 刷新列表
		this.getView().getModel().setValue("byyy_textfield", "共" + billList.getListModel().getBillDataCount() + "条");
	}

	public DynamicObject GetGroupId(String groupnumber) {

		QFilter qFilter = new QFilter("number", QCP.equals, groupnumber);
		DynamicObject group = BusinessDataServiceHelper.loadSingle("byyy_mailgroup", "id", new QFilter[] { qFilter });
		return group;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void beforeBindData(EventObject e) {

		group = this.getView().getFormShowParameter().getCustomParam("group");
		String userId = RequestContext.get().getUserId();
		String billnm = this.getView().getModel().getDataEntityType().getName();
		if (billnm.equals("byyy_draftsbox")) {
			group = "draftbox";
		}
		if (billnm.equals("byyy_sendbox")) {
			group = "sentbox";
		}
		if (billnm.equals("byyy_dustbinbox")) {
			group = "trashbox";
		}
		if (billnm.equals("byyy_listdetails")) {
			group = "inbox";
		}
		BillList billList = this.getControl("billlistap");
		QFilter filter = buildQfilter(userId, group);
		// //2021年3月8日15:43:37
		// //新增过滤未读
		// //删除也需要
		// //刷新也需要
		// //标记已读为重新进入收件箱
		// if (billnm.equals("byyy_listdetails")) {
		// QFilter unreadfilter = new QFilter("byyy_isread", QCP.equals, "0");
		// filter.and(unreadfilter);
		// }
		billList.setFilter(filter);

		setButtonVisible(group);
		super.beforeBindData(e);
	}

	@Override
	public void afterBindData(EventObject e) {
		super.afterBindData(e);
		BillList billList = this.getControl("billlistap");
		this.getView().getModel().setValue("byyy_textfield", "共" + billList.getListModel().getBillDataCount() + "条");
		this.getView().updateView("byyy_textfield");
	}

	@Override
	public void click(EventObject e) {
		Control control = (Control) e.getSource();
		String key = control.getKey();

		if (StringUtils.equals("byyy_sjx", key)) {

			FormShowParameter fsp = new FormShowParameter();
			fsp.setFormId("byyy_listdetails");
			fsp.setCustomParam("group", "inbox");
			fsp.getOpenStyle().setShowType(ShowType.Floating);
			this.getView().showForm(fsp);

		} else if (StringUtils.equals("byyy_fjx", key)) {
			FormShowParameter fsp = new FormShowParameter();
			fsp.setFormId("byyy_sendbox");
			fsp.setCustomParam("group", "sentbox");
			fsp.getOpenStyle().setShowType(ShowType.Floating);
			this.getView().showForm(fsp);

		} else if (StringUtils.equals("byyy_cgx", key)) {
			FormShowParameter fsp = new FormShowParameter();
			fsp.setFormId("byyy_draftsbox");
			fsp.setCustomParam("group", "draftbox");
			fsp.getOpenStyle().setShowType(ShowType.Floating);
			this.getView().showForm(fsp);

		} else if (StringUtils.equals("byyy_ljx", key)) {
			FormShowParameter fsp = new FormShowParameter();
			fsp.setFormId("byyy_dustbinbox");
			fsp.setCustomParam("group", "trashbox");
			fsp.getOpenStyle().setShowType(ShowType.Floating);
			this.getView().showForm(fsp);

		} else if (StringUtils.equals("byyy_xyj", key) || StringUtils.equals("byyy_newmail", key)) {
			MobileBillShowParameter mbsp = new MobileBillShowParameter();
			mbsp.setFormId("byyy_im_internalmail_mob");
			mbsp.getOpenStyle().setShowType(ShowType.Floating);
			this.getView().showForm(mbsp);

		} else if (StringUtils.equals("byyy_cardflexpanelap2", key)
				|| StringUtils.equals("byyy_byyy_cardflexpanelap", key)
				|| StringUtils.equals("byyy_cardflexpanelap", key)) {
			MobileBillShowParameter mlsp = new MobileBillShowParameter();
			// 表单标识
			mlsp.setFormId("byyy_mailview");
			MobileBillShowParameter mlsp2 = new MobileBillShowParameter();
			// 表单标识
			mlsp2.setFormId("byyy_im_internalmail_mob");
			BillList billList = this.getControl("billlistap");
			// 获取当前列表的选中行
			Object primaryKeyValue = billList.getFocusRowPkId();

			QFilter qfilter = new QFilter("id", QCP.equals, primaryKeyValue);
			QFilter[] filters = new QFilter[] { qfilter };
			DynamicObject byyy_im_internalmail = BusinessDataServiceHelper.loadSingle("byyy_im_internalmail",
					"id,number,name,group", filters);
			if (byyy_im_internalmail != null) {
				group = byyy_im_internalmail.getDynamicObject("group").get("number").toString();
				if (!"draftbox".equals(group)) {
					// 返回主键id到父页面并关闭子页面
					mlsp.setPkId(primaryKeyValue);
					mlsp.getOpenStyle().setShowType(ShowType.Floating);
					mlsp.setStatus(OperationStatus.VIEW);
					// 设置弹出页面的编码
					this.getView().showForm(mlsp);
				} else {
					// 返回主键id到父页面并关闭子页面
					mlsp2.setPkId(primaryKeyValue);
					mlsp2.getOpenStyle().setShowType(ShowType.Floating);
					mlsp2.setStatus(OperationStatus.EDIT);
					// 设置弹出页面的编码
					this.getView().showForm(mlsp2);
				}
			}
		} else if (StringUtils.equals("byyy_writeemail", key)) {

			MobileBillShowParameter mlsp = new MobileBillShowParameter();
			// 表单标识
			mlsp.setFormId("byyy_im_internalmail_mob");
			mlsp.getOpenStyle().setShowType(ShowType.Floating);
			mlsp.setStatus(OperationStatus.ADDNEW);
			// 设置弹出页面的编码
			this.getView().showForm(mlsp);

		} else if (StringUtils.equals("byyy_delete", key)) {

			BillList billList = this.getControl("billlistap");
			// 获取当前列表的选中行
			ListSelectedRowCollection selectedRows = billList.getSelectedRows();
			if (selectedRows.size() > 0) {
				List<DynamicObject> successObjs = new ArrayList<>();

				for (ListSelectedRow list : selectedRows) {

					DynamicObject obj = BusinessDataServiceHelper.loadSingle(list.getPrimaryKeyValue(),
							"byyy_im_internalmail");

					obj.set("byyy_deletestutas", "1");
					if (obj.getDynamicObject("group") != null) {
						DynamicObject groupobj = obj.getDynamicObject("group");
						obj.set("byyy_rmailstutas", groupobj.getString("Number"));
					}
					obj.set("group", this.GetGroupId("trashbox"));
					obj.set("modifytime", new Date());
					successObjs.add(obj);
				}

				SaveServiceHelper.update((DynamicObject[]) successObjs.toArray(new DynamicObject[0]));

				group = this.getPageCache().get("mobgroup");
				QFilter filter = this.buildQfilter(RequestContext.get().getUserId(), group);
				// //2021年3月8日15:54:30
				// //删除新增
				// if ("inbox".equals(group)) {
				// QFilter unreadfilter = new QFilter("byyy_isread", QCP.equals, "0");
				// filter.and(unreadfilter);
				// }
				FilterParameter filterParameter = new FilterParameter();
				filterParameter.setFilter(filter);
				billList.setFilterParameter(filterParameter);
				billList.refresh();// 刷新列表

				this.getView().showTipNotification("删除完成!", 1000);
				this.getView().getModel().setValue("byyy_textfield",
						"共" + billList.getListModel().getBillDataCount() + "条");
			} else {
				this.getView().showTipNotification("请选择需要删除的邮件!", 1000);
			}

		} else if (StringUtils.equals("byyy_deletecompletely", key)) {

			BillList billList = this.getControl("billlistap");
			// 获取当前列表的选中行
			ListSelectedRowCollection selectedRows = billList.getSelectedRows();
			if (selectedRows.size() > 0) {
				List<DynamicObject> successObjs = new ArrayList<>();

				for (ListSelectedRow list : selectedRows) {

					DynamicObject obj = BusinessDataServiceHelper.loadSingle(list.getPrimaryKeyValue(),
							"byyy_im_internalmail");

					obj.set("byyy_deletestutas", "2");
					successObjs.add(obj);
				}

				SaveServiceHelper.update((DynamicObject[]) successObjs.toArray(new DynamicObject[0]));

				group = this.getPageCache().get("mobgroup");
				QFilter filter = this.buildQfilter(RequestContext.get().getUserId(), group);

				FilterParameter filterParameter = new FilterParameter();
				filterParameter.setFilter(filter);
				billList.setFilterParameter(filterParameter);
				billList.refresh();// 刷新列表
				this.getView().getModel().setValue("byyy_textfield",
						"共" + billList.getListModel().getBillDataCount() + "条");

				this.getView().showTipNotification("彻底删除完成!", 1000);
				this.getView().getModel().setValue("byyy_textfield",
						"共" + billList.getListModel().getBillDataCount() + "条");
			} else {
				this.getView().showTipNotification("请选择需要彻底删除的邮件!", 1000);
			}

		} else if (StringUtils.equals("byyy_markasread", key)) {

			BillList billList = this.getControl("billlistap");
			// 获取当前列表的选中行
			ListSelectedRowCollection selectedRows = billList.getSelectedRows();
			if (selectedRows.size() > 0) {
				List<DynamicObject> successObjs = new ArrayList<>();

				for (ListSelectedRow list : selectedRows) {

					DynamicObject obj = BusinessDataServiceHelper.loadSingle(list.getPrimaryKeyValue(),
							"byyy_im_internalmail");

					Long pid = null;

					if (!"".equals(obj.get("byyy_rid")) && obj.get("byyy_rid").toString() != "0") {
						String id = obj.get("byyy_rid").toString();
						// 收件人
						if (!"".equals(obj.get("byyy_rrecipients")) && obj.get("byyy_rrecipients") != null) {
							QFilter qfilter2 = new QFilter("id", QCP.equals, obj.get("id"));
							QFilter[] filters2 = new QFilter[] { qfilter2 };
							DynamicObjectCollection ends1 = QueryServiceHelper.query("byyy_im_internalmail",
									"byyy_rrecipients", filters2);
							pid = ends1.get(0).getLong(0);
						}
						// 抄送人
						if (!"".equals(obj.get("byyy_rcrecipients")) && obj.get("byyy_rcrecipients") != null) {
							QFilter qfilter3 = new QFilter("id", QCP.equals, obj.get("id"));
							QFilter[] filters3 = new QFilter[] { qfilter3 };
							DynamicObjectCollection ends1 = QueryServiceHelper.query("byyy_im_internalmail",
									"byyy_rcrecipients", filters3);
							pid = ends1.get(0).getLong(0);
						}
						// 密送人
						if (!"".equals(obj.get("byyy_rsrecipients")) && obj.get("byyy_rsrecipients") != null) {
							QFilter qfilter4 = new QFilter("id", QCP.equals, obj.get("id"));
							QFilter[] filters4 = new QFilter[] { qfilter4 };
							DynamicObjectCollection ends1 = QueryServiceHelper.query("byyy_im_internalmail",
									"byyy_rsrecipients", filters4);
							pid = ends1.get(0).getLong(0);

						}
						// 原单id
						QFilter qfilter = new QFilter("byyy_corrid", QCP.equals, id);
						QFilter[] filters = new QFilter[] { qfilter };
						DynamicObjectCollection ends = QueryServiceHelper.query("byyy_im_sendstutas",
								"byyy_senddetails.seq,id", filters);
						if (ends.size() != 0) {
							DynamicObject dynamicObject = (DynamicObject) ends.get(0);

							DynamicObject aldynamicObject = BusinessDataServiceHelper
									.loadSingle(dynamicObject.get("id"), "byyy_im_sendstutas");
							DynamicObjectCollection aCollection = (DynamicObjectCollection) aldynamicObject
									.get("byyy_senddetails");

							for (DynamicObject row : aCollection) {

								DynamicObject sjr = row.getDynamicObject("byyy_alrecipient");
								if (sjr.getString("masterid").equals(pid.toString())) {

									row.set("byyy_isread", 1);
									DynamicObject[] dynamicObjectscen = new DynamicObject[] { aldynamicObject };
									SaveServiceHelper.save(dynamicObjectscen);
								}

							}

							Long pkid = (long) obj.getPkValue();
							DynamicObject dynamicObject1 = BusinessDataServiceHelper.loadSingle(pkid,
									"byyy_im_internalmail");
							dynamicObject1.set("byyy_isread", 1);
							DynamicObject[] dynamicObjectsce = new DynamicObject[] { dynamicObject1 };
							SaveServiceHelper.update(dynamicObjectsce);
						}
					}
					successObjs.add(obj);
				}

				SaveServiceHelper.update((DynamicObject[]) successObjs.toArray(new DynamicObject[0]));

				FormShowParameter fsp = new FormShowParameter();
				fsp.setFormId("byyy_listdetails");
				fsp.setCustomParam("group", "inbox");
				fsp.getOpenStyle().setShowType(ShowType.Floating);
				this.getView().showForm(fsp);

			} else {
				this.getView().showTipNotification("请选择需要标记已读的邮件!", 1000);
			}

		} else if (StringUtils.equals("byyy_refresh", key)) {

			BillList billList = this.getControl("billlistap");
			group = this.getPageCache().get("mobgroup");
			QFilter filter = this.buildQfilter(RequestContext.get().getUserId(), group);
			// //2021年3月8日15:54:30
			// //删除新增
			// if ("inbox".equals(group)) {
			// QFilter unreadfilter = new QFilter("byyy_isread", QCP.equals, "0");
			// filter.and(unreadfilter);
			// }

			FilterParameter filterParameter = new FilterParameter();
			filterParameter.setFilter(filter);
			billList.setFilterParameter(filterParameter);
			billList.refresh();// 刷新列表
			this.getView().getModel().setValue("byyy_textfield",
					"共" + billList.getListModel().getBillDataCount() + "条");

			this.getView().showTipNotification("刷新完成!", 1000);

		} else if (StringUtils.equals("byyy_restore", key)) {

			BillList billList = this.getControl("billlistap");
			// 获取当前列表的选中行
			ListSelectedRowCollection selectedRows = billList.getSelectedRows();
			if (selectedRows.size() > 0) {
				List<DynamicObject> successObjs = new ArrayList<>();

				for (ListSelectedRow list : selectedRows) {

					DynamicObject obj = BusinessDataServiceHelper.loadSingle(list.getPrimaryKeyValue(),
							"byyy_im_internalmail");

					obj.set("byyy_deletestutas", "0");
					obj.set("group", this.GetGroupId(obj.get("byyy_rmailstutas").toString()));
					successObjs.add(obj);
				}
				SaveServiceHelper.update((DynamicObject[]) successObjs.toArray(new DynamicObject[0]));

				group = this.getPageCache().get("mobgroup");
				QFilter filter = this.buildQfilter(RequestContext.get().getUserId(), group);

				FilterParameter filterParameter = new FilterParameter();
				filterParameter.setFilter(filter);
				billList.setFilterParameter(filterParameter);
				billList.refresh();// 刷新列表
				this.getView().getModel().setValue("byyy_textfield",
						"共" + billList.getListModel().getBillDataCount() + "条");

				this.getView().showTipNotification("还原完成!", 1000);
				this.getView().getModel().setValue("byyy_textfield",
						"共" + billList.getListModel().getBillDataCount() + "条");

			} else {
				this.getView().showTipNotification("请选择需要还原的邮件!", 1000);
			}
		} else if (StringUtils.equals("byyy_unread", key)) {

			// 2021年3月8日15:55:58修改
			// 移动端收件箱过滤未读的邮件
			QFilter filter = this.buildQfilter(RequestContext.get().getUserId(), "inbox");
			this.getPageCache().put("mobgroup", "inbox");
			QFilter unreadfilter = new QFilter("byyy_isread", QCP.equals, "0");
			BillList billList = this.getControl("billlistap");

			FilterParameter filterParameter = new FilterParameter();
			filterParameter.setFilter(filter.and(unreadfilter));
			billList.setFilterParameter(filterParameter);
			billList.refresh();// 刷新列表
			this.getView().getModel().setValue("byyy_textfield",
					"共" + billList.getListModel().getBillDataCount() + "条");

			// BillList billList = this.getControl("billlistap");
			// group = this.getPageCache().get("mobgroup");
			// QFilter filter = this.buildQfilter(RequestContext.get().getUserId(), group);
			//
			// FilterParameter filterParameter = new FilterParameter();
			// filterParameter.setFilter(filter);
			// billList.setFilterParameter(filterParameter);
			// billList.refresh();// 刷新列表
			// this.getView().getModel().setValue("byyy_textfield",
			// "共" + billList.getListModel().getBillDataCount() + "条");
			//
			// this.getView().showTipNotification("显示全部完成!", 1000);
		}

		setButtonVisible(group);
	}

	public void setButtonVisible(String group) {
		// 如果发件箱
		if ("sentbox".equals(group)) {
			this.getView().setVisible(true, "byyy_writeemail");
			this.getView().setVisible(false, "byyy_forward");
			this.getView().setVisible(true, "byyy_delete");
			this.getView().setVisible(false, "byyy_deletecompletely");
			this.getView().setVisible(false, "byyy_markasread");
			this.getView().setVisible(false, "byyy_reduction");
			this.getView().setVisible(true, "byyy_refresh");
			this.getView().setVisible(false, "byyy_restore");
			this.getView().setVisible(false, "byyy_unread");
		}
		// 如果草稿箱
		else if ("draftbox".equals(group)) {
			this.getView().setVisible(true, "byyy_writeemail");
			this.getView().setVisible(false, "byyy_forward");
			this.getView().setVisible(true, "byyy_delete");
			this.getView().setVisible(false, "byyy_deletecompletely");
			this.getView().setVisible(false, "byyy_markasread");
			this.getView().setVisible(false, "byyy_reduction");
			this.getView().setVisible(true, "byyy_refresh");
			this.getView().setVisible(false, "byyy_restore");
			this.getView().setVisible(false, "byyy_unread");
		}
		// 如果垃圾箱
		else if ("trashbox".equals(group)) {
			// 禁掉删除，回复
			this.getView().setVisible(true, "byyy_writeemail");

			this.getView().setVisible(false, "byyy_reply");
			this.getView().setVisible(false, "byyy_forward");
			this.getView().setVisible(false, "byyy_delete");
			this.getView().setVisible(true, "byyy_deletecompletely");
			this.getView().setVisible(false, "byyy_markasread");
			this.getView().setVisible(false, "byyy_reduction");
			this.getView().setVisible(true, "byyy_refresh");
			this.getView().setVisible(true, "byyy_restore");
			this.getView().setVisible(false, "byyy_unread");
		}
		// 如果收件箱
		else if ("inbox".equals(group) || group == null) {
			// 禁掉删除，回复
			this.getView().setVisible(true, "byyy_writeemail");
			this.getView().setVisible(true, "byyy_forward");
			this.getView().setVisible(true, "byyy_delete");
			this.getView().setVisible(false, "byyy_deletecompletely");
			this.getView().setVisible(true, "byyy_markasread");
			this.getView().setVisible(false, "byyy_reduction");
			this.getView().setVisible(true, "byyy_refresh");
			this.getView().setVisible(false, "byyy_withdraw");
			this.getView().setVisible(false, "byyy_restore");
			this.getView().setVisible(true, "byyy_unread");
		}

	}

	public void executeQfilter(String userId, String group) {
		BillList billList = this.getControl("billlistap");
		// 为列表添加filter
		FilterParameter filterParameter = new FilterParameter();
		QFilter filters = buildQfilter(userId, group);
		filterParameter.setFilter(filters);
		billList.setFilterParameter(filterParameter);
		billList.refresh();// 刷新列表

		this.getView().getModel().setValue("byyy_textfield", "共" + billList.getListModel().getBillDataCount() + "条");
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

}
