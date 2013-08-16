<%@page import="org.opencms.json.JSONML"%>
<%@page import="org.opencms.json.JSONWriter"%>
<%@page import="org.opencms.json.JSONTokener"%>
<%@page import="org.opencms.json.JSONObject"%>
<%@page import="com.thesys.opencms.laphone.system.ThesysParamHandler"%>
<%@ page pageEncoding="UTF-8" %><%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.main.*, org.opencms.search.*, org.opencms.file.*, org.opencms.jsp.*, java.util.*, java.sql.*, java.net.*, com.fol.utils.DateUtil" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/jsp/offline/system/modules/com.thesys.opencms.manager/elements/loginCheck.jsp"%>
<cms:include property="template" element="head" /> 
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:useBean id="date" class="java.util.Date" /> 
<%
String productFolder = "/product";
pageContext.setAttribute("productFolder",productFolder+"/" );
DateUtil du = new DateUtil();
ThesysParamHandler paramhandler = new ThesysParamHandler(pageContext, request, response);
String p_threshold = paramhandler.getParamVal("/sites/laphone", "PRODUCT_WARN_THRESHOLD");
out.print(p_threshold);
double change_rate = Double.valueOf(p_threshold.split(";")[0]);
int publish_day = Integer.valueOf(p_threshold.split(";")[1]);
%>
<script>
$(function(){
	$("#searchForm").bind('reset',(function(){
		$("#searchForm :input").each(function(){
			$(this).val("");
		});
		return false;
	}));
	$("#importForm").bind('submit',(function(){
		var fileName = $("#fileUpload").val().toLowerCase();
		if(fileName.length==0){
			alert("請先選擇檔案");
			return false;
		}else{
			var fileType = $("#fileType").val();
			if(fileType =='5'){
				var suffix = ".xls";
				if(fileName.substr(fileName.length - suffix.length) === suffix){
					return true;
				}else{
					alert("您選擇的檔案副檔名應為.xls");
					return false;
				}
			}else{
				var suffix = ".txt";
				if(fileName.substr(fileName.length - suffix.length) === suffix){
					return true;
				}else{
					alert("您選擇的檔案副檔名應為.txt");
					return false;
				}
			}
		}
		
	}));
});
</script>
<c:set var="pageSize" value="50" />
<c:set var="change_rate" value='<%=change_rate %>' />
<c:set var="publish_day" value='<%=publish_day %>' />

