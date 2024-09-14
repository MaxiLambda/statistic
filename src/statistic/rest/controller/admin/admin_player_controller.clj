(ns statistic.rest.controller.admin.admin-player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes POST]]
            [statistic.db.tables.players :as players])
  (:import (org.postgresql.util PSQLException)))

;;requires :body {:name <name>}
(defn create-player-handler [req]
  (let [player-name (get-in [:body :name] req)]
    (if (nil? player-name)
      (do
        (println "Parameter :name is missing from body")
        {:status 400})                                         ;;parameter is missing
      (try
        (json/write-str (players/create-player {:name player-name}))
        {:status  200
         :headers {"Content-Type" "text/json"}}
        (catch PSQLException e
          (-> e .getMessage println)
          (.printStackTrace e)
          {:status 500})))))

(defroutes routes
           (POST "/players/create" [] create-player-handler))