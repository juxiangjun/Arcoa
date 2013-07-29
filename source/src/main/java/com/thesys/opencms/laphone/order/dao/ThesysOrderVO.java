package com.thesys.opencms.laphone.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.thesys.opencms.dao.ThesysAbstractVO;
import com.thesys.opencms.laphone.promote.dao.ThesysCouponVO;

/**
 * shopping cart data vo
 * @author maggie
 *
 */
public class ThesysOrderVO extends ThesysAbstractVO{
	/** 付款方式：超商取貨付款 **/
	public final static int PAY_TYPE_CVS = 0;
	/** 付款方式：信用卡 **/
	public final static int PAY_TYPE_CREDIT = 1;
	/** 付款方式：信用卡分期 **/
	public final static int PAY_TYPE_INSTALLMENT = 2;
	/** 付款方式：中信分期 **/
	public final static int PAY_TYPE_CHINATRUST = 4;
	/** 付款方式：ATM轉帳 **/
	public final static int PAY_TYPE_ATM = 3;
	
	/**(ATM)會員完成購物車流程：會員完成購物車流程 */
	public final static int ORDER_STATUS_ATM = 0;
	public final static String ORD_STATUS_NAME_ATM = "尚未付款"; //尚未付款，請於X日~X日期間內付款/尚未付款，可允許轉帳的期間為訂單成立日+4日曆天
	
	/**(信用卡)會員完成購物車流程：會員完成購物車流程，未完成刷卡 */
	public final static int ORDER_STATUS_CREDIT = -1;
	public final static String ORDER_STATUS_NAME_CREDIT = "信用卡尚未取得授權"; //尚未取得授權
	
	/** 訂單成立：會員完成購物車流程；(ATM)會員已匯款：會員填寫銀行轉帳後5碼**/  
	public final static int ORDER_STATUS_DEFAULT = 1;		
	public final static String ORDER_STATUS_NAME_DEFAULT = "尚未核單";//訂單處理中/尚未核單
	/** 審核訂單：出貨中/已核單 **/  
	public final static int ORDER_STATUS_CHECKED = 2;
	public final static String ORDER_STATUS_NAME_CHECKED = "已核單";	//出貨中/已核單
	/** SAP訂單回傳：已拋轉訂單至SAP **/  
	public final static int ORDER_STATUS_SAP_POSTED = 3;
	public final static String ORDER_STATUS_NAME_SAP_POSTED = "已核單(拋轉訂單至SAP)";	//出貨中/已核單
	/** WMS完成出貨作業回傳SAP過帳：物流人員確認檢料出貨，SAP已進行庫存扣帳 **/
	public final static int ORDER_STATUS_SHIPPED = 4; 
	public final static String ORDER_STATUS_NAME_SHIPPED = "已出貨(宅配)";		 //預計收貨日期XX月XX日/已出貨，SAP 銷貨完成過帳檔的過帳日期+1日曆天當做宅配預計送貨日
	/** 便利達康到店資料回傳：便利達康將進店檔回傳EC **/
	public final static int ORDER_STATUS_CVS = 5; 
	public final static String ORDER_STATUS_NAME_CVS = "已出貨(便利達康)";	 //X日已到門市，取貨時間為Y日~Y日/已出貨，便利商店可取貨時間為商品到店時間+6個日曆天
	/** SAP回傳WMS已收貨資料：會員收到貨  || 便利達康回傳取貨資料：便利達康取貨完成檔回傳EC **/
	public final static int ORDER_STATUS_RECEIVED = 6; 
	public final static String ORDER_STATUS_NAME_RECEIVED = "已完成"; //收貨日期XX月XX日/已完成
	
	/** SAP回傳WMS送貨失敗資料：會員未收貨；便利達康回傳會員未取貨資料(物品退回至大物流)：便利達康大物流驗退檔回傳EC **/
	public final static int ORDER_STATUS_UNRECEIVED = 7; 
	public final static String ORDER_STATUS_NAME_UNRECEIVED = "送貨失敗"; //未收貨/送貨失敗
	
	/** 取消訂單 **/
	public final static int ORDER_STATUS_CANCELING = 8; 
	public final static String ORDER_STATUS_NAME_CANCELING = "申請取消中";	 //取消處理中/申請取消中
	/** 訂單已取消 **/
	public final static int ORDER_STATUS_CANCELED = 9; 
	public final static String ORDER_STATUS_NAME_CANCELED = "訂單已取消";	
	/** 申請退貨/退貨未確認 **/
	public final static int ORDER_STATUS_RETURN_DEFAULT = 10; 
	public final static String ORDER_STATUS_NAME_RETURN_DEFAULT = "退貨未確認";	
	/** 退貨確認中/退貨已確認 **/
	public final static int ORDER_STATUS_RETURN_CHECKED = 11; 
	public final static String ORDER_STATUS_NAME_RETURN_CHECKED = "退貨已確認";	
	/** 退貨處理中 **/
	public final static int ORDER_STATUS_RETURN_PROCESSING = 12; 
	public final static String ORDER_STATUS_NAME_RETURN_PROCESSING = "退貨處理中";		
	/** 已退貨 **/
	public final static int ORDER_STATUS_RETURNED = 13; 
	public final static String ORDER_STATUS_NAME_RETURNED = "已退貨";	

	/** 訂單不成立 **/
	public final static int ORDER_STATUS_FAILURE = 99; 
	public final static String ORDER_STATUS_NAME_FAILURE = "訂單不成立";	
	
