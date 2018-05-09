package set1;

/*
 * Write a function that takes two equal-length buffers and produces their XOR combination.
 * If your function works properly, then when you feed it the string:
 * 1c0111001f010100061a024b53535009181c
 * ... after hex decoding, and when XOR'd against:
 * 686974207468652062756c6c277320657965
 * ... should produce:
 * 746865206b696420646f6e277420706c6179
 */
import java.util.Arrays;

public class Challenge2 {

  public static void main(String[] args) {
    String hex1 = "1c0111001f010100061a024b53535009181c";
    String hex2 = "686974207468652062756c6c277320657965";
    int bArrLen = hex1.length() / 2;
    byte byte1, byte2;
    byte[] result = new byte[bArrLen];
    // String parser. Not safe for odd sized strings
    for (int ix = 0; ix < hex1.length(); ix += 2) {
      byte1 = (byte) ((Character.digit(hex1.charAt(ix), 16) << 4)
              + Character.digit(hex1.charAt(ix + 1), 16));
      byte2 = (byte) ((Character.digit(hex2.charAt(ix), 16) << 4)
              + Character.digit(hex2.charAt(ix + 1), 16));
      result[ix / 2] = (byte) (byte1 ^ byte2);
    }
    System.out.println(Arrays.toString(result));
    // Just for fun: instead of using String.format("%02X", byte) I decided
    // to run my own hex string converter.
    String HexCharTable = "0123456789ABCDEF";
    String letter1, letter2;
    int position;
    String xored = "";
    for (byte b : result) {
      position = (int) (b & 0xF0) >> 4;
      letter1 = Character.toString(HexCharTable.charAt(position));
      position = (int) b & 0xF;
      letter2 = Character.toString(HexCharTable.charAt(position));
      xored += letter1 + letter2;
    }
    System.out.println(xored);
  }

}
