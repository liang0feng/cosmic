package kded.demo.fromplugin;

import kd.bos.context.RequestContext;
import kd.bos.exception.KDException;
import kd.bos.message.api.ShortMessageInfo;
import kd.bos.schedule.executor.AbstractTask;
import kd.bos.servicehelper.message.MessageServiceHelper;
import kd.bos.servicehelper.workflow.MessageCenterServiceHelper;
import kd.bos.workflow.engine.msg.info.MessageInfo;

import java.util.Arrays;
import java.util.Map;

/**
 * @author rd_feng_liang
 * @date 2020/11/17
 */
public class SMSTestTask extends AbstractTask {

    @Override
    public void execute(RequestContext requestContext, Map<String, Object> map) throws KDException {
        ShortMessageInfo shortMessageInfo = new ShortMessageInfo();
        shortMessageInfo.setPhone(Arrays.asList("15986766149"));
        shortMessageInfo.setMessage("");
        shortMessageInfo.setSignature("kk");
//        MessageServiceHelper.sendShortMessage(shortMessageInfo);
    }
}
