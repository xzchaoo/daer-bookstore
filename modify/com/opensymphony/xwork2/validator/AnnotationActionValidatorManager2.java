package com.opensymphony.xwork2.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.xzc.sshb.utils.JSONUtils;

import com.opensymphony.xwork2.FileManager;
import com.opensymphony.xwork2.util.ClassLoaderUtil;
import com.opensymphony.xwork2.util.logging.Logger;

public class AnnotationActionValidatorManager2 extends AnnotationActionValidatorManager1 {

	private static final boolean my1(Object o, boolean nullB) {
		return o == null ? nullB : o.toString().equalsIgnoreCase( "true" );
	}

	private static final String my2(Object o, String def) {
		return o == null ? def : o.toString();
	}

	private static final Object inputStreamToObject(InputStream is) throws JSONException, IOException {
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			cx.evaluateString( scope, IOUtils.toString( is, "utf-8" ), null, 0, null );
			Object result = scope.get( "config", scope );
			return JSONUtil.deserialize( NativeJSON.stringify( cx, scope, result, null, null ).toString() );
		} finally {
			Context.exit();
		}
	}

	private List<ValidatorConfig> createValidatorConfigJSONList(ValidatorFactory validatorFactory, InputStream is, String fileName, Logger LOG)
			throws JSONException, IOException {
		List<ValidatorConfig> validatorCfgs = new ArrayList<ValidatorConfig>();
		//Map<String, Object> json = (Map<String, Object>) JSONUtils.stringToObject2( IOUtils.toString( is, "utf-8" ) );// inputStreamToObject( is );
		Map<String, Object> json = (Map<String, Object>) inputStreamToObject(is );// inputStreamToObject( is );
		for (Map.Entry<String, Object> e : json.entrySet()) {
			String fieldName = e.getKey();
			Map<String, Object> validatorsConfig = (Map<String, Object>) e.getValue();

			for (Map.Entry<String, Object> vc : validatorsConfig.entrySet()) {
				String validatorType = vc.getKey();
				Map<String, String> params = new HashMap<String, String>( (Map<String, String>) vc.getValue() );

				boolean enabled = my1( params.get( "enabled" ), true );

				if (!enabled)
					continue;

				boolean shortCircuit2 = my1( params.get( "shortCircuit" ), false );
				String msg2 = my2( params.get( "msg" ), "错误" );
				String key = params.get( "key" );

				// 其实可以不删除 还是删了吧 不然有些警告
				params.remove( "enabled" );
				params.remove( "msg" );
				params.remove( "shortCircuit" );

				params.put( "fieldName", fieldName );

				ValidatorConfig.Builder vCfg = new ValidatorConfig.Builder( validatorType ).addParams( (Map<String, Object>) (Object) params )
						.defaultMessage( msg2 ).shortCircuit( shortCircuit2 ).messageKey( key );
				validatorCfgs.add( vCfg.build() );
			}
		}
		return validatorCfgs;
	}

	@Override
	protected List<ValidatorConfig> buildAliasValidatorConfigsJSON(Class clazz, String context, boolean checkFile, FileManager fileManager,
			Map<String, List<ValidatorConfig>> validatorFileCache, ValidatorFactory validatorFactory, Logger LOG) {
		String fileName = clazz.getName().replace( '.', '/' ) + "-" + context.replace( '/', '-' ) + "-validation.json";
		List<ValidatorConfig> retList = Collections.emptyList();
		URL fileUrl = ClassLoaderUtil.getResource( fileName, clazz );
		if (( checkFile && fileManager.fileNeedsReloading( fileUrl ) ) || !validatorFileCache.containsKey( fileName )) {
			InputStream is = null;
			try {
				is = fileManager.loadFile( fileUrl );
				if (is != null) {// 解析我的JSON
					retList = new ArrayList<ValidatorConfig>( createValidatorConfigJSONList( validatorFactory, is, fileName, LOG ) );
				}
			} catch (Exception e) {
				LOG.error( "Caught exception while loading file " + fileName, e );
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						LOG.error( "Unable to close input stream for " + fileName, e );
					}
				}
			}
			validatorFileCache.put( fileName, retList );
		} else {
			retList = validatorFileCache.get( fileName );
		}
		return retList;
	}

}