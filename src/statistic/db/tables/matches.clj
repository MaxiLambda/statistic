(ns statistic.db.tables.matches
  (:require [next.jdbc :as jdbc]
            [statistic.db.connection :refer [db]]
            [statistic.db.utils :refer [execute!]]))

(defn create-match [{datetime   :date
                     winner     :winner
                     discipline :discipline
                     tag        :string}]
  "create a match"
  (let [query (str "INSERT INTO matches(datetime,winner,discipline,tag)
    VALUES('" datetime "','" winner "','" discipline "','" tag "')")]
    (jdbc/execute-one! db [query])))

(defn get-all []
  (execute! ["SELECT * FROM matches"]))