LengthRule r1 = new LengthRule(8, 16);

CharacterCharacteristicsRule r2 = new CharacterCharacteristicsRule(
  // Define M (3 in this case)
  3,
  // Define elements of N (upper, lower, digit, symbol)
  new CharacterRule(EnglishCharacterData.UpperCase, 1),
  new CharacterRule(EnglishCharacterData.LowerCase, 1),
  new CharacterRule(EnglishCharacterData.Digit, 1),
  new CharacterRule(EnglishCharacterData.Special, 1));

WhitespaceRule r3 = new WhitespaceRule();

PasswordValidator validator = new DefaultPasswordValidator(r1, r2, r3);
