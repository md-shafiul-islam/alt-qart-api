package com.altqart.helper.services;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Calendar;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.altqart.model.Access;
import com.altqart.model.User;
import com.altqart.resp.model.RespAccessUser;

import jakarta.servlet.http.HttpSession;

public interface HelperServices {

	public User checkUserAccess(HttpSession httpSession);

	public int getPageSize();

	public String getProductBarCode();

	public User checkUserAccess(HttpSession httpSession, String type, int numType);

	public String getStaticDir();

	public String getUploadDir();

	/**
	 * 
	 * @param {@link MultipartFile} file
	 * @param {@link String path type DIR}
	 * @return {@link String} image DIR
	 */
	public String uploadImageAndGetUrl(MultipartFile file, String string);

	public Access getCurrentAccess();

	/**
	 * 
	 * @param int length
	 * @return {@link String} Generated Random string
	 */
	public String getRandomString(int length);

	/**
	 * 
	 * @param authUser Curent User Autor
	 * @param string   Option
	 * @param i        Option Num Value
	 * @return Access
	 */
	public Access getAccessByUser(User authUser, String string, int i);

	public boolean isValidAndLenghtCheck(String text, int lenght);

	public Map<String, RespAccessUser> getRestAccessByUser(User user);

	public User getUserByPrincipal(Principal principal);

	public Map<String, RespAccessUser> getAccessMapByPrincipal(Principal principal);

	public String getGenaretedIDUsingDateTime();

	/**
	 * Get Unic Id String
	 * 
	 * @return {@link String } Unic ID
	 */
	public String getUnicId();

	public boolean isNullOrEmpty(String val);

	public String getGenPublicId();

	public String getPurchaseBarCode(Calendar date);

	public String getProductPublicId();

	public boolean isEqual(String strVal, String strVal2);

	public BigDecimal getBigDec(double val);

	public String getInvId();

	public String getRandomNumDateString(int i);

	public int getStringToInt(String val);

	public String getBatchCode(int count);

	public String getDateGenIdUsingCount(int dayCount);

	public String getPurchaseBarCodeUsingBatch(String batchCode);

	public String getSubStringWithId(String text, int size, int id);

	public String getStringReplaceAll(String text, String repText, String pattern);

	public double getCharStringToDouble(String text);

	public String getLowerCase(String text);

	public String getDateGenSaleIdUsingCount(int count);

	public boolean isEqualAndFirstOneIsNotNull(String textOne, String textTwo);

	public boolean isNotEqualAndFirstOneIsNotNull(String textOne, String textTwo);

	public String getGenaretedReceiptIDUsingDateTime();

	public String getUserGenId();

	public String getKey(String name);

	public String getProductOriginBarCode(String originCode);

	public String getProductBarCodeByOrigin(String originBar, String code);

	public String getShortText(String text, int size);

	public String getKeyById(String name, int code);

	public String getInvIdbyStakeholderGenId();

	public String getStakeholderGenId();

}
