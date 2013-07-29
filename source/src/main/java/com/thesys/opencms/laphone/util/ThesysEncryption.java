package com.thesys.opencms.laphone.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

public class ThesysEncryption {

	static String randomstr;
	static boolean allchars = false;
	// 如果沒有指定長度則是8碼
	static Integer length = new Integer(8);
	static HashMap hmap;
	static ArrayList lower = null;
	static ArrayList upper = null;
	static char[] single = null;
	static int singlecount = 0;
	static boolean singles = false;
	static String algorithm = null;
	static String provider = null;
	static boolean secure = false;
	static Random random = null;
	static SecureRandom secrandom = null;

	private static float getFloat() {
		if (random == null) {
			return secrandom.nextFloat();
		} else {
			return random.nextFloat();
		}
	}

	public static void generateRandomObject() throws Exception {
		// check to see if the object is a SecureRandom object
		if (secure) {
			try {
				// get an instance of a SecureRandom object
				if (provider != null) {

					random = SecureRandom.getInstance(algorithm, provider);
				} else {
					random = SecureRandom.getInstance(algorithm);
				}
			} catch (NoSuchAlgorithmException ne) {
				throw new Exception(ne.getMessage());
			} catch (NoSuchProviderException pe) {
				throw new Exception(pe.getMessage());
			}
		} else {
			random = new Random();
		}
	}

	/**
	 * generate the random string
	 */
	private static void generaterandom() {
		if (allchars) {
			for (int i = 0; i < length.intValue(); i++) {
				randomstr = randomstr
						+ new Character(
								(char) ((int) 34 + ((int) (getFloat() * 93))))
								.toString();
			}
		} else if (singles) {
			// check if there are single chars to be included
			if (upper.size() == 3) {
				// check for the number of ranges max 3 uppercase lowercase
				// digits build the random string
				for (int i = 0; i < length.intValue(); i++) {
					// you have four groups to choose a random number from, to
					// make
					// the choice a little more random select a number out of
					// 100 get a random number

					// even or odd
					if (((int) (getFloat() * 100)) % 2 == 0) {
						// the number was even get another number even or odd
						if (((int) (getFloat() * 100)) % 2 == 0) {
							// choose a random char from the single char group
							randomstr = randomstr + randomSingle().toString();
						} else {
							// get a random char from the first range
							randomstr = randomstr
									+ randomChar((Character) lower.get(2),
											(Character) upper.get(2))
											.toString();
						}
					} else {
						// the number was odd

						if (((int) (getFloat() * 100)) % 2 == 0) {
							// choose a random char from the second range
							randomstr = randomstr
									+ randomChar((Character) lower.get(1),
											(Character) upper.get(1))
											.toString();
						} else {
							// choose a random char from the third range
							randomstr = randomstr
									+ randomChar((Character) lower.get(0),
											(Character) upper.get(0))
											.toString();
						}
					}
				}
			} else if (upper.size() == 2) {
				// single chars are to be included choose a random char from
				// two different ranges build the random char from single chars
				// and two ranges
				for (int i = 0; i < length.intValue(); i++) {
					// select the single chars or a range to get each random
					// charfrom

					if (((int) (getFloat() * 100)) % 2 == 0) {
						// get random char from the single chars
						randomstr = randomstr + randomSingle().toString();
					} else if (((int) (getFloat() * 100)) % 2 == 0) {
						// get the random char from the first range
						randomstr = randomstr
								+ randomChar((Character) lower.get(1),
										(Character) upper.get(1)).toString();
					} else {
						// get the random char from the second range
						randomstr = randomstr
								+ randomChar((Character) lower.get(0),
										(Character) upper.get(0)).toString();
					}
				}
			} else if (upper.size() == 1) {
				// build the random string from single chars and one range
				for (int i = 0; i < length.intValue(); i++) {
					if (((int) getFloat() * 100) % 2 == 0) {
						// get a random single char
						randomstr = randomstr + randomSingle().toString();
					} else {
						// get a random char from the range
						randomstr = randomstr
								+ randomChar((Character) lower.get(0),
										(Character) upper.get(0)).toString();
					}
				}
			} else {
				// build the rand string from single chars
				for (int i = 0; i < length.intValue(); i++) {
					randomstr = randomstr + randomSingle().toString();
				}
			}
		} else {
			// no single chars are to be included in the random string
			if (upper.size() == 3) {
				// build random strng from three ranges
				for (int i = 0; i < length.intValue(); i++) {
					if (((int) (getFloat() * 100)) % 2 == 0) {
						// get random char from first range
						randomstr = randomstr
								+ randomChar((Character) lower.get(2),
										(Character) upper.get(2)).toString();
					} else if (((int) (getFloat() * 100)) % 2 == 0) {
						// get random char form second range
						randomstr = randomstr
								+ randomChar((Character) lower.get(1),
										(Character) upper.get(1)).toString();
					} else {
						// get random char from third range
						randomstr = randomstr
								+ randomChar((Character) lower.get(0),
										(Character) upper.get(0)).toString();
					}
				}
			} else if (upper.size() == 2) {
				// build random string from two ranges
				for (int i = 0; i < length.intValue(); i++) {
					if (((int) (getFloat() * 100)) % 2 == 0) {
						// get random char from first range
						randomstr = randomstr
								+ randomChar((Character) lower.get(1),
										(Character) upper.get(1)).toString();
					} else {
						// get random char from second range
						randomstr = randomstr
								+ randomChar((Character) lower.get(0),
										(Character) upper.get(0)).toString();
					}
				}
			} else {
				// build random string
				for (int i = 0; i < length.intValue(); i++) {
					// get random char from only range
					randomstr = randomstr
							+ randomChar((Character) lower.get(0),
									(Character) upper.get(0)).toString();
				}
			}
		}
	}

