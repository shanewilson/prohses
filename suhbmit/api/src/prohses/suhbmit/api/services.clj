(ns prohses.suhbmit.api.services
  (:require [datomic.api :as d]))

;; store database uri
(def uri "datomic:mem://test")

;; create database
(d/delete-database uri)
(d/create-database uri)

;; connect to database
(def conn (d/connect uri))

;; parse schema edn file
(def schema-tx (read-string (slurp "resources/schema.edn")))
@(d/transact conn schema-tx)

;; parse seed data
(def data-tx (read-string (slurp "resources/fixtures.edn")))
@(d/transact conn data-tx)

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
       [?r :release/state ?state]
       [?r :release/dictionary ?dictionary]]
     [:id :number :state :dictionary]))
