package com.thesys.opencms.laphone.report.dao;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import au.com.bytecode.opencsv.CSVWriter;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;

public class ThesysOrderReportDAO extends ThesysLaphoneDAO {
	private static ThesysOrderReportDAO m_instance;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	
	private ThesysOrderReportDAO() {
		super.init();
	}
	
	public static synchronized ThesysOrderReportDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysOrderReportDAO();
        }
        return m_instance;
    }
	
	
	
    /**
     * 分頁報表列表
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @param orderStatus
     * @param startOrderDate
     * @param endOrderDate
     * @return
     * @throws Exception
     */
    public List<ThesysOrderVO> listByPage(String siteId,int pageSize,int pageIndex,int reportType,String receiptFlag, String startDate, String endDate) throws Exception{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysOrderVO> result = new ArrayList<ThesysOrderVO>();
        try {
        	conn = getConnection();
            
            stmt = this.generateStatement(conn, siteId, false, pageSize, pageIndex, reportType,receiptFlag, startDate, endDate);           
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysOrderVO item = ThesysOrderVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
    private void addExcelCell(jxl.write.WritableSheet ws,int row,int column,String text){
        try{

          jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.createFont("新細明體"),
            10, WritableFont.NO_BOLD, false,
            UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

          jxl.write.Label labelC = new jxl.write.Label(column,row, text);
          jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);
          wcfFC.setAlignment(jxl.format.Alignment.CENTRE);          
          labelC.setCellFormat(wcfFC);
          ws.addCell(labelC);
        }catch(Exception ex){
          ex.printStackTrace();
        }
      }
    /**
     * 匯出Excel
     * @param columns
     * @param siteId
     * @param reportType
     * @param checkedFlag
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public void exportExcel(OutputStream out,String[] headers,String[] columns,String siteId,int reportType, String receiptFlag, String startDate, String endDate) throws Exception{
    	WritableWorkbook book = Workbook.createWorkbook(out);
    	WritableSheet sheet = book.createSheet("Sheet 1", 0);
		
    	//寫入欄位名稱
		int rowNum = 0;  
		for(int i=0;i<headers.length;i++){
			 addExcelCell(sheet, rowNum, i, headers[i]);
		}
		rowNum++;
    	
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = generateStatement(conn, siteId, false, -1, 0,reportType,receiptFlag, startDate, endDate);
            rs = stmt.executeQuery();
            int length = columns.length;
            while (rs.next()) {
            	
            	for(int i=0;i<length;i++){
            		String column = columns[i];
            		String value = "";
            		if("ORD_DATE".equals(column) || "INVOICE_DATE".equals(column)){
            			Timestamp date = rs.getTimestamp(column);
            			if(date!=null){
            				value = fmt.format(convert(rs.getTimestamp(column)));
            			}else{
            				value = "";
            			}
            		}else if("PAY_TYPE".equals(column)){
            			int payType = rs.getInt(column);
            			value = ThesysOrderVO.getPayTypeName(payType);
            		}else if("INVOICE_TITLE".equals(column)){
            			if(rs.getInt("INVOICE_TYPE")==3){
            				value = rs.getString("COMP_NAME");
            			}else{
            				value = rs.getString("INVOICE_BUYER");
            			}
            			
            		}else{            			
            			value = rs.getString(column);
            		}
            		addExcelCell(sheet, rowNum, i, value);
            		
            	}
            	rowNum++;
            }
            book.write();
            book.close();
            out.close();
        } finally {
            closeAll(conn, stmt, rs);
        }
    }
    /**
     * 匯出全部
     * @param columns
     * @param siteId
     * @param reportType
     * @param checkedFlag
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public void export(OutputStream out,String[] headers,String[] columns,String siteId,int reportType, String receiptFlag, String startDate, String endDate) throws Exception{
    	char EOF = (char)0x00;
    	CSVWriter writer = new CSVWriter(new OutputStreamWriter(out),',',EOF , "\r\n");
    	writer.writeNext(headers);
    	
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = generateStatement(conn, siteId, false, -1, 0,reportType,receiptFlag, startDate, endDate);
            rs = stmt.executeQuery();
            int length = columns.length;
            while (rs.next()) {
            	String[] row = new String[length];
            	for(int i=0;i<length;i++){
            		String column = columns[i];
            		if("ORD_DATE".equals(column) || "INVOICE_DATE".equals(column)){
            			Timestamp date = rs.getTimestamp(column);
            			if(date!=null){
            				row[i] = fmt.format(convert(rs.getTimestamp(column)));
            			}else{
            				row[i] = "";
            			}
            		}else if("PAY_TYPE".equals(column)){
            			int payType = rs.getInt(column);
            			row[i] = ThesysOrderVO.getPayTypeName(payType);
            		}else if("INVOICE_TITLE".equals(column)){
            			if(rs.getInt("INVOICE_TYPE")==3){
            				row[i] = rs.getString("COMP_NAME");
            			}else{
            				row[i] = rs.getString("INVOICE_BUYER");
            			}
            			
            		}else{            			
            			row[i] = rs.getString(column);
            		}
            	}
            	writer.writeNext(row);
            }
            writer.flush();
            writer.close();
            out.close();
        } finally {
            closeAll(conn, stmt, rs);
        }
    }
    
    /**
     * 取得報表筆數
     * @param siteId
     * @param orderStatus
     * @param startOrderDate
     * @param endOrderDate
     * @return
     * @throws Exception
     */
    public int count(String siteId,int reportType, String receiptFlag, String startDate, String endDate) throws Exception{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = getConnection();
            stmt = generateStatement(conn, siteId, true, 0, 0,reportType,receiptFlag, startDate, endDate);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
    private PreparedStatement generateStatement(Connection conn,String siteId,boolean countFlag,int pageSize,int pageIndex,int reportType,
    		String receiptFlag,String startDate,String endDate) throws Exception{
    	
    	String where = "WHERE SITE_ID = ? ";
    	if(reportType==1){  //訂單收款管理(每日對帳報表)
    		where += " AND ORD_ST = ? ";
	    	if("Yes".equals(receiptFlag))
	    		where += " AND RECEIPT_DATE IS NOT NULL ";
	        else if("No".equals(receiptFlag))
	        	where += " AND RECEIPT_DATE IS NULL ";  
    	}else if(reportType==2){ //每日到貨未取異常表：已到店，超過十日未取貨
    		where += " AND ORD_ST = ? AND DATEADD(day,10,CVS_ARR_DATE) < GETDATE() ";
    	}else if(reportType==3){ //發票寄送管理(有效訂單報表)
    		where += " AND ORD_ST = ? AND DATEADD(DAY,8,REC_DATE) < GETDATE() ";
    	
    	}
    	if(reportType==3){ //發票寄送管理(有效訂單報表)
    		if(startDate!=null && !"".equals(startDate))
	        	where += " AND REC_DATE>=? ";
	        if(endDate!=null && !"".equals(endDate))
	        	where += " AND REC_DATE<=? ";
    	}else{
	        if(startDate!=null && !"".equals(startDate))
	        	where += " AND ORD_DATE>=? ";
	        if(endDate!=null && !"".equals(endDate))
	        	where += " AND ORD_DATE<=? ";
    	}
    	String sql = "";
    	int stmtCount = 1;
    	if(countFlag){
    		sql = " select count(ORD_ID) FROM LAPHONE_ORD_VIEW "+where;
    	}else if(pageSize==-1){
    		sql = "SELECT  * FROM LAPHONE_ORD_VIEW "+where +" ORDER BY ORD_DATE DESC";    		
    	    	
    	}else{
    		stmtCount = 2;
    		sql = "SELECT top "+pageSize+" * FROM LAPHONE_ORD_VIEW "+where +
   			 " AND ORD_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1))+" ORD_ID FROM LAPHONE_ORD_VIEW "+where+" ORDER BY ORD_DATE DESC)  ORDER BY ORD_DATE DESC";    		
    	}
    	
    	PreparedStatement stmt = conn.prepareStatement(sql);
    	int idx = 1;
    	for(int i=0;i<stmtCount;i++){
            stmt.setString(idx++, siteId);
            if(reportType==1){ //訂單收款管理(每日對帳報表)
            	stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_RECEIVED);
            }else if(reportType==3){ //發票寄送管理(有效訂單報表)
            	stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_RECEIVED);
        	}else if(reportType==2){ //每日到貨未取異常表：已到店，超過十日未取貨
            	stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_CVS);
        	}
            if(startDate!=null && !"".equals(startDate))
            	stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
            if(endDate!=null && !"".equals(endDate))
            	stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
            
        }
    	return stmt;
    }

	@Override
	protected String getDBTableName() {
		return null;
	}

}
