(ns statistic.db.tables.player-matches
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-player-match [{matchId  :match_id
                            playerId :player_id
                            team     :team}]
  "create a match"
  (let [query (str "INSERT INTO player_matches(match_id,player_id,team)
    VALUES('" matchId "','" playerId "','" team "')")]
    (execute-one! [query])))

(defn get-all []
  (execute! ["SELECT * from player_matches"]))