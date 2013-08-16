package com.thesys.opencms.laphone.member;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.thesys.opencms.laphone.epaper.ThesysSubscribeHandler;
import com.thesys.opencms.laphone.epaper.dao.ThesysSubscribeVO;
import com.thesys.opencms.laphone.member.dao.ThesysMemberVO;
import com.thesys.opencms.laphone.util.ThesysEncryption;

public class ThesysReadOldMember {

	/**
	 * 取得文章內容，以一行一行存在ArrayList
	 * @param fileName
	 * @param fmt
	 * @return
	 */
	public ArrayList<String> readFileToArrayList(String fileName, String fmt) {
		ArrayList<String> list = new ArrayList<String>();

		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fileInputStream, fmt));
			String line = "";

			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					int c = line.charAt(0);
					if (c == 65279) {
						line = line.substring(1, line.length());
					}
				}
				list.add(line);
			}
			fileInputStream.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void arrayListToSql(ThesysMemberHandler mHandler,ThesysSubscribeHandler  sHandler,ArrayList<String> altxt) {
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat birthdayFmt = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat pwdFmt = new SimpleDateFormat("MMdd");  //密碼生日格式
		java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		java.util.Date now = new java.util.Date();
		ThesysMemberVO vo = null;
//		String maxCardId = "3210000000000";
		for (int i = 0; i < altxt.size(); i++) {
			try{
				String[] s =  altxt.get(i).split("	");
	
				vo = new ThesysMemberVO();
				String smsVerifiCode = ThesysEncryption.getRandomStr(6);
				String mailVerifiCode = ThesysEncryption.getRandomStr(6);
				int idType = 1; //國籍
				int mailStatus = 0; //驗證mail
				int status = 2; //手機驗證
				String email = s[11];
				int edm = Integer.parseInt(s[31]);
				vo.setUsername(s[2]);
				vo.setIdType(idType);
				vo.setIdNo(s[1]);
				vo.setEmail(email);
				vo.setGender(Integer.parseInt(s[5]));
				vo.setCellphone(s[9]);
				vo.setZipCode(s[13]);
				vo.setZipCounty(s[14]);
				vo.setZipArea(s[15]);
				vo.setFullAddress(s[16]);
				vo.setEdm(edm);
				vo.setBirthday(birthdayFmt.format(sdf1.parse(s[6])));
				vo.setSmsVerifyCode(smsVerifiCode);
				vo.setMailVerifyCode(mailVerifiCode);
				vo.setMailStatus(mailStatus);
				vo.setStatus(status);
				vo.setEnglishName( s[4]+" "+s[3]);
				vo.setIncome(Integer.parseInt(s[17]));
				vo.setOccupation(Integer.parseInt(s[19]));
				vo.setMarriage(Integer.parseInt(s[21]));
				vo.setCellphoneBrands(Integer.parseInt(s[18]));				
				vo.setEducation(Integer.parseInt(s[20]));
				vo.setOffspring(Integer.parseInt(s[22]));
				if(s[32]==null || s[32].length()==0){
					vo.setCreateDate(now);
				}else{
					vo.setCreateDate(sdf1.parse(s[32]));
				} 
				if(s[34]==null || s[34].length()==0){
					vo.setLastUpdatedDate(now);
				}else{
					vo.setLastUpdatedDate(sdf1.parse(s[34]));
				} 		
				vo.setLastChangePwdDate(now);
				vo.setRegisterIP(s[35]);
				vo.setCardId(s[0]);
				vo.setUseDate(sdf2.parse(s[33]));
				vo.setEmailFlag(Integer.parseInt(s[31]) == 1 || Integer.parseInt(s[31]) ==2);
				vo.setMobileFlag(Integer.parseInt(s[31]) == 1 || Integer.parseInt(s[31]) ==3);
//				if( Long.valueOf(s[0]) > Long.valueOf(maxCardId)) maxCardId = s[0]; //取得最大卡號
				
				//預設密碼：手機號碼10碼加生日1月1日
				String pwd = vo.getCellphone() + pwdFmt.format(birthdayFmt.parse(vo.getBirthday()));	
				vo.setPwd(pwd);
				
				int result = mHandler.insertOldMember(vo);
				if(result ==  1){
					if(sHandler.getRow(email) == null){
						sHandler.subscribe(email,"MEMBER");
						if(edm ==3 || edm == 4){
							ThesysSubscribeVO svo = sHandler.getRow(email);
							svo.setSubscribeFlag(false);
							sHandler.chengStatus(svo);
						}
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	

}
