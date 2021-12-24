# Rille

Miscellaneous Clojure utilites.

## rille.reader

A fork of tools.reader that supports a backtick reader macro in which double quotes do not need to be escaped and values can be interpolated. For example, passing the following string to `rille.reader/read-string`:

```clojure
#`Hello "~{person}," I see you now have ~{5} dogs. That's ~(if (< yesterday 5) "more" "less") than yesterday.`
```

produces the following list of strings and interpolated forms:

```clojure
["Hello \"" person "\"I see you now have " 5 " dogs. That's " (if (< yesterday 5) "more" "less") " than yesterday."]
```
