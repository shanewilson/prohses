(ns prohses.suhbmit.api.t-routes
  (:use midje.sweet)
  (:require [datomic.api :as d]
            [cheshire.core :refer :all]
            [prohses.suhbmit.api.handler :as handler]))

;; :body is a string for some reason
(defn parse-body
  [r]
  ""
  {:status (:status r)
   :body (-> r :body  (parse-string true))})

;; create in-mem database and load test data
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

;; point queries to the in-mem test db
(background (around :facts (with-redefs [prohses.suhbmit.api.services/conn (mem-db!)]
                             ?form)))

(facts "Dictionary route"
       (fact "/api/dictionaries should return all dictionaries"
             (parse-body (handler/app {:uri "/api/dictionaries" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data [{:version 1
                                          :state "frozen"}
                                         {:version 2
                                          :state "fluid"}]}}))
       (fact "/api/dictionaries/:version should return one dictionary with version :version"
             (parse-body (handler/app {:uri "/api/dictionaries/1" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data {:version 1
                                         :state "frozen"}}}))
       (fact "/api/dictionaries/:version/releases should return all releases using that version dictionary"
             (parse-body (handler/app {:uri "/api/dictionaries/1/releases" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data [{:number 1
                                          :state "closed"
                                          :dictionary 1}
                                         {:number 2
                                          :state "closed"
                                          :dictionary 1}]}})))

(facts "Release route"
       (fact "/api/releases should return all Releases"
             (parse-body (handler/app {:uri "/api/releases" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data [{:number 1
                                          :state "closed"
                                          :dictionary 1}
                                         {:number 2
                                          :state "closed"
                                          :dictionary 1}
                                         {:number 3
                                          :state "open"
                                          :dictionary 2}]}}))
       (fact "/api/releases/:number should return one Release"
             (parse-body (handler/app {:uri "/api/releases/1" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data {:number 1
                                         :state "closed"
                                         :dictionary 1}}}))
       (fact "/api/releases/:number/dictionary should return the Dictionary used"
             (parse-body (handler/app {:uri "/api/releases/1/dictionary" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data {:version 1
                                         :state "frozen"}}}))
       (fact "/api/releases/:number/projects should return all Project Submissions for Release :number"
             (parse-body (handler/app {:uri "/api/releases/1/projects" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data [{:project "PRJ1"
                                          :release 1
                                          :state "signed-off"}
                                         {:project "PRJ2"
                                          :release 1
                                          :state "signed-off"}
                                         {:project "PRJ3"
                                          :release 1
                                          :state "signed-off"}]}}))
       (fact "/api/releases/:number/projects/:code should return Project :code Submission for Release :number"
             (parse-body (handler/app {:uri "/api/releases/1/projects/PRJ1" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data {:project "PRJ1"
                                         :release 1
                                         :state "signed-off"}}}))
       ;;  (fact "/api/releases/:version/report should return a summary report if it exists"
       ;;    (parse-body (handler/app {:uri "/api/releases/1/report" :request-method :get}))
       ;;    => (contains {:status 200
       ;;                  :body {:status "success"
       ;;                         :data {:id 1
       ;;                         :state "CLOSED"
       ;;                         :data "{\"key\":\"value\"}"}}}))
       ;;  (fact "/api/releases/:version/report should return a 404 if summary report does not exist"
       ;;    (parse-body (handler/app {:uri "/api/releases/1/report" :request-method :get}))
       ;;    => (contains {:status 200
       ;;                  :body {:status "success"
       ;;                         :data {:id 1
       ;;                         :state "CLOSED"
       ;;                         :data "{\"key\":\"value\"}"}}}))
       )


(facts "Project resources"
       (fact "/api/projects should return all Projects"
             (parse-body (handler/app {:uri "/api/projects" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success",
                                  :data [{:code "PRJ1"
                                          :name "Project One"}
                                         {:code "PRJ2"
                                          :name "Project Two"}
                                         {:code "PRJ3"
                                          :name "Project Three"}]}}))
       (fact "/api/projects/:code should return one Project with code :code"
             (parse-body (handler/app {:uri "/api/projects/PRJ1" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data {:code "PRJ1"
                                         :name "Project One"}}}))
       (fact "/api/projects/1/releases should return all Release submissions for Project :code"
             (parse-body (handler/app {:uri "/api/projects/PRJ1/releases" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data [{:project "PRJ1"
                                          :release 1
                                          :state "signed-off"}
                                         {:project "PRJ1"
                                          :release 2
                                          :state "signed-off"}
                                         {:project "PRJ1"
                                          :release 3
                                          :state "not-validated"}]}}))
       (fact "/api/projects/PRJ1/releases/1 should return Release :number Submission for Project :code"
             (parse-body (handler/app {:uri "/api/projects/PRJ1/releases/1" :request-method :get}))
             => (contains {:status 200
                           :body {:status "success"
                                  :data {:project "PRJ1"
                                         :release 1
                                         :state "signed-off"}}})))

