package kded.devclass.preplugindev;

import java.util.List;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.IFormView;
import kd.bos.form.control.events.BeforeItemClickEvent;

public class AnotherEdit extends AbstractBillPlugIn{
	@Override
	public void beforeItemClick(BeforeItemClickEvent evt) {
		if(evt.getItemKey().equals("kded_printparam")) {
			String list = this.getView().getFormShowParameter().getCustomParam("list");
			List<String> objArr = (List<String>) SerializationUtils.fromJsonStringToList(list,String.class);
			this.getView().showMessage(objArr.toString());
		}
		
		//界面关闭前将数据返回给父页面
		if(evt.getItemKey().equals("bar_close")) {
			this.getView().returnDataToParent("{hahahha}");

		}

		/**
		 * 操作父页面，和操作当前页面是一样的，唯一的区别就是在最后统一加上:this.getView().sendFormAction(parentView);
		 */
		if(evt.getItemKey().equals("kded_notifyparent")) {
			//跨页面之间的控制       1.获取到目标页面的编程模型   2.调用sendFormAction指令
			IFormView parentView = this.getView().getParentView();
			parentView.showSuccessNotification("子页面控制父页面提示");
//			IDataModel parentViewModel = parentView.getModel();
			this.getView().sendFormAction(parentView);
		}
	}
}
