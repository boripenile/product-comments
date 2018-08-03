package com.example.app.products.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CodeHelper {
	private static List<Character> NUMBERS = new ArrayList<Character>();

	static {
		NUMBERS.add('1');
		NUMBERS.add('4');
		NUMBERS.add('7');
		NUMBERS.add('2');
		NUMBERS.add('9');
		NUMBERS.add('6');
		NUMBERS.add('3');
		NUMBERS.add('8');
		NUMBERS.add('5');
		NUMBERS.add('0');
	}

	private static final List<String> ascii = new ArrayList<String>(36);
	private static final List<String> digits = new ArrayList<String>(10);

	static {
		for (char c = 'A'; c <= 'Z'; c++) {
			ascii.add(String.valueOf(c));
		}
		for (char c = '0'; c <= '9'; c++) {
			digits.add(String.valueOf(c));
		}
		ascii.addAll(digits);
	}

	public static String genCode(int codeLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(UUID.randomUUID().toString().replaceAll("-", ""));
		StringBuffer code = new StringBuffer();
		String str = sb.substring(0, 30);

		int size = 0;
		for (char c : str.toCharArray()) {
			if (size == codeLength) {
				return code.toString();
			}
			if (NUMBERS.contains(c)) {
				code.append(c);
				size++;
			}
		}
		return null;
	}

}
