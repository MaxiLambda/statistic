(ns statistic.db.tables.matches
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]
            [statistic.db.utils :refer [execute!]]))

(defn create-match [{datetime   :date
                     winner     :winner
                     discipline :discipline
                     tag        :tag}]
  "create a match"
  (let [query (str "INSERT INTO matches(datetime,winner,discipline,tag)
    VALUES('" datetime "','" winner "','" discipline "','" tag "')")]
    (jdbc/execute-one! db [query])))

(defn get-all []
  (execute! ["SELECT * FROM matches"]))

(defn get-by-id [id]
  (execute! [(str "SELECT * FROM matches WHERE matches.id = " id)]))