package com.wtj.aop;

import com.wtj.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Aspect
public class LoginAop {

	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 环绕增强，添加了IsLogin注解的都会被增强
	 * @param joinPoint
	 * @return
	 */
	@Around("@annotation(IsLogin)")
	public Object isLogin(ProceedingJoinPoint joinPoint){
		try {
			//reids中用户信息的key
			String loginKey=null;
			//获得request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				//获得cookie中的loginKey
				if (cookie!=null){
					System.out.println("cookie"+cookie.getName());
					if ("login_token".equals(cookie.getName())){
						loginKey=cookie.getValue();
						System.out.println("loginKey:"+loginKey);
						break;
					}
				}
			}
			User user=null;
			if (loginKey!=null){
				//从redis中获取用户判断是否登录
				user = (User) redisTemplate.opsForValue().get(loginKey);
				System.out.println("user:"+user);
			}
			if (user==null){//用户为空
				//判断mustLogin的值
				MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
				Method method = methodSignature.getMethod();
				IsLogin isLogin = method.getAnnotation(IsLogin.class);
				boolean mustLogin = isLogin.mustLogin();
				System.out.println(mustLogin);
				if (mustLogin){
					System.out.println("requestUrl:"+request.getRequestURL());
					System.out.println(""+request.getQueryString());
					String returnUrl =request.getRequestURL()+"?"+request.getQueryString();
					returnUrl = URLEncoder.encode(returnUrl,"utf-8");
					return "redirect:http://localhost:8084/sso/toLogin?returnUrl="+returnUrl;
				}
			}
			//已经登录则将用户信息注入被注解的参数里
			System.out.println("aop的user"+user);
			Object[] params = joinPoint.getArgs();
			for (int i=0;i<params.length;i++){
				if (params[i].getClass()==User.class){
					params[i]=user;
				}
			}
			// 执行被注解的方法，将修改后参数传进去
			Object result= joinPoint.proceed(params);
			return result;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return null;
	}
}
