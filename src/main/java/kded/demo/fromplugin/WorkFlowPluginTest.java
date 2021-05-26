package kded.demo.fromplugin;

import java.util.List;

import kd.bos.form.control.EntryGrid;
import kd.bos.servicehelper.workflow.WorkflowServiceHelper;
import kd.bos.workflow.api.AgentExecution;
import kd.bos.workflow.api.WorkflowElement;
import kd.bos.workflow.engine.extitf.IWorkflowPlugin;
import kd.taxc.common.template.domain.CellType;

public class WorkFlowPluginTest implements IWorkflowPlugin{

	@Override
	public List<Long> calcUserIds(AgentExecution execution) {
		// TODO Auto-generated method stub
		WorkflowElement currentFlowElement = execution.getCurrentFlowElement();
		List<Long> currentApprover = execution.getCurrentApprover();
		System.out.println(currentApprover);
		EntryGrid entryGrid = new EntryGrid();
		return IWorkflowPlugin.super.calcUserIds(execution);
	}
	
	

}
