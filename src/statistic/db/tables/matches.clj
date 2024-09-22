(ns statistic.db.tables.matches
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-match [{datetime   :date
                     winner     :winner
                     discipline :discipline
                     tag        :tag}]

  "create a match, returns {:id} with the id of the created match"

  (first (execute-one! ["INSERT INTO matches(datetime,winner,discipline,tag)
    VALUES(?,?,?,?) RETURNING id" datetime winner discipline tag])))

(defn get-all []
  (execute! ["SELECT * FROM matches"]))

(defn get-used-tags []
  (map :tag  (execute! ["SELECT DISTINCT tag FROM matches"])))

(defn get-used-disciplines []
  (map :discipline (execute! ["SELECT DISTINCT discipline FROM matches"])))

(defn get-by-id [id]
  (first (execute-one! [(str "SELECT * FROM matches WHERE matches.id = " id)])))