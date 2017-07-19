DictionaryRule rule = new DictionaryRule(
  new WordListDictionary(WordLists.createFromReader(
    // Reader around the word list file
    new FileReader[] {new FileReader("path/to/top100.txt")},
    // True for case sensitivity, false otherwise
    false,
    // Dictionaries must be sorted
    new ArraysSort())));
