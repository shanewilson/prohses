(defproject prohses/suhbmit/api "0.1.0-SNAPSHOT"
  :description "Prohses Suhbmit API"
  :url "https://github.com/shanewilson/prohses/suhbmit/api"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.taoensso/timbre "2.6.2"]
                 [compojure "1.1.5"]
                 [ring/ring-json "0.2.0"]
                 [liberator "0.9.0"]
                 [com.datomic/datomic-free "0.8.4215"]]
  :plugins []
  :profiles  {:dev {:dependencies [[http-kit "2.1.11"]
                                   [midje "1.5.1"]
                                   [ring-mock "0.1.5"]
                                   [ring/ring-devel "1.2.0"]
                                   [ring-cors "0.1.0"]]
                    :plugins [[lein-midje "3.1.1"]
                              [lein-ring "0.8.5"]]}}
  :ring {:handler prohses.suhbmit.api.handler/app}
  :main prohses.suhbmit.api.core)
