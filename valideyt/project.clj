(defproject prohses/valideyt "0.1.0-SNAPSHOT"
  :description "Prohses Valideyt"
  :url "https://github.com/shanewilson/prohses/valideyt"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :aot :all
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :plugins [[lein-cascade "0.1.1"]]
  :profiles  {:dev {:dependencies [[midje "1.5.1"]
                                   [storm "0.8.2"]]
                    :plugins [[lein-midje "3.1.1"]]}}
  :cascade {"test" [["midje"]]}
  :main prohses.valideyt.core)
