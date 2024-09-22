(ns statistic.db.tables.player-matches
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-player-match [{matchId  :match_id
                            playerId :player_id
                            team     :team}]
  "create a match"
  (execute-one! ["INSERT INTO player_matches VALUES (?,?,?)" matchId playerId team]))

(defn get-all []
  (execute! ["SELECT * from player_matches"]))