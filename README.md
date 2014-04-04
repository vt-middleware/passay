# Passay

Passay is a Java library for verifying that a password meets a define ruleset.

This library includes the following rule implementations:

Rule | Descrption
---- | ----------
AllowedCharacterRule | Does a password contain only a specific list of characters
AlphabeticalSequenceRule | Does a password contain an alphabetical sequence
CharacterCharacteristicRule | Does a password contain the desired mix of character types
DictionaryRule | Does a password match a word in a dictionary
DictionarySubstringRule | Does a password contain a word in a dictionary
DigitCharacterRule | Does a password contain a digit
HistoryRule | Does a password match a previous password, supports hashes
IllegalCharacterRule | Does a password contain an illegal character
LengthRule | Is a password of a certain length
LowercaseCharacterRule | Does a password contain a lowercase character
NonAlphanumericCharacterRule | Does a password contain a non-alphanumeric character
NumericalSequenceRule | Does a password contain a numerical sequence
QwertySequenceRule | Does a password contain a QWERTY keyboard sequence
RegexRule | Does a password match a regular expression
RepeatCharacterRegexRule | Does a password contain a repeated character
SourceRule | Does a password match the password from another system or source
UppercaseCharacterRule | Does a password contain an uppercase character
UsernameRule | Does a password contain a username
WhitespaceRule | Does a password contain whitespace

