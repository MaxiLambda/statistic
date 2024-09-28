(ns statistic.db.tables.players
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-player [{name :name space :space}]
  "create a new player, return its id {:id}"
  (execute-one! ["INSERT INTO players(name,space_id) VALUES (?,?) RETURNING id" name space]))

(defn get-all
  "as [{:id :name :space_id}]"
  [{space :space}]
  "get all players from the db"
  (execute! ["SELECT * FROM players WHERE space_id = ?" space]))