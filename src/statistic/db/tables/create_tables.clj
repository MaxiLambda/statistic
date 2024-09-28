(ns statistic.db.tables.create-tables
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]))
;;Initialize a new DB with this

(defn create-spaces-table
  "Creates the spaces table"
  []
  (jdbc/execute-one! db ["CREATE TABLE spaces (
  id SERIAL PRIMARY KEY,
  name varchar(20) UNIQUE,
  view_password varchar(20),
  edit_password varchar(20));"]))

(defn create-players-table
  "Creates the players table"
  []
  (jdbc/execute-one! db ["CREATE TABLE players (
    id SERIAL,
    space_id integer REFERENCES spaces,
    name varchar(15),
    PRIMARY KEY(id, space_id),
    UNIQUE(name,space_id),
    UNIQUE(id));"]))

(defn create-match-table
  "Creates the matches table"
  []
  (jdbc/execute-one! db ["CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    space_id integer REFERENCES spaces,
    datetime timestamp with time zone,
    winner smallint,
    discipline varchar(20),
    tag varchar(20));"]))

(defn create-player-match-table
  "creates the association table between players and teams in a match"
  []
  (jdbc/execute-one! db ["CREATE TABLE player_matches (
     match_id integer REFERENCES matches(id),
     player_id integer REFERENCES players(id),
     team smallint,
     PRIMARY KEY(match_id,player_id));"]))

(defn create-tables []
  (create-spaces-table)
  (create-players-table)
  (create-match-table)
  (create-player-match-table))