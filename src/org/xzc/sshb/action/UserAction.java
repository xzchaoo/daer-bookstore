package org.xzc.sshb.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.util.DigestUtils;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.interceptor.IResultCodeReplace;
import org.xzc.sshb.service.IStateService;
import org.xzc.sshb.service.IUserService;
import org.xzc.sshb.utils.ActionException;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport implements SessionAware, IResultCodeReplace {
	// 用于存放json结果
	private Map<String, Object> jsonResult = new HashMap<String, Object>();
	// session
	private Map<String, Object> session;

	// 一般来说 操作上下文都会有一个用户
	// 比如要添加用户时 修改用户时 删除用户时
	private User user;

	// 有些操作要列出一群用户
	private List<User> userList;

	// User的逻辑层
	private IUserService userService;
	private IStateService stateService;

	// 管理员方式添加一名用户
	// 无条件添加
	// 由admin拦截器这个函数只有管理员才能访问
	// TODO
	public String add() {
		userService.add( user );
		return SUCCESS;
	}

	// 跳转到管理员添加用户的页面
	// 由admin拦截器这个函数只有管理员才能访问
	public String addUI() {
		return SUCCESS;
	}

	// 用于用户登录和注册时候检测名字是否合法的ajax
	// 可以考虑不要硬编码
	// TODO
	public String checkNameAvailableAjax() {
		Boolean flag = user != null && user.getName() != null;
		if (flag) {
			String name = user.getName();
			if (name.length() >= 4 && name.length() <= 20) {
				flag = !userService.isNameExists( name );
				jsonResult.put( "success", flag );
				jsonResult.put( "msg", flag ? "可用的名称" : "名字已经存在" );
				return SUCCESS;
			}
		}
		jsonResult.put( "success", false );
		jsonResult.put( "msg", "名字长度必须介于4~20" );
		return SUCCESS;
	}

	public String checkNameExistsAjax() {
		Boolean flag = user != null && user.getName() != null;
		if (flag) {
			String name = user.getName();
			flag = userService.isNameExists( name );
			p( "success", flag ).p( "msg", flag ? "名字已经存在" : "名称不存在" );
		} else {
			p( "success", false ).p( "msg", "名称不存在" );
		}
		return SUCCESS;
	}

	private UserAction p(String key, Object value) {
		jsonResult.put( key, value );
		return this;
	}

	// 需要带一个user.id过来
	// 管理员删除一个用户
	// 由admin拦截器这个函数只有管理员才能访问
	public String delete() {
		if (user == null || user.getId() == null)
			throw new ActionException( "没有指定参数user.id" );
		// 可能需要做一些检查 来判断这个用户能不能被删除
		user = userService.get( user.getId() );
		User cu = userService.getCurrentUser();
		if (cu.equals( user ))
			throw new ActionException( "你不能删除自己" );
		if (user.isAdmin())
			throw new ActionException( "当前不允许你删除另外一个管理员" );

		// 可以删除的
		// 但其实不要删除而是打上一个标记
		userService.markAsDeleted( user );

		return SUCCESS;
	}

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public User getUser() {
		return user;
	}

	public List<User> getUserList() {
		return userList;
	}

	// 这个只有管理员可以访问
	public String listUI() throws Exception {
		userList = userService.list();
		return SUCCESS;
	}

	public String login() {
		if (user != null && user.getName() != null && user.getPassword() != null) {
			// 加密
			user.setPassword( DigestUtils.md5DigestAsHex( user.getPassword().getBytes() ) );
			user = userService.get( user.getName(), user.getPassword() );
			if (user != null) {
				userService.setCurrentUser( user );
				return SUCCESS;
			}
		}
		return "wrongPassword";
	}

	public String loginUI() {
		return SUCCESS;
	}

	/**
	 * 注销
	 * 
	 */
	public String logout() {
		session.clear();
		return SUCCESS;
	}

	/**
	 * 进行注册 我们只提取有效的注册参数 比如对于字段money是不理会的
	 */
	public String register() throws Exception {
		// 清除无效参数
		// 送点钱
		user.setMoney( 100 );
		user.setState( stateService.getByClass( User.class ) );
		user.setRecords( null );
		user.setPassword( DigestUtils.md5DigestAsHex( user.getPassword().getBytes() ) );
		userService.add( user );
		userService.setCurrentUser( user );
		// addFieldError( fieldName, errorMessage );
		return SUCCESS;
	}

	public String registerUI() {
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * 修改当前用户,用与用户更新自己,这里并不更新用户密码,需要另外的地方处理更新密码的问题
	 * 
	 * @return
	 */
	public String update() {
//		/只更新这4个字段其他不更新
		User cu = userService.getCurrentUser();
		cu.setBirthday( user.getBirthday() );
		cu.setEmail( user.getEmail() );
		cu.setSex( user.getSex() );
		cu.setAddress( user.getAddress() );
		// cu已经被自动刷新了(MyAutoInjectInterceptor)
		// 此时cu已经在session里了
		// 所以不需要主动的去更新它==//
		// userService.update( user );
		return SUCCESS;
	}

	/**
	 * 由管理员来更新某用户,管理员对此有至高无上的权力
	 * 
	 * @return
	 */
	public String updateByAdminUI() {
		user = userService.get( user.getId() );
		return SUCCESS;
	}

	/**
	 * 由管理员来更新某用户,管理员对此有至高无上的权力
	 * 
	 * @return
	 */
	public String updateByAdmin() {
		return SUCCESS;
	}

	public String viewUserUI() {
		user = userService.getCurrentUser();
		return SUCCESS;
	}

	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}
}
