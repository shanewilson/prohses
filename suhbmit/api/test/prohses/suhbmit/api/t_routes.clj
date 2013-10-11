(ns prohses.suhbmit.api.t-routes
  (:use midje.sweet)
  (:require [datomic.api :as d]
            [prohses.suhbmit.api.routes :as routes]))

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


;; (facts "Dictionary route"
;;  (fact "/api/dictionaries should return all dictionaries"
;;    (routes/api {:uri "/api/dictionaries" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:id 1
;;                          :state "CLOSED"}
;;                         {:id 2
;;                          :state "OPEN"}]}))
;;  (fact "/api/dictionaries/:version should return one dictionary with version :version"
;;    (routes/api {:uri "/api/dictionaries/1" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :state "CLOSED"
;;                         :data "{\"key\":\"value\"}"}}))
;;  (fact "/api/dictionaries/:version/releases should return all releases using that version dictionary"
;;    (routes/api {:uri "/api/dictionaries/1/releases" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:id 1
;;                          :name "Release 1"
;;                          :state "CLOSED"
;;                          :released nil
;;                          :dictionary_id 1}
;;                         {:id 2
;;                          :name "Release 2"
;;                          :state "OPEN"
;;                          :released nil
;;                          :dictionary_id 1}]})))

;; (facts "Release route"
;;  (fact "/api/releases should return all Releases"
;;    (routes/api {:uri "/api/releases" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:id 1
;;                          :name "Release 1"
;;                          :state "CLOSED"
;;                          :released nil
;;                          :dictionary_id 1}
;;                         {:id 2
;;                          :name "Release 2"
;;                          :state "OPEN"
;;                          :released nil
;;                          :dictionary_id 1}]}))
;;  (fact "/api/releases/:version should return one Release with version :version"
;;    (routes/api {:uri "/api/releases/1" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :name "Release 1"
;;                         :state "CLOSED"
;;                         :released nil
;;                         :dictionary_id 1}}))
;;  (fact "/api/releases/:version/dictionary should return the dictionary used"
;;    (routes/api {:uri "/api/releases/1/dictionary" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :state "CLOSED"
;;                         :data "{\"key\":\"value\"}"}}))
;;  (fact "/api/releases/:version/report should return a summary report if it exists"
;;    (routes/api {:uri "/api/releases/1/report" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :state "CLOSED"
;;                         :data "{\"key\":\"value\"}"}}))
;;  (fact "/api/releases/:version/report should return a 404 if summary report does not exist"
;;    (routes/api {:uri "/api/releases/1/report" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:id 1
;;                         :state "CLOSED"
;;                         :data "{\"key\":\"value\"}"}})))

(facts "Project resources"
 (fact "/api/projects should return all Projects"
   (routes/api {:uri "/api/projects" :request-method :get})
   => (contains {:status 200
                 :body [{:name "Project 1"
                         :id 1}
                        {:name "Project 2"
                         :id 2}]}))
 (fact "/api/projects/:code should return one Project with code :code"
   (routes/api {:uri "/api/projects/1" :request-method :get})
   => (contains {:status 200
                 :body {:name "Project 1"
                        :id 1}})))

;; (facts "about Submission resources"
;;  (fact "that return all Submissions for a Release"
;;    (routes/api {:uri "/api/releases/1/submissions" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:release_name "Release 1"
;;                          :release_id 1
;;                          :project_name "Project 1"
;;                          :project_id 1
;;                          :state "SIGNED OFF"
;;                          :id 1}
;;                         {:release_name "Release 1"
;;                          :release_id 1
;;                          :project_name "Project 2"
;;                          :project_id 2
;;                          :state "SIGNED OFF"
;;                          :id 2}]}))
;;  (fact "that return one Submission for a Release"
;;    (routes/api {:uri "/api/releases/1/submissions/1" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:release_name "Release 1"
;;                         :release_id 1
;;                         :project_name "Project 1"
;;                         :project_id 1
;;                         :state "SIGNED OFF"
;;                         :id 1}}))
;;  (fact "that return all Submissions for a Project"
;;    (routes/api {:uri "/api/projects/1/submissions" :request-method :get})
;;    => (contains {:status 200
;;                  :body [{:project_id 1
;;                          :project_name "Project 1"
;;                          :release_id 1
;;                          :release_name "Release 1"
;;                          :state "SIGNED OFF"
;;                          :id 1}
;;                         {:project_id 1
;;                          :project_name "Project 1"
;;                          :release_id 2
;;                          :release_name "Release 2"
;;                          :state "VALID"
;;                          :id 3}]}))
;;  (fact "that return one Submission for a Project"
;;    (routes/api {:uri "/api/projects/1/submissions/1" :request-method :get})
;;    => (contains {:status 200
;;                  :body {:project_id 1
;;                         :project_name "Project 1"
;;                         :release_id 1
;;                         :release_name "Release 1"
;;                         :state "SIGNED OFF"
;;                         :id 1}}))
;;  )

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
