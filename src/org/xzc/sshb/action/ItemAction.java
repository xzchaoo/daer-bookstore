package org.xzc.sshb.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ognl.Ognl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;
import org.xzc.sshb.domain.Category;
import org.xzc.sshb.domain.Item;
import org.xzc.sshb.domain.State;
import org.xzc.sshb.service.ICartService;
import org.xzc.sshb.service.ICategoryService;
import org.xzc.sshb.service.IItemService;
import org.xzc.sshb.service.IRecordItemService;
import org.xzc.sshb.utils.ActionException;
import org.xzc.sshb.utils.ObjectUtils;
import org.xzc.sshb.utils.StringUtils;
import org.xzc.sshb.value.ItemViewDetailSalesVolumeValue;
import org.xzc.sshb.value.RankItemValue;

import com.opensymphony.xwork2.inject.Inject;

//http://www.open-open.com/lib/view/open1325518231062.html json

public class ItemAction extends MyBaseAction {
	public static final class GetRankItemValue {
		public List<RankItemValue> items;
		public String names;
		public String quantities;
	}

	private static final Log LOG = LogFactory.getLog( ItemAction.class );

	/**
	 * 结果发现 字符串不会自带单引号...
	 */
	private String dayRankItemNamesString;

	private String dayRankItemQuantitiesString;

	private List<RankItemValue> dayRankItems;

	/**
	 * 所有用户对该商品的评论
	 */
	private List<ItemEvaluationValue> evaluationList;

	/**
	 * 用于保存上传的图片
	 */
	private File img;

	/**
	 * fileupload的图片名,用于add和update方法中对物品图片的信息
	 */
	private String imgFileName;

	/**
	 * 用于传送item信息
	 */
	private Item item;

	/**
	 * 用于显示itemList列表 listUI
	 */
	private List<Item> itemList;

	/**
	 * 要搜索的关键字
	 */
	private String keyword;
	private String monthRankItemNamesString;

	private String monthRankItemQuantitiesString;
	private List<RankItemValue> monthRankItems;

	private IRecordItemService recordItemService;
	/**
	 * 返回销量情况 比如 最近7天 哪些人(记得打码)购买了这个商品 目标找出买了该商品的用户 及其订单(因为需要日期) 而订单上已经关联了用户 所以本质上是找出订单 select r.id from Record as r
	 * left join RecordItem as ri on r.id=ri.record_id where ri.item_id= 指定的itemid select ri.record from RecordItem as
	 * ri where ri.item=指定的item
	 */
	private List<ItemViewDetailSalesVolumeValue> salesVolumeList;

	/**
	 * 用于保存struts2注入的保存上传文件的位置
	 */
	private String uploadPath;

	/**
	 * 上次更新排行榜的时间
	 */
	private Date lastUpdateSaleRankDate;

	public Date getLastUpdateSaleRankDate() {
		return lastUpdateSaleRankDate;
	}

	private String generateRandomFileName() {
		String filename = img.getName();
		filename = FilenameUtils.getBaseName( filename ) + "." + FilenameUtils.getExtension( imgFileName );
		return filename;
	}

	/**
	 * 处理添加商品的请求 TODO有待改善
	 * 
	 * @return
	 */
	public String add() throws Exception {
		String filename = saveImageFile();
		item.setImgPath( uploadPath + "/" + filename );
		StringUtils.escapeHtml( item, "author", "name", "shortDescription", "longDescription" );
		item.setState( stateService.get( State.ITEM_NORMAL ) );
		itemService.add( item );
		return SUCCESS;
	}

	private List<Category> categoryList;

	public List<Category> getCategoryList() {
		return categoryList;
	}

	private ICategoryService categoryService;

