new CharacterRule(new CharacterData() {
  @Override
  public String getErrorCode() {
    return "ERR_SPACE";
  }

  @Override
  public String getCharacters() {
    return " ";
  }
}, 1);
