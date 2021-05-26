package kded.workflow;

import kd.bos.workflow.api.AgentExecution;
import kd.bos.workflow.engine.extitf.IWorkflowPlugin;

import java.util.List;

/**
 * @author rd_feng_liang
 * @date 2021/3/8
 */
public class WFTestPlugin implements IWorkflowPlugin {

    @Override
    public List<Long> calcUserIds(AgentExecution execution) {
        return null;
    }

    @Override
    public void notify(AgentExecution execution) {
    }
}
