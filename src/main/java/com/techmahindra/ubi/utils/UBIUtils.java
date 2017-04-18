package com.techmahindra.ubi.utils;

import java.io.IOException;
import java.util.Properties;

public class UBIUtils {

	private static Properties props = new Properties();

	private static final String UBI_PROP_FILE = "ubi.properties";

	private static final String OS_PERCENTAGE = "OS_PERCENTAGE";
	private static final String HB_PERCENTAGE = "HB_PERCENTAGE";
	private static final String HC_PERCENTAGE = "HC_PERCENTAGE";
	private static final String HA_PERCENTAGE = "HA_PERCENTAGE";

	//default values
	private static final String DEFAULT_OS_PERCENTAGE = "16";
	private static final String DEFAULT_HB_PERCENTAGE = "8";
	private static final String DEFAULT_HC_PERCENTAGE = "44";
	private static final String DEFAULT_HA_PERCENTAGE = "32";

	private static final String MAX_DISC = "MAX_DISC";
	private static final String MAX_NDB = "MAX_NDB";
	private static final String MAX_DB = "MAX_DB";
	private static final String MAX_TOD = "MAX_TOD";
	private static final String MAX_DOW = "MAX_DOW";

	//default values
	/*maxDiscount=30
	maxNDB=20
	maxDB=80
	maxTOD=80
	maxDOW=20*/
	private static final String DEFAULT_MAX_DISC = "30";
	private static final String DEFAULT_MAX_NDB = "20";
	private static final String DEFAULT_MAX_DB = "80";
	private static final String DEFAULT_MAX_TOD = "80";
	private static final String DEFAULT_MAX_DOW = "20";

	private static final String SCORE_PERC_GT80 = "SCORE_PERC_GT80";
	private static final String SCORE_PERC_GT60 = "SCORE_PERC_GT60";
	private static final String SCORE_PERC_GT40 = "SCORE_PERC_GT40";
	private static final String SCORE_PERC_GT20 = "SCORE_PERC_GT20";

	//default values
	private static final String DEFAULT_SCORE_PERC_GT80 = "80";
	private static final String DEFAULT_SCORE_PERC_GT60 = "60";
	private static final String DEFAULT_SCORE_PERC_GT40 = "40";
	private static final String DEFAULT_SCORE_PERC_GT20 = "20";

	private static final String SCORE_5 = "SCORE_5";
	private static final String SCORE_4 = "SCORE_4";
	private static final String SCORE_3 = "SCORE_3";
	private static final String SCORE_2 = "SCORE_2";
	private static final String SCORE_1 = "SCORE_1";

	//default values
	private static final String DEFAULT_SCORE_5 = "5";
	private static final String DEFAULT_SCORE_4 = "4";
	private static final String DEFAULT_SCORE_3 = "3";
	private static final String DEFAULT_SCORE_2 = "2";
	private static final String DEFAULT_SCORE_1 = "1";

	public UBIUtils() throws IOException 
	{
		if(props!=null && props.size()==0)
		{
			props.load(UBIUtils.class.getClassLoader().getResourceAsStream(UBI_PROP_FILE));
		}
	}
	//    OS%	16.00%
	//		HA%	32.00%
	//		HC%	44.00%
	//		HB%	8.00%

	public static float getOS_PERC() {
		return getFloat(getValue(props.getProperty(OS_PERCENTAGE),DEFAULT_OS_PERCENTAGE));
	}

	public static float getHB_PERC() {
		return getFloat(getValue(props.getProperty(HB_PERCENTAGE),DEFAULT_HB_PERCENTAGE));
	}

	public static float getHC_PERC() {
		return getFloat(getValue(props.getProperty(HC_PERCENTAGE),DEFAULT_HC_PERCENTAGE));
	}

	public static float getHA_PERC() {
		return getFloat(getValue(props.getProperty(HA_PERCENTAGE),DEFAULT_HA_PERCENTAGE));
	}
	
	public static float getMAX_DISC() {
		return getFloat(getValue(props.getProperty(MAX_DISC),DEFAULT_MAX_DISC));
	}

	public static float getMAX_NDB() {
		return getFloat(getValue(props.getProperty(MAX_NDB),DEFAULT_MAX_NDB));
	}

	public static float getMAX_DB() {
		return getFloat(getValue(props.getProperty(MAX_DB),DEFAULT_MAX_DB));
	}

	public static float getMAX_TOD() {
		return getFloat(getValue(props.getProperty(MAX_TOD),DEFAULT_MAX_TOD));
	}

	public static float getMAX_DOW() {
		return getFloat(getValue(props.getProperty(MAX_DOW),DEFAULT_MAX_DOW));
	}
	public static float getSCORE_PERC_GT80() {
		return getFloat(getValue(props.getProperty(SCORE_PERC_GT80),DEFAULT_SCORE_PERC_GT80));
	}

	public static float getSCORE_PERC_GT60() {
		return getFloat(getValue(props.getProperty(SCORE_PERC_GT60),DEFAULT_SCORE_PERC_GT60));
	}

	public static float getSCORE_PERC_GT40() {
		return getFloat(getValue(props.getProperty(SCORE_PERC_GT40),DEFAULT_SCORE_PERC_GT40));
	}

	public static float getSCORE_PERC_GT20() {
		return getFloat(getValue(props.getProperty(SCORE_PERC_GT20),DEFAULT_SCORE_PERC_GT20));
	}

	public static int getSCORE_5() {
		return getInt(getValue(props.getProperty(SCORE_5),DEFAULT_SCORE_5));
	}

	public static int getSCORE_4() {
		return getInt(getValue(props.getProperty(SCORE_4),DEFAULT_SCORE_4));
	}

	public static int getSCORE_3() {
		return getInt(getValue(props.getProperty(SCORE_3),DEFAULT_SCORE_3));
	}

	public static int getSCORE_2() {
		return getInt(getValue(props.getProperty(SCORE_2),DEFAULT_SCORE_2));
	}
	public static int getSCORE_1() {
		return getInt(getValue(props.getProperty(SCORE_1),DEFAULT_SCORE_1));
	}

	private static int getInt(String value) {
		return Integer.valueOf(value);
	}

	private static float getFloat(String value) {
		return Float.valueOf(value);
	}

	private static String getValue(String input, String defaultValue) {
		return (input==null||input.trim().length()==0?defaultValue:input);
	}

	public static void main(String[] args) {
		System.out.println(getHB_PERC());
	}
}