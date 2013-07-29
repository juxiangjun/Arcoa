<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<cms:include property="template" element="head" />
     <cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
   	<!-- begin 田字格 -->
        <cms:contentloop element="TopLeftBlockGroup">
        <c:set var="blockIndex" value="0"/>
        <div class="block_group block_group<cms:contentshow element="GroupType"/>">
            <cms:contentloop element="Product"><c:set var="blockIndex" value="${blockIndex+1}"/>            
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	            	<cms:param name="blockIndex">${blockIndex}</cms:param>
	            	<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
	            	<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
	            </cms:include>
            </cms:contentloop>
        </div>
        </cms:contentloop>
        <!-- end 田字格 -->
        <!-- begin 田字格 -->
        <cms:contentloop element="TopRightBlockGroup">
        <c:set var="blockIndex" value="0"/>
        <div class="block_group block_group<cms:contentshow element="GroupType"/>">
            <cms:contentloop element="Product"><c:set var="blockIndex" value="${blockIndex+1}"/>            
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	            	<cms:param name="blockIndex">${blockIndex}</cms:param>
	            	<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
	            	<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
	            </cms:include>
            </cms:contentloop>
        </div>
        </cms:contentloop>
        <!-- end 田字格 -->
        <cms:contentcheck ifexists="TopRightBlockGroup">  
        <!-- 廣告 -->
            <c:set var="currentUri"><cms:info property="opencms.request.uri"/></c:set>
	    <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
	    	<cms:param name="PrintPosition">${currentUri == '/new.html'?"AD-new943x100":"AD-sp943x100"}</cms:param>
	    </cms:include>
	<!-- 廣告 -->
	</cms:contentcheck>
   	<!-- begin 田字格 -->
        <cms:contentloop element="MiddleLeftBlockGroup">
        <c:set var="blockIndex" value="0"/>
        <div class="block_group block_group<cms:contentshow element="GroupType"/>">
            <cms:contentloop element="Product"><c:set var="blockIndex" value="${blockIndex+1}"/>            
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	            	<cms:param name="blockIndex">${blockIndex}</cms:param>
	            	<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
	            	<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
	            </cms:include>
            </cms:contentloop>
        </div>
        </cms:contentloop>
        <!-- end 田字格 -->
        <!-- begin 田字格 -->
        <cms:contentloop element="MiddleRightBlockGroup">
        <c:set var="blockIndex" value="0"/>
        <div class="block_group block_group<cms:contentshow element="GroupType"/>">
            <cms:contentloop element="Product"><c:set var="blockIndex" value="${blockIndex+1}"/>            
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	            	<cms:param name="blockIndex">${blockIndex}</cms:param>
	            	<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
	            	<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
	            </cms:include>
            </cms:contentloop>
        </div>
        </cms:contentloop>
        <!-- end 田字格 -->
        <cms:contentcheck ifexists="MiddleRightBlockGroup">  
        <!-- 廣告 -->
	    <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
            	<cms:param name="PrintPosition">${currentUri == '/new.html'?"AD-new943x100":"AD-sp943x100"}</cms:param>
            </cms:include>
	<!-- 廣告 -->
	</cms:contentcheck>
        <!-- begin 田字格 -->
        <cms:contentloop element="BottomBlockGroup">
        <c:set var="blockIndex" value="0"/>
        <div class="block_group block_group<cms:contentshow element="GroupType"/>">
            <cms:contentloop element="Product"><c:set var="blockIndex" value="${blockIndex+1}"/>            
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	            	<cms:param name="blockIndex">${blockIndex}</cms:param>
	            	<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
	            	<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
	            </cms:include>
            </cms:contentloop>
        </div>
        </cms:contentloop>
        <!-- end 田字格 -->
        <cms:contentcheck ifexists="BottomBlockGroup">        	
        	<!-- 廣告 -->
		    <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
	            	<cms:param name="PrintPosition">${currentUri == '/new.html'?"AD-new943x100":"AD-sp943x100"}</cms:param>
	            </cms:include>
		<!-- 廣告 -->
	</cms:contentcheck>
     </cms:contentload>
<cms:include property="template" element="foot" />  