	/**
	 * generate a random char from the single char list
	 * @returns - a randomly selscted character from the single char list
	 */
	private static Character randomSingle() {
		return (new Character(single[(int) ((getFloat() * singlecount) - 1)]));
	}

	/**
	 * generate a random character
	 * @param lower
	 *            lower bound from which to get a random char
	 * @param upper
	 *            upper bound from which to get a random char
	 * @returns - a randomly generated character
	 */
	private static Character randomChar(Character lower, Character upper) {
		int tempval;
		char low = lower.charValue();
		char up = upper.charValue();

		// get a random number in the range lowlow - lowup
		tempval = (int) ((int) low + (getFloat() * ((int) (up - low))));

		// return the random char
		return (new Character((char) tempval));
	}

	/**
	 * get the randomly created string for use with the &lt;jsp:getProperty
	 * name=<i>"id"</i> property="randomstr"/&gt;
	 * 
	 * @return - randomly created string
	 * 
	 */
	public static String getRandom() {
		randomstr = new String();
		generaterandom(); // generate the first random string

		if (hmap != null) {

			while (hmap.containsKey(randomstr)) {
				// random string has already been created generate a different
				// one
				generaterandom();
			}

			hmap.put(randomstr, null); // add the new random string
		}

		return randomstr;
	}

	/**
	 * set the ranges from which to choose the characters for the random string
	 * 
	 * @param low  set of lower ranges
	 * @param up   set of upper ranges
	 * 
	 */
	public static void setRanges(ArrayList low, ArrayList up) {
		lower = low;
		upper = up;
	}

	/**
	 * set the hashmap that is used to check the uniqueness of random strings
	 * @param map
	 *            hashmap whose keys are used to insure uniqueness of random
	 *            strgs
	  */
	public static void setHmap(HashMap map) {
		hmap = map;
	}

	/**
	 * set the length of the random string
	 * @param value  length of the random string
	 */
	public static void setLength(String value) {
		length = new Integer(value);
	}

	/**
	 * set the length of the random string
	 * @param value    length of the random string
	 */
	public static void setLength(int value) {
		length = value;
	}

	/**
	 * set the algorithm name
	 * @param value  name of the algorithm to use for a SecureRandom object
	 */
	public static void setAlgorithm(String value) {
		algorithm = value;
		secure = true; // a SecureRandom object is to be used
	}

	/**
	 * set the provider name
	 * @param value  name of the package to check for the algorithm 
	 */
	public static void setProvider(String value) {
		provider = value;
	}

	/**
	 * set the allchars flag
	 * @param value   boolean value of the allchars flag
	 */
	public static void setAllchars(boolean value) {
		allchars = value;
	}

	/**
	 * set the array of single chars to choose from for this random string and
	 * the number of chars in the array
	 * @param chars  the array of single chars
	 * @param value  the number of single chars
	 */
	public static void setSingle(char[] chars, int value) {
		single = chars; // set the array of chars
		singlecount = value; // set the number of chars in array single
		singles = true; // set flag that single chars are in use
	}

	public static void setCharset(String value) {
		// values tells the method whether or not to check for single chars
		boolean more = true;

		// create the arraylists to hold the upper and lower bounds for the char
		// ranges
		lower = new ArrayList(3);
		upper = new ArrayList(3);

		// user has chosen to use all possible characters in the random string
		if (value.compareTo("all") == 0) {
			allchars = true; // set allchars flag
			// all chars are to be used so there are no single chars to sort
			// through
			more = false;
		} else if ((value.charAt(1) == '-') && (value.charAt(0) != '\\')) {
			// run through the ranges at most 3
			while (more && (value.charAt(1) == '-')) {
				// check to make sure that the dash is not the single char
				if (value.charAt(0) == '\\') {
					break;
				} else {
					// add upper and lower ranges to there list
					lower.add(new Character(value.charAt(0)));
					upper.add(new Character(value.charAt(2)));
				}

				// check to see if there is more to the charset
				if (value.length() <= 3) {
					more = false;
				} else {
					// create a new string so that the next range if there is
					// one
					// starts it
					value = value.substring(3);
				}
			}
		}

		// if more = false there are no single chars in the charset
		if (more) {
			single = new char[30]; // create single

			// create a set of tokens from the string of single chars
			StringTokenizer tokens = new StringTokenizer(value);

			while (tokens.hasMoreTokens()) {
				// get the next token from the string
				String token = tokens.nextToken();

				if (token.length() > 1) {
					// char is a - add it to the list
					single[singlecount++] = '-';
				}

				// add the current char to the list
				single[singlecount++] = token.charAt(0);
			}
		}
		if ((lower == null) && (single == null))
			setCharset("a-zA-Z0-9");
	}

	/**
	 * 產生一串英數隨機字串 
	 * @param int charLength 要幾碼
	 * @return String  產生指定長度的字串
	 */
	public static String getRandomStr(int charLength) {
		String str =null;
		try {
			ThesysEncryption.setCharset("a-z0-9");// 只有英數小寫
			// Encryption.setCharset("0-z");//甚麼符號都有
			ThesysEncryption.setLength(charLength);// 指定字符長度
			ThesysEncryption.generateRandomObject();// 生成隨機數
			str = ThesysEncryption.getRandom();// 得到隨機數
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(getRandomStr(6));
	}

}
