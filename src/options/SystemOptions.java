package options;

import options.MarioOptions.StringOption;

/**
 * Read System values from {@link MarioOptions}.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class SystemOptions {

	/**
	 * Auto-adjusts certain SYSTEM options.
	 * <br/><br/>
	 * Currently do not do anything, reserved for future use.
	 */
	public static void reset() {		
	}
	
	public static boolean isSaveLevelFileName() {
		String value = MarioOptions.getInstance().getString(StringOption.SYSTEM_SAVE_LEVEL_FILE_NAME);
		if (value.length() == 0) return false;
		if ("off".equalsIgnoreCase(value)) return false;
		if ("false".equalsIgnoreCase(value)) return false;
		return true;
	}
	
	public static String getSaveLevelFileName() {
		return MarioOptions.getInstance().getString(StringOption.SYSTEM_SAVE_LEVEL_FILE_NAME);
	}
}
