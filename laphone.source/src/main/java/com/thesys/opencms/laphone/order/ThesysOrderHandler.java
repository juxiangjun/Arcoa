package com.thesys.opencms.laphone.order;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.order.dao.ThesysOrderCreditVO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;


public class ThesysOrderHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysOrderHandler.class);
	
	public int SEARCH_TYPE_DEFAULT = 1; //未出貨及一個月內訂單
	public int SEARCH_TYPE_UNSHIPPED = 2; //未出貨訂單
	public int SEARCH_TYPE_CVS = 6; //已到店
	public int SEARCH_TYPE_SHIPPED = 7; //已寄出
	
	private String orderId;
	private int searchType = SEARCH_TYPE_DEFAULT; //查詢型態- 1:未出貨及一個月內訂單,2:未出貨訂單,3:退換貨訂單,4:六個月內訂單,5:訂單編號查詢,6：已到店,7：已寄出
	private String startDate;
	private String endDate;
	private String searchMemberId;
	private String memberName;
	private String orderStatus;
	private String orderMsg;
	private String [] orderIds;
	private String atmCode;
	private String userName;
	private String payType ;

	private ThesysSerialHandler serialHandler = new ThesysSerialHandler();
	public ThesysOrderHandler(){}
	public ThesysOrderHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		init(context, req, res);
	}
	public void init(PageContext context, HttpServletRequest req,HttpServletResponse res){			
		serialHandler.init(context, req, res);
		super.init(context, req, res);
	}	
	/**
	 * 白板註記
	 * @param orderId
	 * @param note
	 * @return
	 */
	public boolean appendProcessNote(String orderId,String note){
		//TODO:
		try{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			note +="\r\n[註記人："+getUserId()+"，日期："+fmt.format(new java.util.Date())+"]\r\n";
			ThesysOrderDAO.getInstance().appendProcessNote(getSiteId(), orderId, note);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return true;
	}
	/**
	 * 後台分頁訂單
	 * @return
	 */
	public List<ThesysOrderVO> getBackendPageList(){
		List<ThesysOrderVO> result = new ArrayList<ThesysOrderVO>();
		try{
			result = ThesysOrderDAO.getInstance().listByPageForBackend(getSiteId(), getPageSize(), getPageIndex(), getOrderStatus(),
					getStartDate(), getEndDate(), getSearchMemberId(), getMemberName(), getOrderId(),getPayType());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	/**
	 * 後台訂單筆數
	 * @return
	 */
	public int getBackendCount(){
		int result =0;
		try{
			result = ThesysOrderDAO.getInstance().countForBackend(getSiteId(), getOrderStatus(), getStartDate(), 
					getEndDate(), getSearchMemberId(), getMemberName(), getOrderId(),getPayType());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}

	public List<ThesysOrderVO> getPageList(){
		List<ThesysOrderVO> result = new ArrayList<ThesysOrderVO>();
		try{
			result = ThesysOrderDAO.getInstance().listByPage(getSiteId(), getMemberId(), getPageSize(), getPageIndex(), getSearchType(), getOrderId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;		
	}
	/**
	 * 未出貨訂單
	 * @return
	 */
	public int getUnshippedCount(){
		return getSearchCount(SEARCH_TYPE_UNSHIPPED);
	}
	/**
	 * 已寄出訂單
	 * @return
	 */
	public int getShippedCount(){
		return getSearchCount(SEARCH_TYPE_SHIPPED);
	}
	/**
	 * 已到店訂單
	 * @return
	 */
	public int getCvsCount(){
		return getSearchCount(SEARCH_TYPE_CVS);
	}
	public int getCount(){
		return getSearchCount(getSearchType());
	}
	public int getSearchCount(int SearchType){
		int result =0;
		try{
			result = ThesysOrderDAO.getInstance().count(getSiteId(), getMemberId(), SearchType, this.getOrderId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	public ThesysOrderVO getSelectedOrder(){
		return getSelectedOrder(orderId);
	}
	
	public ThesysOrderVO getSelectedOrder(String orderId){
		ThesysOrderVO result = null;
		try{
			result = ThesysOrderDAO.getInstance().read(getSiteId(), orderId);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 建立訂單
	 * @param vo
	 * @return
	 */
	public boolean createOrder(ThesysOrderVO orderVO){
		try{
			//取得單號
			String orderId = serialHandler.getNextOrderNo();
			orderVO.setOrderId(orderId);
			orderVO.setSiteId(getSiteId());
			if(orderVO.getPayType()==ThesysOrderVO.PAY_TYPE_ATM){
				orderVO.setOrderStatus(ThesysOrderVO.ORDER_STATUS_ATM);
			}else if(orderVO.getPayType()==ThesysOrderVO.PAY_TYPE_CREDIT || orderVO.getPayType()==ThesysOrderVO.PAY_TYPE_INSTALLMENT || orderVO.getPayType()==ThesysOrderVO.PAY_TYPE_CHINATRUST  ){
				orderVO.setOrderStatus(ThesysOrderVO.ORDER_STATUS_CREDIT);
			}else{
				orderVO.setOrderStatus(ThesysOrderVO.ORDER_STATUS_DEFAULT);
			}
			
			ThesysOrderDAO.getInstance().insert(orderVO);
			
			if(orderVO.getPayType()==ThesysOrderVO.PAY_TYPE_CVS || orderVO.getPayType()==ThesysOrderVO.PAY_TYPE_ATM){			
				//發送信件「收到訂單」
				String host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();			
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("orderId",orderVO.getOrderId() );
				jsonObj.put("payType",orderVO.getPayTypeName() );
				jsonObj.put("shipType",orderVO.getShipTypeName() );
				jsonObj.put("receiver",orderVO.getMaskReceiver());
				jsonObj.put("orderDetail",orderVO.getMaskOrderDetail() );
				jsonObj.put("host",host);
				 
				ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				msgHandler.setMsgName("orderCreate");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setMemberId(orderVO.getMemberId());
				msgHandler.setCellphone(orderVO.getRecMobile());
				msgHandler.setEmail(orderVO.getRecEmail());
				msgHandler.sendMsg();
			}
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	/**
	 * 刷卡授權
	 * @param vo
	 * @return
	 */
	public boolean authCreditCard(ThesysOrderCreditVO vo){
		try{
			
			vo.setOrderId(vo.getOd_sob());
			vo.setSiteId(getSiteId());
			ThesysOrderDAO.getInstance().insertCredit(vo);
			
			String host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();			
			ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
			//取得訂單資料
			ThesysOrderVO orderVO = getSelectedOrder(vo.getOrderId());
			if(vo.isAuthSuccess()){ //授權成功
				//發送信件「收到訂單」
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("orderId",orderVO.getOrderId() );
				jsonObj.put("payType",orderVO.getPayTypeName() );
				jsonObj.put("shipType",orderVO.getShipTypeName() );
				jsonObj.put("receiver",orderVO.getMaskReceiver());
				jsonObj.put("orderDetail",orderVO.getMaskOrderDetail() );
				jsonObj.put("host",host);
					 
				msgHandler.setMsgName("orderCreate");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setMemberId(orderVO.getMemberId());
				msgHandler.setCellphone(orderVO.getRecMobile());
				msgHandler.setEmail(orderVO.getRecEmail());
				msgHandler.sendMsg();
			}else{	
				//授權失敗，發送付款失敗信件			
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("orderId", orderVO.getOrderId());
				jsonObj.put("host",host);
				msgHandler.setMsgName("orderFailure");
				msgHandler.setJsonObj(jsonObj);		
		 		msgHandler.setMemberId(orderVO.getMemberId());
				msgHandler.setEmail(orderVO.getMemberEmail()); 
				msgHandler.sendMsg(); 
			}
			
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	/**
	 * 審核取消的訂單
	 * @return
	 */
	public boolean checkCanceledOrder(){
		try{
			ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
			int result = dao.checkCanceledOrder(getSiteId(), getOrderId(),ThesysOrderVO.ORDER_STATUS_CANCELED,getUserId());
			return result==1;
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 審核退貨訂單(客服已聯絡退貨)
	 * @return
	 */
	public boolean checkReturnOrder(){
		try{
			ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
			return dao.checkReturn(getSiteId(), getOrderId(),ThesysOrderVO.ORDER_STATUS_RETURN_CHECKED,getUserId())==1;
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	/**
	 * 處理退貨訂單(貨運已收取退貨商品)
	 * @return
	 */
	public boolean processReturnOrder(){
		try{
			ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
			return dao.processReturn(getSiteId(), getOrderId(),ThesysOrderVO.ORDER_STATUS_RETURN_PROCESSING,getUserId())==1;
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 取消訂單
	 * @return
	 */
	public boolean cancelOrder(ThesysOrderVO orderVO){
		try{
			ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
			//尚未拋轉至SAP的訂單，可以直接取消
			boolean isCanceled = (orderVO.getOrderStatus()<ThesysOrderVO.ORDER_STATUS_SAP_POSTED);
			orderVO.setCancelDate(new Date());
			int result = ThesysOrderDAO.getInstance().cancelOrder(getSiteId(), orderVO.getMemberId(), orderVO.getOrderId(),
					isCanceled?ThesysOrderVO.ORDER_STATUS_CANCELED:ThesysOrderVO.ORDER_STATUS_CANCELING , 
							orderVO.getCancelContacter(), orderVO.getCancelContactPhone(), orderVO.getCancelCode(), orderVO.getCancelReason(),orderVO.getCancelDate());
			if(result==1){
				
				//發送訂單已取消通知  		
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();  
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("orderId", orderId);
				jsonObj.put("cancelDate", fmt.format(orderVO.getCancelDate()));  				
				jsonObj.put("host",host);
				msgHandler.setMsgName("orderCancel");
				msgHandler.setJsonObj(jsonObj);		
		 		msgHandler.setMemberId(orderVO.getMemberId());
				msgHandler.setEmail(orderVO.getMemberEmail()); 
				msgHandler.sendMsg(); 
				
				if(!isCanceled){ 
					//發送取消訂單通知給客服人員
					String serviceEmail = ThesysParamDAO.getInstance().getParam(getSiteId(),"OP_EMAIL").getParamVal();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					
					jsonObj = new JSONObject();
					jsonObj.put("memberId",orderVO.getMemberId());
					jsonObj.put("memberName",orderVO.getMemberName());
					//jsonObj.put("memberPhone",orderVO.getMemberMobile());
					//jsonObj.put("contactName",orderVO.getCancelContacter());
					jsonObj.put("contactPhone",orderVO.getCancelContactPhone());
					jsonObj.put("orderId",orderVO.getOrderId());
					jsonObj.put("orderDate",sdf.format(orderVO.getOrderDate()));
					jsonObj.put("payType",orderVO.getPayTypeName());
					jsonObj.put("shipType",orderVO.getShipTypeName());
					jsonObj.put("orderAmount",String.valueOf(orderVO.getOrderAmount()));
					jsonObj.put("cancelReason",orderVO.getCancelReason());
								
    				msgHandler.setMsgName("orderCancelNotice");
    				msgHandler.setJsonObj(jsonObj);		
    				msgHandler.setEmail(serviceEmail); 
    				msgHandler.sendMsg(); 
				}
				return true;
				
			}
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}

	/**
	 * 申請退貨
	 * @return
	 */
	public boolean returnOrder(ThesysOrderVO orderVO){
		ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
		ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
		try{
			String host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();  
			if(orderVO!=null){	
				orderVO.setReturnDate(new Date());
				int result = dao.returnOrder(getSiteId(), orderVO.getOrderId(),ThesysOrderVO.ORDER_STATUS_RETURN_DEFAULT,
						orderVO.getReturnType(), orderVO.getReturnCode(), orderVO.getReturnReason(),orderVO.getReturnDate());
				if(result==1){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");
					String returnDate = sdf.format(orderVO.getReturnDate()).replace("AM", "上午").replace("PM", "下午");
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("returnDate",returnDate);
					jsonObj.put("returnReason",orderVO.getReturnReason());
					jsonObj.put("orderId",orderVO.getOrderId() );
					jsonObj.put("receiver",orderVO.getMaskReceiver());
					jsonObj.put("recAddress",orderVO.getMaskRecAddress());
					jsonObj.put("recMobile",orderVO.getMaskRecMobile());
					jsonObj.put("host",host);
					
					msgHandler.setMsgName("orderReturn");
					msgHandler.setJsonObj(jsonObj);
					msgHandler.setEmail(orderVO.getMemberEmail());
					msgHandler.sendMsg();
				}else{
					throw new Exception("訂單編號「"+orderId+"」申請退貨失敗");
				}
			}else{
				throw new Exception("訂單編號「"+orderId+"」不存在");
			}
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	/**
	 * 更新ATM後五碼,並將訂單狀態改成ThesysOrderVO.ORDER_STATUS_DEFAULT
	 * @return
	 */
	public int updateAtmCode(){
		try{
			ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
			String memId = getMemberId();
			if(memId!=null && !"".equals(memId)){
				ThesysOrderVO vo = dao.read(getSiteId(), memId, getOrderId());
				if(vo!=null && vo.isCanEnterAtmCode())
					return dao.updateAtmCode(getSiteId(), memId, getOrderId(), getAtmCode());
			}
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return 0;
	}
	/**
	 * 註記收款報告單
	 * @return
	 */
	public boolean checkReceipt(String orderId){
		try{
			return ThesysOrderDAO.getInstance().updateReceiptDate(getSiteId(), orderId, new java.util.Date(), getUserId())==1;
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	/**
	 * 更新發票寄送日期
	 * @param orderId
	 * @param invoiceDate
	 * @return
	 */
	public boolean updateInvoiceDate(String orderId,Date invoiceDate){
		try{
			return ThesysOrderDAO.getInstance().updateInvoiceDate(getSiteId(), orderId, invoiceDate, getUserId())==1;
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	/**
	 * 批次更新發票寄送日期
	 * @param orderIds
	 * @param invoiceDate
	 * @return
	 */
	public int batchUpdateInvoiceDate(String[] orderIds,Date invoiceDate){
		ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
		int checkCount = 0;
		try{
			
			for(int i=0;i<orderIds.length;i++){
			
				try{
					String orderId = orderIds[i];
					ThesysOrderVO orderVO = dao.read(getSiteId(), orderId);
					if(orderVO!=null){
						if(1 == dao.updateInvoiceDate(getSiteId(), orderId, invoiceDate, getUserId())){							
								checkCount++;
						}else{
							throw new Exception("訂單編號「"+orderId+"」審核失敗");
						}
					}else{
						throw new Exception("訂單編號「"+orderId+"」不存在");
					}
				}catch(Exception ex){
					ex.printStackTrace();
					LOG.error(ex, ex.fillInStackTrace());
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return checkCount;
	}
	/**
	 * 批次註記收款報告單
	 * @return
	 */
	public int batchCheckReceipt(String[] orderIds){
		ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
		int checkCount = 0;
		try{
			
			for(int i=0;i<orderIds.length;i++){
			
				try{
					String orderId = orderIds[i];
					ThesysOrderVO orderVO = dao.read(getSiteId(), orderId);
					if(orderVO!=null){
						if(1 == dao.updateReceiptDate(getSiteId(), orderId, new java.util.Date(), getUserId())){							
								checkCount++;
						}else{
							throw new Exception("訂單編號「"+orderId+"」審核失敗");
						}
					}else{
						throw new Exception("訂單編號「"+orderId+"」不存在");
					}
				}catch(Exception ex){
					ex.printStackTrace();
					LOG.error(ex, ex.fillInStackTrace());
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return checkCount;
	}
	
	/**
	 * 批次審核訂單
	 * @return
	 */
	public int checkOrder(){
		ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
		ThesysOrderDAO dao = ThesysOrderDAO.getInstance();
		String[] orderIds = getOrderIds();
		int checkCount = 0;
		try{
			String host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();  
		
			for(int i=0;i<orderIds.length;i++){
			
				try{
					String orderId = orderIds[i];
					ThesysOrderVO orderVO = dao.read(getSiteId(), orderId);
					if(orderVO!=null){
						if(1 == dao.checkOrder(getSiteId(), orderId,ThesysOrderVO.ORDER_STATUS_CHECKED, getUserId())){
							//信用卡及ATM發送付款成功信件
							if(orderVO.getOrderStatus() == ThesysOrderVO.PAY_TYPE_ATM ||
									orderVO.getOrderStatus() == ThesysOrderVO.PAY_TYPE_CREDIT ||
									orderVO.getOrderStatus() == ThesysOrderVO.PAY_TYPE_INSTALLMENT ||
									orderVO.getOrderStatus() == ThesysOrderVO.PAY_TYPE_CHINATRUST){
								//發送付款成功信件
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("orderId",orderVO.getOrderId() );
								jsonObj.put("payType",orderVO.getPayTypeName() );
								jsonObj.put("shipType",orderVO.getShipTypeName() );
								jsonObj.put("receiver",orderVO.getMaskReceiver());
								jsonObj.put("orderDetail",orderVO.getMaskOrderDetail() );
								jsonObj.put("host",host);				 
								msgHandler.setMsgName("orderSuccess");
								msgHandler.setJsonObj(jsonObj);
								msgHandler.setMemberId(orderVO.getMemberId());
								msgHandler.setCellphone(orderVO.getRecMobile());
								msgHandler.setEmail(orderVO.getRecEmail());
								msgHandler.sendMsg();
								checkCount++;
							}
						}else{
							throw new Exception("訂單編號「"+orderId+"」審核失敗");
						}
					}else{
						throw new Exception("訂單編號「"+orderId+"」不存在");
					}
				}catch(Exception ex){
					ex.printStackTrace();
					LOG.error(ex, ex.fillInStackTrace());
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return checkCount;
	}
	
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public int getSearchType() {
		return searchType;
	}
	
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSearchMemberId() {
		return searchMemberId;
	}
	public void setSearchMemberId(String searchMemberId) {
		this.searchMemberId = searchMemberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderMsg() {
		return orderMsg;
	}
	public void setOrderMsg(String orderMsg) {
		this.orderMsg = orderMsg;
	}
	public String[] getOrderIds() {
		return orderIds;
	}
	public void setOrderIds(String[] orderIds) {
		this.orderIds = orderIds;
	}
	public String getAtmCode() {
		return atmCode;
	}
	public void setAtmCode(String atmCode) {
		this.atmCode = atmCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the payType
	 */
	public String getPayType() {
		return payType;
	}
	/**
	 * @param payType the payType to set
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
}
