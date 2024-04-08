package top.zeds1aw.store.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import top.zeds1aw.store.common.common.Constant;
import top.zeds1aw.store.user.model.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
描述：用户鉴权过滤器
 */
@Component
public class UserFilter extends ZuulFilter {

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
        /* 图片和付款直接放行，不通过过滤器 */
        if (requestURI.contains("images") || requestURI.contains("pay")) {
            return false;
        }
        /* 购物车和订单必须先登录，需要过滤器鉴权 */
        if (requestURI.contains("cart") || requestURI.contains("order")) {
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
                    + "    \"status\": 10007,\n"
                    + "    \"msg\": \"NEED_LOGIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            /* 设置返回状态码 */
            currentContext.setResponseStatusCode(200);
        }
        return null;
    }
}
