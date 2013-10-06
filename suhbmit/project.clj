(defproject prohses/suhbmit "0.1.0-SNAPSHOT"
  :description "Prohses Suhbmit Parent"
  :url "https://github.com/shanewilson/prohses/suhbmit"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :plugins [[lein-sub "0.2.0"]
            [lein-cascade "0.1.1"]]
  :sub ["suhbmit/api"]
  :cascade {"test" [["sub" "test"]]})
