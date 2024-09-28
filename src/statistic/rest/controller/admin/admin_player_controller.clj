(ns statistic.rest.controller.admin.admin-player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [statistic.db.tables.players :as players])
  (:import (org.postgresql.util PSQLException)))

(defn create-player-handler
  "requires :body {:name <name>}"
  [req]
  (let [player-name (get-in req [:body :name])]
    (if (nil? player-name)
      (do
        (println "Parameter :name is missing from body: " (:body req))
        {:status 400})                                      ;;parameter is missing
      (try
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str (players/create-player {:name player-name
                                                          ;;TODO get real space
                                                          :space 1}))}
        (catch PSQLException e
          (-> e .getMessage println)
          (.printStackTrace e)
          {:status 500})))))

(defroutes routes
           (POST "/players/create" [] create-player-handler))