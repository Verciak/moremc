package net.moremc.api.helper;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomStringHelper {

  public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  
  public static final String lower = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase(Locale.ROOT);
  
  public static final String digits = "0123456789";
  
  public static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + lower + "0123456789";
  
  private final Random random;
  
  private final char[] symbols;
  
  private final char[] buf;
  
  public RandomStringHelper(int length, Random random, String symbols) {
    if (length < 1)
      throw new IllegalArgumentException(); 
    if (symbols.length() < 2)
      throw new IllegalArgumentException(); 
    this.random = Objects.<Random>requireNonNull(random);
    this.symbols = symbols.toCharArray();
    this.buf = new char[length];
  }
  
  public RandomStringHelper(int length, Random random) {
    this(length, random, alphanum);
  }
  
  public RandomStringHelper(int length) {
    this(length, new SecureRandom());
  }
  public RandomStringHelper() {
    this(21);
  }
  public static String getString(int length) {
    RandomStringHelper randomStringHelper = new RandomStringHelper(length);
    return randomStringHelper.nextString();
  }
  public String nextString() {
    for (int idx = 0; idx < this.buf.length; idx++)
      this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)]; 
    return new String(this.buf);
  }
}