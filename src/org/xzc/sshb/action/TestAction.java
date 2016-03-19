package org.xzc.sshb.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {
	private Date date;
	private Map<String, Object> jsonResult = new HashMap<String, Object>();

	public String testDate() {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		System.out.println( sdf.format( date ) );
		return SUCCESS;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	private List<File> img;
	private List<String> imgFileName;
	private List<String> imgContentType;

	public String uploadUI() {
		return SUCCESS;
	}

	public String upload() throws Exception {
		System.out.println( "正在上传" );
		System.out.println( img );
		System.out.println( imgFileName );
		System.out.println( imgContentType );
		if (img != null)
			for (File f : img) {
			System.out.println( f.length() );
		}
		// for (int i = 0; i < 10; ++i) {
		// Thread.sleep( 1000 );
		// System.out.println( i );
		// }
		System.out.println( "上传成功" );
		jsonResult.put( "msg", "上传成功" );
		jsonResult.put( "success", true );
		return SUCCESS;
	}

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public List<File> getImg() {
		return img;
	}

	public void setImg(List<File> img) {
		this.img = img;
	}

	public List<String> getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(List<String> imgFileName) {
		this.imgFileName = imgFileName;
	}

	public List<String> getImgContentType() {
		return imgContentType;
	}

	public void setImgContentType(List<String> imgContentType) {
		this.imgContentType = imgContentType;
	}

	public void setJsonResult(Map<String, Object> jsonResult) {
		this.jsonResult = jsonResult;
	}

}
