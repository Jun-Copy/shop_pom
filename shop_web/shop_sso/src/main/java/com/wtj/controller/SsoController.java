package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wtj.entity.Email;
import com.wtj.entity.User;
import com.wtj.service.IShopCartService;
import com.wtj.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SsoController {

	@Reference
	private IUserService userService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Reference
	private IShopCartService shopCartService;

	@RequestMapping("/toLogin")
	public String toLogin(String returnUrl,Model model){
		model.addAttribute("returnUrl",returnUrl);
		return "login";
	}

	@RequestMapping("/toRegister")
	public String toRegister(){
		return "register";
	}

	@RequestMapping("/login")
	public String login(@CookieValue(name = "cart_token",required = false)String cartToken,
						User user,String returnUrl, Model model, HttpServletResponse response){
		System.out.println("登录的信息："+user);
		User tbUser = userService.getUser(user.getUserName(),user.getPassword());
		System.out.println("查询的信息："+tbUser);
		if (tbUser==null){	//用户名或密码错误
			model.addAttribute("error","0");
			return "login";
		}
		if (tbUser.getStatus()==0){	//用户未激活
			model.addAttribute("error","1");
			int index=tbUser.getEmail().lastIndexOf("@");
			String activatePath = "http://mail."+tbUser.getEmail().substring(index+1);
			model.addAttribute("activatePath",activatePath);
			return "login";
		}
		//将用户的登录信息上传到缓存服务器
		String key = UUID.randomUUID().toString()+tbUser.getId(); //获取一个唯一的key
		redisTemplate.opsForValue().set(key,tbUser);
		redisTemplate.expire(key,30, TimeUnit.MINUTES);
		Cookie cookie = new Cookie("login_token",key);		//将缓存的key存入客户端的cookie中
		cookie.setMaxAge(60*30);
		cookie.setPath("/");
/*		cookie的安全问题
		//表示cookie只能通过请求获取，不能通过脚本获取
		cookie.setHttpOnly(true);
		//设置cookie的所属域名
		cookie.setDomain("local");
		//设置cookie的有效路径
		cookie.setPath("/");
		//设置只能通过https协议获取
		cookie.setSecure(true);*/

		//合并购物车
		shopCartService.mergeShopCart(cartToken,tbUser);

		response.addCookie(cookie);
		if(returnUrl==null || "".equals(returnUrl)){
			returnUrl="http://localhost:8081/";
		}
		return "redirect:"+returnUrl;
	}
	@RequestMapping("/register")
	public String register(User user){
		System.out.println("注册的信息："+user);
		activate(user);
		//注册用户
		User insertUser = userService.insertUser(user);
		return "login";
	}

	/**
	 * 发送激活邮件的方法
	 * @param user
	 */
	public void activate(User user){
		//生成注册邮箱
		String uuid = UUID.randomUUID().toString();
		String activatePath="http://localhost:8084/sso/activate?userName="
				+ user.getUserName()+"&activate_token="+uuid;
		Email email = new Email();
		email.setSubject("地猫商城注册邮件");
		email.setTo(user.getEmail());
		email.setContent("您正在注册地猫商城用户，需要激活，请点击：<a href='"+activatePath+"'>"+activatePath+"</a>,该连接5分钟有效，只能使用一次");
		email.setCreateTime(new Date());
		//缓存邮箱
		redisTemplate.opsForValue().set("activate_token"+user.getUserName(),uuid);
		redisTemplate.expire("activate_token"+user.getUserName(),5,TimeUnit.MINUTES);
		//添加发送邮件的消息到rabbitMQ
		rabbitTemplate.convertAndSend("email_queue",email);
	}
	@RequestMapping("/isLogin")
	@ResponseBody
	public String isLogin(@CookieValue(name = "login_token",required = false)String key){
		System.out.println("key:"+key);
		if(key==null){
			return "callback('0')";
		}
		User user = (User) redisTemplate.opsForValue().get(key);
		System.out.println("缓存中取出的user："+user);
		if (user==null){
			return "callback('0')";
		}
		return "callback('"+user.getNickName()+"')";
	}
	@RequestMapping("/activate")
	public String activate(String userName,String activate_token,Model model){
		System.out.println("userName:"+userName);
		String uuid = (String) redisTemplate.opsForValue().get("activate_token" + userName);
		System.out.println("缓存中取出的uuid："+uuid);
		if (activate_token.equals(uuid)){
			userService.updateStatus(userName);
			redisTemplate.delete("activate_token" + userName);
			model.addAttribute("error","激活成功");
			return "redirect:toLogin";
		}
		model.addAttribute("error","激活失败");
		return "redirect:toRegister";
	}

	@RequestMapping("/exitLogin")
	public String exitLogin(@CookieValue(name = "login_token",required = false)String key) {
		if (key!=null){
			redisTemplate.delete(key);
		}
		return "redirect:toLogin";
	}
}