	public static Map<Integer,String> orderStatusMap = null;
	
	
	private String siteId;
	private String orderId;
	private String memberId;
	private String memberPid; //身份證後四碼
	private String memberName; //會員姓名
	private String memberMobile; //會員電話
	private String memberEmail; //會員Email
	private Date orderDate;
	private int orderStatus = ORDER_STATUS_DEFAULT;
	private String orderMessage;
	private String shipType; //取貨方式
	private boolean cvsShipFlag = true; //可超商取貨	
	private int payType; //付款方式
	private int installment; //分期付款期數
//	private int maxInstallment; //最多可分期付款期數
	private int installmentStart; //分期首期
	private int installmentEach;//分期每期
	
	private String interInstallment;//可分期付款期數
	
	private int totalQuantity;
	private int totalAmount; //商品總金額	
	private String couponCode; //抵用券號碼
	private int couponAmount; //折抵金額
	private int shipFee; //物流費
	private int orderAmount; //訂單總金額 = 商品總金額 -折抵金額 + 物流費

	private int couponCountAmount; //抵用券可計算金額
	private int couponUseAmount; //抵用券可抵用金額
	
	private String receiver; //收件人
	private String recPhone; //聯絡電話
	private String recMobile; //手機
	private String recEmail;//收件人email
	private String recZipCode;//收件郵遞區號
	private String recCounty;//收件縣市
	private String recArea;//收件鄉鎮市區	
	private String recAddress;//收件地址
	private String recTime;//送貨時段
	private String recNote;//備註
	private String cvsStoreNo; //便利商店代號
	private String cvsStoreName; //便利商店名稱
	private String cvsStoreAddress; //便利商店地址
	
	private int invoiceType; //發票種類
	private String invoiceAddress; //發票寄送地址
	private String invoiceBuyer; //發票買受人
	private String companyNo; //統一編號
	private String companyName; //發票抬頭
	private String companyAddress; //發票地址
	private String atmCode; //付款銀行帳號後五碼
	private Date atmDate; //付款銀行帳號後五碼輸入日期
	

	private Date checkedDate; //審核日期
	private String checkedUserId; //審核人員
	
	
	/** sap 及 超商相關變數 **/
	private Date postDate;//產生SAP訂單檔日期
	private String sapOrderNo;//SAP訂單編號
	private String sapShipNo; //SAP交貨單號碼
	private String sapBillingNo; //SAP請款文件號碼
	private Date postingDate; //SAP過帳日期
	private Date cvsArrivalDate; //到店日期
	private Date cvsAccountDate; //(超商)結帳基準日期
	private int receivedStatus; //收貨狀態；1.宅配成功 2.宅配失敗, 3.大物流成功, 4.大物流失敗
	private Date receivedDate; //收貨日期
	private String invoiceNo; //發票號碼
	private Date invoiceDate; //發票寄送日期
	private String invoiceUserId; //發票寄送日期註記人員
	
	
	private int cancelCode; //取消原因代碼
	private String cancelReason; //取消原因說明
	private String cancelContacter; //取消訂單聯絡人
	private String cancelContactPhone; //取消訂單聯絡電話
	private Date cancelDate; //申請取消日期
	private Date canceledDate; //訂單取消日期
	private String canceledUserId; //取消訂單審核人員

	private int returnType; //退貨方式
	private int returnCode; //退貨原因代碼
	private String returnReason; //退貨原因說明
	private Date returnDate; //申請退貨日期
	private Date returnPostDate; //退貨完成過帳日期
	
	private Date receiptDate; //收款報告單註記日期
	private String receiptUserId; //收款報告單註記人員

	private String processNote; //白板註記
	
	
	private List<ThesysOrderItemVO> orderItems = new ArrayList<ThesysOrderItemVO>();
	
