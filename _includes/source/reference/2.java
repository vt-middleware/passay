Properties props = new Properties();
props.load(new FileInputStream("/path/to/messages.properties"));
MessageResolver resolver = new PropertiesMessageResolver(props);
PasswordValidator validator = new PasswordValidator(resolver, ruleList);
