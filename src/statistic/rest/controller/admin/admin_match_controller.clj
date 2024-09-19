(ns statistic.rest.controller.admin.admin-match-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [statistic.service.match :as match])
  (:import (org.postgresql.util PSQLException)))

(defn create-match-handler
  "requires :body {:date
                   :winner -> 0,1,2
                   :discipline -> char(20)
                   :tag -> char(20)
                   :team1 -> id
                   :team2 -> id"
  [req]
  (let [{:keys [date winner discipline tag team1 team2]} (:body req)
        team1-mapped (map #({:team 1 :id %}) team1)
        team2-mapped (map #({:team 2 :id %}) team2)
        players (concat team1-mapped team2-mapped)]
    (try
      (let [new-match-id (match/create-new-match {:date       date
                                                  :winner     winner
                                                  :discipline discipline
                                                  :tag        tag
                                                  :players    players})]
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str {:new-match new-match-id})})
      (catch PSQLException e
        (-> e .getMessage println)
        (.printStackTrace e)
        {:status 500}))))

(defroutes routes
           (POST "/matches/create" [] create-match-handler))