package com.meiya;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author xiaopf
 */
@Aspect
@Slf4j
@Component
@ConditionalOnProperty(name = "xscaffold.log.aspect.enable",havingValue = "true",matchIfMissing = true)
public class LogAspect {

    @Pointcut("execution(* com.meiya..controller.UserController.*(..)) || execution(* com.meiya..service.*Service.*(..))")
    private void pointCut(){}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] reqArgs = pjp.getArgs();
        String req = new Gson().toJson(reqArgs);
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //类名+方法名
        String methodName = signature.getDeclaringType().getName() + "." + signature.getName();
        log.info("【{}】,request:【{}】",methodName,req);
        Long start = System.currentTimeMillis();
        //直接抛异常 不能catch 否则相当于把业务方法catch了 就没有报错信息
        Object respObj = pjp.proceed();
        Long end = System.currentTimeMillis();
        String resp = new Gson().toJson(respObj);
        log.info("【{}】,response:【{}】,costTime:【{}】ms",methodName,resp,end - start);
        return respObj;
    }
}
