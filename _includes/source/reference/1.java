PasswordValidator validator = new PasswordValidator(
  // length between 8 and 16 characters
  new LengthRule(8, 16),

  // at least one upper-case character
  new CharacterRule(EnglishCharacterData.UpperCase, 1),

  // at least one lower-case character
  new CharacterRule(EnglishCharacterData.LowerCase, 1),

  // at least one digit character
  new CharacterRule(EnglishCharacterData.Digit, 1),

  // at least one symbol (special character)
  new CharacterRule(EnglishCharacterData.Special, 1),

  // define some illegal sequences that will fail when >= 5 chars long
  // alphabetical is of the form 'abcde', numerical is '34567', qwery is 'asdfg'
  // the false parameter indicates that wrapped sequences are allowed; e.g. 'xyzabc'
  new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
  new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
  new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),

  // no whitespace
  new WhitespaceRule());

final char[] password = System.console().readPassword("Password: ");
RuleResult result = validator.validate(new PasswordData(new String(password)));
if (result.isValid()) {
  System.out.println("Password is valid");
} else {
  System.out.println("Invalid password:");
  for (String msg : validator.getMessages(result)) {
    System.out.println(msg);
  }
}
