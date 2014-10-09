# Passay

Passay is a Java library for verifying that a password meets a define ruleset.

This library includes the following rule implementations:

Rule | Descrption
---- | ----------
AllowedCharacterRule | Does a password contain only a specific list of characters
AllowedRegexRule | Does a password match an allowed regular expression
AlphabeticalSequenceRule | Does a password contain an alphabetical sequence
CharacterCharacteristicRule | Does a password contain the desired mix of character types
DictionaryRule | Does a password match a word in a dictionary
DictionarySubstringRule | Does a password contain a word in a dictionary
DigitCharacterRule | Does a password contain the desired number of digits
HistoryRule | Does a password match a previous password, supports hashes
IllegalCharacterRule | Does a password contain an illegal character
IllegalRegexRule | Does a password match an illegal regular expression
LengthRule | Is a password of a certain length
LowercaseCharacterRule | Does a password contain the desired number of lowercase characters
NumericalSequenceRule | Does a password contain a numerical sequence
QwertySequenceRule | Does a password contain a QWERTY keyboard sequence
RepeatCharacterRegexRule | Does a password contain a repeated character
SourceRule | Does a password match the password from another system or source
SpecialCharacterRule | Does a password contain the desired number of special characters
UppercaseCharacterRule | Does a password contain the desired number of uppercase characters
UsernameRule | Does a password contain a username
WhitespaceRule | Does a password contain whitespace

