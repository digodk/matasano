package resources;

public class Parser {

  // Parses hex strings to byte arrays. Odd sized strings receive a leading 0;
  public byte[] hexStringToByteArray(String s) {
    int len = s.length();
    if (len % 2 != 0) {
      s = "0" + s;
      len++;
    }
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
              + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  // Parses a pair of hex string chars at a giver position to their
  // correspondent byte value. Not safe if position is the last index for string.
  byte hexStringPairToByte(String s, int position) {
    return (byte) ((Character.digit(s.charAt(position), 16) << 4)
            + Character.digit(s.charAt(position + 1), 16));
  }

}
