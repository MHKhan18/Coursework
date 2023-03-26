package edu.stevens.cs594.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;


public class StringUtils {
	
	public static final String CHARSET = "UTF-8";
	
	public static String toString(byte[] b) {
		return toString(b, b.length);
	}
	
	public static String toString(byte[] b, int len) {
		try {
			return new String(b, 0, len, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unsupported UTF-8 encoding!");
		}
	}

	public static <T> String toString(Collection<T> set) {
		StringBuffer sb = new StringBuffer("[");
		int j=0;
		for (T s : set) {
			if (j > 0) {
				sb.append(',');
			}
			sb.append(s==null ? "null" : s.toString());
			j++;
		}
		sb.append(']');
		return new String(sb);
	}
	
	public static String toString(char[] b) {
		return toString(b, b.length);
	}
	
	public static String toString(char[] b, int len) {
		return new String(b, 0, len);
	}

}
