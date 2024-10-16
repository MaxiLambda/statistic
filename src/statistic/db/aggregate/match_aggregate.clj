(ns statistic.db.aggregate.match-aggregate
  (:require [statistic.db.tables.matches :as matches]
            [statistic.db.tables.player-matches :as player-matches]
            [statistic.db.utils :refer [execute!]]))

(defn vector-no-nil [& args]
  (->> args
       (filter (comp not nil?))
       (apply vector)))

(defn get-teams
  "get all teams for all matches in a space - enriched with player names in teams as:
  [{:id int, :team int, :members string[], :member_ids int[]}]

  required:
    :space    => the space to search the teams in
  optional:
    :match-id => only return teams for the match with that id
    :team     => only return information about this team
    "
  [{match-id :match-id team :team space :space}]
  (let [match-clause (if (-> match-id nil? not) "AND matches.id = ? " "")
        team-clause (if (-> team nil? not) "AND player_matches.team = ? " "")]
    (execute! (vector-no-nil (str "SELECT matches.id, player_matches.team, array_agg(players.name) as members, "
                                  "array_agg(players.id) as member_ids "
                                  "FROM matches, players, player_matches "
                                  "WHERE player_matches.match_id = matches.id "
                                  "AND matches.space_id = ? "
                                  "AND player_matches.player_id = players.id "
                                  match-clause
                                  team-clause
                                  "GROUP BY matches.id, player_matches.team") space match-id team))))

(defn get-match-with-teams
  "enrich a match with its teams.
  You need to either supply :match or :match-id.

  If match is supplied, it is enriched with the teams, otherwise match is fetched from the db by using :match-id"
  [{match-in :match match-id :match-id}]
  (let [match (or match-in (some-> match-id matches/get-by-id))
        team (get-teams {:match-id (:id match) :space (:space_id match)})
        team1 (first (filter #(-> % :team #{1}) team))
        team2 (first (filter #(-> % :team #{2}) team))]
    (-> match
        (assoc :team1 {:names (:members team1) :ids (:member_ids team1)}
               :team2 {:names (:members team2) :ids (:member_ids team2)}))))

(defn get-all-matches-with-teams
  "returns all matches with resolved teams.
  First fetches all matches, then enriches them with the teams"
  [{space :space}]
  (for [match (matches/get-all {:space space})]
    (get-match-with-teams {:match match})))

(defn get-number-wins
  "returns the number of wins for each player in a space as:
  [{:id int, :name string, :count int}]

  If [] is returned, no wins exist for this configuration.
  This might happen for example if no player won with the given tag.
  required:
    :space            => the space to get the data for
  optional:
    :player-id        => only return wins for this player
    :discipline       => only count wins with the given discipline
    :tag              => only count wins with the given tag (only works when discipline is set)"
  [{player :player-id tag :tag discipline :discipline space :space}]
  (let [is-player-set (-> player nil? not)
        is-discipline-set (-> discipline nil? not)
        is-tag-set (-> tag nil? not)
        player-clause (if is-player-set "AND players.id = ? " "")
        discipline-clause (if is-discipline-set "AND matches.discipline LIKE ? " "")
        tag-clause (if (and is-discipline-set is-tag-set) "AND matches.tag LIKE ? " "")]
    (execute! (vector-no-nil
                (str "SELECT players.id, players.name, count(players.name) "
                     "FROM players, player_matches, matches "
                     "WHERE matches.winner = player_matches.team "
                     "AND matches.space_id = ? "
                     "AND players.space_id = ? "
                     player-clause
                     tag-clause
                     discipline-clause
                     "AND player_matches.match_id = matches.id "
                     "AND player_matches.player_id = players.id "
                     "GROUP BY players.name, players.id")
                space space player tag discipline))))

(defn create-new-match
  "creates a new match in the db. Can't handle new players.
  :players should be a list of all players with the form [{:id :team}].

  :match-params consists of:
  {datetime   :date
   winner     :winner
   discipline :discipline
   tag        :tag
   players    :players
   space      :space}

  First a new match is created in the matches table, then the players are added to their
  corresponding teams in the player_matches table."
  [{players :players :as match-params}]
  (let [new-match-id (:id (matches/create-match match-params))]
    (doseq [{player :id team :team} players]
      (player-matches/create-player-match {:match_id new-match-id :player_id player :team team}))
    new-match-id))