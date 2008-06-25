package se.krka.kahlua.stdlib;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;

public final class OsLib implements JavaFunction {
	
	private static final int DATE = 0;
	private static final int DIFFTIME = 1;
	private static final int TIME = 2;
	
	private static final int NUM_FUNCTIONS = 3;
	
	private static String[] names;
	static {
		names = new String[NUM_FUNCTIONS];
		names[DATE] = "date";
		names[DIFFTIME] = "difftime";
		names[TIME] = "time";
	}
	
	private int index;
	private static OsLib[] functions;
	
	public OsLib(int index) {
		this.index = index;
	}
	
	public static void register(LuaState state) {
		if (functions == null) {
			functions = new OsLib[NUM_FUNCTIONS];
			for (int i = 0; i < NUM_FUNCTIONS; i++) {
				functions[i] = new OsLib(i);
			}
		}
		LuaTable os = new LuaTable();
		state.environment.rawset("os", os);

		for (int i = 0; i < NUM_FUNCTIONS; i++) {
			os.rawset(names[i], functions[i]);
		}
	}

	public String toString() {
		return "os." + names[index];
	}

	public int call(LuaCallFrame callFrame, int nArguments) {
		switch (index) {
			case DATE: return date(callFrame, nArguments);
			case DIFFTIME: return difftime(callFrame, nArguments);
			case TIME: return time(callFrame, nArguments);
			default: return 0;
		}
	}
	
	private static int date (LuaCallFrame frame, int nArguments) {
		String format = "%c";
		if (nArguments > 0) format = (String) BaseLib.getArg(frame, 1, "string", "date");
		if (format.length() == 0) {
			frame.push("");
			return 1;
		}
		
		TimeZone tz;
		if (format.charAt(0) == '!') {
			format = format.substring(1);
			tz = TimeZone.getTimeZone("UTC");
		} else {
			tz = TimeZone.getDefault();
		}
		Calendar cal = Calendar.getInstance(tz);
			
		if (nArguments > 1) {
			Double time = (Double) BaseLib.getArg(frame, 2, "number", "date");
			cal.setTime(new Date(time.longValue()));
		}
		
		if (format.equals("*t")) { // table
			LuaTable timetab = new LuaTable(9);
			timetab.rawset("year", new Double(cal.get(Calendar.YEAR)));
			timetab.rawset("month", new Double(cal.get(Calendar.MONTH)));
			timetab.rawset("day", new Double(cal.get(Calendar.DAY_OF_MONTH)));
			timetab.rawset("hour", new Double(cal.get(Calendar.HOUR_OF_DAY)));
			timetab.rawset("min", new Double(cal.get(Calendar.MINUTE)));
			timetab.rawset("sec", new Double(cal.get(Calendar.SECOND)));
			timetab.rawset("wday", new Double(cal.get(Calendar.DAY_OF_WEEK)));
			timetab.rawset("yday", null); // day of year - dunno, we would have to compute that
			timetab.rawset("isdst", null); // check whether it's DST. i don't think we can obtain this
			frame.push(timetab);
			return 1;
		}		
		return 0;
	}

	private static int difftime (LuaCallFrame frame, int nArguments) {
		BaseLib.luaAssert(nArguments >= 2, "not enough arguments");
		Double timeA = (Double) BaseLib.getArg(frame, 1, "number", "difftime");
		Double timeB = (Double) BaseLib.getArg(frame, 2, "number", "difftime");
		frame.push(new Double(timeB.longValue() - timeA.longValue()));
		return 1;
	}
	
	private static int time (LuaCallFrame frame, int nArguments) {
		Calendar cal = Calendar.getInstance();
		if (nArguments > 0) {
			LuaTable timetab = (LuaTable)BaseLib.getArg(frame, 1, "table", "time");
			Double year = BaseLib.rawTonumber(timetab.rawget("year"));
			Double month = BaseLib.rawTonumber(timetab.rawget("month"));
			Double day = BaseLib.rawTonumber(timetab.rawget("day"));
			Double hour = BaseLib.rawTonumber(timetab.rawget("hour"));
			Double min = BaseLib.rawTonumber(timetab.rawget("min"));
			Double sec = BaseLib.rawTonumber(timetab.rawget("sec"));
			if (year != null) cal.set(Calendar.YEAR, year.intValue());
			if (month != null) cal.set(Calendar.MONTH, month.intValue());
			if (day != null) cal.set(Calendar.DAY_OF_MONTH, day.intValue());
			if (hour != null) cal.set(Calendar.HOUR_OF_DAY, hour.intValue());
			if (min != null) cal.set(Calendar.MINUTE, min.intValue());
			if (sec != null) cal.set(Calendar.SECOND, sec.intValue());
		}
		frame.push(new Double(cal.getTime().getTime()));
		return 1;
	}
}