	private ThesysCouponVO coupon = null;
	private ThesysOrderCreditVO credit = null;
	public ThesysOrderVO(){
		super();
	}
	public static ThesysOrderVO getInstance(ResultSet rs) throws SQLException{
		ThesysOrderVO result = new ThesysOrderVO();
		result.setSiteId(rs.getString("SITE_ID"));   
		result.setOrderId(rs.getString("ORD_ID"));   
		result.setMemberId(rs.getString("MEM_ID"));  
		result.setMemberPid(rs.getString("MEM_PID"));  
		result.setMemberName(rs.getString("MEM_NAME"));
		result.setMemberMobile(rs.getString("MEM_MOBILE"));
		result.setMemberEmail(rs.getString("MEM_EMAIL"));
		result.setOrderStatus(rs.getInt("ORD_ST"));              
		result.setOrderMessage(rs.getString("ORD_MSG"));              
		result.setOrderDate(convert(rs.getTimestamp("ORD_DATE"))); 
		result.setShipType(rs.getString("SHIP_TYPE"));    
		result.setPayType(rs.getInt("PAY_TYPE"));    
		result.setTotalQuantity(rs.getInt("TT_QTY"));    
		result.setTotalAmount(rs.getInt("TT_AMT"));    
		result.setCouponCode(rs.getString("CP_CODE"));    
		result.setCouponAmount(rs.getInt("CP_AMT"));    
		result.setShipFee(rs.getInt("SHIP_FEE"));    
		result.setOrderAmount(rs.getInt("ORD_AMT"));    
		result.setCouponCountAmount(rs.getInt("CP_CNT_AMT"));    
		result.setCouponUseAmount(rs.getInt("CP_USE_AMT"));    
		result.setReceiver(rs.getString("REC_NAME"));    
		result.setRecPhone(rs.getString("REC_PHONE"));    
		result.setRecMobile(rs.getString("REC_MOBILE"));    
		result.setRecEmail(rs.getString("REC_EMAIL"));   
		result.setRecZipCode(rs.getString("REC_ZIPCODE"));  
		result.setRecCounty(rs.getString("REC_COUNTY"));  
		result.setRecArea(rs.getString("REC_AREA"));  
		result.setRecAddress(rs.getString("REC_ADDR"));
		result.setRecTime(rs.getString("REC_TIME"));  
		result.setRecNote(rs.getString("REC_NOTE"));  
		result.setCvsStoreNo(rs.getString("CVS_ST_NO"));      
		result.setInvoiceType(rs.getInt("INVOICE_TYPE"));    
		result.setInvoiceAddress(rs.getString("INVOICE_ADDR"));    
		result.setInvoiceBuyer(rs.getString("INVOICE_BUYER"));    
		result.setCompanyNo(rs.getString("COMP_NO"));    
		result.setCompanyName(rs.getString("COMP_NAME"));    
		result.setCompanyAddress(rs.getString("COMP_ADDR"));
		result.setAtmCode(rs.getString("ATM_CODE"));
		result.setAtmDate(convert(rs.getTimestamp("ATM_DATE")));

		result.setCheckedDate(convert(rs.getTimestamp("CHECKED_DATE")));
		result.setCheckedUserId(rs.getString("CHECKED_USR_ID"));
		result.setPostDate(convert(rs.getTimestamp("POST_DATE")));
		result.setSapOrderNo(rs.getString("SAP_ORD_NO"));
		result.setSapShipNo(rs.getString("SAP_SHIP_NO"));
		result.setSapBillingNo(rs.getString("SAP_BILLING_NO"));
		result.setPostingDate(convert(rs.getTimestamp("SAP_POSTING_DATE")));
		

		result.setCvsArrivalDate(convert(rs.getTimestamp("CVS_ARR_DATE")));
		result.setCvsAccountDate(convert(rs.getTimestamp("CVS_ACC_DATE")));
		

		result.setReceivedStatus(rs.getInt("REC_ST"));
		result.setReceivedDate(convert(rs.getTimestamp("REC_DATE")));
		
		result.setInvoiceNo(rs.getString("INVOICE_NO"));
		result.setInvoiceDate(convert(rs.getTimestamp("INVOICE_DATE")));
		result.setInvoiceUserId(rs.getString("INVOICE_USR_ID"));
		

		result.setCancelCode(rs.getInt("CANCEL_CODE"));
		result.setCancelReason(rs.getString("CANCEL_REASON"));
		result.setCancelContacter(rs.getString("CANCEL_NAME"));
		result.setCancelContactPhone(rs.getString("CANCEL_PHONE"));
		result.setCancelDate(convert(rs.getTimestamp("CANCEL_DATE")));
		result.setCanceledDate(convert(rs.getTimestamp("CANCELED_DATE")));
		result.setCanceledUserId(rs.getString("CANCELED_USR_ID"));
		
		result.setReturnType(rs.getInt("RTN_TYPE"));
		result.setReturnCode(rs.getInt("RTN_CODE"));
		result.setReturnReason(rs.getString("RTN_REASON"));
		result.setReturnDate(convert(rs.getTimestamp("RTN_DATE")));
		result.setReturnPostDate(convert(rs.getTimestamp("RTN_POST_DATE")));

		result.setReceiptDate(convert(rs.getTimestamp("RECEIPT_DATE")));
		result.setReceiptUserId(rs.getString("RECEIPT_USR_ID"));
		
		result.setCvsStoreName(rs.getString("STNM"));
		result.setCvsStoreAddress(rs.getString("STADR"));
		

		result.setProcessNote(rs.getString("PROCESS_NOTE"));
		

    	return result;
	}
	
	public static Map<Integer,String> getOrderStatusMap(){
		if(orderStatusMap==null){
			orderStatusMap = new TreeMap<Integer,String>();
			orderStatusMap.put(ORDER_STATUS_CREDIT,ORDER_STATUS_NAME_CREDIT);
			orderStatusMap.put(ORDER_STATUS_ATM,ORD_STATUS_NAME_ATM);
			orderStatusMap.put(ORDER_STATUS_DEFAULT,ORDER_STATUS_NAME_DEFAULT );
			orderStatusMap.put(ORDER_STATUS_CHECKED,ORDER_STATUS_NAME_CHECKED );
			orderStatusMap.put(ORDER_STATUS_SAP_POSTED ,ORDER_STATUS_NAME_SAP_POSTED );
			orderStatusMap.put(ORDER_STATUS_SHIPPED,ORDER_STATUS_NAME_SHIPPED );	
			orderStatusMap.put(ORDER_STATUS_CVS,ORDER_STATUS_NAME_CVS );
			orderStatusMap.put(ORDER_STATUS_RECEIVED,ORDER_STATUS_NAME_RECEIVED );	
			orderStatusMap.put(ORDER_STATUS_UNRECEIVED ,ORDER_STATUS_NAME_UNRECEIVED );	
			orderStatusMap.put(ORDER_STATUS_CANCELING,ORDER_STATUS_NAME_CANCELING );
			orderStatusMap.put(ORDER_STATUS_CANCELED,ORDER_STATUS_NAME_CANCELED );
			orderStatusMap.put(ORDER_STATUS_RETURN_DEFAULT,ORDER_STATUS_NAME_RETURN_DEFAULT );
			orderStatusMap.put(ORDER_STATUS_RETURN_CHECKED,ORDER_STATUS_NAME_RETURN_CHECKED );
			orderStatusMap.put(ORDER_STATUS_RETURN_PROCESSING,ORDER_STATUS_NAME_RETURN_PROCESSING);
			orderStatusMap.put(ORDER_STATUS_RETURNED,ORDER_STATUS_NAME_RETURNED );
			
		}
		return orderStatusMap;
	}
	/**
	 * 是否可以退貨
	 * @return
	 */
	public boolean isReturnable(){
		if(this.getOrderStatus()==ORDER_STATUS_RECEIVED){
			Calendar cal = Calendar.getInstance();
			cal.setTime(getReceivedDate());
			cal.add(Calendar.DATE, 7);
			if(cal.getTime().after(new Date())){ //取貨日期+7日晚於系統日期
				return true;
			}	
		}
		return false;
	}
	
