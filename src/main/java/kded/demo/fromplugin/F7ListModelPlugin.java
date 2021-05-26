package kded.demo.fromplugin;

import java.util.EventObject;

import kd.bos.form.control.Control;
import kd.bos.form.control.events.BeforeClickEvent;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;

public class F7ListModelPlugin extends AbstractFormPlugin implements BeforeF7SelectListener{

	@Override
	public void registerListener(EventObject e) {
		// TODO Auto-generated method stub
		super.registerListener(e);
		BasedataEdit baseDataEdit = this.getControl("kded_basedatafield2");
		baseDataEdit.addBeforeF7SelectListener(this);
	}

	
	@Override
	public void beforeClick(BeforeClickEvent evt) {
		// TODO Auto-generated method stub
		super.beforeClick(evt);

	}


	@Override
	public void beforeF7Select(BeforeF7SelectEvent arg0) {
		// TODO Auto-generated method stub
		Object source = arg0.getSource();
		int row = arg0.getRow();
	}
	
	
	

}
