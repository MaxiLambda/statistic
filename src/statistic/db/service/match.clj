(ns statistic.db.service.match
  (:require [statistic.db.utils :refer [execute!]]))

;;TODO add optional para, to get teams for specific match
(defn get-teams []
  "get all matches - enriched with players in teams"
  (execute! ["SELECT matches.id, player_matches.team, array_agg(players.name) as members
              FROM matches, players, player_matches
              WHERE player_matches.match_id = matches.id
              AND player_matches.player_id = players.id
              GROUP BY matches.id, player_matches.team"]))

;;TODO add optional parameter to get wins for specific player
;;TODO add optional parameter to get wins for specific tag
(defn get-number-wins []
  (execute! ["SELECT players.name, count(players.name)
              FROM players, player_matches, matches
              WHERE matches.winner = player_matches.team
              AND player_matches.match_id = matches.id
              AND player_matches.player_id = players.id
              GROUP BY players.name"]))
