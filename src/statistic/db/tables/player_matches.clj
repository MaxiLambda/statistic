(ns statistic.db.tables.player-matches
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-player-match [{matchId  :match_id
                            playerId :player_id
                            team     :team}]
  "create a match"
  (execute-one! ["INSERT INTO player_matches VALUES (?,?,?)" matchId playerId team]))

(defn get-all [{space :space}]
  (execute! ["SELECT player_matches.* FROM player_matches, matches
              WHERE player_matches.match_id = matches.id
              AND matches.space_id = ?" space]))