<form id="searchForm" method="post" action="<cms:info property="opencms.uri"/>"  class="hiddenForm">
	<table width="360px" style="margin-right:15px;margin-bottom:15px;">
	  <tr>
	    <td width="15%" align="right" class="tr_b">上架日期:</td>
	    <td width="34%" class="tr_b">
	    <input type="text" class="date" name="startDate" value="${param.startDate}">至<input type="text" class="date" name="endDate" value="${param.endDate}">
	    </td>
	  </tr>
	  <tr>
	    <td align="right" class="tr_b2">關鍵字:</td>
	    <td class="tr_b2"><input type="text" name="keyword" value="${param.keyword}"/></td>	    
	    </tr>	  
	  <tr>
	    <td align="right" class="tr_b">上下架:</td>
	    <td class="tr_b">
	    	<select name="onlineFlag">
	    		<option value="">全部</option>
	    		<option value="true" <c:if test="${param.onlineFlag=='true'}">selected</c:if>>上架</option>
	    		<option value="false" <c:if test="${param.onlineFlag=='false'}">selected</c:if>>下架</option>
	    	</select>
	    </td>	    
	    </tr>
	   <tr>
	    <td align="right" class="tr_b">狀態:</td>
	    <td class="tr_b">
	    	<select name="fileStatus">
	    		<option value="">全部</option>
	    		<option value="new" <c:if test="${param.fileStatus=='new'}">selected</c:if>>新增待發佈</option>
	    		<option value="changed" <c:if test="${param.fileStatus=='changed'}">selected</c:if>>修改待發佈</option>
	    		<option value="published" <c:if test="${param.fileStatus=='published'}">selected</c:if>>無異動</option>
	    	</select>
	    </td>	    
	    </tr>
        <tr>
            <td align="right" class="tr_b">
               lastImportedDate:
            </td>
            <td>
                <div id="divLastImportedDate">
                    <select name="selLastImportedDate" id="selLastImportedDate" onchange="onLastImportedSpecificDateChanged()">
                        <option value="today"> Today </option>
                        <option value="yesterday"> Yesterday </option>
                        <option value="this_week"> This week </option>
                        <option value="last_week"> Last week </option>
                        <option value="last_7_days"> Last 7 days</option>
                        <option value="this_month"> This month </option>
                        <option value="last_month"> Last month </option>
                        <option value="-1"> Specific date </option>
                        <option value="all"> All </option>
                    </select>
                </div>
                
                <div id="divSpecificDate" style="display:none">
                       <input type="text" value="<fmt:formatDate value="${date}" pattern="yyyy/MM/dd" />" name="txtLastImportedDateStart" id="txtLastImportedDateStart" class="date"/>
                       <input type="text" value="<fmt:formatDate value="${date}" pattern="yyyy/MM/dd" />" name="txtLastImportedDateEnd" id="txtLastImportedDateEnd" class="date"/>
                        <a href="javascript:switchImportedDateSelector('1')"> return </a>
                </div>   
            </td>
        </tr>
	</table>    
        </form>
        <form id="importForm" method="post" enctype="multipart/form-data" action="<cms:link>import.html</cms:link>" class="hiddenForm">			
        <table width="380px" >
	  <tr>	    
	    <td width="24%" align="right" valign="top" class="tr_b" rowspan="3">匯入檔案: </td>
	    <td width="76%" class="tr_b">
	    	<select name="fileType"  id="fileType">
			<option value="1">物料主檔</option>
			<option value="2">單品價格檔</option>
			<option value="3">組案價格檔</option>
			<option value="4">商品庫存檔</option>
			<option value="5">商品基本資料(Excel)</option>
		</select>
	    </td>
	  </tr>
	  <tr>
	    <td width="76%" class="tr_b">
	    <a href="productExample.xls" target="_blank">Excel範例下載</a>
	    </td>
	  </tr>	  
	  <tr>
	    <td width="76%" class="tr_b">
	    <input name="fileUpload" id="fileUpload" type="file"/><a href="#importForm" class="button btnSubmit">匯入</a>
	    </td>
	  </tr>
	  <tr>	    
	    <td colspan="2"><a href="#searchForm" class="button btnSubmit">查詢</a><a href="#searchForm" class="button btnReset">清空</></td>
	  </tr>
	</table> 
	</form>
        <br/>	
	<table class="list-table" border="0" cellpadding="0" cellspacing="0">
		<tr>
            
			<th class="w50">缩图</th>
			<th class="w120">狀態</th>
			<th class="w80">商品編號</th>
			<th>商品名稱</th>
			<th class="w50">售價</th>
			<th class="w50">會員價</th>
			<th class="w80">上架日期</th>
			<th class="w50">庫存量</th>
			<th class="w50">分期期數</th>
			<th class="w50">上下架</th>
		</tr>
		<jsp:useBean id="stock" scope="request" class="com.thesys.opencms.laphone.product.ThesysStockHandler">
			<%stock.init(pageContext,request,response);%>				
		</jsp:useBean>		   
                                
		<jsp:useBean id="search" scope="request" class="org.opencms.search.CmsSearch">
			<jsp:setProperty name="search" property="index" value="OFFLINE_PRODUCT_INDEX"/>
			<jsp:setProperty name="search" property="matchesPerPage" value="${pageSize}"/>
			<jsp:setProperty name="search" property="searchPage" param="pageIndex"/>
			
			<c:set var="startDateLong" value="0"/>
			<c:if test="${!empty param.startDate}">
				<fmt:parseDate var="startDate" value="${param.startDate} 00:00:00" pattern="yyyy/MM/dd HH:mm:ss" />
				<c:set var="startDateLong" value="${startDate.time}"/>
			</c:if>
			
			<c:set var="endDateLong" value="2556028800000"/>
			<c:if test="${!empty param.endDate}">
				<fmt:parseDate var="endDate" value="${param.endDate} 23:59:59" pattern="yyyy/MM/dd HH:mm:ss" />
				<c:set var="endDateLong" value="${endDate.time}"/>
			</c:if>
			<c:set var="query">(OnlineDate:[${startDateLong} TO ${endDateLong}])</c:set>
			<c:set var="query">${query} (ProductName:[0 TO 9] [a TO z])</c:set>
				
			<c:if test="${!empty param.keyword}">
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:set var="query">${query} ( (SapProductCode:${param.keyword}) OR (SapProductCode:${param.keyword}*)  OR  (ProductName:${param.keyword}) OR  (ProductName:${param.keyword}*) (SapCategoryCode:${param.keyword}*))</c:set>
			</c:if>
			<c:if test="${!empty param.onlineFlag}">
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:choose><c:when test="${param.onlineFlag=='true'}">
					<c:set var="query">${query} (OnlineFlag:"${param.onlineFlag}")</c:set>
				</c:when><c:otherwise>					
					<c:set var="query">${query} NOT (OnlineFlag:"true")</c:set>
				</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${!empty param.fileStatus}">
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:set var="query">${query} (FileStatus:"${param.fileStatus}")</c:set>				
			</c:if>
            <c:out value="${query}"/>
		<% 
		search.init(cms.getCmsObject());
		CmsSearchParameters params = search.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		search.setParameters(params);	
		//search.setSortOrder(com.thesys.opencms.search.SearchHelper.getIntSort("OnlineDate",true)); 
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		long dtNow =  cal.getTimeInMillis();
		
		request.setAttribute("dtNow", dtNow);
		%>
		</jsp:useBean>
		<c:set var="searchResult" value="${search.searchResult}"/>
		<jsp:useBean id="now" class="java.util.Date"/>
		<c:choose>
			<c:when test="${!empty searchResult}">
			
			<c:forEach var="entry" items="${searchResult}" varStatus="status">
				<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
				<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
				<c:set var="specialPrice"><cms:contentshow element="SpecialPrice"/></c:set>
				<c:set var="sapProductCode"><cms:contentshow element="SapProductCode"/></c:set>
				<c:set var="originalPrice"><cms:contentshow element="OriginalPrice"/></c:set>
				<c:set var="dateLong"><cms:contentshow element="OnlineDate"/></c:set>
				
				<c:set var="bgColor" value="#ffffff"/>
                <c:if test="${!empty specialPrice}">
                	
                    <c:set var="changeColor" value="${specialPrice/originalPrice< change_rate}"/>&nbsp;
                    <c:if test="${changeColor}">
                        <c:set var="bgColor" value="#FF6699"/>
                    </c:if>
                </c:if>
                
                <c:if test="${!empty lastImportedDate}">
                    <c:set var="diffence" value="${(dtNow-lastImportedDate)/(1000*60*60*24)}"/>
                    <c:if test="${diffence>publish_day}">
                            <c:set var="bgColor" value="#999999"/>
                            <c:if test="${bgColor=='#FF6699'}">
                                <c:set var="bgColor" value="#009999"/>
                            </c:if>
                        </c:if>
                    
                     </c:if>
                 <tr bgcolor="<c:out value="${bgColor}"/>">
                    <td> <img height="40" width="40" src="/export/sites/laphone/images/${sapProductCode}_1.jpg"> </td>
					<td class="text-right">
					<cms:include file="/system/modules/com.thesys.opencms.manager/elements/xmlListButton.jsp">
						<cms:param name="rowId">${status.index+1}</cms:param>
						<cms:param name="fromUrl"><cms:info property="opencms.uri"/></cms:param>
						<cms:param name="xmlPath">${xmlPath}</cms:param>
						<cms:param name="editUrl"><cms:link>editForm.html</cms:link></cms:param>
						<cms:param name="publishUrl"><cms:link>publishForm.html</cms:link></cms:param>
						<cms:param name="deleteButton">false</cms:param>
					</cms:include>
					</td>
					<td>${sapProductCode}</td>
					<td><cms:contentshow element="ProductName"/>&nbsp;</td>
					<td>
						<fmt:formatNumber value="${originalPrice}"/>&nbsp;
					</td>
					<td>
						<fmt:formatNumber value="${specialPrice}"/>&nbsp;
					</td>
					<td>					
						
						<c:if test="${dateLong>0}">
						<jsp:setProperty name="date" property="time" value="${dateLong}" /> 
						<fmt:formatDate value="${date}" pattern="yyyy/MM/dd" />&nbsp;
						</c:if>		
					</td>
					<td>
					<jsp:setProperty name="stock" property="itemId" value="${sapProductCode}"/> 
                           		<c:out value="${stock.stockQuantity}"/> &nbsp;
					</td>
					<td><cms:contentshow element="Installment"/>&nbsp;</td>
					<td>
					<c:set var="onlineFlag"><cms:contentshow element="OnlineFlag"/></c:set>
					<c:if test="${onlineFlag}">上架</c:if>
					<c:if test="${!onlineFlag}">下架</c:if>&nbsp;
					</td>
					
				</tr>
				</cms:contentload>				
			</c:forEach>
				<tr>
					<td colspan="9" class="btn-line">
					<c:set var="resultCount" value="${search.searchResultCount}"/>
					<c:set var="pageCount" value="${resultCount/pageSize}"/>
					<c:if test="${resultCount%pageSize>0}"><c:set var="pageCount" value="${pageCount+1}"/></c:if>
					<c:set var="pageCount"><fmt:formatNumber value="${pageCount}" maxFractionDigits="0"/></c:set>
					
					<c:if test="${pageCount == 0 }"><c:set var="pageCount" value="${pageCount+1}"/></c:if>
					<cms:include file="/system/modules/com.thesys.opencms.manager/elements/pager.jsp">
						<cms:param name="pageCount">${pageCount}</cms:param>
					</cms:include>
					</td>
				</tr>
		</c:when>
		<c:otherwise>	
			<tr><td colspan="9">無商品資料</td></tr>
		</c:otherwise>
		</c:choose>
	</table>

