package com.thesys.opencms.laphone.epaper.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysEpaperVO  extends ThesysAbstractVO{
	
	private String siteId;
	private String eno;
	private String subject;
	private String creater;
	private Date createDate = new java.util.Date();
	private String lastUpdater;
	private Date lastUpdateDate ;
	private Date releaseDate ;
	
	public ThesysEpaperVO(){}
	public ThesysEpaperVO(String siteId,String eno, String subject, String creater,
			Date createDate, String lastUpdater, Date lastUpdateDate,
		    Date releaseDate) {
		super();
		this.siteId = siteId;
		this.eno = eno;
		this.subject = subject;
		this.creater = creater;
		this.createDate = createDate;
		this.lastUpdater = lastUpdater;
		this.lastUpdateDate = lastUpdateDate;
		this.releaseDate = releaseDate;
	}
	/**
	 * 匯入用建構子
	 * @param subject
	 * @param creater
	 * @param createDate
	 * @param releaseDate
	 */
	public ThesysEpaperVO(String siteId,String subject, String creater, Date createDate,
			 Date releaseDate) {
		super();
		this.siteId = siteId;
		this.subject = subject;
		this.creater = creater;
		this.createDate = createDate;
		this.releaseDate = releaseDate;
	}
	
	 
	
	@Override
	public String toString() {
		return "ThesysEpaperVO [siteId=" + siteId + ", eno=" + eno
				+ ", subject=" + subject + ", creater=" + creater
				+ ", createDate=" + createDate + ", lastUpdater=" + lastUpdater
				+ ", lastUpdateDate=" + lastUpdateDate + ", releaseDate="
				+ releaseDate + "]";
	}
	
	public static ThesysEpaperVO getInstence(ResultSet rs) throws SQLException{
		ThesysEpaperVO vo = new ThesysEpaperVO();
		vo.setSiteId(rs.getString("SITE_ID"));
		vo.setEno(rs.getString("ENO"));
		vo.setSubject(rs.getString("SUBJECT"));
		vo.setCreater(rs.getString("CRT_USR_ID"));
		vo.setCreateDate(convert(rs.getTimestamp("CRT_DATE")));
		vo.setLastUpdater(rs.getString("LM_USR_ID"));
		vo.setLastUpdateDate(convert(rs.getTimestamp("LM_DATE")));
		vo.setReleaseDate(new java.util.Date(rs.getDate("RELEASE_DATE").getTime()));
		return vo;
	}
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getEno() {
		return eno;
	}
	public void setEno(String eno) {
		this.eno = eno;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getLastUpdater() {
		return lastUpdater;
	}
	public void setLastUpdater(String lastUpdater) {
		this.lastUpdater = lastUpdater;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}


	
	
	
}
