LengthRule r1 = new LengthRule(8, 16);

CharacterCharacteristicsRule r2 = new CharacterCharacteristicsRule();

// Define M (3 in this case)
r2.setNumberOfCharacteristics(3);

// Define elements of N (upper, lower, digit, symbol)
r2.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
r2.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
r2.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
r2.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

WhitespaceRule r3 = new WhitespaceRule();

PasswordValidator validator = new PasswordValidator(Arrays.asList(r1, r2, r3));
