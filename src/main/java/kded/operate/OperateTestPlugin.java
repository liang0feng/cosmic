package kded.operate;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.entity.plugin.AbstractOperationServicePlugIn;
import kd.bos.entity.plugin.AddValidatorsEventArgs;
import kd.bos.entity.plugin.args.AfterOperationArgs;
import kd.bos.entity.plugin.args.BeforeOperationArgs;
import kd.bos.entity.plugin.args.EndOperationTransactionArgs;
import kd.bos.entity.validate.AbstractValidator;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.List;

/**
 * @author rd_feng_liang
 * @date 2020/12/17
 */
public class OperateTestPlugin extends AbstractOperationServicePlugIn {

    @Override
    public void beforeExecuteOperationTransaction(BeforeOperationArgs e) {

    }

    @Override
    public void endOperationTransaction(EndOperationTransactionArgs e) {
        DynamicObject[] dataEntities = e.getDataEntities();
        dataEntities[0].set("billno", "B");
        SaveServiceHelper.update(dataEntities);
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
    }

    @Override
    public void onAddValidators(AddValidatorsEventArgs e) {
        List<AbstractValidator> validators = e.getValidators();
    }

    @Override
    public OperationResult getOperationResult() {
        OperationResult operationResult = super.getOperationResult();
        System.out.println(operationResult);
        return super.getOperationResult();
    }
}
