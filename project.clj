(defproject prohses "0.1.0-SNAPSHOT"
  :description "Prohses Parent"
  :url "https://github.com/shanewilson/prohses"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :plugins [[lein-sub "0.2.0"]
            [lein-cascade "0.1.1"]]
  :sub ["core", "suhbmit", "valideyt"]
  :cascade {"test" [["sub" "cascade" "test"]]})
