package org.xzc.sshb.interceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.xzc.sshb.constant.Constants;
import org.xzc.sshb.domain.User;
import org.xzc.sshb.service.IUserService;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * TODO 用于记住用户爱好的选项 目前就这样用一下 用更好的想法再改动
 * 
 * @author xzchaoo
 * 
 */
public class RememberInterceptor extends AbstractInterceptor {
	/**
	 * 保存了用户的偏好设置
	 */
	private Map<String, Object> preferencesDefaultValue = new HashMap<String, Object>();
	private MongoClient mc;
	private MongoDatabase md;
	private MongoCollection<Document> preferences;

	public RememberInterceptor() {
		preferencesDefaultValue.put( "rows", 10 );
		preferencesDefaultValue.put( "defaultRecordType", 0 );

		preferencesDefaultValue = Collections.unmodifiableMap( preferencesDefaultValue );
	}

	@Override
	public void init() {
		mc = new MongoClient( "127.0.0.1", 27017 );
		md = mc.getDatabase( "sshb1" );
		preferences = md.getCollection( "preference" );
	}

	@Override
	public void destroy() {
		mc.close();
	}

	private static void fetchFromMongoDB() {
	}

	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * 目前的实现是自动将值(从params处获得的是一个字符串数组), 注入到params里 然后利用struts2的params拦截器自动帮我们注入值,这样效率不够高 因为每次都要从字符处再进行类型转换
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		Map<String, Object> params = invocation.getInvocationContext().getParameters();
		Map<String, Object> pres = (Map<String, Object>) session.get( Constants.PREFERENCE );

		if (pres == null) {
			pres = new HashMap<String, Object>( this.preferencesDefaultValue );
			session.put( Constants.PREFERENCE, pres );
		}

		User cu = userService.getCurrentUser();
		Document _id = null;
		Document d = null;
		if (cu != null) {
			_id = new Document().append( "_id", cu.getId() );
			d = preferences.find( _id ).first();
			if (d != null) {
				pres = d;
				session.put( Constants.PREFERENCE, pres );
			}
		}

		for (Entry<String, Object> e : pres.entrySet()) {
			String key = e.getKey();
			// 如果params有值 就用它更新旧的值
			if (params.containsKey( key )) {
				// 这边其实是一个String数组
				e.setValue( ( (Object[]) params.get( key ) )[0] );
			} else {
				params.put( key, e.getValue() );
			}
		}

		if (cu != null) {
			preferences.updateOne( _id, new BasicDBObject( "$set", pres ), new UpdateOptions().upsert( true ) );
		}

		return invocation.invoke();
	}
}
