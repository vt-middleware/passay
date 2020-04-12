Properties props = new Properties();
props.load(new FileInputStream("/path/to/passay.properties"));
MessageResolver resolver = new PropertiesMessageResolver(props);
PasswordValidator validator = new PasswordValidator(
  resolver, new LengthRule(8, 16), new WhitespaceRule());
