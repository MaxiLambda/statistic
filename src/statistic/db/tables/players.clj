(ns statistic.db.tables.players
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]
            [statistic.db.utils :refer [execute!]]))

(defn create-player [{name :name}]
  "create a new player"
  (let [query (str "INSERT INTO players(name) VALUES('" name "')")]
    (jdbc/execute-one! db [query])))

(defn get-all []
  "get all players from the db"
  (execute! ["SELECT * FROM players"]))