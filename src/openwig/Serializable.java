package openwig;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Serializable {
	public void serialize (DataOutput out) throws IOException;
	public void deserialize (DataInput in) throws IOException;
}
