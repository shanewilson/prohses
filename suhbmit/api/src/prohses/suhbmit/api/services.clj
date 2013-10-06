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

(defn get-projects
  []
  ""
  (d/q '[:find ?n :where [?n :news/title]] db))