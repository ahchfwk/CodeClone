package main;

import java.io.IOException;

import code2token.*;
import nilsimsa.*;

public class codehub {
	public static void main(String[] args) throws IOException {
		Word word = new Word();
		String string = word.segment("Hive.java").getTokenStr();
		System.out.println(string);
		Nilsimsa nilsimsa = new Nilsimsa(string);
		String hash = nilsimsa.toHexDigest();
		System.out.println(hash);
	}
}
