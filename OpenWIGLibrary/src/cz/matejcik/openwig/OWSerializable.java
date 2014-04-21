/* 
 * Copyright (C) 2014 matejcik
 *
 * This program is covered by the GNU General Public License version 3 or any later version.
 * You can find the full license text at <http://www.gnu.org/licenses/gpl-3.0.html>.
 * No express or implied warranty of any kind is provided.
 */
package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface OWSerializable {
	public void serialize (DataOutputStream out) throws IOException;
	public void deserialize (DataInputStream in) throws IOException;
}
