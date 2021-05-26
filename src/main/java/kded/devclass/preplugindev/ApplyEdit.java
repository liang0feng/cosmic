package kded.devclass.preplugindev;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.BillShowParameter;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.entity.operate.Save;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.report.CellStyle;
import kd.bos.form.*;
import kd.bos.form.control.Control;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.list.ListShowParameter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

public class ApplyEdit extends AbstractBillPlugIn{
	@Override
	public void registerListener(EventObject e) {
		super.registerListener(e);
		this.getControl("tbmain").getClass();
		
		
	}
	
	@Override
	public void afterCreateNewData(EventObject e) {
		super.afterCreateNewData(e);
		
		/*BillShowParameter bi = new BillShowParameter();
		bi.setFormId("bos_user");
		bi.getOpenStyle().setShowType(ShowType.Modal);
		this.getView().showForm(bi);*/
	}
	
	
	@Override
	public void beforeBindData(EventObject e) {
		super.beforeBindData(e);
	}
	
	@Override
	public void afterBindData(EventObject e) {
		//设置字段锁定性
		//this.getView().setEnable(false, "billno");
		//设置控件可见性
		this.getView().setVisible(false, "billno");
		
		
		IClientViewProxy proxy = this.getView().getService(IClientViewProxy.class);  
		//Q1-----设置基本 面板背景色
//		ClientActions.createControlStyleBuilder().setBackColor("#d4aaff").build(proxy, "fs_baseinfo");
//		ClientActions.createControlStyleBuilder().setFontSize(20).build(proxy, "billno");

		//Q2-----设置单据体第1行文本1字段的前景色
		ArrayList<CellStyle> list = new ArrayList<>();
		CellStyle style = new CellStyle();
		style.setRow(0);
		style.setForeColor("red");
		style.setFieldKey("kded_textfield");
		list.add(style);
		ClientActions.createCellStyle().addAll(list).invokeControlMethod(proxy, "kded_entryentity");
		
		
		
		//Q3-----修改billno值域背景颜色      参考https://club.kdcloud.com/article/183388
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> item = new HashMap<>();
		item.put(ClientProperties.BackColor, "grey");
		map.put("item", item);
		this.getView().updateControlMetadata("billno", map);

		/**
		 * 总结：
		 * 	  共同点：3种样式修改方式，都是通过表单视图代理对象，addAction添加向前端发送的指令
		 *		IClientViewProxy proxy = this.getView().getService(IClientViewProxy.class)
		 */
	}
	
	
	@Override
	public void beforeItemClick(BeforeItemClickEvent evt) {
		super.beforeItemClick(evt);
		if(evt.getItemKey().equals("kded_reset")) {
			//Q1-----数据包获取及设置
			//way1---将测试文本设置为hello   此方式会触发propertyChanged
			this.getModel().setValue("kded_testtext", "hello");
			//way2---将测试文本设置为hello   此方式不触发propertyChanged
//			this.getModel().getDataEntity(true).set("kded_testtext", "hello");
//			this.getView().updateView("kded_testtext");
			
			//Q2-----调试数据包结构
		}else if(evt.getItemKey().equals("kded_save")) {
			customSave();
		}else if(evt.getItemKey().equals("kded_showform")) {
			BillShowParameter bill = new BillShowParameter();
			bill.setFormId("kded_preplugindev_002");
//			bill.setPkId(940578331726708736L);
			bill.getOpenStyle().setShowType(ShowType.InContainer);
			bill.getOpenStyle().setTargetKey("kded_flexpanelap");
			
			
			
			//Q4-----界面之间参数传递
			List<String> list = new ArrayList<>();
			list.add("param01");
			list.add("param02");
			String json = SerializationUtils.toJsonString(list);
			bill.setCustomParam("list", json);
			
			//Q5-----界面回调
			bill.setCloseCallBack(new CloseCallBack(this, "customShow"));
			this.getView().showForm(bill);
		}
	}

	private void customSave() {
		//Q3-----自己实现界面数据保存
		//------step1 准备数据包
		DynamicObject myObj = BusinessDataServiceHelper.newDynamicObject("kded_preplugindev_001");
		myObj.set("billno", "test005");
		myObj.set("kded_testtext", "test001");
		myObj.set("billstatus", "A");
		DynamicObjectCollection entry = myObj.getDynamicObjectCollection("kded_entryentity");
		DynamicObject row = entry.addNew();
		row.set("kded_textfield", "kkk");
		//------step2保存API
		//***跳过操作校验
		//SaveServiceHelper.save(new DynamicObject[] {myObj});
		//***含操作校验
		OperationResult saveOperate = SaveServiceHelper.saveOperate("kded_preplugindev_001", new DynamicObject[] {myObj}, OperateOption.create());
		boolean success = saveOperate.isSuccess();
		if(!success) {
			this.getView().showErrorNotification("保存失败");
		}else {
			this.getView().showSuccessNotification("保存成功");
		}
	}
	
	@Override
	public void propertyChanged(PropertyChangedArgs e) {
		super.propertyChanged(e);
	}
	
	
	@Override
	public void closedCallBack(ClosedCallBackEvent e) {
		if(e.getActionId().equals("customShow")) {
			this.getView().showMessage("子页面返回数据"+e.getReturnData());
		}
	}
	
	
	@Override
	public void beforeDoOperation(BeforeDoOperationEventArgs args) {
		super.beforeDoOperation(args);
	}
	
}
