package com.itheima.mm.security;

import com.itheima.mm.framework.annotation.Controller;
import com.itheima.mm.framework.annotation.RequestMapping;
import com.itheima.mm.framework.utils.ClassScannerUtils;
import com.itheima.mm.pojo.User;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/08
 */
public class SecurityFilter implements Filter {
    /**
     * 每次资源，所需要的角色。比如：
     *      资源：/pages/questionBasicList.html，能访问的角色：ROLE_ADMIN,ROLE_QUESTION_RECORDER
     *      资源：/pages/questionClassicList.html，能访问的角色：ROLE_ADMIN
     *
     *      资源：/review/reviewQuestion，能访问的权限：QUESTION_REVIEW_UPDATE
     */
    private Map<String, String> map = new HashMap<>();

    /**
     * 需要加载配置文件mm-security.xml
     * 可以得到：
     *      每个页面资源，需要什么角色才可以访问
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        InputStream is = null;
        try {
            //通过过滤器的FilterConfig对象，可以得到web.xml里，配置的初始化参数
            String configLocation = config.getInitParameter("configLocation");
            is = this.getClass().getClassLoader().getResourceAsStream(configLocation);

            //要读取xml文件，得到里边的配置：用dom4j
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);

            //获取xml文件里所有的security标签。用xpath表达式：//security，表示从xml里全文搜索security标签
            List<Element> securityElements = document.selectNodes("//security");
            for (Element securityElement : securityElements) {
                //获取security标签的pattern属性值：资源路径
                String pattern = securityElement.attributeValue("pattern");
                //获取security标签的has_role属性值：哪些角色可以访问
                String hasRole = securityElement.attributeValue("has_role");

                map.put(pattern, hasRole);
            }


            //扫描Controller类里的方法，得到：资源 需要  什么权限 可以访问
            Element scanElement = (Element) document.selectSingleNode("//scan");
            String packageName = scanElement.attributeValue("package");

            //扫描指定包下所有的类
            List<Class<?>> classList = ClassScannerUtils.getClasssFromPackage(packageName);
            if (classList != null && classList.size()>0) {
                for (Class<?> clazz : classList) {
                    //判断类是否是Controller。如果不是，不需要做其它操作
                    boolean isController = clazz.isAnnotationPresent(Controller.class);
                    if (isController) {
                        //得到Controller类里所有的方法
                        Method[] methods = clazz.getMethods();
                        for (Method method : methods) {
                            //判断方法是否需要权限控制
                            boolean isAuthorizeMethod = method.isAnnotationPresent(PreAurhorize.class);
                            if (isAuthorizeMethod) {
                                //得到资源的访问路径
                                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                                String mappingPath = requestMapping.value();

                                //得到资源 需要的权限
                                PreAurhorize preAurhorize = method.getAnnotation(PreAurhorize.class);
                                String authorize = preAurhorize.value();

                                //把这些数据，保存到map里
                                map.put(mappingPath, authorize);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 权限过滤：判断用户拥有的权限，能否访问目标资源
     *
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //1. 获取客户端请求的资源是：  /mm/pages/questionBasicList.html  或者  /mm/review/reviewQuestion.do
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        uri = uri.substring(contextPath.length());
        if (uri.endsWith(".do")) {
            uri = uri.replace(".do", "");
        }

        //2. 从map里获取 访问资源所需要的角色（权限）
        String hasRole = map.get(uri);
        if (hasRole == null) {
            //说明 请求的这个资源，不需要进行权限控制，直接放行即可
            System.out.println(uri + "：不需要权限控制，直接放行");
            chain.doFilter(req, resp);
            return;
        }

        //3. 获取当前用户拥有的角色（权限）
        //3.1 未登录的情况处理
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            //说明  没有登录，跳转到登录页面
            System.out.println(uri + "：没有登录，即将跳转到登录页面");
            resp.sendRedirect(contextPath +"/login.html");
            return;
        }

        //3.2 已登录的情况，得到当前用户拥有的权限（包含了用户拥有的角色、角色关联的权限）
        List<String> userAuthorityList = user.getAuthorityList();
        if (userAuthorityList == null || userAuthorityList.size() == 0) {
            //说明  当前用户没有任何权限，响应一个提示信息“当前用户权限不足，请切换用户”
            System.out.println(uri + "：当前用户权限不足，请切换用户");
            resp.getWriter().print("当前用户权限不足，请切换用户");
            return;
        }

        //4. 判断 用户的权限，是否足够访问目标资源。用户拥有的角色（权限）里，是否含有 目标资源所需要的任何一个角色（权限）
        boolean canAccess = false;
        //比如：访问资源，有任意一个角色即可：ROLE_ADMIN,ROLE_QUESTION_RECORDER
        String[] hasRoleArray = hasRole.split(",");
        for (String role : hasRoleArray) {
            //用户是否有role这个角色？如果有，说明权限足够；如果没有，说明权限不足
            if (userAuthorityList.contains(role)) {
                canAccess = true;
                break;
            }
        }

        if (canAccess) {
            System.out.println(uri + "：当前用户权限足够，放行请求");
            chain.doFilter(req, resp);
        }else{
            System.out.println(uri + "：当前用户权限不足，请切换用户");
            resp.getWriter().print("当前用户权限不足，请切换用户");
            //{"flag":false,"message":"用户权限不足，请切换用户"}
            //resp.getWriter().print("{\"flag\":false,\"message\":\"用户权限不足，请切换用户\"}");
        }
    }

    @Override
    public void destroy() {

    }
}
