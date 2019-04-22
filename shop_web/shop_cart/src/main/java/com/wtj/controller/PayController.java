package com.wtj.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wtj.entity.Orders;
import com.wtj.service.IOrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pay")
public class PayController {
	private final String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArCV7GKmz/qR2+wNIRyJO53kVfGO8LsAPgoHMgc0rDZg/2V0yIH4kEg78rUOfxeOuBPKZWoZT5+lyqCfpqymFT1SCGy2AQI/7aSWJF2cUORpwO1Q5ZRti63qnHc7BqcSU6bgte/+BllzlkrnPTI86CEG7VscUcICB4i96v1nP0YwUpOnAq8v/1pDjHy67CTqXZFo9fY28HaJ01k/WGk1yKQ5AIk+n1oeqE6tAp3zdQ62QCMe7fHkXsWJlWIh/xLsP8REJG2Hnk0NCRWQKdzadC36nHEnEMW7Ov0XkLL3im1mJnnXx+0xWgpGyh2RS4XviasqSJLPjTzP51bdXmRu1DQIDAQAB";
	private final String APP_PRIVATE_KEY="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDj/gU3+4nOkQBVhIZWv0Dcl4Sc5KRqeGoPZZV3OgbrpDqZi+0+LUoLXao9uPXf/tnG0AmbdGrXMTjpt0S8X3xVJk2+lf3WC0aG09gDLt70F4Fd+2kb4QljftxVbcouH+zWS4yEnKfXc6OPBJx24gFkPagRlOE9AJPbmDiOr597bVcD/qsO7CVaE83HvN9cUrFaq0exP/QU5nIz2hgHGm5SHuxGvHD7al3QVtQTxeDYSi8BxWrqOsyRWZ+bgfsJG1J5w68e+TRYY+LJjWX5c7LfDyqxFVg61P2Bvp/VshRl1cVtt/VVevDkyD0LK4U69DncNwLcu2TzAlk6TqfSfWtjAgMBAAECggEBANXKqvwfHM+eOzD2D78iouD4GDvDT8YjWllLyce+myS2oNjBVNvcPjXARFaSrcW5Rl/fT1L97P4TwymuWH9IDuAOBhM0tY3UjvL5jfMWLa24qPAHRD4HXPV2zYgyZsev2jftKjxkZiNr5bDEAzCbp/BmvMaX1EqO0xe1ezuZxjRGwxmMy5PT/xc1T/1HNROfCU0wSLQQcj0XREI96hjmiDbrMAGBflfsGvhISLQ8wJMHfsOUa9588WgH/i/T34Kww65X1bIfSCj9eU5QzVOC0mdE1F9BwZ2T1/ve4smR/IPdjHALTa4h7OSlpG37xAS2EdiozMhLG12wtCCYUtf8A+kCgYEA/WHV803a5ZwRhz7+Q//umAQb3AqjIiNO8/UY1LIV5I0JLHGf2arSaLOtJya9zhsJPKDyDxXkxHTKFl//dsTkRTsyzEJP/qzQhVofRA3hf9LQqRy/oi8Hz7l48DlHFx034CuLcB2hXnAJQ0iOx318v0a97BSThTM+PvknzsnF5ocCgYEA5lkIESAmbXBEki4Nqk7W9XEY3uijM1ruPLekMIVS+yT8IHKZrpc6h92+Z2DSe593AEl59CNyWrh95dXp3oNOjHI8CkG2HimOA9qWQNyA2Y6KYXyl/OXzUkQp0KaqlxWP86xfwk9Myzov7lof3QJ4Z8xE+ZFZSMHz5EMkG+/wr0UCgYBtnaWxXD0WmC39FINrZ40QtrZWu0NXwW6mm6HiYiiWd21AjOdag0wDf7eo+FmkkI2ubPJLFG2jj+LdSyp5dzRJvxJ6yzVQN/L9vb9I8bhcVBdxEb7VqwL2Gob3ZdAbb8h8clogZrEo6c8YDIu4QdF17mS7fStUtJZ5J9DQ5Pl4gQKBgHveIJJlAYUUPCuHjF2q67ksyj0ESM6S9U29SxO0fj0DVHl2ZuIV/KmXOo6I6GcGB44l9OiuLw5N65WuhIbdgV3RCCxTzcVGf1HfN7FFYhybpBpsVy450bDJatsC5PU96R0V8qCg2CDo92FrIXtNmsXmeKjj2mlfsvFqkX5cdscpAoGBAKzXCJTnRM6KuAV+l+gCUdYtow5/zasjwjU3ANpKPbW8spaAw1s5OXlw+Zumycm0sEDbNeVfr5BiKRNvvXbjlDIp1Kj0fh8XmyICwtLHIJtpDAUVrmCyJ1jUex84V7OSV48mGXiRkMG72BnnGgFZorVK39IvIlO9c/OULKzcJ6Ie";
	private static AlipayClient alipayClient=null;
	@Reference
	private IOrdersService ordersService;

	public PayController(){

		alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",	//沙箱的网关
				"2016100100637869", APP_PRIVATE_KEY,
				"json", "UTF-8",
				ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient
	}
	/**
	 * 去支付宝支付
	 * @param httpResponse
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/aliPay")
	public void aliPay(String orderId,
						 HttpServletResponse httpResponse) throws IOException {
		Orders orders = ordersService.getOrderByOId(orderId);
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
		alipayRequest.setReturnUrl("http://2u454575h6.wicp.vip/pay/returnPary");
		alipayRequest.setNotifyUrl("http://2u454575h6.wicp.vip/pay/notifyPay");//在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" +
				"    \"out_trade_no\":\""+orderId+"\"," +
				"    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
				"    \"total_amount\":"+orders.getAllPrice().doubleValue()+"," +
				"    \"subject\":\"测试产品\"," +
				"    \"body\":\"测试产品\"," +
				"    \"extend_params\":{" +
				"    \"sys_service_provider_id\":\"2088511833207846\"" +
				"    }"+
				"  }");//填充业务参数
		String form="";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		httpResponse.setContentType("text/html;charset=utf-8");
		httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();
	}

	/**
	 * 支付宝同步响应地址
	 * @return
	 */
	@RequestMapping("/returnPary")
	public String returnPay(){
		return "returnPay";
	}

	/**
	 * 异步接受响应结果
	 * @return
	 */
	@RequestMapping("/notifyPay")
	public void notifyPay(String out_trade_no,String trade_status){
		Orders orders = ordersService.getOrderByOId(out_trade_no);
		System.out.println("支付结果页面，，"+out_trade_no+"||"+trade_status+"||"+orders);
		if ("TRADE_SUCCESS".equals(trade_status)){
			orders.setStatus(1);
			System.out.println("修改订单的状态。。"+ orders);
			ordersService.updateOrder(orders);
		}
	}

}
