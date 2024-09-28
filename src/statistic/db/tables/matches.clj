(ns statistic.db.tables.matches
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn create-match [{datetime   :date
                     winner     :winner
                     discipline :discipline
                     tag        :tag
                     space      :space}]

  "create a match, returns {:id} with the id of the created match"

  (first (execute-one! ["INSERT INTO matches(space_id,datetime,winner,discipline,tag)
    VALUES(?,?,?,?,?) RETURNING id" space datetime winner discipline tag])))

(defn get-all [{space :space}]
  (execute! ["SELECT * FROM matches WHERE space_id = ?" space]))

(defn get-used-tags
  "Fetches all available tags in a space, can be optionally reduced to only tags for {:discipline}"
  [{space :space discipline :discipline}]
  (if (nil? discipline)
    (map :tag (execute! ["SELECT DISTINCT tag FROM matches WHERE space_id = ?" space]))
    (map :tag (execute! ["SELECT DISTINCT tag FROM matches WHERE discipline LIKE ? AND space_id = ?" discipline space]))))

(defn get-used-disciplines [{space :space}]
  (map :discipline (execute! ["SELECT DISTINCT discipline FROM matches WHERE space_id = ?" space])))

(defn get-by-id [id]
  (first (execute-one! ["SELECT * FROM matches WHERE matches.id = ?"id])))