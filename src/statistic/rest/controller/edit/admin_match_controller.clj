(ns statistic.rest.controller.edit.admin-match-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [statistic.db.aggregate.match-aggregate :as match-aggregate])
  (:import (java.time OffsetDateTime)
           (org.postgresql.util PSQLException)))

(defn create-match-handler
  "requires :body {:date
                   :winner -> 0,1,2
                   :discipline -> char(20)
                   :tag -> char(20)
                   :team1 -> id
                   :team2 -> id
                   :space -> id}"

  [{body :body auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (-> auth Integer/parseInt (get 2)) (:space body))
    (let [{:keys [date winner discipline tag team1 team2 space]} body
          team1-mapped (map (fn [id] {:team 1 :id id}) team1)
          team2-mapped (map (fn [id] {:team 2 :id id}) team2)
          players (concat team1-mapped team2-mapped)
          param {:date       (OffsetDateTime/parse date)
                 :winner     winner
                 :discipline discipline
                 :tag        tag
                 :players    players
                 :space      space}]
      (try
        (let [new-match-id (match-aggregate/create-new-match param)]
          {:status  200
           :headers {"Content-Type" "text/json"}
           :body    (json/write-str {:new-match new-match-id})})
        (catch PSQLException e
          (-> e .getMessage println)
          (.printStackTrace e)
          {:status 500})))
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defroutes routes
           (POST "/matches/create" [] create-match-handler))