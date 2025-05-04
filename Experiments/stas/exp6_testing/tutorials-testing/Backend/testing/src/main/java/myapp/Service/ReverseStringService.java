package myapp.Service;

import org.springframework.stereotype.Service;

@Service
public class ReverseStringService {

	/**
	 * Uses StringBuilder to reverse the given string
	 * @param str	Given string
	 * @return	Reversed string
	 */
	public String reverse(String str) {
		return new StringBuilder().append(str).reverse().toString();
	}
}
