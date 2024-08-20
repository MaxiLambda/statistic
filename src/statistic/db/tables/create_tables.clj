(ns statistic.db.tables.create-tables
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]))

(defn create-players-table []
  "Creates the players table"
  (jdbc/execute-one! db ["CREATE TABLE players (
 name varchar(15) PRIMARY KEY,
 role varchar(10)
 );"]))
