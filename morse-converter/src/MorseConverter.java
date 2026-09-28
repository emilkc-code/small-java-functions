static final byte[] morseLetters = {
        0b101, 0b11000, 0b11010, 0b1100, 0b10,
        0b10010, 0b1110, 0b10000, 0b100, 0b10111,
        0b1101, 0b10100, 0b111, 0b110, 0b1111,
        0b10110, 0b11101, 0b1010, 0b1000, 0b11,
        0b1001, 0b10001, 0b1011, 0b11001, 0b11011, 0b11100
};

static final byte[] morseNumbers = {
        0b111111, 0b101111, 0b100111, 0b100011, 0b100001,
        0b100000, 0b110000, 0b111000, 0b111100, 0b111110
};

void main(String[] args) {
  switch (args[0]) {
    case "-t":
      IO.println(morseTextToText(args[1]));
      break;
    case "-m":
      IO.println(textToMorseText(args[1]));
      break;
  }
}

public static String textToMorseText(String text) {
  StringBuilder stringBuilder = new StringBuilder();

  char[] splitText = text.toCharArray();
  for (char letter : splitText) {
    stringBuilder.append(" ");

    if (letter == ' ') {
      stringBuilder.append("/");
      continue;
    }

    byte morse = getMorseByteFromChar(letter);
    String morseString = morseToString(morse);
    stringBuilder.append(morseString);
  }

  return stringBuilder.toString();
}

public static String morseTextToText(String morseText) {
  StringBuilder stringBuilder = new StringBuilder();

  String[] splitMorseText = morseText.split(" ");
  for (String morseString : splitMorseText) {
    if (morseString.equals("/")) {
      stringBuilder.append(" ");
      continue;
    }

    byte morse = morseStringToByte(morseString);
    int unicode = morseToUnicode(morse);
    char letter = (char) unicode;
    stringBuilder.append(letter);
  }

  return stringBuilder.toString();
}

private static int morseToUnicode(byte morse) {
  int morseIndex = getMorseIndex(morse, morseLetters);
  int unicodeOffset = 65;

  if (morseIndex == -1) {
    morseIndex = getMorseIndex(morse, morseNumbers);
    unicodeOffset = 48;
  }

  return morseIndex + unicodeOffset;
}

private static byte morseStringToByte(String morseText) {
  byte morse = 1;

  char[] splitMorseText = morseText.toCharArray();
  for (char letter : splitMorseText) {
    morse = (byte) (morse << 1);
    if (letter == '-') {
      morse += 1;
    }
  }

  return morse;
}

private static int getMorseIndex(byte morseByte, byte[] morse) {
  for (int i = 0; i < morse.length; i++) {
    if (morseByte != morse[i]) {
      continue;
    }
    return i;
  }

  return -1;
}

private static byte getMorseByteFromChar(char letter) {
  int unicode = letter;
  int unicodeOffset = 65;

  if (unicode < 65) {
    unicodeOffset = 48;
    unicode -= unicodeOffset;
    return morseNumbers[(unicode)];
  }

  unicode -= unicodeOffset;
  return morseLetters[unicode];
}

private static String morseToString(byte morse) {
  int loadingZeros = Integer.numberOfLeadingZeros(morse);
  int totalBits = 31 - loadingZeros;

  StringBuilder stringBuilder = new StringBuilder();
  for (int i = totalBits - 1; i >= 0; i--) {
    int bit = ((morse >> i) & 1);
    stringBuilder.append(bit == 0 ? '.' : '-');
  }
  return stringBuilder.toString();
}