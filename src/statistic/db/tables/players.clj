(ns statistic.db.tables.players
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-player [{name :name}]
  "create a new player, return its id {:id}"
  (let [query (str "INSERT INTO players(name) VALUES('" name "') RETURNING id")]
    (execute-one! [query])))

(defn get-all
  "as [{:id :name}]"
  []
  "get all players from the db"
  (execute! ["SELECT * FROM players"]))

(defn update-player [{id :id new-name :new-name}]
  "change a players name"
  (let [query (str "UPDATE players SET name = '" new-name "'  WHERE id = " id)]
    (execute! [query])))