	public int getShipFeeType(){
		if("W".equals(getShipType())){
			return 1;
		}else{
			return 2;
		}
	}
	public String getOrderStatusName(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("MM月dd日");
		switch(orderStatus){
			case -1:
				return "訂單確認中";
			case 0:
				//可允許轉帳的期間為訂單成立日+4日曆天
				cal.setTime(getOrderDate());
				cal.add(Calendar.DATE, +4);
				
				return "尚未付款，請於"+fmt.format(getOrderDate())+"~"+fmt.format(cal.getTime())+"期間內付款";
			case 1:
				return "訂單處理中";
			case 2:
				return "出貨中";
			case 3:
				return "出貨中";
			case 4:
				//預計收貨日期XX月XX日/已出貨，SAP 銷貨完成過帳檔的過帳日期+1日曆天當做宅配預計送貨日
				cal.setTime(getPostingDate());
				cal.add(Calendar.DATE, +1);
				return "預計收貨日期"+fmt.format(cal.getTime());
			case 5:
				//X日已到門市，取貨時間為Y日~Y日/已出貨，便利商店可取貨時間為商品到店時間+6個日曆天
				cal.setTime(getCvsArrivalDate());
				cal.add(Calendar.DATE, +6);
				return fmt.format(getCvsArrivalDate())+"已到門市，取貨時間為"+fmt.format(getCvsArrivalDate())+"~"+fmt.format(cal.getTime());
			case 6:
				return "收貨日期"+fmt.format(getReceivedDate());
			case 7:
				return "未收貨";
			case 8:
				return "申請取消中";
			case 9:
				return "訂單已取消";
			case 10:
				return "申請退貨";
			case 11:
				return "退貨確認中";
			case 12:
				return "退貨處理中";
			case 13:
				return "已退貨";
			case 99:
				return "訂單不成立";
		
		}
		return "狀態代碼「"+orderStatus+"」";
	}
	public String getBackendOrderStatusName(){
		switch(orderStatus){
			case -1:
				return "信用卡尚未取得授權";
			case 0:
				return "尚未付款";
			case 1:
				return "尚未核單";
			case 2:
				return "已核單";
			case 3:
				return "已核單(已拋轉至SAP)";
			case 4:
				return "已出貨"; //宅配
			case 5:
				return "已出貨";//超商
			case 6:
				return "已完成";
			case 7:
				return "送貨失敗";
			case 8:
				return "申請取消中";
			case 9:
				return "訂單已取消";
			case 10:
				return "退貨未確認";
			case 11:
				return "退貨已確認";
			case 12:
				return "退貨處理中";
			case 13:
				return "已退貨";
			case 99:
				return "訂單不成立";
		
		}
		return "狀態代碼「"+orderStatus+"」";
	}
	
	/**
	 * 計算金額
	 */
	public void countOrder(){
		cvsShipFlag = true;
		totalQuantity = 0;
		totalAmount = 0;
		couponCountAmount = 0;
		couponUseAmount = 0;
		couponAmount = 0;
//		maxInstallment = 1;
		interInstallment = "";
		orderAmount = 0;
		if(orderItems!=null){
			Iterator<ThesysOrderItemVO> it = orderItems.iterator();
			while(it.hasNext()){
				ThesysOrderItemVO item = it.next();
				if(item.isCouponUseFlag()){ ////計算一般商品之是否可使用coupon
					couponUseAmount += item.getQuantity()*item.getSpecialPrice();
				}
				if(item.isCouponCountFlag()){ //計算一般商品之是否可產生coupon
					couponCountAmount += item.getQuantity()*item.getSpecialPrice();
				}
				if(item.isGroupFlag()){ //計算群組商品之是否可產生coupon
					Iterator<ThesysOrderItemDetailVO> dtIt = item.getItemDetailList().iterator();
					while(dtIt.hasNext()){
						ThesysOrderItemDetailVO detail = dtIt.next();
						if(detail.isCouponCountFlag()){
							couponCountAmount += item.getQuantity()*detail.getSpecialPrice()*detail.getGroupQuantity();
						}
					}
				}
				if(!item.isCvsShipFlag()) cvsShipFlag = false;
				totalAmount += item.getQuantity()*item.getSpecialPrice();
				totalQuantity += item.getQuantity();
				
				if(interInstallment.length() == 0 ){
					interInstallment = item.getInstallments();
				}else{
					interInstallment = getIntersection(interInstallment, item.getInstallments());
				}
//				if(maxInstallment==1 || maxInstallment> item.getMaxInstallment()) 
//					maxInstallment = item.getMaxInstallment();
				
			}	
			
		}
		if(coupon!=null && couponUseAmount>0){
			if(couponUseAmount < coupon.getCouponAmount() ){//可折抵金額<抵用券面額
				couponAmount = couponUseAmount;  
			}else{
				couponAmount = coupon.getCouponAmount() ;
			}
			
		}
		orderAmount = totalAmount-couponAmount+shipFee; //訂單金額 = 商品總金額-折價券+運費
		
	}

