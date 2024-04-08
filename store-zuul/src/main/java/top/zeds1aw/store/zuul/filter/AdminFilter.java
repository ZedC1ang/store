package top.zeds1aw.store.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import top.zeds1aw.store.common.common.Constant;
import top.zeds1aw.store.user.model.pojo.User;
import top.zeds1aw.store.zuul.feign.UserFeignClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
描述：管理员鉴权过滤器
 */
@Component
public class AdminFilter extends ZuulFilter {

    @Autowired
    UserFeignClient userFeignClient;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        /* 获取关于请求的一系列内容 */
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        /* 管理员登录接口不需要拦住 */
        if (requestURI.contains("adminLogin")) {
            return false;
        }
        if (requestURI.contains("admin")) {
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.ZEDS1AW_STORE_USER);
        /* 如果当前用户为空，需要将请求拦住 */
        if (currentUser == null) {
            currentContext.setSendZuulResponse(false);
            /* 设置返回异常抛出信息 */
            currentContext.setResponseBody("{\n"
                    + "    \"status\": 10010,\n"
                    + "    \"msg\": \"NEED_LOGIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            /* 设置返回状态码 */
            currentContext.setResponseStatusCode(200);
            return null;
        }

        /* 如果用户不为空，还要进一步鉴定是否为管理员身份，调用User模块checkAdminRole方法即可 */
        Boolean adminRole = userFeignClient.checkAdminRole(currentUser);
        if (!adminRole) { /* 如果不是管理员，则需要拦住 */
            currentContext.setSendZuulResponse(false);
            /* 设置返回异常抛出信息 */
            currentContext.setResponseBody("{\n"
                    + "    \"status\": 10011,\n"
                    + "    \"msg\": \"NEED_ADMIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            /* 设置返回状态码 */
            currentContext.setResponseStatusCode(200);
        }

        return null;
    }
}
