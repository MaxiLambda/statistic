(ns statistic.db.service.match
  (:require [statistic.db.tables.matches :as matches]
            [statistic.db.utils :refer [execute!]]))

(defn get-teams
  "get all teams for all matches - enriched with player names in teams as:
  [{:id int, :team int, :members string[], :member_ids int[]}]

  optional:
    :match-id => only return teams for the match with that id
    :team     => only return information about this team
    "
  ([] (get-teams {}))
  ([{match-id :match-id team :team}]
   (let [match-clause (some-> match-id (#(str "AND matches.id = " % " ")))
         team-clause (some-> team (#(str "AND player_matches.team = " % " ")))]
     (execute! [(str "SELECT matches.id, player_matches.team, array_agg(players.name) as members, array_agg(players.id) as member_ids "
                     "FROM matches, players, player_matches "
                     "WHERE player_matches.match_id = matches.id "
                     "AND player_matches.player_id = players.id "
                     match-clause
                     team-clause
                     "GROUP BY matches.id, player_matches.team")]))))

(defn get-match-with-teams [{match :match match-id :match-id}]
  "enrich a match with its teams.
  You need to either supply :match or :match-id.

  If match is supplied, it is enriched with the teams, otherwise match is fetched from the db by using :match-id"
  (let [match (or match (some-> match-id matches/get-by-id))
        team (get-teams {:match-id match-id})
        team1 (first (filter #(-> % :team #{1}) team))
        team2 (first (filter #(-> % :team #{2}) team))]
    (-> match
        first
        (assoc :team1 {:names (:members team1) :ids (:member_ids team1)}
               :team2 {:names (:members team2) :ids (:member_ids team2)}))))

(defn get-number-wins
  "returns the number of wins for each player as:
  [{:id int, :name string, :count int}]

  If [] is returned, no wins exist for this configuration.
  This might happen for example if no player won with the given tag.

  optional:
    :player-id => only return wins for this player
    :tag       => only count wins with the given tag"
  ([] (get-number-wins {}))
  ([{player :player-id tag :tag}]
   (let [player-clause (some-> player (#(str "AND players.id = " % " ")))
         tag-clause (some-> tag (#(str "AND matches.tag LIKE '" % "' ")))]
     (execute! [(str "SELECT players.id, players.name, count(players.name) "
                     "FROM players, player_matches, matches "
                     "WHERE matches.winner = player_matches.team "
                     player-clause
                     tag-clause
                     "AND player_matches.match_id = matches.id "
                     "AND player_matches.player_id = players.id "
                     "GROUP BY players.name, players.id")]))))
