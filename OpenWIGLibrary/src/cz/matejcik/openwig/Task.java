/* 
 * Copyright (C) 2014 matejcik
 *
 * This program is covered by the GNU General Public License version 3 or any later version.
 * You can find the full license text at <http://www.gnu.org/licenses/gpl-3.0.html>.
 * No express or implied warranty of any kind is provided.
 */
package cz.matejcik.openwig;

import se.krka.kahlua.vm.*;

public class Task extends EventTable {

	private boolean active;
	private boolean complete;
	
	public static final int PENDING = 0;
	public static final int DONE = 1;
	public static final int FAILED = 2;
	private int state = DONE;
	
	public boolean isVisible () { return visible && active; }
	public boolean isComplete () { return complete; }
	public int state () {
		if (!complete) return PENDING;
		else return state;
	}

	protected String luaTostring () { return "a ZTask instance"; }
	
	protected void setItem (String key, Object value) {
		if ("Active".equals(key)) {
			boolean a = KahluaUtil.boolEval(value);
			if (a != active) {
				active = a;
				callEvent("OnSetActive", null);
			}
		} else if ("Complete".equals(key)) {
			boolean c = KahluaUtil.boolEval(value);
			if (c != complete) {
				complete = c;
				callEvent("OnSetComplete", null);
			}
		} else if ("CorrectState".equals(key) && value instanceof String) {
			String v = (String)value;
			int s = DONE;
			if ("incorrect".equals(v)) s = FAILED;
			if (s != state) {
				state = s;
				callEvent("OnSetCorrectState", null);
			}
		} else super.setItem(key, value);
	}
}
