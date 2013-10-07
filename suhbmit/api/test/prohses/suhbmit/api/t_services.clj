(ns prohses.suhbmit.api.t-services
  (:require [midje.sweet :refer [fact facts]]
            [datomic.api :as d]
            [prohses.suhbmit.api.services :as services]))

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

(facts "about services"
       (fact "projects"
             (services/get-projects) => {:name "blah"}))