	public String getPayTypeName(){
		return getPayTypeName(this.payType);
	}
	public static String  getPayTypeName(int payType){
		if(payType==0)
			return "到店取貨付款";
		else if(payType==1)
			return "線上刷卡一次付清";
		else if(payType==2)
			return "信用卡分期付款";
		else if(payType==4)
			return "中信分期付款";
		else if(payType==3)
			return "ATM 匯款";
		return "";
	}
	public String getShipTypeName(){
		if("W".equals(this.shipType))
			return "宅配取貨";
		else if("F".equals(this.shipType))
			return "全家超商取貨";
		else if("K".equals(this.shipType))
			return "萊爾富超商取貨";
		else if("L".equals(this.shipType))
			return "OK超商取貨商";
		return "";
	}
	/**
	 * 取得訂購明細(部份遮罩)
	 * @return
	 */
	public String getMaskOrderDetail(){
		String orderDetail = "";
		List<ThesysOrderItemVO> list = getOrderItems();
		if(list!=null){
			for(int i=0 ;i<list.size();i++){
				ThesysOrderItemVO itemVO = list.get(i);
				String itemName = itemVO.getItemName();
				if(itemName.length() <=9 )
					orderDetail += itemName.substring(0,3)+"***********X"+itemVO.getQuantity()+"<BR>";
				else
					orderDetail += itemName.substring(0,9)+"***********X"+itemVO.getQuantity()+"<BR>";
			}
		}
		return orderDetail;
		
	}
	/**
	 * 取得收貨人名稱(部份遮罩)
	 * @return
	 */
	public String getMaskReceiver(){
		String star = "**********";//用來補名字第一個字後的字串
		String receiver = getReceiver();
		return receiver.substring(0,1)+star.substring(0,receiver.length()-1);
	}
	public String getMaskRecAddress(){
		String star = "**********************";//用來補名字與地址後的**
		String address = "";
		if(getRecCounty() != null){
			address =getRecCounty()+getRecArea()+getRecAddress();
	 		int l1 = address.length();
			if(address.indexOf("路") != -1){
				address = address.substring(0,address.indexOf("路")+1);
			}else if(address.indexOf("街") != -1){
				address = address.substring(0,address.indexOf("街")+1);
			}else if(address.indexOf("道") != -1){
				address = address.substring(0,address.indexOf("道")+1);
			}else{
				address = address.substring(0,address.length()-4);
			}
			address = 	address+star.substring(l1-address.length());
		}
		return address;
	}
	public String getMaskRecMobile(){
		return getRecMobile().substring(0,5)+"******";
	}
	
	public boolean isCanEnterAtmCode(){
		if(payType==3 && orderDate!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -4);
			return calendar.getTime().before(orderDate);
		}
		return false;
	}
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
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
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the orderStatus
	 */
	public int getOrderStatus() {
		return orderStatus;
	}
	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * @return the orderMessage
	 */
	public String getOrderMessage() {
		return orderMessage;
	}
	/**
	 * @param orderMessage the orderMessage to set
	 */
	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}
	/**
	 * @return the shipType
	 */
	public String getShipType() {
		return shipType;
	}
	/**
	 * @param shipType the shipType to set
	 */
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	/**
	 * @return the cvsShipFlag
	 */
	public boolean isCvsShipFlag() {
		return cvsShipFlag;
	}
	/**
	 * @param cvsShipFlag the cvsShipFlag to set
	 */
	public void setCvsShipFlag(boolean cvsShipFlag) {
		this.cvsShipFlag = cvsShipFlag;
	}
	/**
	 * @return the payType
	 */
	public int getPayType() {
		return payType;
	}
	/**
	 * @param payType the payType to set
	 */
	public void setPayType(int payType) {
		this.payType = payType;
	}
	/**
	 * @return the installment
	 */
	public int getInstallment() {
		return installment;
	}
	/**
	 * @param installment the installment to set
	 */
	public void setInstallment(int installment) {
		this.installment = installment;
	}
