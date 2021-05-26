package kded.demo.fromplugin;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.tree.TreeNode;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.workflow.api.AgentExecution;
import kd.bos.workflow.api.WorkflowElement;
import kd.bos.workflow.engine.extitf.IWorkflowPlugin;

import java.util.List;

/**
 * @author rd_feng_liang
 * @date 2020/10/30
 */
public class GetauditresultPlugin implements IWorkflowPlugin {
    @Override
    public List<Long> calcUserIds(AgentExecution execution) {
        List<Long> currentApprover = execution.getCurrentApprover();
        currentApprover.clear();
        return currentApprover;
    }

    @Override
    public void notify(AgentExecution execution) {
        String entityNumber = execution.getEntityNumber();//单据编号
        String businessKey = execution.getBusinessKey();
        List<Long> currentApprover = execution.getCurrentApprover();
        List<Long> person = execution.getAllApprover();
        WorkflowElement<?> flowElement = execution.getCurrentFlowElement();//当前节点
        String flowElementname = flowElement.getName();//当前节点名称
        String approvers = "";
        for(int i = 0 ; i <currentApprover.size();i++) {
            QFilter qFilter=new QFilter("id", QCP.equals, String.valueOf(currentApprover.get(i)));
            DynamicObjectCollection pDynamicObjects = QueryServiceHelper.query("byyy_bd_person","id", new QFilter[]{qFilter});
            String name =  pDynamicObjects.get(0).getString("byyy_personname");
            approvers = approvers + name;
        }
        DynamicObject b2bDynamicObjects = BusinessDataServiceHelper.loadSingle(businessKey, entityNumber);
        b2bDynamicObjects.set("byyy_flowelement",flowElementname);
        b2bDynamicObjects.set("byyy_currentapprover",approvers);
        TreeNode treeNode = new TreeNode();
        SaveServiceHelper.save(new DynamicObject[] {b2bDynamicObjects});

    }
}