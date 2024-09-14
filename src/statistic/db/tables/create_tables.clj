(ns statistic.db.tables.create-tables
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]))
;;Initialize a new DB with this

(defn create-players-table
  "Creates the players table"
  []
  (jdbc/execute-one! db ["CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name varchar(15) UNIQUE);"]))

(defn create-match-table
  "Creates the matches table"
  []
  (jdbc/execute-one! db ["CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    datetime timestamp with time zone,
    winner smallint,
    discipline varchar(20),
    tag varchar(20));"]))

(defn create-player-match-table
  "creates the association table between players and teams in a match"
  []
  (jdbc/execute-one! db ["CREATE TABLE player_matches (
     match_id integer REFERENCES matches,
     player_id integer REFERENCES players,
     team smallint);"]))