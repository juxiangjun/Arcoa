package com.thesys.opencms.laphone.job;
/**
 * 重建Search Index的Job
 */

import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;


public class ThesysIndexRebuildJob  extends ThesysAbstractJob{
		
	protected static final Log LOG = CmsLog.getLog(ThesysIndexRebuildJob.class);
	
	
	
	/* (non-Javadoc)
	 * @see org.opencms.scheduler.I_CmsScheduledJob#launch(org.opencms.file.CmsObject, java.util.Map)
	 */
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);	
		//重建前台Index
		OpenCms.getSearchManager().rebuildIndex("LAPHONE_BANNER_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("LAPHONE_BLOCKGROUP_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("LAPHONE_EVENT_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("LAPHONE_NEWS_INDEX", null);
//		OpenCms.getSearchManager().rebuildIndex("LAPHONE_PRODUCT_INDEX", null);
		//重建後台Index
		OpenCms.getSearchManager().rebuildIndex("OFFLINE_BANNER_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("OFFLINE_BLOCKGROUP_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("OFFLINE_EVENT_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("OFFLINE_FAQ_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("OFFLINE_NEWS_INDEX", null);
		OpenCms.getSearchManager().rebuildIndex("OFFLINE_PRODUCT_INDEX", null);
        
		//重新初始化
		OpenCms.getWorkplaceManager().initialize(cmso);
		return null;
	}
	
	
		
}
