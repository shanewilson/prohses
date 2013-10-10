(ns prohses.suhbmit.api.t-services
  (:require [midje.sweet :refer [fact facts background]]
            [datomic.api :as d]
            [prohses.suhbmit.api.services :as services]))

;; store database uri
(defn- mem-db!
  []
  ""
  (let [uri "datomic:mem://test"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")
          fixtures (load-file "resources/datomic/fixtures.edn")]
      @(d/transact conn schema)
      @(d/transact conn fixtures)
      conn)))

(background (around :facts (with-redefs [prohses.suhbmit.api.services/conn (mem-db!)]
                             ?form)))

(facts "Project Service"
       (fact "get-projects should return all projects"
             (services/get-projects) => {:name "blah"}))
