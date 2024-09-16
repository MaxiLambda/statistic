(ns statistic.db.tables.matches
  (:require [statistic.db.connection :refer [db]]
            [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-match [{datetime   :date
                     winner     :winner
                     discipline :discipline
                     tag        :tag}]
  "create a match, returns {:id} with the id of the created match"
  (let [query (str "INSERT INTO matches(datetime,winner,discipline,tag)
    VALUES('" datetime "','" winner "','" discipline "','" tag "')  RETURNING id")]
    (execute-one! db [query])))

(defn get-all []
  (execute! ["SELECT * FROM matches"]))

(defn get-used-tags []
  (execute! ["SELECT DISTINCT tag FROM matches"]))

(defn get-used-disciplines []
  (execute! ["SELECT DISTINCT discipline FROM matches"]))

(defn get-by-id [id]
  (first (execute-one! [(str "SELECT * FROM matches WHERE matches.id = " id)])))