	public void setCategoryService(ICategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public String addUI() {
		categoryList = categoryService.list();
		return SUCCESS;
	}

	/**
	 * TODO 考虑处理掉
	 * 
	 * @return
	 */
	@Deprecated
	public String delete() {
		checkItem();
		itemService.delete( item );
		return SUCCESS;
	}

	public String getDayRankItemNamesString() {
		return dayRankItemNamesString;
	}

	public String getDayRankItemQuantitiesString() {
		return dayRankItemQuantitiesString;
	}

	public List<RankItemValue> getDayRankItems() {
		return dayRankItems;
	}

	public List<ItemEvaluationValue> getEvaluationList() {
		return evaluationList;
	}

	public File getImg() {
		return img;
	}

	public Item getItem() {
		return item;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getMonthRankItemNamesString() {
		return monthRankItemNamesString;
	}

	public String getMonthRankItemQuantitiesString() {
		return monthRankItemQuantitiesString;
	}

	public List<RankItemValue> getMonthRankItems() {
		return monthRankItems;
	}

	public List<ItemViewDetailSalesVolumeValue> getSalesVolumeList() {
		return salesVolumeList;
	}

	/**
	 * 用于list页面显示分类用的
	 */
	private int category;

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * 列出所有商品(一般是要分页的),并且还可以处理关键字搜索
	 * 
	 * @return
	 */
	public String listUI() {
		System.out.println( category );
		if (keyword != null && !keyword.isEmpty()) {
			itemList = itemService.listByNameKeyword( category,keyword, offset, count );
			total = itemService.listByNameKeywordCount( category,keyword );
		} else {
			//=0说明不用分类,显示所有类型
			if (category == 0) {
				itemList = itemService.list( offset, count );
				total = itemService.listCount();
			}else{//否则就要有分类
				itemList=itemService.listByCategory(category,offset,count);
				total = itemService.listByCategoryCount(category);
			}
		}
		categoryList = categoryService.list();
		return SUCCESS;
	}

	/**
	 * 准备排行榜数据
	 * 
	 * @return
	 */
	public String rankUI() {
		monthRankItems = itemService.getMonthRankItems();
		lastUpdateSaleRankDate = itemService.getLastUpdateSaleRankDate();
		try {
			monthRankItemNamesString = JSONUtil.serialize( Ognl.getValue( "#this.{item.name}", monthRankItems ) );
			monthRankItemQuantitiesString = JSONUtil.serialize( Ognl.getValue( "#this.{quantity}", monthRankItems ) );
		} catch (Exception e) {
			// ignore
		}
		// 当天排行榜
		// Date today = new Date();
		// now = DateUtils.addDays( now, -1 );
		// today = DateUtils.truncate( today, Calendar.DAY_OF_MONTH );
		// get top 10
		// GetRankItemValue ret = getRankItem( today, 10 );

		// monthRankItems = ret.items;
		// monthRankItemNamesString = ret.names;
		// monthRankItemQuantitiesString = ret.quantities;

		// Date currentMonth = DateUtils.truncate( today, Calendar.MONTH );
		// ret = getRankItem( currentMonth, 10 );
		// monthRankItems = ret.items;
		// monthRankItemNamesString = ret.names;
		// monthRankItemQuantitiesString = ret.quantities;

		return SUCCESS;
	}

	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

	public void setImg(File img) {
		this.img = img;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setRecordItemService(IRecordItemService recordItemService) {
		this.recordItemService = recordItemService;
	}

	/**
	 * 注入文件上传后保存的路径名,由struts2注入
	 */
	@Inject(value = "item.image.uploadPath")
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	private String saveImageFile() throws IOException {
		// null表明不修改图片
		if (img == null)
			return null;
		String dir = ServletActionContext.getServletContext().getRealPath( uploadPath );
		String filename = generateRandomFileName();
		File save = new File( dir, filename );
		FileUtils.copyFile( img, save );
		return filename;
	}

	/**
	 * 对物品进行更新,先从数据库get,再修改就可以不用update
	 * 
	 * @return
	 */
	public String update() throws Exception {
		Item i = itemService.get( item.getId() );

		StringUtils.escapeHtml( item, "name", "author", "shortDescription", "longDescription" );
		item.setState( i.getState() );
		String filename = saveImageFile();
		if (filename != null)
			item.setImgPath( uploadPath + "/" + filename );

		BeanUtils.copyProperties( i, item );

		itemService.update( i );
		return SUCCESS;
	}

	public String updateUI() {
		item = itemService.get( item.getId() );
		categoryList = categoryService.list();
		return SUCCESS;
	}

	public String viewDetailDescriptionUI() {
		item = itemService.get( item.getId() );
		return SUCCESS;
	}

	/**
	 * 获得对商品的评论,是一个片段
	 * 
	 * @return
	 */
	public String viewDetailEvaluationUI() {
		checkItem();

		// 获得商品的评论,分页
		evaluationList = recordItemService.listEvaluations( item, offset, count );

		// 获得商品评论的总数,因为你要分页 所以需要再查询总数
		total = recordItemService.listEvaluationsCount( item );

		return SUCCESS;
	}

	/**
	 * 获得商品的销量情况,是一个片段
	 * 
	 * @return
	 */
	public String viewDetailSalesVolumeUI() {
		checkItem();
		salesVolumeList = recordItemService.listSalesVolume( item, offset, count );
		total = recordItemService.listByItemCount( item );
		return SUCCESS;
	}

	/**
	 * 查看商品详情
	 * 
	 * @return
	 */
	public String viewDetailUI() {
		checkItem();
		item = itemService.get( item.getId() );
		return SUCCESS;
	}

	/**
	 * 检查item是否存在,并且顺便刷新一下item
	 */
	private void checkItem() {
		if (item == null)
			throw new ActionException( "没有指定item.id" );

		int oldId = item.getId();
		item = itemService.get( item.getId() );
		if (item == null)
			throw new ActionException( "item.id=" + oldId + "对应的item不存在" );
	}

	/**
	 * 
	 * @param date
	 * @param count
	 * @return
	 */
	/*private GetRankItemValue getRankItem(Date date, int count) {
		GetRankItemValue ret = new GetRankItemValue();
		ret.items = itemService.listSaleRank( date, count );
		List<String> names = new ArrayList<String>();
		List<Integer> quantities = new ArrayList<Integer>();
		for (RankItemValue v : ret.items) {
			names.add( v.item.getName() );
			quantities.add( v.quantity );
		}

		try {
			ret.names = JSONUtil.serialize( names );
			ret.quantities = JSONUtil.serialize( quantities );
		} catch (Exception e) {
			// ignore
		}
		return ret;
		
	}*/

}

// 获得购物车内容
// 返回总价和书本(包含名字价格数量id)
// 顺便返回一下用户的余额吧
/*	public String doGetCartAjax() {
		System.out.println( "doGetCartAjax rows=" + rows + " page=" + page );
		List<ShowCartItemValue> list = new ArrayList<ShowCartItemValue>();
		List<CartItem> cis = cartService.getCart().getCartItemList();
		for (int index = rows * ( page - 1 ); index < rows * page && index < cis.size(); ++index) {
			CartItem ci = cis.get( index );
			ShowCartItemValue i = new ShowCartItemValue();
			i.setId( ci.item.getId() );
			i.setName( ci.item.getName() );
			i.setPrice( ci.item.getPrice() );
			i.setQuantity( ci.quantity );
			list.add( i );
		}
		jsonResult.put( "rows", list );
		jsonResult.put( "total", cis.size() );
		jsonResult.put( "success", true );
		jsonResult.put( "msg", "成功" );
		jsonResult.put( "totalPrice", cartService.getTotalPrice() );
		jsonResult.put( "userMoney", userService.getCurrentUser().getMoney() );
		return SUCCESS;
	}
*/
