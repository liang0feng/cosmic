package kded.demo.fromplugin;

import kd.bos.login.thirdauth.ThirdSSOAuthHandler;
import kd.bos.login.thirdauth.UserAuthResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rd_feng_liang
 * @date 2020/11/20
 */
public class ThLogOut implements ThirdSSOAuthHandler {

    @Override
    public boolean processLogoutLogic(HttpServletRequest servletRequest) {
        System.out.println(servletRequest.getRequestURI());

        return false;
    }

    @Override
    public void callTrdSSOLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
        System.out.println(httpServletRequest.getRequestURI());
        System.out.println(httpServletRequest.getRequestURI());
        try {
            httpServletResponse.sendRedirect("https://baidu.com");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public UserAuthResult getTrdSSOAuth(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        System.out.println(httpServletRequest.getRequestURI());
        System.out.println(httpServletRequest.getRequestURI());

        return null;
    }
}