//	/**
//	 * @return the maxInstallment
//	 */
//	public int getMaxInstallment() {
//		return maxInstallment;
//	}
//	/**
//	 * @param maxInstallment the maxInstallment to set
//	 */
//	public void setMaxInstallment(int maxInstallment) {
//		this.maxInstallment = maxInstallment;
//	}
	/**
	 * @return the installmentStart
	 */
	public int getInstallmentStart() {
		return installmentStart;
	}
	/**
	 * @param installmentStart the installmentStart to set
	 */
	public void setInstallmentStart(int installmentStart) {
		this.installmentStart = installmentStart;
	}
	/**
	 * @return the installmentEach
	 */
	public int getInstallmentEach() {
		return installmentEach;
	}
	/**
	 * @param installmentEach the installmentEach to set
	 */
	public void setInstallmentEach(int installmentEach) {
		this.installmentEach = installmentEach;
	}
	/**
	 * @return the totalQuantity
	 */
	public int getTotalQuantity() {
		return totalQuantity;
	}
	/**
	 * @param totalQuantity the totalQuantity to set
	 */
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	/**
	 * @return the totalAmount
	 */
	public int getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}
	/**
	 * @param couponCode the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	/**
	 * @return the couponAmount
	 */
	public int getCouponAmount() {
		return couponAmount;
	}
	/**
	 * @param couponAmount the couponAmount to set
	 */
	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}
	/**
	 * @return the shipFee
	 */
	public int getShipFee() {
		return shipFee;
	}
	/**
	 * @param shipFee the shipFee to set
	 */
	public void setShipFee(int shipFee) {
		this.shipFee = shipFee;
	}
	/**
	 * @return the orderAmount
	 */
	public int getOrderAmount() {
		return orderAmount;
	}
	/**
	 * @param orderAmount the orderAmount to set
	 */
	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}
	/**
	 * @return the couponCountAmount
	 */
	public int getCouponCountAmount() {
		return couponCountAmount;
	}
	/**
	 * @param couponCountAmount the couponCountAmount to set
	 */
	public void setCouponCountAmount(int couponCountAmount) {
		this.couponCountAmount = couponCountAmount;
	}
	/**
	 * @return the couponUseAmount
	 */
	public int getCouponUseAmount() {
		return couponUseAmount;
	}
	/**
	 * @param couponUseAmount the couponUseAmount to set
	 */
	public void setCouponUseAmount(int couponUseAmount) {
		this.couponUseAmount = couponUseAmount;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return the recPhone
	 */
	public String getRecPhone() {
		return recPhone;
	}
	/**
	 * @param recPhone the recPhone to set
	 */
	public void setRecPhone(String recPhone) {
		this.recPhone = recPhone;
	}
	/**
	 * @return the recMobile
	 */
	public String getRecMobile() {
		return recMobile;
	}
	/**
	 * @param recMobile the recMobile to set
	 */
	public void setRecMobile(String recMobile) {
		this.recMobile = recMobile;
	}
	/**
	 * @return the recEmail
	 */
	public String getRecEmail() {
		return recEmail;
	}
	/**
	 * @param recEmail the recEmail to set
	 */
	public void setRecEmail(String recEmail) {
		this.recEmail = recEmail;
	}
	/**
	 * @return the recAddress
	 */
	public String getRecAddress() {
		return recAddress;
	}
	/**
	 * @param recAddress the recAddress to set
	 */
	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
	}
	/**
	 * @return the recZipCode
	 */
	public String getRecZipCode() {
		return recZipCode;
	}
	/**
	 * @param recZipCode the recZipCode to set
	 */
	public void setRecZipCode(String recZipCode) {
		this.recZipCode = recZipCode;
	}
	/**
	 * @return the recCounty
	 */
	public String getRecCounty() {
		return recCounty;
	}
	/**
	 * @param recCounty the recCounty to set
	 */
	public void setRecCounty(String recCounty) {
		this.recCounty = recCounty;
	}
	/**
	 * @return the recArea
	 */
	public String getRecArea() {
		return recArea;
	}
	/**
	 * @param recArea the recArea to set
	 */
	public void setRecArea(String recArea) {
		this.recArea = recArea;
	}
	/**
	 * @return the recTime
	 */
	public String getRecTime() {
		return recTime;
	}
	/**
	 * @param recTime the recTime to set
	 */
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	/**
	 * @return the recNote
	 */
	public String getRecNote() {
		return recNote;
	}
	/**
	 * @param recNote the recNote to set
	 */
	public void setRecNote(String recNote) {
		this.recNote = recNote;
	}
	/**
	 * @return the cvsStoreNo
	 */
	public String getCvsStoreNo() {
		return cvsStoreNo;
	}
	/**
	 * @param cvsStoreNo the cvsStoreNo to set
	 */
	public void setCvsStoreNo(String cvsStoreNo) {
		this.cvsStoreNo = cvsStoreNo;
	}
	/**
	 * @return the cvsStoreName
	 */
	public String getCvsStoreName() {
		return cvsStoreName;
	}
	/**
	 * @param cvsStoreName the cvsStoreName to set
	 */
	public void setCvsStoreName(String cvsStoreName) {
		this.cvsStoreName = cvsStoreName;
	}
	/**
	 * @return the cvsStoreAddress
	 */
	public String getCvsStoreAddress() {
		return cvsStoreAddress;
	}
	/**
	 * @param cvsStoreAddress the cvsStoreAddress to set
	 */
	public void setCvsStoreAddress(String cvsStoreAddress) {
		this.cvsStoreAddress = cvsStoreAddress;
	}
	/**
	 * @return the invoiceType
	 */
	public int getInvoiceType() {
		return invoiceType;
	}
	/**
	 * @param invoiceType the invoiceType to set
	 */
	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}
	/**
	 * @return the invoiceAddress
	 */
	public String getInvoiceAddress() {
		return invoiceAddress;
	}
	/**
	 * @param invoiceAddress the invoiceAddress to set
	 */
	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}
	/**
	 * @return the invoiceBuyer
	 */
	public String getInvoiceBuyer() {
		return invoiceBuyer;
	}
	/**
	 * @param invoiceBuyer the invoiceBuyer to set
	 */
	public void setInvoiceBuyer(String invoiceBuyer) {
		this.invoiceBuyer = invoiceBuyer;
	}
	/**
	 * @return the companyNo
	 */
	public String getCompanyNo() {
		return companyNo;
	}
	/**
	 * @param companyNo the companyNo to set
	 */
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the companyAddress
	 */
	public String getCompanyAddress() {
		return companyAddress;
	}
	/**
	 * @param companyAddress the companyAddress to set
	 */
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	/**
	 * @return the atmCode
	 */
	public String getAtmCode() {
		return atmCode;
	}
	/**
	 * @param atmCode the atmCode to set
	 */
	public void setAtmCode(String atmCode) {
		this.atmCode = atmCode;
	}
	/**
	 * @return the atmDate
	 */
	public Date getAtmDate() {
		return atmDate;
	}
	/**
	 * @param atmDate the atmDate to set
	 */
	public void setAtmDate(Date atmDate) {
		this.atmDate = atmDate;
	}
	
	/**
	 * @return the memberPid
	 */
	public String getMemberPid() {
		return memberPid;
	}
	/**
	 * @param memberPid the memberPid to set
	 */
	public void setMemberPid(String memberPid) {
		this.memberPid = memberPid;
	}
	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}
	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * @return the memberMobile
	 */
	public String getMemberMobile() {
		return memberMobile;
	}
	/**
	 * @param memberMobile the memberPhone to set
	 */
	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}
	/**
	 * @return the memberEmail
	 */
	public String getMemberEmail() {
		return memberEmail;
	}
	/**
	 * @param memberEmail the memberEmail to set
	 */
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	/**
	 * @return the checkedDate
	 */
	public Date getCheckedDate() {
		return checkedDate;
	}
	/**
	 * @param checkedDate the checkedDate to set
	 */
	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}
	/**
	 * @return the checkedUserId
	 */
	public String getCheckedUserId() {
		return checkedUserId;
	}
	/**
	 * @param checkedUserId the checkedUserId to set
	 */
	public void setCheckedUserId(String checkedUserId) {
		this.checkedUserId = checkedUserId;
	}
	/**
	 * @return the postDate
	 */
	public Date getPostDate() {
		return postDate;
	}
	/**
	 * @param postDate the postDate to set
	 */
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	/**
	 * @return the sapOrderNo
	 */
	public String getSapOrderNo() {
		return sapOrderNo;
	}
	/**
	 * @param sapOrderNo the sapOrderNo to set
	 */
	public void setSapOrderNo(String sapOrderNo) {
		this.sapOrderNo = sapOrderNo;
	}
	/**
	 * @return the sapShipNo
	 */
	public String getSapShipNo() {
		return sapShipNo;
	}
	/**
	 * @param sapShipNo the sapShipNo to set
	 */
	public void setSapShipNo(String sapShipNo) {
		this.sapShipNo = sapShipNo;
	}

	/**
	 * @return the sapShipNo
	 */
	public String getSapBillingNo() {
		return sapBillingNo;
	}
	/**
	 * @param sapShipNo the sapShipNo to set
	 */
	public void setSapBillingNo(String sapBillingNo) {
		this.sapBillingNo = sapBillingNo;
	}
	/**
	 * @return the sapPostDate
	 */
	public Date getPostingDate() {
		return postingDate;
	}
	/**
	 * @param sapPostDate the sapPostDate to set
	 */
	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}
	/**
	 * @return the cvsArrivalDate
	 */
	public Date getCvsArrivalDate() {
		return cvsArrivalDate;
	}
	/**
	 * @param cvsArrivalDate the cvsArrivalDate to set
	 */
	public void setCvsArrivalDate(Date cvsArrivalDate) {
		this.cvsArrivalDate = cvsArrivalDate;
	}
	/**
	 * @return the cvsAccountDate
	 */
	public Date getCvsAccountDate() {
		return cvsAccountDate;
	}
	/**
	 * @param cvsAccountDate the cvsAccountDate to set
	 */
	public void setCvsAccountDate(Date cvsAccountDate) {
		this.cvsAccountDate = cvsAccountDate;
	}
	/**
	 * @return the receivedStatus
	 */
	public int getReceivedStatus() {
		return receivedStatus;
	}
	/**
	 * @param receivedStatus the receivedStatus to set
	 */
	public void setReceivedStatus(int receivedStatus) {
		this.receivedStatus = receivedStatus;
	}
	/**
	 * @return the receivedDate
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}
	/**
	 * @param receivedDate the receivedDate to set
	 */
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	/**
	 * @return the invoiceDate
	 */
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	/**
	 * @return the invoiceUserId
	 */
	public String getInvoiceUserId() {
		return invoiceUserId;
	}
	/**
	 * @param invoiceUserId the invoiceUserId to set
	 */
	public void setInvoiceUserId(String invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}
	/**
	 * @return the cancelDate
	 */
	public Date getCancelDate() {
		return cancelDate;
	}
	/**
	 * @param cancelDate the cancelDate to set
	 */
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	/**
	 * @return the canceledDate
	 */
	public Date getCanceledDate() {
		return canceledDate;
	}
	/**
	 * @param canceledDate the canceledDate to set
	 */
	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}
	/**
	 * @return the canceledUserId
	 */
	public String getCanceledUserId() {
		return canceledUserId;
	}
	/**
	 * @param canceledUserId the canceledUserId to set
	 */
	public void setCanceledUserId(String canceledUserId) {
		this.canceledUserId = canceledUserId;
	}
	/**
	 * @return the cancelCode
	 */
	public int getCancelCode() {
		return cancelCode;
	}
	/**
	 * @param cancelCode the cancelCode to set
	 */
	public void setCancelCode(int cancelCode) {
		this.cancelCode = cancelCode;
	}
	/**
	 * @return the cancelReason
	 */
	public String getCancelReason() {
		return cancelReason;
	}
	/**
	 * @param cancelReason the cancelReason to set
	 */
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	/**
	 * @return the cancelContacter
	 */
	public String getCancelContacter() {
		return cancelContacter;
	}
	/**
	 * @param cancelContacter the cancelContacter to set
	 */
	public void setCancelContacter(String cancelContacter) {
		this.cancelContacter = cancelContacter;
	}
	/**
	 * @return the cancelContactPhone
	 */
	public String getCancelContactPhone() {
		return cancelContactPhone;
	}
	/**
	 * @param cancelContactPhone the cancelContactPhone to set
	 */
	public void setCancelContactPhone(String cancelContactPhone) {
		this.cancelContactPhone = cancelContactPhone;
	}
	/**
	 * @return the returnType
	 */
	public int getReturnType() {
		return returnType;
	}
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}
	/**
	 * @return the returnCode
	 */
	public int getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * @return the returnReason
	 */
	public String getReturnReason() {
		return returnReason;
	}
	/**
	 * @param returnReason the returnReason to set
	 */
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	/**
	 * @return the returnDate
	 */
	public Date getReturnDate() {
		return returnDate;
	}
	/**
	 * @param returnDate the returnDate to set
	 */
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	/**
	 * @return the returnPostDate
	 */
	public Date getReturnPostDate() {
		return returnPostDate;
	}
	/**
	 * @param returnPostDate the returnPostDate to set
	 */
	public void setReturnPostDate(Date returnPostDate) {
		this.returnPostDate = returnPostDate;
	}
	/**
	 * @return the receiptDate
	 */
	public Date getReceiptDate() {
		return receiptDate;
	}
	/**
	 * @param receiptDate the receiptDate to set
	 */
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	/**
	 * @return the receiptUserId
	 */
	public String getReceiptUserId() {
		return receiptUserId;
	}
	/**
	 * @param receiptUserId the receiptUserId to set
	 */
	public void setReceiptUserId(String receiptUserId) {
		this.receiptUserId = receiptUserId;
	}
	/**
	 * @return the orderItems
	 */
	public List<ThesysOrderItemVO> getOrderItems() {
		return orderItems;
	}
	/**
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(List<ThesysOrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}
	/**
	 * @return the coupon
	 */
	public ThesysCouponVO getCoupon() {
		return coupon;
	}
	/**
	 * @param coupon the coupon to set
	 */
	public void setCoupon(ThesysCouponVO coupon) {
		this.coupon = coupon;
	}
	
	/**
	 * @return the credit
	 */
	public ThesysOrderCreditVO getCredit() {
		return credit;
	}
	/**
	 * @param credit the credit to set
	 */
	public void setCredit(ThesysOrderCreditVO credit) {
		this.credit = credit;
	}
	public String getInterInstallment() {
		return interInstallment;
	}
	public void setInterInstallment(String interInstallment) {
		this.interInstallment = interInstallment;
	}
	
	
	/**
	 * @return the processNote
	 */
	public String getProcessNote() {
		return processNote;
	}
	/**
	 * @param processNote the processNote to set
	 */
	public void setProcessNote(String processNote) {
		this.processNote = processNote;
	}
	@Override
	public String toString() {
		return "ThesysOrderVO [siteId=" + siteId + ", orderId=" + orderId
				+ ", memberId=" + memberId + ", memberPid=" + memberPid
				+ ", memberName=" + memberName + ", memberMobile="
				+ memberMobile + ", memberEmail=" + memberEmail
				+ ", orderDate=" + orderDate + ", orderStatus=" + orderStatus
				+ ", orderMessage=" + orderMessage + ", shipType=" + shipType
				+ ", cvsShipFlag=" + cvsShipFlag + ", payType=" + payType
				+ ", installment=" + installment 
//				+ ", maxInstallment="+ maxInstallment 
				+ ", installmentStart=" + installmentStart
				+ ", installmentEach=" + installmentEach
				+ ", interInstallment=" + interInstallment + ", totalQuantity="
				+ totalQuantity + ", totalAmount=" + totalAmount
				+ ", couponCode=" + couponCode + ", couponAmount="
				+ couponAmount + ", shipFee=" + shipFee + ", orderAmount="
				+ orderAmount + ", couponCountAmount=" + couponCountAmount
				+ ", couponUseAmount=" + couponUseAmount + ", receiver="
				+ receiver + ", recPhone=" + recPhone + ", recMobile="
				+ recMobile + ", recEmail=" + recEmail + ", recZipCode="
				+ recZipCode + ", recCounty=" + recCounty + ", recArea="
				+ recArea + ", recAddress=" + recAddress + ", recTime="
				+ recTime + ", recNote=" + recNote + ", cvsStoreNo="
				+ cvsStoreNo + ", cvsStoreName=" + cvsStoreName
				+ ", cvsStoreAddress=" + cvsStoreAddress + ", invoiceType="
				+ invoiceType + ", invoiceAddress=" + invoiceAddress
				+ ", invoiceBuyer=" + invoiceBuyer + ", companyNo=" + companyNo
				+ ", companyName=" + companyName + ", companyAddress="
				+ companyAddress + ", atmCode=" + atmCode + ", atmDate="
				+ atmDate + ", checkedDate=" + checkedDate + ", checkedUserId="
				+ checkedUserId + ", postDate=" + postDate + ", sapOrderNo="
				+ sapOrderNo + ", sapShipNo=" + sapShipNo + ", sapBillingNo="
				+ sapBillingNo + ", postingDate=" + postingDate
				+ ", cvsArrivalDate=" + cvsArrivalDate + ", cvsAccountDate="
				+ cvsAccountDate + ", receivedStatus=" + receivedStatus
				+ ", receivedDate=" + receivedDate + ", invoiceNo=" + invoiceNo
				+ ", invoiceDate=" + invoiceDate + ", invoiceUserId="
				+ invoiceUserId + ", cancelCode=" + cancelCode
				+ ", cancelReason=" + cancelReason + ", cancelContacter="
				+ cancelContacter + ", cancelContactPhone="
				+ cancelContactPhone + ", cancelDate=" + cancelDate
				+ ", canceledDate=" + canceledDate + ", canceledUserId="
				+ canceledUserId + ", returnType=" + returnType
				+ ", returnCode=" + returnCode + ", returnReason="
				+ returnReason + ", returnDate=" + returnDate
				+ ", returnPostDate=" + returnPostDate + ", receiptDate="
				+ receiptDate + ", receiptUserId=" + receiptUserId
				+ ", orderItems=" + orderItems + ", coupon=" + coupon
				+ ", credit=" + credit + "]";
	}
	
	public String getIntersection(String str1 ,String str2){
		List<String> al = java.util.Arrays.asList(str1.split(";"));
		List<String> bl = java.util.Arrays.asList(str2.split(";"));
		List<String> cl =(List) org.apache.commons.collections.CollectionUtils.intersection(al, bl);		
		String returnStr = "";
		for(String s : cl)
			returnStr = returnStr + ";" + s ;
		if(returnStr.length() != 0)
			returnStr = returnStr.substring(1);
		return  returnStr;
	}
	
}
