(ns statistic.rest.controller.edit.admin-player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [statistic.db.tables.players :as players])
  (:import (org.postgresql.util PSQLException)))

(defn create-player-handler
  "requires :body {:name <name>}"
  [{body :body auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (-> auth Integer/parseInt (get 2)) (:space body))
    (let [player-name (:name body)
          space (:space body)]
      (if (nil? player-name)
        (do
          (println "Parameter :name is missing from body: " body)
          {:status 400})                                    ;;parameter is missing
        (try
          {:status  200
           :headers {"Content-Type" "text/json"}
           :body    (json/write-str (players/create-player {:name  player-name
                                                            :space space}))}
          (catch PSQLException e
            (-> e .getMessage println)
            (.printStackTrace e)
            {:status 500}))))
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defroutes routes
           (POST "/players/create" [] create-player-handler))