<script>

    function switchImportedDateSelector(value) {
        if (value == "-1") {
            $("#divLastImportedDate").hide();
            $("#divSpecificDate").show();
        } else  if (value=='1') {
            $("#divLastImportedDate").show();
            $("#divSpecificDate").hide();
        } 
    }


    function onLastImportedSpecificDateChanged() {
        var value = $("#selLastImportedDate").val();
        if (value=="-1") {
            switchImportedDateSelector(value);
        } else {
            setSpecificDateValue(value);
        }
    }

    function setSpecificDateValue(val) {
        console.log(val);
        <%
            String[] durations = null;
        %>
        var startDate, endDate;
        if (val=="today") {
           <%
                durations = du.getTodayDuration();
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }
        
        if (val=="yesterday") {
           <%
                durations = du.getYesterdayDuration();
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }


        if (val=="this_week") {
           <%
                durations = du.getThisWeekDuration();
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }


        if (val=="last_week") {
           <%
                durations = du.getLastWeekDuration();
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }


        if (val=="last_7_days") {
           <%
                durations = du.getDaysOffsetTodayDuration(-6);
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }

        if (val=="this_month") {
           <%
                durations = du.getThisMonthDuration();
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }

        if (val=="last_month") {
           <%
                durations = du.getLastMonthDuration();
           %>
           startDate = '<%=durations[0]%>'
           endDate = '<%=durations[1]%>'
        }

        if (val=="all") {
            startDate = '';
            endDate = '';
        }

        setLastImportedDateDurationValue(startDate, endDate) 
    }

    function setLastImportedDateDurationValue(startDate, endDate) {
        console.log(startDate);
        console.log(endDate);
        $("#txtLastImportedDateStart").val(startDate);
        $("#txtLastImportedDateEnd").val(endDate);
    }

</script>


<cms:include property="template" element="foot" /> 
