package com.thesys.opencms.laphone.job;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;
import org.opencms.scheduler.I_CmsScheduledJob;

import com.thesys.opencms.laphone.job.ftp.ThesysCvsFtpClient;
import com.thesys.opencms.laphone.job.ftp.ThesysSapFtpClient;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;



public abstract class ThesysAbstractJob  implements I_CmsScheduledJob{

    /** The log object for this class. */
    protected static final Log LOG = CmsLog.getLog(ThesysAbstractJob.class);
	
    protected CmsObject cmsObject = null;
	
    
    public abstract String launch(CmsObject cmso, Map parameters) throws Exception;
	
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return getCmsObject().getRequestContext().getSiteRoot();
//		return "/sites/laphone";
	}
	/**
	 * @return the cmsObject
	 */
	public CmsObject getCmsObject() {
		return cmsObject;
	}
	/**
	 * @param cmsObject the cmsObject to set
	 */
	public void setCmsObject(CmsObject cmsObject) {
		this.cmsObject = cmsObject;
	}
	/**
	 * 取得SAP Ftp Client
	 * @return
	 */
	public ThesysSapFtpClient getSapFtpClient() throws Exception{
		String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "SAP_FTP_HOST").getParamVal();
		int port = Integer.parseInt(ThesysParamDAO.getInstance().getParam(getSiteId(), "SAP_FTP_PORT").getParamVal());
		String account = ThesysParamDAO.getInstance().getParam(getSiteId(), "SAP_FTP_USER").getParamVal();
		String pwd = ThesysParamDAO.getInstance().getParam(getSiteId(), "SAP_FTP_PWD").getParamVal();
		String rootPath = ThesysParamDAO.getInstance().getParam(getSiteId(), "SAP_FTP_ROOT").getParamVal();
		boolean tlsProtocol = Boolean.valueOf(ThesysParamDAO.getInstance().getParam(getSiteId(), "SAP_FTP_TLS").getParamVal());
		ThesysSapFtpClient client = new ThesysSapFtpClient(host,port,tlsProtocol,account,pwd,rootPath);
		return client;
	}
	/**
	 * 取得便利達康Ftp Client
	 * @return
	 */
	public ThesysCvsFtpClient getCvsFtpClient() throws Exception{
		String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_FTP_HOST").getParamVal();
		int port = Integer.parseInt(ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_FTP_PORT").getParamVal());
		String account = ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_FTP_USER").getParamVal();
		String pwd = ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_FTP_PWD").getParamVal();
		String rootPath = ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_FTP_ROOT").getParamVal();
		boolean tlsProtocol = Boolean.valueOf(ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_FTP_TLS").getParamVal());
		String ecAccount = ThesysParamDAO.getInstance().getParam(getSiteId(), "CVS_EC_ACCOUNT").getParamVal();
		ThesysCvsFtpClient client = new ThesysCvsFtpClient(host,port,tlsProtocol,account,pwd,rootPath,ecAccount);
		return client;
	}

	
}
