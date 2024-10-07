(ns statistic.rest.controller.admin.admin-space-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [POST defroutes]]
            [statistic.db.tables.spaces :as spaces])
  (:import (org.postgresql.util PSQLException)))

(defn create-space-handler
  "requires :body {:name -> char(20)
                   :view-pw -> char(20)
                   :edit-pw -> char(20)}"
  [{body :body}]
  (try
    (let [new-space (spaces/create (select-keys body [:name :view-pw :edit-pw]))]
      {:status  200
       :headers {"Content-Type" "text/json"}
       :body    (json/write-str {:new-space new-space})}
      (catch PSQLException e
        (-> e .getMessage println)
        (.printStackTrace e)
        {:status 500}))))

(defroutes routes
           (POST "/spaces/create" [] create-space-handler))