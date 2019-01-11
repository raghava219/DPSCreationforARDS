package com.utility.xml;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "SELECT oha.order_number, CS.cam_location_id ,CS.site_use_id, category_code, customer_number,payment_term_id"
		            + "Description of Monkey's website."
		            + "www.foo.com/Archives/pigs.txt"
		            + "Description of Pig's website.";
	   
		String[] out = s.split("\\r?\\n");
		
		System.out.println(out.length);
		
	}

}
