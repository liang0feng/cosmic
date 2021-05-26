package kded.demo.fromplugin;

import kd.bos.login.thirdauth.ThirdSSOAuthHandler;
import kd.bos.login.thirdauth.UserAuthResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author rd_feng_liang
 * @date 2021/1/8
 */
public class SSOLoginTest implements ThirdSSOAuthHandler {
    @Override
    public boolean processLogoutLogic(HttpServletRequest servletRequest) {
        return false;
    }

    @Override
    public void callTrdSSOLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
    }

    @Override
    public UserAuthResult getTrdSSOAuth(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return null;
    }
}
