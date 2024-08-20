(ns statistic.db.tables.players
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as result-set]
            [statistic.db.connection :refer [db]]))

(defn create-players-table []
  "Creates the players table"
  (jdbc/execute-one! db ["CREATE TABLE players (
 name varchar(15) PRIMARY KEY,
 role varchar(10)
 );"]))

(defn execute!
  "wrapper around jdbc/execute. removes table names before qualified maps"
  ([params ops]
    (jdbc/execute! db params (merge {:builder-fn result-set/as-unqualified-maps} ops)))
  ([params]
   (execute! params {})))

(defn create-player [{name :name role :role}]
  "create a new player"
  (let [query (str "INSERT INTO players(name, role) VALUES('" name "','" role "')")]
    (jdbc/execute-one! db [query])))

(defn get-players []
  "get all players from the db"
  (execute! ["SELECT * FROM players"]))