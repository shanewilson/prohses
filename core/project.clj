(defproject prohses/core "0.1.0-SNAPSHOT"
  :description "Prohses Core"
  :url "https://github.com/shanewilson/prohses/core"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :plugins [[lein-cascade "0.1.1"]]
  :profiles  {:dev {:dependencies [[midje "1.6-beta1"]]
                    :plugins [[lein-midje "3.1.1"]]}}
  :cascade {"test" [["midje"]]})

