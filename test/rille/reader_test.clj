(ns rille.reader-test
  (:require [clojure.test :refer [deftest is testing]]
            [rille.reader :as reader]))

(deftest basic-functionality
  (testing "reads to the next backtick"
    (is (= ["Hello"] (reader/read-string "#`Hello` Goodbye"))))
  (testing "errors when unclosed by backtick"
    (is (thrown? clojure.lang.ExceptionInfo (reader/read-string "#`Hello")))))

(deftest interpolates-values
  (testing "interpolates values"
    (is (= ["a " 2 " d"] (reader/read-string "#`a ~{2} d`"))))
  (testing "interpolates values from local scope"
    (is (= ["a " 3 " d"]
           (eval (reader/read-string "(let [a 3] #`a ~{a} d`)")))))
  (testing "errors when unclosed by right brace"
    (is (thrown? clojure.lang.ExceptionInfo (reader/read-string "#`~{2`")))))

(deftest interpolates-forms
  (testing "interpolates forms"
    (is (= ["a " '(* 1 2) " d"] (reader/read-string "#`a ~(* 1 2) d`"))))
  (testing "interpolates forms from local scope"
    (is (= ["a " 6 " d"]
           (eval (reader/read-string "(let [a 3 b 2] #`a ~(* a b) d`)")))))
  (testing "errors when unclosed by right paren"
    (is (thrown? clojure.lang.ExceptionInfo (reader/read-string "#`~(* 1 2`")))))

(deftest escaping
  (testing "double quotes do not need to be escaped"
    (is (= ["\"Greetings\""] (reader/read-string "#`\"Greetings\"`"))))
  (testing "backticks can be escaped"
    (is (= ["`"] (reader/read-string "#`\\``"))))
  (testing "tildes can be escaped before values"
    (is (= ["~{5}"] (reader/read-string "#`\\~{5}`"))))
  (testing "tildes can be escaped before forms"
    (is (= ["~(* 3 2)"] (reader/read-string "#`\\~(* 3 2)`"))))
  (testing "tildes are included when not interpolating"
    (is (= ["~[4 8]"] (reader/read-string "#`~[4 8]`")))))

(defn run-tests [_]
  (clojure.test/run-tests 'rille.reader-test))
