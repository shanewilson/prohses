(ns prohses.suhbmit.api.services
  (:require [datomic.api :as d]))

(def uri "datomic:free://localhost:4334/prohses")
(def conn (d/connect uri))

(defn decorate
  "Simple function to pull out all the attributes of an entity into a map"
  [id]
  (let [ db (d/db conn)
         e (d/entity db id)]
    (select-keys e (keys e))))

(defn decorate-result
  ""
  [r]
  (-> r ffirst decorate))

(defn decorate-results
  "maps through a result set where each item is a single entity and decorates it"
  [r]
  (map #(decorate (first %)) r))

(defn get-dictionaries
  "Returns all Dictionary Versions"
  []
  (decorate-results (d/q '[:find ?d :where [?d :dictionary/version]] (d/db conn))))

(defn get-dictionary-for-release
  [number]
  ""
  (decorate-result
   (d/q '[:find ?d
          :in $ ?number
          :where
          [?r :release/number ?number]
          [?r :release/dictionary ?version]
          [?d :dictionary/version ?version]]
        (d/db conn)
        number)))

(defn get-dictionary
  [version]
  ""
  (decorate-result
   (d/q '[:find ?d
          :in $ ?version
          :where
          [?d :dictionary/version ?version]]
        (d/db conn)
        version)))


(defn get-projects []
  ""
  (decorate-results
   (d/q '[:find ?p :where [?p :project/code]] (d/db conn))))

(defn get-project
  [code]
  ""
  (decorate-result
   (d/q '[:find ?p
          :in $ ?code
          :where
          [?p :project/code ?code]]
        (d/db conn)
        code)))

(defn get-releases []
  ""
  (decorate-results
   (d/q '[:find ?r :where [?r :release/number]] (d/db conn))))


(defn get-release
  [number]
  ""
  (decorate-result
   (d/q '[:find ?r
          :in $ ?number
          :where
          [?r :release/number ?number]]
        (d/db conn)
        number)))

(defn get-releases-by-dictionary
  [version]
  ""
  (decorate-results
   (d/q '[:find ?r
          :in $ ?version
          :where
          [?r :release/dictionary ?version]]
        (d/db conn)
        version)))

(defn get-submissions-for-release
  [number]
  ""
  (decorate-results
   (d/q '[:find ?s
          :in $ ?number
          :where
          [?s :submission/release ?number]]
        (d/db conn)
        number)))

(defn get-submissions-for-project
  [code]
  ""
  (decorate-results
   (d/q '[:find ?s
          :in $ ?code
          :where
          [?s :submission/project ?code]]
        (d/db conn)
        code)))

(defn get-submission
  [number code]
  ""
  (decorate-result
   (d/q '[:find ?s
          :in $ ?number ?code
          :where
          [?s :submission/release ?number]
          [?s :submission/project ?code]]
        (d/db conn)
        number code)))
