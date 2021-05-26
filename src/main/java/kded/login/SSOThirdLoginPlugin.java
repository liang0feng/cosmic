package kded.login;

import com.alibaba.fastjson.JSONObject;
import kd.bos.login.thirdauth.ThirdSSOAuthHandler;
import kd.bos.login.thirdauth.UserAuthResult;
import kd.bos.login.thirdauth.UserProperType;
import kd.bos.login.thirdauth.app.UserType;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.parameter.SystemParamServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rd_feng_liang
 * @date 2021/3/25
 */
public class SSOThirdLoginPlugin implements ThirdSSOAuthHandler {
    @Override
    public String getERPCallback(HttpServletRequest servletRequest, String tenantNo, String sandboxNo) {
        return null;
    }

    @Override
    public boolean processLogoutLogic(HttpServletRequest servletRequest) {
        return false;
    }

    @Override
    public void processSucceedLogin(HttpServletRequest request, String globalSessionId) {

    }

    @Override
    public void callTrdSSOLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
        System.out.print("111111");
        String url="https://www.baidu.com/";//跳转的url链接
        try {
            httpServletResponse.sendRedirect(url);
            java.util.Base64.getDecoder().decode("".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserAuthResult getTrdSSOAuth(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        UserAuthResult result=new UserAuthResult();
        SystemParamServiceHelper.getPublicParameter();
        result.setUserType(UserProperType.Mobile);
        //这里编写自己的登录逻辑，判断是否登陆成功，并填写正确的返回类型和返回值
        JSONObject jsonResult = SSOPluginsUtil.getLoginUserName(httpServletRequest);
        if(result.isSucess()){
//			String userName=jsonResult.getString("userName");//
            String userName=jsonResult.getString("mobiel");//
            //当前返回类型手机，用户名，email，工号      
//			result.setUserType(UserProperType.UserName);
            result.setUserType(UserProperType.Mobile);
            result.setUser(userName);
            result.setSucess(true);
        }
        return result;
    }
}
