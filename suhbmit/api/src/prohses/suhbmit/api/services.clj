(ns prohses.suhbmit.api.services
  (:require [datomic.api :as d]))

(def conn nil)

(defn q
  [query keys]
  "Datomic Query Shorthand for returning results as a map"
  (->>
   (d/q query (d/db conn))
   (map (partial zipmap keys))))

(defn get-projects
  []
  ""
  (q '[:find ?p ?code ?name
       :where
       [?p :project/code ?code]
       [?p :project/name ?name]]
     [:id :code :name]))

(defn get-releases
  []
  ""
  (q '[:find ?r ?number ?state ?dictionary
       :where
       [?r :release/number ?number]
       [?r :release/state ?s]
       [?s :db/ident ?state]
       [?r :release/dictionary ?d]
       [?d :dictionary/version ?dictionary]]
     [:id :number :state :dictionary]))
