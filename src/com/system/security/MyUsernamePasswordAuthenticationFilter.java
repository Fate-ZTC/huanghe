package com.system.security;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.system.controller.LoginFromCCR;
import com.system.model.ResponseResult;
import com.system.service.TokenService;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.system.model.Manager;
import com.system.service.ManagerService;
import com.system.service.OptLogsService;
import com.system.service.SysconfigService;
import com.system.utils.DESHelper;
import com.system.utils.RequestUtils;
import com.system.utils.StringUtil;
import com.system.utils.WebUtils;
import sun.misc.BASE64Decoder;


public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private static final Logger logger = Logger.getLogger(MyUsernamePasswordAuthenticationFilter.class);
	public static final String USERNAME = "username";//用户名
	public static final String PASSWORD = "loginkey";//密码
	public static final String REMEMBER = "remember";//记住密码
	public static final String VALIDATE_CODE = "validateCode";//验证码
	@Resource(name = "managerService")
	private ManagerService managerService;
	@Resource(name="optLogsService")
	private OptLogsService optLogsService;
	@Resource
	private SysconfigService sysconfigService;
	@Resource(name = "tokenService")
	private TokenService tokenService;
	public ManagerService getManagerService() {
		return managerService;
	}
	public void setManagerService(ManagerService managerService) {
		this.managerService = managerService;
	}

	final BASE64Decoder decoder = new BASE64Decoder();

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled()) {
				logger.debug("不支持的提交方式: " + request.getMethod()); //$NON-NLS-1$
			}
			throw new AuthenticationServiceException("不支持的提交方式: " + request.getMethod());
		}
		checkValidateCode(request);
		String username = obtainUsername(request).trim();
		String password = obtainPassword(request).trim();

//		try {
//			password =new String(decoder.decodeBuffer(password), "UTF-8");
//			username =new String(decoder.decodeBuffer(username), "UTF-8");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		boolean remember = obtainRemember(request);
		//根据username查询出用户信息，第一次查询
		Manager manager = new Manager();
		if(WebUtils.getDefaultInstance().isVaildEmail(username)){
			manager = managerService.getUniqueByProperty("email", username);
		}
		else{
			ResponseResult responseResult = tokenService.getToken(username, password);
			//得到用户信息
			manager = tokenService.getManagerInfo(responseResult);
		}
		if(manager == null){
			if (logger.isDebugEnabled()) {
				logger.debug("用户不存在！"); //$NON-NLS-1$
			}
			throw new AuthenticationServiceException("用户不存在！");
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(manager.getUsername(), manager.getPassword());
		HttpSession session = request.getSession();
		session.setAttribute(USERNAME, manager.getUsername());
		session.setAttribute("loginUser", manager);
		session.setAttribute("systemConfig", sysconfigService.getConfig());
		setDetails(request, authRequest);
		if(remember) {
			addCookie("xygisadminrememberUserInfo", DESHelper.encryptDES("{\"username\":\"" + username+ "\"," +
					"\"password\":\"" + password + "\"}", "lqkj*gis"), 60 * 60 * 24 * 7,request.getServerName(),response);
		}else{
			addCookie("xygisadminrememberUserInfo", "", -1,request.getServerName(),response);
		}
		optLogsService.addLogo("登录", manager, "登录到系统");
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	protected void checkValidateCode(HttpServletRequest request) {
		HttpSession session = request.getSession();

	    String sessionValidateCode = obtainSessionValidateCode(session);
	    //让上一次的验证码失效
	    session.setAttribute(VALIDATE_CODE, null);
	    String validateCodeParameter = obtainValidateCodeParameter(request);
	    if (StringUtil.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
	    	if (logger.isDebugEnabled()) {
				logger.debug("验证码错误！");
			}
	        throw new AuthenticationServiceException("验证码错误！");
	    }
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		Object obj = request.getParameter(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	protected String obtainSessionValidateCode(HttpSession session) {
		Object obj = session.getAttribute(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(USERNAME);
		String s = String.valueOf(obj);
		try {
			s =new String(decoder.decodeBuffer(s), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null == s ? "" : s;
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(PASSWORD);
		String s = String.valueOf(obj);
		try {
			s =new String(decoder.decodeBuffer(s), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null == s ? "" : s;
	}
	protected boolean obtainRemember(HttpServletRequest request) {
		Object obj = request.getParameter(REMEMBER);
		if(obj != null && obj.toString().equals("on")){
			return true;
		}else{
			return false;
		}
	}
	protected void addCookie(String name,String value,int expiry,String domain,HttpServletResponse response){
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(expiry);
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}
}
