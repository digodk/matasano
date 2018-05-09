package resources;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Base64Codec {
  public final char paddingChar = '=';
  public final String paddingString = "==";
  public final String strTbl = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
  public final byte[] byteTbl = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
      81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107,
      108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51,
      52, 53, 54, 55, 56, 57, 43, 47 };
  
  class Encoder {
    Parser parser = new Parser();

    public byte[] byteToByte(byte[] data) {
      int arraySize = data.length;

      // returns empty array if data array is empty
      if (arraySize == 0) {
        return new byte[0];
      }

      int paddingCount = (3 - (arraySize % 3)) % 3;
      int encodedArraySize = (arraySize + paddingCount) * 4 / 3;
      int base64Byte, byteIndex;
      byte[] encodedData = new byte[encodedArraySize];
      // Adds padding if necessary
      byte[] paddedData = new byte[arraySize + paddingCount];
      System.arraycopy(data, 0, paddedData, 0, arraySize);

      for (int ix = 0; ix < arraySize; ix += 3) {
        // Gets 3 bytes from data array and stores them in an int variable
        base64Byte = ((paddedData[ix] & 0xff) << 16) + ((paddedData[ix + 1] & 0xff) << 8)
            + (paddedData[ix + 2] & 0xff);

        // Split 3 bytes in 4 base64 representations, each is an index for a byte in byteTbl
        for (int jx = 0; jx < 4; jx++) {
          byteIndex = base64Byte >> (18 - jx * 6) & 0x3f;
          encodedData[ix * 4 + jx] = byteTbl[byteIndex];
        }
      }
      // Adds padding char byte to the end of array if needed
      while (paddingCount > 0) {
        encodedData[encodedArraySize - paddingCount] = (byte) paddingChar;
        paddingCount--;
      }

      return encodedData;
    }

    public String byteToString(byte[] data, boolean split) {
      int arraySize = data.length;

      // returns empty string if data array is empty
      if (arraySize == 0) {
        return "";
      }

      int paddingCount = (3 - (arraySize % 3)) % 3;
      int base64Byte, strIndex;
      String encodedData = "", encodedSplitData = "";
      // Adds padding if necessary
      byte[] paddedData = new byte[arraySize + paddingCount];
      System.arraycopy(data, 0, paddedData, 0, arraySize);

      for (int ix = 0; ix < arraySize; ix += 3) {
        // Gets 3 bytes from data array and stores them in an int variable
        base64Byte = ((paddedData[ix] & 0xff) << 16) + ((paddedData[ix + 1] & 0xff) << 8)
            + (paddedData[ix + 2] & 0xff);

        // Split 3 bytes in 4 base64 representations, each is an index for a char in strTbl
        for (int jx = 0; jx < 4; jx++) {
          strIndex = base64Byte >> (18 - jx * 6) & 0x3f;
          encodedData += strTbl.charAt(strIndex);
        }
      }
      // replace encoded padding nulls with "="
      encodedData = encodedData.substring(0, encodedData.length() - paddingCount)
          + paddingString.substring(0, paddingCount);

      // split into multiple lines
      if (split) {
        for (int ix = 0; ix < encodedData.length(); ix += 76) {
          encodedSplitData += encodedData.substring(ix, Math.min(encodedData.length(), ix + 76))
              + "\r\n";
        }
        // Saves the resulting split string, trimming if necessary.
        encodedData = encodedSplitData.trim();
      }
      // return the result
      return encodedData;
    }

    public String byteToString(byte[] data) {
      return byteToString(data, false);
    }

    // Converts Hex to string
    public String hexToString(int data, boolean split) {
      String encodedData = "";
      // Stores data in byte order, ensures big endian order and converts to array
      ByteBuffer buff = ByteBuffer.allocate(data);
      buff.order(ByteOrder.BIG_ENDIAN);
      byte[] byteData = buff.array();
      // Converts data
      encodedData = byteToString(byteData, split);
      return encodedData;

    }

    public String hexToString(int data) {
      return hexToString(data, false);
    }

    // Converts hex strings to base64 strings
    public String hexStringToString(String stringData, boolean split) {
      byte[] data = parser.hexStringToByteArray(stringData);
      return byteToString(data, split);
    }

    public String hexStringToString(String stringData) {
      return hexStringToString(stringData, false);
    }
  }

  class Parser {
    public byte[] customHexStringToByeArray(String s) {
      
      int arrLen = s.length() / 2;
      arrLen += s.length() % 2 != 0 ? 1 : 0;
      
      byte[] data = new byte[arrLen];

      return data;
    }
    // Parses hex strings to byte arrays. Not safe for strings of odd length.
    public byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
            + Character.digit(s.charAt(i + 1), 16));
      }
      return data;
    }
    
  }
  
  class decode {

  }

}
