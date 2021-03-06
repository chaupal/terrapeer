/**
 * SchemaString.java
 *
 * This file was generated by XMLSPY 5 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSPY Documentation for further details.
 * http://www.altova.com/xmlspy
 */

package terrapeer.net.xml.types;

public class SchemaString implements SchemaType {
	protected String value;

	public SchemaString(String newvalue) {
		value = newvalue;
	}

	public SchemaString(SchemaString newvalue) {
		value = newvalue.value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String newvalue) {
		value = newvalue;
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof SchemaString))
			return false;
		return value.equals(((SchemaString)obj).value);
	}

	public Object clone() {
		return new SchemaString(new String(value));
	}

	public String toString() {
		return value;
	}

	public String asString() {
		return toString();
	}

	public int compareTo(Object obj) {
		return compareTo((SchemaString)obj);
	}

	public int compareTo(SchemaString obj) {
		return value.compareTo(obj.value);
	}
}
