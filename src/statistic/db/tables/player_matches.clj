(ns statistic.db.tables.player-matches
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]
            [statistic.db.utils :refer [execute!]]))

(defn create-player-match [{matchId :match_id
                            playerId :player_id
                            team :team}]
  "create a match"
  (let [query (str "INSERT INTO player_matches(match_id,player_id,team)
    VALUES('" matchId "','" playerId "','" team "')")]
    (jdbc/execute-one! db [query])))

(defn get-all []
  (execute! ["SELECT * from player_matches"]))