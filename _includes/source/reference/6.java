List<CharacterRule> rules = Arrays.asList(
  // at least one upper-case character
  new CharacterRule(EnglishCharacterData.UpperCase, 1),

  // at least one lower-case character
  new CharacterRule(EnglishCharacterData.LowerCase, 1),

  // at least one digit character
  new CharacterRule(EnglishCharacterData.Digit, 1),

  // at least one symbol (special character)
  new CharacterRule(EnglishCharacterData.Special, 1));

PasswordGenerator generator = new PasswordGenerator();

// Generated password is 12 characters long, which complies with policy
String password = generator.generatePassword(12, rules);
