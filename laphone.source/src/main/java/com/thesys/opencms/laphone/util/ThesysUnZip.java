package com.thesys.opencms.laphone.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.types.CmsResourceTypeFolder;
import org.opencms.file.types.CmsResourceTypePlain;
import org.opencms.main.CmsLog;

/**
 * 解壓縮
 */
public class ThesysUnZip {    
    
	protected static final Log LOG = CmsLog.getLog(ThesysUnZip.class);
	/** 儲存至OpenCms VFS*/
	public static final int SAVE_TO_OPENCMS = 0;
	/** 儲存至主機實體檔案*/
	public static final int SAVE_TO_LOCAL = 1;
	static final int EOF = -1;
    static final int BUFFER = 2048;

    private String strFile;
    private String targetDirectory;
    private File file;
    private InputStream is ;
    private ZipInputStream zis;
    private CmsObject cmsObj;
    /**存放位置，預設存至主機*/
    private int savePlace = SAVE_TO_LOCAL;

	/** Constructor */
    public ThesysUnZip() {     
    }
    
    /** Constructor */
    public ThesysUnZip(String strFile, String targetDirectory) {
        this.setStrFile(strFile);
        this.targetDirectory = targetDirectory;
    }
    
    /** Constructor */
	public ThesysUnZip(File file, String targetDirectory) {
        this.setFile(file);
        this.targetDirectory = targetDirectory;
    }
    
	/** Constructor */
	public ThesysUnZip(InputStream is, String targetDirectory) {
        this.setIs(is);
        this.targetDirectory = targetDirectory;
    }
	
	/**
	 * 存放至OpenCms VFS 
	 * Constructor
	 * @param is
	 * @param targetDirectory
	 * @param cmsObj
	 */
	public ThesysUnZip(InputStream is, String targetDirectory,CmsObject cmsObj) {
        this.setIs(is);
        this.targetDirectory = targetDirectory;
        this.cmsObj = cmsObj;
        this.savePlace = SAVE_TO_OPENCMS;
    }
	
	public static void main(String[] args) {
    	String strFile ="E:\\laphone_20130103.zip";
    	File file = new File(strFile);
    	InputStream in = null;
    	try {
			 in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	ThesysUnZip uzb = new ThesysUnZip(strFile, "E:\\test" ); //outFile可以是java.io.File 也可以是String 路徑
		System.out.println(uzb.unzip());
	}
	
	/**
	 * 執行解壓縮
	 * @return	-1 : 解壓縮失敗
	 * 			 0 : 此壓縮檔根目錄無index.html檔
	 * 		  	 1 : 解壓縮成功
	 */
	public int unzip() {
		int i = -1 ; 
		boolean hasIndex = false;
		try {  
	    	if(zis != null){
    			ZipEntry target;
    			while((target = zis.getNextEntry()) != null){
    				boolean isIndex = false;
    				if(savePlace == 0)
    					isIndex = saveEntryToOpenCms(target,cmsObj);
    				else
    					isIndex = saveEntry(target);
    				if(isIndex) hasIndex = true;
    			}
	    	}
	    	if(hasIndex) i = 1 ;
	    	else i = 0; 
		}catch (Exception e){//IO error
			LOG.error(e, e.fillInStackTrace());
		} finally {
			try {
				if(is != null)is.close();
			} catch (IOException e) {//IO error...Can't close zip file
				LOG.error(e, e.fillInStackTrace());
			}
		}
		return i ;
    }

	private boolean saveEntryToOpenCms(ZipEntry entry ,CmsObject cmsObj) throws Exception {
		byte[] data = null;
		boolean isIndex = false;
		String resourceName = targetDirectory + entry.getName();
		if(entry.getName().equals("index.html")) isIndex = true;
		if(!cmsObj.existsResource(resourceName )){
			List properties = new ArrayList();
			if(!entry .isDirectory()){
				int size = (int) entry.getSize();
				if (size == -1)
					throw new IOException("zipentry has unspecified size -> " + entry.getName());
				data = loadBytesFromStreamForSize(zis, size);
				cmsObj.createResource(resourceName, CmsResourceTypePlain.getStaticTypeId(), data, properties);
			}else{
				cmsObj.createResource(resourceName, CmsResourceTypeFolder.RESOURCE_TYPE_ID, null, properties);
			}
		}else{
			if(!entry .isDirectory()){
				CmsFile cmsFile = cmsObj.readFile(resourceName);
				int size = (int) entry.getSize();
				if (size == -1)
					throw new IOException("zipentry has unspecified size -> " + entry.getName());
				data = loadBytesFromStreamForSize(zis, size);
				cmsFile.setContents(data);
				cmsObj.lockResource(resourceName );
				cmsObj.writeFile(cmsFile);
				cmsObj.unlockResource(resourceName );
			}
		}
		return isIndex ;
	}
	
	public byte[] loadBytesFromStreamForSize(InputStream in, int size) throws IOException {
		int count, index = 0;
		byte[] b = new byte[size];

		// read in the bytes from input stream
		while ((count = in.read(b, index, size)) > 0) {
			size -= count;
			index += count;
		}
		return b;
	}
    
	private boolean saveEntry(ZipEntry entry ) throws ZipException, IOException {
		File file = new File(targetDirectory + File.separator+ entry.getName());
		boolean isIndex = false;
		if(entry.getName().equals("index.html")) isIndex = true;
		if (entry.isDirectory()) {
			file.mkdirs();
		} else {
			File dir = new File(file.getParent());
			dir.mkdirs();
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			int c;
			byte[] data = new byte[BUFFER];
			while ((c = zis.read(data, 0, BUFFER)) != EOF) {
				bos.write(data, 0, c);
			}
			bos.flush();
			bos.close();
			fos.close();
		}
		return isIndex ;
	}

    public void setStrFile(String strFile) {
        this.strFile = strFile;
        this.setFile(new File(strFile));
    }
    
    public void setTargetDirectory(String targetDirectory) {
    	this.targetDirectory = targetDirectory;
    }
      
    public void setFile(File file) {
		this.file = file;
		try {
			this.setIs(new FileInputStream(file));
		} catch (IOException e) {
			LOG.error(e, e.fillInStackTrace());
		} 
	}
    
    public void setIs(InputStream is) {
		this.is = is;
		this.setZis(new ZipInputStream(is));
	}
    
    public void setZis(ZipInputStream zis) {
		this.zis = zis;
	}
    
	public CmsObject getCmsObj() {
		return cmsObj;
	}

	public void setCmsObj(CmsObject cmsObj) {
		this.cmsObj = cmsObj;
	}
	
	public int getSavePlace() {
		return savePlace;
	}

	public void setSavePlace(int savePlace) {
		this.savePlace = savePlace;
	}
	
}