package org.september.easyconf.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.september.easyconf.util.RecordLogUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey on 2016/6/7.
 */


@Aspect
@Component
public class YoueAspect {

    //需要过滤的controller集合
    private static List<String> filterList = new ArrayList<String>();
    private static List<String> filterMethodList = new ArrayList<String>();

    static {
        filterList.add("LoginController");
        filterList.add("HttpdServerController");
        filterMethodList.add("spiderScheduled");
    }

    @Pointcut("execution(public * org.september.*.controller..*.*(..))"
            //+ "or" + "execution(public * com.youedata.nncloud.modular.system.controller..*.*(..))"
            )
    private void youePoint() {
    }

    @Before("youePoint()")
    public void before(JoinPoint point) {
        String prefix = "enter=";
        writeLog(point, prefix, null);
    }

    @After("youePoint()")
    public void after(JoinPoint point) {
        String prefix = "exiting=";
        writeLog(point, prefix, null);
    }

    @Around("youePoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = new Object();
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            try {
                String prefix = "exception=";
                writeLog(joinPoint, prefix, e);
            } catch (Exception ex) {
                String prefix = "exception=";
                writeLog(joinPoint, prefix, e);
            }
            return result;
        }
    }

    /**
     * 写入日志
     *
     * @param point
     * @param prefix
     * @param e
     */
    private void writeLog(JoinPoint point, String prefix, Throwable e) {
        try {
            String controllerName = point.getSignature().getDeclaringTypeName();
            String msg = null;
            if (controllerName.endsWith("Controller")
                    && !checkIsFilterByControllerName(controllerName)) {

                String args = getArgs(point.getArgs());
                String userName = getUserName();

                String realMethod = point.getSignature().getName();
                if (filterMethodList.contains(realMethod)) {
                    return;
                }
                String method = controllerName + "." + realMethod;

                if ("exception=".equals(prefix)) {
                    msg = prefix + "请求异常=" + e + ",请求方法=" + method + ", 参数=" + args + ", 请求人=" + userName;
                    RecordLogUtil.info(msg);
                    //LogManager.me().executeLog(LogTaskFactory.bussinessLog(getUserId(), realMethod, controllerName, realMethod, msg));
                    return;
                }
                msg = prefix + "请求方法=" + method + ", 参数=" + args + ", 请求人=" + userName;
                RecordLogUtil.info(msg);
                if ("enter=".equals(prefix)) {
                    //LogManager.me().executeLog(LogTaskFactory.bussinessLog(getUserId(), realMethod, controllerName, realMethod, msg));
                }
            }
        } catch (Exception ex) {
            RecordLogUtil.errorLog("nobody", "YoueAspect.writeLog", point, ex);
            //LogManager.me().executeLog(LogTaskFactory.exceptionLog(getUserId(), ex));
        }
    }

    /**
     * 获取参数
     *
     * @param object
     * @return
     */
    public String getArgs(Object[] object) {
        List args = new ArrayList();
        for (Object obj : object) {
            if (obj instanceof HttpServletRequest) {
                continue;
            } else if (obj instanceof HttpServletResponse) {
                continue;
            } else if (obj instanceof HttpSession) {
                continue;
            }
            args.add(obj);
        }
        return args.toString();
    }

    /**
     * 得到userName
     *
     * @return
     */
    public String getUserName() {
        String userName = null;
        try {
//            userName = ShiroKit.getUser().name;
        } catch (Exception e) {
            return userName;
        }
        return userName;
    }
    /**
     * @Author: Monkey
     * @Param: []
     * @Date: Created in  2018/8/16 10:36.
     * @Returns java.lang.String
     */
    public int getUserId() {
        int userId = 0;
        try {
//            userId = ShiroKit.getUser().id;
        } catch (Exception e) {
            return userId;
        }
        return userId;
    }
    /**
     * 检查是否是过滤的controller
     *
     * @return
     */
    public boolean checkIsFilterByControllerName(String controllerName) {
        int offset = 1;
        String suffix = controllerName.substring(controllerName.lastIndexOf(".") + offset);

        if (filterList.contains(suffix)) {
            return true;
        }

        return false;
    }

}
