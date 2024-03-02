package com.altqart.helper.services.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Access;
import com.altqart.model.User;
import com.altqart.resp.model.RespAccessUser;
import com.altqart.services.UserServices;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HelperServicesImpl implements HelperServices {

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private UserMapper userMapper;

	private int pageSize = 8;

	private final String STATIC_FILE_DIR = "src/main/resources/static";

	private final String UPLOAD_DIR = "/uimages/";

	@Autowired
	private UserServices userServices;

	private static Access cAccess;

	private boolean useLetters = true;

	private boolean useNumbers = true;

	private Access cByPrincipalUserAccess;

	static Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

	@Override
	public String getUnicId() {

		UUID uuid = UUID.randomUUID();

		String unicId = "";

		unicId = uuid.toString();

		unicId = unicId.replace("-", "");

		Calendar calendar = getCalenderDate();

		String dateTime = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.DATE))
				+ Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + Integer.toString(calendar.get(Calendar.MINUTE))
				+ Integer.toString(calendar.get(Calendar.SECOND)) + Integer.toString(calendar.get(Calendar.MILLISECOND))
				+ getRandomNum(3);

		unicId = unicId + dateTime;

		return unicId;
	}

	@Override
	public String getPurchaseBarCode(Calendar date) {

		String hr = date.get(Calendar.HOUR) > 10 ? converterServices.getIntToString(date.get(Calendar.HOUR))
				: "0" + converterServices.getIntToString(date.get(Calendar.HOUR));
		String min = date.get(Calendar.MINUTE) < 10 ? "0" + converterServices.getIntToString(date.get(Calendar.MINUTE))
				: converterServices.getIntToString(date.get(Calendar.MINUTE));
		String sec = date.get(Calendar.SECOND) < 10 ? "0" + converterServices.getIntToString(date.get(Calendar.SECOND))
				: converterServices.getIntToString(date.get(Calendar.SECOND));
		String day = date.get(Calendar.DAY_OF_MONTH) < 10
				? "0" + converterServices.getIntToString(date.get(Calendar.DAY_OF_MONTH))
				: converterServices.getIntToString(date.get(Calendar.DAY_OF_MONTH));
		String month = date.get(Calendar.MONTH) < 10 ? "0" + converterServices.getIntToString(date.get(Calendar.MONTH))
				: converterServices.getIntToString(date.get(Calendar.MONTH));
		String year = date.get(Calendar.YEAR) < 10 ? "0" + converterServices.getIntToString(date.get(Calendar.YEAR))
				: converterServices.getIntToString(date.get(Calendar.YEAR));

		String barCode = "PU" + hr + min + sec + day + month + year + "S";
		return barCode;
	}

	@Override
	public User checkUserAccess(HttpSession httpSession) {
		return checkUserValid(httpSession);
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public String getProductBarCode() {

		String randomNum = RandomStringUtils.random(8, false, true);
		String dateTime = getDateTimeString();
		return "PD D " + dateTime + " C " + randomNum;
	}

	@Override
	public int getStringToInt(String val) {
		if (!isNullOrEmpty(val)) {

			return Integer.parseInt(val);
		}
		return 0;
	}

	@Override
	public String getInvId() {
		String randomNum = RandomStringUtils.random(6, false, true);
		randomNum = getDateString() + "-" + randomNum;
		return randomNum;
	}

	@Override
	public String getDateGenIdUsingCount(int dayCount) {
		return getDateString() + dayCount;
	}

	@Override
	public String getDateGenSaleIdUsingCount(int count) {

		return getDateString() + "-" + count;
	}

	private String getDateString() {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHSS");
		String dateStr = dateTime.format(formatter);
		return dateStr;
	}

	@Override
	public String getGenaretedIDUsingDateTime() {
		return getDateTimeString();
	}

	@Override
	public String getRandomString(int length) {
		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	@Override
	public String getUploadDir() {
		return UPLOAD_DIR;
	}

	@Override
	public String getStaticDir() {
		return STATIC_FILE_DIR;
	}

	@Override
	public Access getCurrentAccess() {
		return cAccess;
	}

	@Override
	public Map<String, RespAccessUser> getRestAccessByUser(User user) {
		return getUserAccessByUser(user);
	}

	@Override
	public User getUserByPrincipal(Principal principal) {

		if (principal == null) {

			return null;
		} else {

			if (principal.getName() != null) {
				return userServices.getUserByUsername(principal.getName());
			} else {
				return null;
			}
		}
	}

	@Override
	public Map<String, RespAccessUser> getAccessMapByPrincipal(Principal principal) {

		User user = getUserByPrincipal(principal);

		if (user != null) {

			return getRestAccessByUser(user);
		}
		return null;
	}

	@Override
	public Access getAccessByUser(User authUser, String type, int numType) {

		if (authUser != null) {

			if (authUser.getName() != null) {

				if (authUser.getName().isEmpty()) {
					return null;
				} else {

					if (authUser.getRole() != null) {

						System.out.println("User Role Not Null ...");

						if (authUser.getRole().getAccesses() != null) {

							System.out.println("PrincipalAccesses Not Null ... ");

							for (Access item : authUser.getRole().getAccesses()) {

								System.out.println("Principal Access ...");

								if (item.getAccessType() != null) {

									String cType = item.getAccessType().getValue();
									String pStType = type;

									int pNValue = item.getAccessType().getNumValue();

									int paramNValue = numType;
									System.out.println("Principal p n Value: " + numType + " DB N type: " + pNValue);

									if (pStType.equals(cType) || pNValue == paramNValue) {

										System.out.println("Principal Selected Access: " + item.getName()
												+ " No Access: " + item.isNoAccess());

										cByPrincipalUserAccess = item;

										if (cByPrincipalUserAccess != null) {
											System.out.println(
													"Current Access Principal: Add " + cByPrincipalUserAccess.isAdd()
															+ " No Access Value: " + cByPrincipalUserAccess.isNoAccess()
															+ " All Access!! " + cByPrincipalUserAccess.isAll());
										}
									}
								}

							}
						}
					}

					return cByPrincipalUserAccess;
				}

			} else {
				return null;
			}

		}

		return null;
	}

	@Override
	public String uploadImageAndGetUrl(MultipartFile file, String path) {
		return uploadAndSaveImage(file, path);
	}

	@Override
	public BigDecimal getBigDec(double d) {

		return new BigDecimal(d);
	}

	@Override
	public boolean isNullOrEmpty(String text) {

		if (text != null) {
			if (!text.isBlank() && !text.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getGenPublicId() {
		return getUnicId();
	}

	@Override
	public String getProductPublicId() {
		return getGenPublicId();
	}

	@Override
	public boolean isEqual(String strVal, String strVal2) {

		if (!isNullOrEmpty(strVal)) {
			if (strVal.equals(strVal2)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isEqualAndFirstOneIsNotNull(String textOne, String textTwo) {

		if (!isNullOrEmpty(textOne)) {
			if (isEqual(textOne, textTwo)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getRandomNumDateString(int i) {
		String randNum = RandomStringUtils.random(i, false, true);
		return randNum + getDateString();
	}

	@Override
	public String getBatchCode(int count) {

		return getDateString() + "B" + count;
	}

	@Override
	public User checkUserAccess(HttpSession httpSession, String type, int numType) {
		return checkUserValid(httpSession, type, numType);
	}

	@Override
	public boolean isValidAndLenghtCheck(String text, int lenght) {
		if (text != null) {

			if (text.length() >= lenght) {

				return true;
			}
		}

		return false;
	}

	@Override
	public String getPurchaseBarCodeUsingBatch(String batchCode) {
		return getPurchaseBarCode(getCalenderDate()) + " B " + batchCode;
	}

	private Map<String, RespAccessUser> getUserAccessByUser(User user) {

		if (user.getRole() != null) {

			if (user.getRole().getAccesses() != null) {

				List<RespAccessUser> accessUsers = userMapper.mapAllUserAccess(user.getRole().getAccesses());

				Map<String, RespAccessUser> accessMap = new HashMap<>();

				for (RespAccessUser access : accessUsers) {

					if (access != null) {

						if (access.getAccessType() != null) {

							accessMap.put(access.getAccessType().getValue(), access);
						}
					}
				}

				return accessMap;

			}
		}

		return null;
	}

	private User checkUserValid(HttpSession httpSession, String type, int numType) {
		User user = (User) httpSession.getAttribute("currentUser");

		if (user != null) {

			int id = user.getId();

			user = null;

			user = userServices.getUserById(id);

			if (user != null) {

				if (user.getName() != null) {

					if (user.getName().isEmpty()) {
						return null;
					} else {

						if (user.getRole() != null) {

							System.out.println("User Role Not Null ...");

							if (user.getRole().getAccesses() != null) {

								System.out.println("Accesses Not Null ... ");

								for (Access item : user.getRole().getAccesses()) {

									System.out.println("Access ...");

									if (item.getAccessType() != null) {
										System.out.println("Access Name: " + item.getAccessType().getName() + " Alies: "
												+ item.getAccessType().getValue() + " Num Vale: "
												+ item.getAccessType().getNumValue());

										System.out.println(
												"Type: " + type + " CItem Type: " + item.getAccessType().getName()
														+ " Alies: " + item.getAccessType().getValue());

										String cType = item.getAccessType().getValue();
										String pStType = type;

										int pNValue = item.getAccessType().getNumValue();

										int paramNValue = numType;
										System.out.println("p n Value: " + numType + " DB N type: " + pNValue);

										if (pStType.equals(cType) || pNValue == paramNValue) {

											System.out.println("Selected Access: " + item.getName() + " No Access: "
													+ item.isNoAccess());
											// Set Access To Return
											cAccess = item;

											System.out.println("Current Access: Add " + cAccess.isAdd()
													+ " No Access Value: " + cAccess.isNoAccess());
										}
									}

								}
							}
						}

						return user;
					}

				} else {
					return null;
				}

			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private String getDateTimeString() {
		LocalDateTime dateTime = LocalDateTime.now();
		String dateStr = getDateTimeString(dateTime);
		return dateStr;
	}

	private String uploadAndSaveImage(MultipartFile mFile, String path) {

		if (mFile != null) {

			if (!mFile.getOriginalFilename().isEmpty()) {

				try {

					String fileExtenstion = FilenameUtils.getExtension(mFile.getOriginalFilename());

					System.out.println("file Name: " + mFile.getName() + "Fiel Extenstion: " + fileExtenstion);

					String scPath = path;
					path = STATIC_FILE_DIR + UPLOAD_DIR + path;
					File makeFile = new File(path);

					if (!makeFile.exists()) {

						if (makeFile.mkdir()) {
							System.out.println("Make Directory Done !");
						} else {
							System.out.println("Make Directory Fail !");
						}

					}

					byte[] bytes = mFile.getBytes();

					long miliSc = new Date(System.currentTimeMillis()).getTime();

					String timeStamp = Long.toString(miliSc);

					System.out.println("time Stamp: " + timeStamp);

					String fileName = timeStamp + getRandomString(15) + "." + fileExtenstion;
					System.out.println("File Name: " + fileName);

					String name = path + "//" + fileName;

					System.out.println("Path: " + path + " File Name: " + fileName);
					System.out.println("Full Path: " + name);

					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name)));
					stream.write(bytes);
					stream.close();

					return UPLOAD_DIR + scPath + "/" + fileName;

				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private String getDateTimeString(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmssSSS");
		String dateStr = dateTime.format(formatter);
		return dateStr;
	}

	private User checkUserValid(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("currentUser");

		if (user != null) {

			if (user.getName() != null) {

				if (user.getName().isEmpty()) {
					return null;
				} else {

					return user;
				}

			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String getSubStringWithId(String text, int size, int id) {

		String genId = Integer.toString(id);

		if (!isNullOrEmpty(text)) {

			if (text.length() > size) {
				if (id > 0) {
					genId = text.substring(0, size) + "-" + id;
				} else {
					genId = text.substring(0, size);
				}

			} else {
				genId = text;
			}
		}

		return genId;
	}

	@Override
	public String getStringReplaceAll(String text, String repText, String pattern) {
		if (!isNullOrEmpty(text)) {
			return text.replaceAll(pattern, repText);
		}

		return text;
	}

	public static double[] extractDoubles(String input) {
		return DOUBLE_PATTERN.matcher(input).results().mapToDouble(m -> Double.parseDouble(m.group())).toArray();
	}

	@Override
	public String getShortText(String text, int size) {

		if (text != null) {
			if (text.length() > size) {
				return text.substring(0, size);
			}

		}

		return text;
	}

	@Override
	public double getCharStringToDouble(String text) {

		try {

			double[] doubleArr = extractDoubles(text);

			if (doubleArr != null) {

				if (doubleArr.getClass().isArray()) {
					String doubleArrStr = "";
					if (doubleArr.length > 1) {

						doubleArrStr = Arrays.toString(doubleArr);
						log.info("Befor Replace String " + doubleArrStr);
						doubleArrStr = doubleArrStr.replaceAll(".", "");
						doubleArrStr = doubleArrStr.replaceAll(",", "");

						log.info("After Replace String " + doubleArrStr);

						if (doubleArrStr.length() > 15) {
							doubleArrStr = doubleArrStr.substring(0, 15);
						}

						if (!isNullOrEmpty(doubleArrStr)) {
							return Double.parseDouble(doubleArrStr);
						}

					} else {

						if (doubleArr[0] > 0) {
							return doubleArr[0];
						} else {
							return 0;
						}

					}
				}

			}

		} catch (NumberFormatException e) {

			e.printStackTrace();
			return 0;
		}
		return 0;

	}

	@Override
	public String getLowerCase(String text) {
		if (!isNullOrEmpty(text)) {
			return text.toLowerCase();
		}
		return null;
	}

	@Override
	public String getGenaretedReceiptIDUsingDateTime() {
		String randNum = RandomStringUtils.random(4, false, true);
		return getDayAndYear() + randNum;
	}

	@Override
	public boolean isNotEqualAndFirstOneIsNotNull(String textOne, String textTwo) {

		if (!isNullOrEmpty(textOne)) {
			if (!isEqual(textOne, textTwo)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getUserGenId() {

		return getRandomString(6);
	}

	@Override
	public String getKey(String text) {

		if (!isNullOrEmpty(text)) {
			text = getStringReplaceAll(text.toLowerCase(), "_", " ");
		}

		return text;
	}

	@Override
	public String getProductBarCodeByOrigin(String originBar, String code) {

		return originBar + "Q" + code;
	}

	@Override
	public String getProductOriginBarCode(String originCode) {

		return originCode + getRandomNum(4);
	}

	@Override
	public String getKeyById(String name, int code) {
		String key = getKey(name) + "_" + code;
		return key;
	}

	private String getRandomNum(int count) {

		return RandomStringUtils.random(count, false, true);
	}

	private String getDayAndYear() {
		Calendar calendar = getCalenderDate();
		String text = calendar.get(Calendar.MILLISECOND) + Integer.toString(calendar.get(Calendar.DATE))
				+ calendar.get(Calendar.YEAR);
		return text;
	}

	private Calendar getCalenderDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		return calendar;
	}

}
