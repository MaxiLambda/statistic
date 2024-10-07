(ns statistic.rest.controller.edit.admin-space-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [statistic.authentication.hash :refer [hash-pw]]
            [statistic.db.tables.spaces :as spaces])
  (:import (org.postgresql.util PSQLException)))

(defn create-space-handler
  "requires :body {:name -> char(20)
                   :view-pw -> char(20)
                   :edit-pw -> char(20)}"
  [{body :body auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (-> auth Integer/parseInt (get 2)) (:space body))
    (try
      (let [new-space (spaces/create (-> body
                                         (update-in [:view-pw :edit-pw] hash-pw)
                                         (select-keys [:name :view-pw :edit-pw])))]
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str {:new-space new-space})})
      (catch PSQLException e
        (-> e .getMessage println)
        (.printStackTrace e)
        {:status 500}))
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defroutes routes
           (POST "/spaces/create" [] create-space-handler))