;; (facts "about Report resources"
;;  (fact "that return all for a Release"
;;    (routes/api {:uri "/api/releases/1/submissions/1/reports" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:release_id 1
;;                          :submission_id 1
;;                          :data "{\"key\":\"value\"}"
;;                          :id 1}
;;                         {:release_id 1
;;                          :submission_id 1
;;                          :data "{\"key\":\"value\"}"
;;                          :id 2}
;;                         {:release_id 1
;;                          :submission_id 1
;;                          :data "{\"key\":\"value\"}"
;;                          :id 3}]}))
;;  (fact "that return one for a Release"
;;    (routes/api {:uri "/api/releases/1/submissions/1/reports/2" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:submission_id 1
;;                         :release_id 1
;;                         :data "{\"key\":\"value\"}"
;;                         :id 2}}))
;;  (fact "that return all for a Project"
;;    (routes/api {:uri "/api/projects/1/submissions/1/reports" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:release_id 1
;;                          :submission_id 1
;;                          :data "{\"key\":\"value\"}"
;;                          :id 1}
;;                         {:release_id 1
;;                          :submission_id 1
;;                          :data "{\"key\":\"value\"}"
;;                          :id 2}
;;                         {:release_id 1
;;                          :submission_id 1
;;                          :data "{\"key\":\"value\"}"
;;                          :id 3}]}))
;;  (fact "that return one for a Project"
;;    (routes/api {:uri "/api/projects/1/submissions/1/reports/2" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:submission_id 1
;;                         :release_id 1
;;                         :data "{\"key\":\"value\"}"
;;                         :id 2}}))
;;  )

;; (facts "about Open Release resource"
;;  (fact "that returns the current OPEN Release"
;;    (routes/api {:uri "/api/open" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 2
;;                         :name "Release 2"
;;                         :state "OPEN"
;;                         :released nil
;;                         :dictionary_id 1}}))
;;  (fact "that returns the Report"
;;    (routes/api {:uri "/api/open/report" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :name "Release 2"
;;                         :state "OPEN"
;;                         :released nil
;;                         :dictionary_id 1}}))
;;  (fact "that returns the Dictionary"
;;    (routes/api {:uri "/api/open/dictionary" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :state "CLOSED"
;;                         :data "{\"key\":\"value\"}"}}))
;;  (fact "that updates the Dictionary version"
;;    (routes/api {:uri "/api/open/dictionary" :request-method :put :params {:version "0.7"}})
;;    => (contains {:status 200
;;                  :body {:id 2
;;                         :state "OPEN"}}))
;;  (fact "that queues Submissions for Validation"
;;    (routes/api {:uri "/api/open/queue" :request-method :put :params {:project 2}})
;;    => (contains {:status 200
;;                  :body {:project_id 2
;;                         :project_name "Project 2"
;;                         :release_id 2
;;                         :release_name "Release 2"
;;                         :state "QUEUED"
;;                         :id 4}}))
;;  (fact "that dequeues QUEUED Submissions"
;;    (routes/api {:uri "/api/open/dequeue" :request-method :put :params {:project 1}})
;;    => (contains {:status 200
;;                  :body {:release_name "Release 1"
;;                         :release_id 1
;;                         :project_name "Project 1"
;;                         :project_id 1
;;                         :state "SIGNED OFF"
;;                         :id 1}}))
;;  (fact "that SIGNED OFF on a VALID Submission"
;;    (routes/api {:uri "/api/open/signoff" :request-method :put :params {:project 1}})
;;    => (contains {:status 200
;;                  :body {:project_id 1
;;                         :project_name "Project 1"
;;                         :release_id 2
;;                         :release_name "Release 2"
;;                         :state "SIGNED OFF"
;;                         :id 3}}))
;;  (fact "that closes the current OPEN Release"
;;    (routes/api {:uri "/api/open/close" :request-method :put :params {:name "Release 3"}})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :name "Release 3"
;;                         :state "OPEN"
;;                         :released nil
;;                         :dictionary_id 1}}))
;;  )
