package kded.login;

import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSONObject;

public class SSOPluginsUtil {

    public static JSONObject getLoginUserName(HttpServletRequest request) {
//		String uid=request.getParameter("uid");
//		String aid=request.getParameter("aid");
//		String kdappid="88687d758c864b47b8f1815fc8016282";
//		String kdappSecret="f94f329c9a89459581cacacdd1160cff";
//		JSONObject resulejsonObject=SSORequest.appTokenGet(kdappid, kdappSecret);
//		if(!resulejsonObject.getBooleanValue("success")) {
//			resulejsonObject=SSORequest.appTokenCreate("88687d758c864b47b8f1815fc8016282", "f94f329c9a89459581cacacdd1160cff");
//		}
//		String tid = resulejsonObject.getString("tid");
//		JSONObject loginJson = SSORequest.loginByApp(uid, tid, aid);
//		JSONObject checkUserJson = SSORequest.userTokenCheck(loginJson.getString("uid"), tid);
//		if(!checkUserJson.getBooleanValue("success")) {
//			loginJson.put("success", false);
//		}
        JSONObject loginJson = new JSONObject();
        loginJson.put("success", true);
        loginJson.put("mobiel", 18571593970L);

        return loginJson;
    }

}
